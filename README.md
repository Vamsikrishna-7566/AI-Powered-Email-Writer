# AI Email Reply Assistant 

##  Overview
An AI-powered email reply assistant that integrates directly with Gmail via a browser extension.  
It extracts email content, sends it to a Spring Boot backend, generates a response using Gemini AI, and inserts the reply into the Gmail compose box.

---

##  Features
- 🔍 Extracts email content directly from Gmail UI
- 🤖 Generates AI-powered replies using Gemini API
- 🎯 Supports tone customization (Professional, Casual, Friendly)
- ⚡ One-click response generation inside Gmail
- 📋 Copy-to-clipboard functionality
- 🔄 Real-time UI feedback (loading, errors)

---

## ⚙️ Tech Stack

### Frontend
- React.js
- Material UI

### Backend
- Spring Boot
- WebClient (for API calls)

### AI
- Gemini API (Google Generative AI)

### Browser Extension
- JavaScript
- DOM Manipulation
- MutationObserver

---

##  Workflow

1. Detect Gmail compose window using MutationObserver  
2. Inject "AI Reply" button into toolbar  
3. Extract email content from DOM  
4. Send request to backend (`/api/email/generate`)  
5. Backend builds prompt and calls Gemini API  
6. AI generates response  
7. Response is inserted into Gmail compose box  

---

## 🚧 Challenges Solved

- Handling dynamic Gmail DOM updates  
- Reliable button injection into Gmail UI  
- Extracting correct email content  
- Managing async communication between extension and backend  
- Designing effective AI prompts  

---

## 🚀 Future Improvements
- Tone selection inside extension UI  
- Streaming AI responses  
- Secure API key handling  
- Authentication for backend APIs  
- Multi-language support  

---

