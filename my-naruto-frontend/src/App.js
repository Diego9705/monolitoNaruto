import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import AldeasPage from './pages/AldeasPage';
import JutsusPage from './pages/JutsusPage';
import MisionesPage from './pages/MisionesPage';
import NinjasPage from './pages/NinjasPage';
import './App.css'; // Importa el CSS específico de App

function App() {
    return (
        <Router>
            <div className="App">
                <nav className="navbar">
                    <Link to="/aldeas">Aldeas</Link>
                    <Link to="/jutsus">Jutsus</Link>
                    <Link to="/misiones">Misiones</Link>
                    <Link to="/ninjas">Ninjas</Link>
                </nav>

                <div className="content">
                    <Routes>
                        <Route path="/" element={<Navigate to="/ninjas" />} /> {/* Redirige a ninjas por defecto */}
                        <Route path="/aldeas" element={<AldeasPage />} />
                        <Route path="/jutsus" element={<JutsusPage />} />
                        <Route path="/misiones" element={<MisionesPage />} />
                        <Route path="/ninjas" element={<NinjasPage />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;