import React, { useState, useEffect } from 'react';

function NinjaForm({ onSubmit, initialData = {}, buttonText = 'Crear Ninja', aldeas = [] }) {
    const [name, setName] = useState(initialData.name || '');
    const [rank, setRank] = useState(initialData.rank || 'Genin');
    const [atk, setAtk] = useState(initialData.atk || 0);
    const [def, setDef] = useState(initialData.def || 0);
    const [chakra, setChakra] = useState(initialData.chakra || 0);
    const [selectedAldeaId, setSelectedAldeaId] = useState('');
    const [prevId, setPrevId] = useState(initialData.id);

    useEffect(() => {
        if (initialData && initialData.id && initialData.id !== prevId) {
            setName(initialData.name || '');
            setRank(initialData.rank || 'Genin');
            setAtk(initialData.atk || 0);
            setDef(initialData.def || 0);
            setChakra(initialData.chakra || 0);
            setSelectedAldeaId(initialData.aldea ? initialData.aldea.id.toString() : '');
            setPrevId(initialData.id);
        }
    }, [initialData, prevId]);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(
            {
                id: initialData.id,
                name,
                rank,
                atk: parseInt(atk),
                def: parseInt(def),
                chakra: parseInt(chakra)
            },
            selectedAldeaId
        );
        if (!initialData.id) {
            setName('');
            setRank('Genin');
            setAtk(0);
            setDef(0);
            setChakra(0);
            setSelectedAldeaId('');
            setPrevId(null);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form-container">
            <h3>{buttonText}</h3>
            <input
                type="text"
                placeholder="Nombre del Ninja"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <label>
                Rango:
                <select value={rank} onChange={(e) => setRank(e.target.value)}>
                    <option value="Genin">Genin</option>
                    <option value="Chunin">Chunin</option>
                    <option value="Jonin">Jonin</option>
                </select>
            </label>
            <input
                type="number"
                placeholder="Ataque"
                value={atk}
                onChange={(e) => setAtk(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Defensa"
                value={def}
                onChange={(e) => setDef(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Chakra"
                value={chakra}
                onChange={(e) => setChakra(e.target.value)}
                required
            />
            <label>
                Asignar Aldea:
                <select
                    value={selectedAldeaId}
                    onChange={(e) => setSelectedAldeaId(e.target.value)}
                >
                    <option value="">(Ninguna)</option>
                    {aldeas.map((aldea) => (
                        <option key={aldea.id} value={aldea.id}>
                            {aldea.name} (ID: {aldea.id})
                        </option>
                    ))}
                </select>
            </label>
            <button type="submit">{buttonText}</button>
        </form>
    );
}

export default NinjaForm;