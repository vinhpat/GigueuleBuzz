const express = require('express');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

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
        winner: null,
        participants: []
    };
    res.json(sessions[id]);
});

app.post('/api/session/join', (req, res) => {
    const { sessionId, userName } = req.body;
    const session = sessions[sessionId];
    
    if (!session) {
        return res.status(404).json({ error: 'Session not found' });
    }
    
    if (!session.participants.includes(userName)) {
        session.participants.push(userName);
    }
    
    res.json(session);
});

app.post('/api/session/start', (req, res) => {
    const { sessionId } = req.body;
    const session = sessions[sessionId];
    
    if (session) {
        session.status = 'active';
        session.winner = null;
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
        session.status = 'finished';
        session.winner = userName;
    }
    
    res.json(session);
});

app.post('/api/session/reset', (req, res) => {
    const { sessionId } = req.body;
    const session = sessions[sessionId];
    
    if (session) {
        session.status = 'waiting';
        session.winner = null;
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
