import axios from 'axios';

const baseUrl = import.meta.env.VITE_PUBLIC_API_URL;

const client = axios.create({
  baseURL: baseUrl,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default client;
