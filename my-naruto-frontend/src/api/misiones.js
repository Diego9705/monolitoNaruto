import axios from 'axios';

const API_URL = 'http://localhost:8080/api/misiones';

export const getAllMisiones = () => axios.get(API_URL);
export const getMisionById = (id) => axios.get(`${API_URL}/${id}`);
export const createMision = (mision) => axios.post(API_URL, mision);
export const updateMision = (mision) => axios.patch(API_URL, mision);
export const connectNinjaToMision = (misionId, ninjaId) => axios.patch(`${API_URL}/${misionId}/${ninjaId}`);
export const deleteMision = (id) => axios.delete(`${API_URL}/${id}`);
export const loadDefaultMisiones = () => axios.get(`${API_URL}/predeterminados`);
export const exportMision = (misionId, option) => axios.get(`${API_URL}/${misionId}/${option}`, { responseType: 'blob' });