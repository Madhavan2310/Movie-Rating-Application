# CineRate — Movie & Web Series Recommendation + Rating System

Full-stack project: **React** frontend, **Java + Spring Boot** REST API backend, **MySQL** database.

```
movie-recommendation-system/
├── backend/     Spring Boot REST API
└── frontend/    React app (theme: dark navy + gold)
```

## Color Theme (already applied in `frontend/src/index.css`)

| Role       | Hex       |
|------------|-----------|
| Background | `#0F172A` |
| Primary    | `#FACC15` |
| Secondary  | `#1E293B` |
| Text       | `#F8FAFC` |

---

## 1. Backend Setup (Spring Boot + MySQL)

**Requirements:** Java 17+, Maven, MySQL running locally.

```bash
cd backend
```

1. Open `src/main/resources/application.properties` and set your MySQL username/password:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   ```
   Hibernate will auto-create the `movie_recommendation_db` database and tables on first run
   (`spring.jpa.hibernate.ddl-auto=update`). A reference `schema-reference.sql` is also included
   if you'd rather create tables manually.

2. Run it:
   ```bash
   mvn spring-boot:run
   ```
   The API starts on **http://localhost:8080**.

### Key Endpoints

| Method | Endpoint                          | Purpose                          |
|--------|------------------------------------|-----------------------------------|
| POST   | `/auth/register`                  | Register a user                  |
| POST   | `/auth/login`                     | Login                             |
| GET    | `/movies`                         | List all movies                  |
| GET    | `/movies/{id}`                    | Movie details + average rating   |
| GET    | `/movies/search?title=...`        | Search by name                   |
| GET    | `/movies/trending`                | Trending (top rated)             |
| POST   | `/movies`                         | Add movie/web series (admin)     |
| PUT    | `/movies/{id}`                    | Edit movie (admin)               |
| DELETE | `/movies/{id}`                    | Delete movie (admin)             |
| POST   | `/ratings`                        | Submit/update a 1–5 star rating  |
| GET    | `/ratings/movie/{id}/average`     | Average rating                   |
| GET    | `/recommendations/{userId}`       | Personalized recommendations     |
| GET    | `/recommendations/genre/{genre}`  | Genre-based recommendations      |
| GET    | `/recommendations/top-rated`      | Rating-based recommendations     |
| POST   | `/reviews`                        | Add a review/comment             |
| GET    | `/reviews/movie/{id}`             | Reviews for a movie              |
| POST   | `/watchlist/{userId}/{movieId}`   | Add to watchlist                 |
| DELETE | `/watchlist/{userId}/{movieId}`   | Remove from watchlist            |
| GET    | `/watchlist/{userId}`             | View watchlist                   |
| GET    | `/tmdb/search?title=...`          | TMDB lookup (once key is added)  |

To make a user an **admin**, just update the row in MySQL:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'you@example.com';
```

---

## 2. Frontend Setup (React)

**Requirements:** Node.js 18+.

```bash
cd frontend
npm install
npm start
```
Runs on **http://localhost:3000** and talks to the backend at `http://localhost:8080`
(configured in `src/api/api.js`).

---

## 3. Adding Your TMDB API Key (Step 8 — Future Enhancement)

1. Get a free key: https://www.themoviedb.org/settings/api
2. Open `backend/src/main/resources/application.properties` and paste it in:
   ```properties
   tmdb.api.key=PASTE_YOUR_KEY_HERE
   ```
3. Restart the backend. `GET /tmdb/search?title=Interstellar` will now return live TMDB data.
4. From there, wire `TmdbService.searchMovie()` into `MovieService.addMovie()` (or a new
   admin "Import from TMDB" button in `AdminPanel.js`) to auto-fill poster, description,
   genre, and release year instead of typing them in by hand.

The rest of the Step 8 roadmap (collaborative filtering, AI-based recommendations, sentiment
analysis on reviews, AWS/Render deployment) builds on top of this same structure — the
`RecommendationService` and `ReviewService` are the natural places to extend.

---

## 4. Recommended Build/Test Order (matches the project plan)

1. Start MySQL, run the backend once to auto-create tables.
2. Test `/auth/register` and `/auth/login` with a REST client (Postman/curl).
3. Add a couple of movies via `POST /movies` (or the Admin Panel in the UI, after
   marking your user as ADMIN).
4. Submit ratings via `POST /ratings` and confirm `/movies/{id}` shows the average.
5. Try `/recommendations/genre/{genre}` and `/recommendations/top-rated`.
6. Rate a few titles as a normal user, then check `/recommendations/{userId}`
   for personalized results.
7. Run the frontend and click through: Register → Login → Browse → Rate →
   Recommendations → Watchlist → Admin Panel.
