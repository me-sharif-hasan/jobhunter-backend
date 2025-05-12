import axios from 'axios';
import Constants from "../values/constants.ts";

const api = axios.create({
    baseURL: Constants.baseUrl,
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('_jobhunteradmintoken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;
