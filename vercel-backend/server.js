const express = require('express');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
    res.json({ status: "ok", message: "PaddyBuzz API is running", version: "1.0.0" });
});

// In-memory store for a simple Proof of Concept.
// For production, replace this with a database (e.g., Redis, MongoDB).
const sessions = {};

// Helper to generate random ID
function generateId() {
    return Math.random().toString(36).substring(2, 6).toUpperCase();
}

app.post('/api/session/create', (req, res) => {
    const { sessionName, description, maxParticipants } = req.body || {};
    const id = generateId();
    sessions[id] = {
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
    res.json(sessions[id]);
});

app.post('/api/session/join', (req, res) => {
    const { sessionId, userName } = req.body;
    const session = sessions[sessionId];
    
    if (!session) {
        return res.status(404).json({ error: 'Session not found' });
    }
    
    if (!session.participants.find(p => p.userName === userName)) {
        session.participants.push({ userName, buzzTime: null });
    }
    
    res.json(session);
});

app.post('/api/session/start', (req, res) => {
    const { sessionId } = req.body;
    const session = sessions[sessionId];
    
    if (session) {
        session.status = 'active';
        session.startTime = Date.now();
        session.participants.forEach(p => p.buzzTime = null);
        res.json(session);
    } else {
        res.status(404).json({ error: 'Session not found' });
    }
});

app.post('/api/session/stop', (req, res) => {
    const { sessionId } = req.body;
    const session = sessions[sessionId];
    
    if (session) {
        session.status = 'stopped';
        res.json(session);
    } else {
        res.status(404).json({ error: 'Session not found' });
    }
});

app.post('/api/session/next-question', (req, res) => {
    const { sessionId } = req.body;
    const session = sessions[sessionId];
    
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
        res.json(session);
    } else {
        res.status(404).json({ error: 'Session not found' });
    }
});

app.post('/api/session/buzz', (req, res) => {
    const { sessionId, userName } = req.body;
    const session = sessions[sessionId];
    
    if (!session) {
        return res.status(404).json({ error: 'Session not found' });
    }
    
    if (session.status === 'active') {
        const participant = session.participants.find(p => p.userName === userName);
        if (participant && !participant.buzzTime) {
            participant.buzzTime = Date.now();
        }
    }
    
    res.json(session);
});

app.post('/api/session/reset', (req, res) => {
    const { sessionId } = req.body;
    const session = sessions[sessionId];
    
    if (session) {
        session.status = 'waiting';
        session.startTime = null;
        session.participants.forEach(p => p.buzzTime = null);
        res.json(session);
    } else {
        res.status(404).json({ error: 'Session not found' });
    }
});

app.get('/api/session/:id', (req, res) => {
    const session = sessions[req.params.id];
    if (session) {
        res.json(session);
    } else {
        res.status(404).json({ error: 'Session not found' });
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});

// For Vercel Serverless support
module.exports = app;
