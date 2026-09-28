import axios from "axios";


const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;


export const registerUser = (data) => api.post("/auth/register", data);
export const loginUser = (data) => api.post("/auth/login", data);


export const getAllMovies = () => api.get("/movies");
export const getMovieById = (id) => api.get(`/movies/${id}`);
export const searchMovies = (title) => api.get("/movies/search", { params: { title } });
export const getTrending = () => api.get("/movies/trending");
export const addMovie = (movie) => api.post("/movies", movie);
export const updateMovie = (id, movie) => api.put(`/movies/${id}`, movie);
export const deleteMovie = (id) => api.delete(`/movies/${id}`);


export const rateMovie = (data) => api.post("/ratings", data);
export const getAverageRating = (movieId) => api.get(`/ratings/movie/${movieId}/average`);


export const getPersonalizedRecommendations = (userId) => api.get(`/recommendations/${userId}`);
export const getRecommendationsByGenre = (genre) => api.get(`/recommendations/genre/${genre}`);
export const getTopRated = () => api.get("/recommendations/top-rated");


export const addReview = (data) => api.post("/reviews", data);
export const getReviewsForMovie = (movieId) => api.get(`/reviews/movie/${movieId}`);


export const addToWatchlist = (userId, movieId) => api.post(`/watchlist/${userId}/${movieId}`);
export const removeFromWatchlist = (userId, movieId) => api.delete(`/watchlist/${userId}/${movieId}`);
export const getWatchlist = (userId) => api.get(`/watchlist/${userId}`);


export const importFromTmdb = (title) =>
  api.post("/tmdb/import", null, { params: { title } });
export const importPopularFromTmdb = (pages = 1) =>
  api.post("/tmdb/import-popular", null, { params: { pages } });