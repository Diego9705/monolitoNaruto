import React, { useState } from 'react';

function AldeaForm({ onSubmit, initialData = {}, buttonText = 'Crear Aldea' }) {
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
                placeholder="Nombre de la Aldea"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <button type="submit">{buttonText}</button>
        </form>
    );
}

export default AldeaForm;