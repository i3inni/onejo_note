import axios from 'axios'

const API_KEY = import.meta.env.VITE_OPENWEATHERMAP_API_KEY
// .env 파일에 VITE_OPENWEATHERMAP_API_URL='https://pro.openweathermap.org/data/2.5' 와 같이 설정되어 있다고 가정합니다.
const BASE_URL = import.meta.env.VITE_OPENWEATHERMAP_API_URL

const LAT = 36.9882
const LON = 127.9264

export const getCurrentWeather = async () => {
  const response = await axios.get(`${BASE_URL}/weather`, {
    params: {
      lat: LAT,
      lon: LON,
      appid: API_KEY,
      units: 'metric',
      lang: 'kr',
    },
  })
  return response.data
}

export const getForecast = async () => {
  // '/forecast/hourly' 경로로 수정합니다.
  const response = await axios.get(`${BASE_URL}/forecast/hourly`, {
    params: {
      lat: LAT,
      lon: LON,
      appid: API_KEY,
      units: 'metric',
      lang: 'kr',
    },
  })
  return response.data
}
