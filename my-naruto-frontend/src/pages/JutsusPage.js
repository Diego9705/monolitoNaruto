import React, { useState, useEffect } from 'react';
import * as jutsusApi from '../api/jutsus';
import JutsuForm from '../components/JutsuForm';

function JutsusPage() {
    const [jutsus, setJutsus] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchJutsus();
    }, []);

    const fetchJutsus = async () => {
        try {
            setLoading(true);
            const response = await jutsusApi.getAllJutsus();
            setJutsus(response.data);
            setError(null);
        } catch (err) {
            setError('Error al cargar los jutsus.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateJutsu = async (jutsuData) => {
        try {
            await jutsusApi.createJutsu(jutsuData);
            fetchJutsus();
        } catch (err) {
            setError('Error al crear el jutsu.');
            console.error(err);
        }
    };

    const handleLoadDefaultJutsus = async () => {
        try {
            await jutsusApi.loadDefaultJutsus();
            fetchJutsus();
        } catch (err) {
            setError('Error al cargar jutsus predeterminados.');
            console.error(err);
        }
    };

    if (loading) return <div>Cargando jutsus...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="page-container">
            <h1>Gestión de Jutsus</h1>

            <JutsuForm onSubmit={handleCreateJutsu} />

            <button onClick={handleLoadDefaultJutsus} className="load-defaults-button">
                Cargar Jutsus Predeterminados
            </button>

            <h2>Lista de Jutsus</h2>
            {jutsus.length === 0 ? (
                <p>No hay jutsus creados aún.</p>
            ) : (
                <ul className="entity-list">
                    {jutsus.map((jutsu) => (
                        <li key={jutsu.id}>
                            ID: {jutsu.id} - Nombre: {jutsu.name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default JutsusPage;