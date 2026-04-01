import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Assume Spring Boot runs on 8080
});

// Add a request interceptor to attach the JWT token to every request
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);


