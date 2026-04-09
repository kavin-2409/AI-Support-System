import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080", // 🔥 FIX HERE
});

// 🔐 attach token automatically
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

console.log("API BASE URL:",API.defaults.baseURL);
export default API;