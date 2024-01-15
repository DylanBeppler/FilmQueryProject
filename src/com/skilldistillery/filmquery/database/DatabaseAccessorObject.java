package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static final String USER = "student";
	private static final String PASSWORD = "student";

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "SELECT film.*, language.id, language.name FROM film JOIN language ON film.language_id = language.id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				film = new Film();
				film.setId(filmResult.getInt("id"));
				film.setTitle(filmResult.getString("title"));
				film.setDescription(filmResult.getString("description"));
				film.setReleaseYear(filmResult.getShort("release_year"));
				film.setLanguage(filmResult.getString("language.name"));
				film.setRentalDuration(filmResult.getInt("rental_duration"));
				film.setRentalRate(filmResult.getDouble("rental_rate"));
				film.setLength(filmResult.getInt("length"));
				film.setReplacementCost(filmResult.getDouble("replacement_cost"));
				film.setRating(filmResult.getString("rating"));
				film.setSpecialFeatures(filmResult.getString("special_features"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	
	
	
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor();
				actor.setId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "SELECT actor.*, film.*\\n\" + \"FROM actor\\n\" + \"JOIN film_actor fa ON actor.id = fa.actor_id\\n\"\n"
					+ "				+ \"JOIN film ON film.id = fa.film_id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet actorResult = stmt.executeQuery();
			while (actorResult.next()) {
				Actor actor = new Actor();
				actor = new Actor();
				actor.setId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));

				actors.add(actor);
			}
			actorResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}
	@Override
	public List<Film> findFilmByKeyword(String keyword) {
	    List<Film> films = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet filmResult = null;
	    try {
	        conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        String sql = "SELECT f.*, l.name AS language_name, a.id AS actor_id, a.first_name, a.last_name "
	                   + "FROM film f "
	                   + "JOIN language l ON f.language_id = l.id "
	                   + "LEFT JOIN film_actor fa ON f.id = fa.film_id "
	                   + "LEFT JOIN actor a ON fa.actor_id = a.id "
	                   + "WHERE f.title LIKE ? OR f.description LIKE ?";
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, "%" + keyword + "%");
	        stmt.setString(2, "%" + keyword + "%");
	        filmResult = stmt.executeQuery();
	        
	        HashMap<Integer, Film> filmMap = new HashMap<>();

	        while (filmResult.next()) {
	            int filmId = filmResult.getInt("id");
	            Film film = filmMap.get(filmId);
	            if (film == null) {
	                film = new Film();
	                film.setId(filmId);
	                film.setTitle(filmResult.getString("title"));
	                film.setDescription(filmResult.getString("description"));
	                film.setReleaseYear(filmResult.getShort("release_year"));
	                film.setLanguage(filmResult.getString("language_name"));
	                film.setRentalDuration(filmResult.getInt("rental_duration"));
	                film.setRentalRate(filmResult.getDouble("rental_rate"));
	                film.setLength(filmResult.getInt("length"));
	                film.setReplacementCost(filmResult.getDouble("replacement_cost"));
	                film.setRating(filmResult.getString("rating"));
	                film.setSpecialFeatures(filmResult.getString("special_features"));
	                film.setActors(new ArrayList<>());
	                filmMap.put(filmId, film);
	            }

	            int actorId = filmResult.getInt("actor_id");
	            if (actorId > 0) {
	                Actor actor = new Actor();
	                actor.setId(actorId);
	                actor.setFirstName(filmResult.getString("first_name"));
	                actor.setLastName(filmResult.getString("last_name"));
	                film.getActors().add(actor);
	            }
	        }
	        
	        films.addAll(filmMap.values());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (filmResult != null) filmResult.close();
	            if (stmt != null) stmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return films;
	}

}
