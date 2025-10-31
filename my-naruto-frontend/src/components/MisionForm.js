import React, { useState, useEffect } from 'react';

// ACEPTA 'ninjas' como prop
function MisionForm({ onSubmit, initialData = {}, buttonText = 'Crear Misión', ninjas = [] }) {
    const [name, setName] = useState(initialData.name || '');
    const [rank, setRank] = useState(initialData.rank || 'D');
    const [recompensa, setRecompensa] = useState(initialData.recompensa || 0);
    const [requisitorango, setRequisitoRango] = useState(initialData.requisitorango || 'Genin');
    const [selectedNinjaId, setSelectedNinjaId] = useState('');
    const [prevId, setPrevId] = useState(initialData.id);

    useEffect(() => {
        if (initialData && initialData.id && initialData.id !== prevId) {
            setName(initialData.name || '');
            setRank(initialData.rank || 'D');
            setRecompensa(initialData.recompensa || 0);
            setRequisitoRango(initialData.requisitorango || 'Genin');
            setSelectedNinjaId(initialData.ninja ? initialData.ninja.id.toString() : '');
            setPrevId(initialData.id);
        }
        // Si es creación, el clear se hace al enviar, no aquí
    }, [initialData, prevId]);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(
            {
                id: initialData.id,
                name,
                rank,
                recompensa: parseInt(recompensa),
                requisitorango
            },
            selectedNinjaId
        );
        if (!initialData.id) {
            setName('');
            setRank('D');
            setRecompensa(0);
            setRequisitoRango('Genin');
            setSelectedNinjaId('');
            setPrevId(null);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form-container">
            <h3>{buttonText}</h3>
            <input
                type="text"
                placeholder="Nombre de la Misión"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <label>
                Rango (Misión):
                <select value={rank} onChange={(e) => setRank(e.target.value)}>
                    <option value="D">D</option>
                    <option value="C">C</option>
                    <option value="B">B</option>
                    <option value="A">A</option>
                    <option value="S">S</option>
                </select>
            </label>
            <input
                type="number"
                placeholder="Recompensa"
                value={recompensa}
                onChange={(e) => setRecompensa(e.target.value)}
                required
            />
            <label>
                Requisito de Rango (Ninja):
                <select value={requisitorango} onChange={(e) => setRequisitoRango(e.target.value)}>
                    <option value="Genin">Genin</option>
                    <option value="Chunin">Chunin</option>
                    <option value="Jonin">Jonin</option>
                </select>
            </label>
            <label>
                Asignar Ninja:
                <select
                    value={selectedNinjaId}
                    onChange={(e) => setSelectedNinjaId(e.target.value)}
                >
                    <option value="">(Ninguno)</option>
                    {ninjas.map((ninja) => (
                        <option key={ninja.id} value={ninja.id}>
                            {ninja.name} (Rango: {ninja.rank})
                        </option>
                    ))}
                </select>
            </label>
            <button type="submit">{buttonText}</button>
        </form>
    );
}

export default MisionForm;
