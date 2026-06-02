const express = require('express');
const cors = require('cors');
const fs = require('fs');
const path = require('path');
const os = require('os');

const app = express();
app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
    // If browser client requests the home page, serve our beautiful, responsive play console web app!
    if (req.headers.accept && req.headers.accept.includes('text/html')) {
        try {
            const htmlPath = path.join(__dirname, 'index.html');
            if (fs.existsSync(htmlPath)) {
                return res.sendFile(htmlPath);
            }
        } catch (err) {
            console.error("Failed to serve web client:", err);
        }
    }
    res.json({ status: "ok", message: "PaddyBuzz API is running", version: "1.0.0" });
});

// In-memory store cache for active games.
const sessions = {};

// Local disk cache folder fallback to make serverless deployments completely state-resilient!
const sessionDir = path.join(os.tmpdir(), 'paddybuzz_sessions');
try {
    if (!fs.existsSync(sessionDir)) {
        fs.mkdirSync(sessionDir, { recursive: true });
    }
} catch (e) {
    console.error("Failed to create backup cache directory:", e);
}

// Initialize Supabase Client
const { createClient } = require('@supabase/supabase-js');
let supabase = null;
if (process.env.SUPABASE_URL && process.env.SUPABASE_KEY) {
    supabase = createClient(process.env.SUPABASE_URL, process.env.SUPABASE_KEY);
    console.log("Supabase client initialized successfully");
} else {
    console.log("Warning: SUPABASE_URL and SUPABASE_KEY not configured. Falling back to resilient local file cache store.");
}

// Read/write helpers
async function getSession(sessionId) {
    if (supabase) {
        const { data, error } = await supabase
            .from('sessions')
            .select('data')
            .eq('id', sessionId)
            .single();
        if (error) {
            console.error(`Error fetching session ${sessionId} from Supabase:`, error.message);
            return null;
        }
        return data ? data.data : null;
    }
    
    // In memory Cache
    if (sessions[sessionId]) {
        return sessions[sessionId];
    }
    
    // Fall back to resilient OS tmp filesystem cache
    try {
        const filePath = path.join(sessionDir, `${sessionId.toUpperCase()}.json`);
        if (fs.existsSync(filePath)) {
            const fileData = fs.readFileSync(filePath, 'utf8');
            const data = JSON.parse(fileData);
            sessions[sessionId] = data; // Cache in memory too
            return data;
        }
    } catch (e) {
        console.error(`Error loading cached session ${sessionId}:`, e.message);
    }
    
    return null;
}

async function saveSession(sessionId, sessionData) {
    if (supabase) {
        const { error } = await supabase
            .from('sessions')
            .upsert({
                id: sessionId,
                data: sessionData,
                updated_at: new Date().toISOString()
            });
        if (error) {
            console.error(`Error saving session ${sessionId} to Supabase:`, error.message);
            throw error;
        }
        return;
    }
    
    sessions[sessionId] = sessionData;
    
    // Save to resilient OS tmp filesystem cache
    try {
        const filePath = path.join(sessionDir, `${sessionId.toUpperCase()}.json`);
        fs.writeFileSync(filePath, JSON.stringify(sessionData, null, 2), 'utf8');
    } catch (e) {
        console.error(`Error saving fallback session cache for ${sessionId}:`, e.message);
    }
}

// Helper to generate random ID
function generateId() {
    return Math.random().toString(36).substring(2, 6).toUpperCase();
}

app.post('/api/session/create', async (req, res) => {
    const { sessionName, description, maxParticipants } = req.body || {};
    const id = generateId();
    const newSession = {
        sessionId: id,
        sessionName: sessionName || 'Untitled Session',
        description: description || '',
        maxParticipants: maxParticipants || 100,
        status: 'waiting',
        questionCounter: 1,
        startTime: null,
        participants: [],
        roundHistory: []
    };
    try {
        await saveSession(id, newSession);
        res.json(newSession);
    } catch (error) {
        res.status(500).json({ error: 'Failed to create session on Supabase', details: error.message });
    }
});

app.post('/api/session/join', async (req, res) => {
    const { sessionId, userName } = req.body;
    try {
        const session = await getSession(sessionId);
        
        if (!session) {
            return res.status(404).json({ error: 'Session not found' });
        }
        
        if (!session.participants.find(p => p.userName === userName)) {
            session.participants.push({ userName, buzzTime: null });
        }
        
        await saveSession(sessionId, session);
        res.json(session);
    } catch (error) {
        res.status(500).json({ error: 'Failed to join session', details: error.message });
    }
});

app.post('/api/session/start', async (req, res) => {
    const { sessionId } = req.body;
    try {
        const session = await getSession(sessionId);
        
        if (session) {
            session.status = 'active';
            session.startTime = Date.now();
            session.participants.forEach(p => p.buzzTime = null);
            await saveSession(sessionId, session);
            res.json(session);
        } else {
            res.status(404).json({ error: 'Session not found' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Failed to start session', details: error.message });
    }
});

app.post('/api/session/stop', async (req, res) => {
    const { sessionId } = req.body;
    try {
        const session = await getSession(sessionId);
        
        if (session) {
            session.status = 'stopped';
            await saveSession(sessionId, session);
            res.json(session);
        } else {
            res.status(404).json({ error: 'Session not found' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Failed to stop session', details: error.message });
    }
});

app.post('/api/session/next-question', async (req, res) => {
    const { sessionId } = req.body;
    try {
        const session = await getSession(sessionId);
        
        if (session) {
            let winner = null;
            let earliestBuzz = Infinity;
            session.participants.forEach(p => {
                if (p.buzzTime !== null && p.buzzTime < earliestBuzz) {
                    earliestBuzz = p.buzzTime;
                    winner = p.userName;
                }
            });
            session.roundHistory = session.roundHistory || [];
            session.roundHistory.push({
                roundNumber: session.questionCounter || 1,
                winnerName: winner || null
            });
            session.questionCounter = (session.questionCounter || 1) + 1;
            session.status = 'waiting';
            session.startTime = null;
            session.participants.forEach(p => p.buzzTime = null);
            await saveSession(sessionId, session);
            res.json(session);
        } else {
            res.status(404).json({ error: 'Session not found' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Failed to transition to next question', details: error.message });
    }
});

app.post('/api/session/buzz', async (req, res) => {
    const { sessionId, userName } = req.body;
    try {
        const session = await getSession(sessionId);
        
        if (!session) {
            return res.status(404).json({ error: 'Session not found' });
        }
        
        if (session.status === 'active') {
            const participant = session.participants.find(p => p.userName === userName);
            if (participant && !participant.buzzTime) {
                participant.buzzTime = Date.now();
            }
        }
        
        await saveSession(sessionId, session);
        res.json(session);
    } catch (error) {
        res.status(500).json({ error: 'Failed to register buzz', details: error.message });
    }
});

app.post('/api/session/reset', async (req, res) => {
    const { sessionId } = req.body;
    try {
        const session = await getSession(sessionId);
        
        if (session) {
            session.status = 'waiting';
            session.startTime = null;
            session.participants.forEach(p => p.buzzTime = null);
            await saveSession(sessionId, session);
            res.json(session);
        } else {
            res.status(404).json({ error: 'Session not found' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Failed to reset session', details: error.message });
    }
});

app.get('/api/session/:id', async (req, res) => {
    try {
        const session = await getSession(req.params.id);
        if (session) {
            res.json(session);
        } else {
            res.status(404).json({ error: 'Session not found' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Failed to fetch session', details: error.message });
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});

// For Vercel Serverless support
module.exports = app;
