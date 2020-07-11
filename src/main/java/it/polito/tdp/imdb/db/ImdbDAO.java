package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Collegamento;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> elencoAnni(){
		String sql = "SELECT DISTINCT m.year anno " + 
				"FROM movies m " + 
				"ORDER BY m.year ";
		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer anno = res.getInt("anno");
				
				result.add(anno);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE DB! Controlla sintassi e/o parametri.");
			return null;
		}
	}
	
	public List<Director> direttoriAnno(int anno){
		String sql = "SELECT distinct d.id cod, d.last_name cogn, d.first_name nom " + 
				"FROM movies m, movies_directors md, directors d " + 
				"WHERE m.year = ? " + 
				"		AND m.id = md.movie_id " + 
				"		AND md.director_id = d.id ";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int cod = res.getInt("cod");
				String cogn = res.getString("cogn");
				String nom = res.getString("nom");
				
				Director director = new Director(cod, nom, cogn);
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE DB! Controlla sintassi e/o parametri.");
			return null;
		}
	}
	
	public List<Collegamento> prendiCollegamenti(int anno, Map<Integer, Director> mappa){
		String sql = "SELECT count(DISTINCT r.actor_id) peso, md.director_id d1, md2.director_id d2 " + 
				"FROM movies m, movies_directors md, directors d, roles r,movies_directors md2, directors d2, movies m2, roles r2 " + 
				"WHERE m.year = ? AND m.year = m2.year " + 
				"		AND m.id = md.movie_id AND m2.id = md2.movie_id " + 
				"		AND md.director_id = d.id AND md2.director_id = d2.id AND md.director_id< md2.director_id " + 
				"		AND md.movie_id = r.movie_id AND md2.movie_id = r2.movie_id " + 
				"		AND r.actor_id = r2.actor_id " + 
				"GROUP BY md.director_id, md2.director_id ";
		List<Collegamento> result = new ArrayList<Collegamento>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(mappa.containsKey(res.getInt("d1")) && mappa.containsKey(res.getInt("d2"))) {
					Director d1 = mappa.get(res.getInt("d1"));
					Director d2 = mappa.get(res.getInt("d2"));
					Integer peso = res.getInt("peso");
					
					Collegamento ctemp = new Collegamento(d1, d2, peso);
					
					result.add(ctemp);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE DB! Controlla sintassi e/o parametri.");
			return null;
		}
	}
}
