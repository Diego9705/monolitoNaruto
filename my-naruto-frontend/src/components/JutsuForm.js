import React, { useState } from 'react';

function JutsuForm({ onSubmit, initialData = {}, buttonText = 'Crear Jutsu' }) {
    const [name, setName] = useState(initialData.name || '');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ name });
        setName('');
    };

    return (
        <form onSubmit={handleSubmit} className="form-container">
            <h3>{buttonText}</h3>
            <input
                type="text"
                placeholder="Nombre del Jutsu"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <button type="submit">{buttonText}</button>
        </form>
    );
}

export default JutsuForm;