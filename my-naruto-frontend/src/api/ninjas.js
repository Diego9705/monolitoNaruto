import axios from 'axios';

const API_URL = 'http://localhost:8080/api/ninjas';

export const getAllNinjas = () => axios.get(API_URL);
export const getNinjaById = (id) => axios.get(`${API_URL}/${id}`);
export const createNinja = (ninja) => axios.post(API_URL, ninja);
export const updateNinja = (ninja) => axios.patch(API_URL, ninja);
export const connectJutsuToNinja = (ninjaId, jutsuId) => axios.patch(`${API_URL}/conectarnj/${ninjaId}/${jutsuId}`);
export const connectAldeaToNinja = (ninjaId, aldeaId) => axios.patch(`${API_URL}/conectarna/${ninjaId}/${aldeaId}`);
export const deleteNinja = (id) => axios.delete(`${API_URL}/${id}`);
export const exportNinja = (ninjaId, option) => axios.get(`${API_URL}/${ninjaId}/${option}`, { responseType: 'blob' });