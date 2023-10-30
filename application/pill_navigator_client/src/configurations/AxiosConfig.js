import axios from "axios";

const axiosConfig = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    "Content-Type": 'application/json',
    Accept: 'application/json'
  },
  withCredentials: true,
  timeout: 5000
});

export default axiosConfig;