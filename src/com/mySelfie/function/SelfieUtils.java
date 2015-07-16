package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.entity.Selfie;

public class SelfieUtils {

	public static void uploadSelfie(Selfie selfie, List<String> hashtags, List<String> usertags) throws NamingException {
		Context context = null; // contesto
		DataSource datasource = null; // dove pescare i dati
		Connection connect = null; // connessione al DB

		try {
			// Get the context and create a connection
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in
			// 'WebContent/META-INF/context.xml'
			datasource = (DataSource) context
					.lookup("java:/comp/env/jdbc/mySelfie");
			// si connette al database
			connect = datasource.getConnection();

			// prepara la query per inserire il nuovo selfie
			String sql = "INSERT INTO Selfie (uploader, description, date, picture) "
					+ "VALUES (?, ?, ?, ?)";
			
			// dichiara lo statement specificando che deve ritornare la chiave della tabella della riga modificata
			PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			// inserisce i parametri
			statement.setInt(1, selfie.getUploader());
			statement.setString(2, selfie.getDescription());
			statement.setTimestamp(3, selfie.getDate());
			statement.setString(4, selfie.getPicture());
			
			// esegue la query e ritorna un intero che indica le righe modificate
	        int affectedRows = statement.executeUpdate();

	        // se non è stata modificata nessuna riga spara un'eccezione
	        if (affectedRows == 0) {
	            throw new SQLException("Creating selfie failed, no rows affected.");
	        }

	        // altrimenti
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            // se è stata ritornata la chiave
	        	if (generatedKeys.next()) {
	        		
	        		// imposta l'id del selfie alla chiave ritornata
	            	int id_selfie = generatedKeys.getInt(1);
	    			
	            	// scorre tutti gli hashtags che devono essere messi nella selfie
	            	for (String hashtag : hashtags) {
	    				
	    				// se l'hashtag esiste
	    				if(HashtagUtils.exist(hashtag, connect))
	    				{
	    					// ricava il suo id
	    					int id_hashtag = HashtagUtils.getId(hashtag, connect);
	    					// inserisce il tag
	    					HashtagUtils.hashtagInSelfie(id_selfie, id_hashtag, connect);
	    				}
	    				// se l'hashtag NON esiste
	    				else
	    				{
	    					// crea un nuovo hashtag e si fa ritornare l'id
	    					int id_hashtag = HashtagUtils.newHashTag(hashtag, connect);
	    					// inserisce il tag
	    					HashtagUtils.hashtagInSelfie(id_selfie, id_hashtag, connect);
	    					
	    				}
	    				
	    			}
	            	
	            	// scorre tutti gli usertags che devono essere messi nella selfie
	            	for (String usertag : usertags) {
	            		
	            		// controlla se lo user esiste
	            		if(UserUtils.exist(usertag, connect))
	            		{
	            			// ricava il suo id
	            			int id_user = UserUtils.getId(usertag, connect);
	            			// inserisce il tag
	            			UserUtils.userTagSelfie(id_user, id_selfie, connect);
	            		}
	            	}
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Prende in input l'id dello user attuale, e carica 10 post a partire dall'ultimo
	 * visualizzato, per questo necessita di un indice che ne tiene traccia.
	 * Si serve della data per caricare solo posts antecedenti al caricamento della pagina
	 * 
	 * @param userId 		id dello user attuale
	 * @param lastIndex		indice che tiene traccia dei post visualizzati
	 * @param maxDate		valore che indica il datetime di caricamento della pagina
	 * @return 
	 */
	public static List<Selfie> getFollowedUsersPosts(int userId, int lastIndex, String maxDate) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> followedUsersPostsList = new ArrayList<Selfie>();
		
		/*
		 * query che restituisce tutti i post pubblicati dagli utenti seguiti dallo user
		 */
		String followedUsersPostsString = 
				"SELECT "
			+ 		"SE.id_selfie, SE.picture, SE.description, SE.uploader "
			+ 	"FROM "
			+ 		"(Selfie AS SE INNER JOIN user_follow_user AS UFU ON SE.uploader = UFU.id_followed) "
			+ 	"WHERE "
			+ 		"UFU.id_follower = ? AND "
			+ 		"SE.uploader=UFU.id_followed AND "
			+ 		"SE.date < ? "
			+ 	"ORDER BY "
			+ 		"SE.date DESC "
			+ 	"LIMIT "
			+ 		"?,10";
		
		// query formato SQL
		PreparedStatement followedUsersPostsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			followedUsersPostsSQL = connect.prepareStatement(followedUsersPostsString);
			followedUsersPostsSQL.setInt(1, userId);		        
			followedUsersPostsSQL.setString(2, maxDate);
			followedUsersPostsSQL.setInt(3, lastIndex); 	        
			ResultSet followedUsersPostsRes = followedUsersPostsSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (followedUsersPostsRes.next()) 
			{		
				// selfie di appoggio per caricare la lista
				Selfie selfie = new Selfie();
				
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				selfie.setId_selfie(followedUsersPostsRes.getInt("id_selfie"));
				selfie.setPicture(followedUsersPostsRes.getString("picture"));
				selfie.setDescription(followedUsersPostsRes.getString("description"));   
				selfie.setUploader(followedUsersPostsRes.getInt("uploader"));
				// il selfie di appoggio viene aggiunto alla lista
				followedUsersPostsList.add(selfie);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
			
		//ritorna la lista dei selfie
		return followedUsersPostsList;

	}
	
	
	
	/**
	 * Prende in input l'id dell'hashtag da ricercare nei selfie, e carica 10 post a partire dall'ultimo
	 * visualizzato, per questo necessita di un indice che ne tiene traccia.
	 * Si serve della data per caricare solo posts antecedenti al caricamento della pagina
	 * 
	 * @param hashtagId 	id dell'hashtag da cercare
	 * @param lastIndex		indice che tiene traccia dei post visualizzati
	 * @param maxDate		valore che indica il datetime di caricamento della pagina
	 * @return 
	 */
	public static List<Selfie> getPostsByHashtag(int hashtagId, int lastIndex, String maxDate) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> hashtagPostsList = new ArrayList<Selfie>();
		
		
		/*
		 * query che restituisce tutti i post che hanno un determinato hashtag
		 */
		String hashtagPostsString = 
					"SELECT "
				+ 		"SE.id_selfie, SE.picture, SE.description, SE.uploader "
				+ 	"FROM "
				+ 		"Selfie AS SE "
				+ 	"WHERE "
				+ 		"id_selfie = ANY(SELECT id_selfie FROM hashtag_in_selfie WHERE id_hashtag= ? ) AND "
				+ 		"SE.date < ?"
				+ 	"ORDER BY "
				+ 		"SE.date DESC "
				+ 	"LIMIT "
				+ 		"?,10";
		// query formato SQL
		PreparedStatement hashtagPostsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			hashtagPostsSQL = connect.prepareStatement(hashtagPostsString);
			hashtagPostsSQL.setInt(1, hashtagId);		        
			hashtagPostsSQL.setString(2, maxDate);
			hashtagPostsSQL.setInt(3, lastIndex); 	        
			ResultSet hashtagPostsRes = hashtagPostsSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (hashtagPostsRes.next()) 
			{		
				// selfie di appoggio per caricare la lista
				Selfie selfie = new Selfie();
				
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				selfie.setId_selfie(hashtagPostsRes.getInt("id_selfie"));
				selfie.setPicture(hashtagPostsRes.getString("picture"));
				selfie.setDescription(hashtagPostsRes.getString("description"));   
				selfie.setUploader(hashtagPostsRes.getInt("uploader"));
				// il selfie di appoggio viene aggiunto alla lista
				hashtagPostsList.add(selfie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista dei selfie
		return hashtagPostsList;
		
	}
	
	

	/**
	 * Prende in input l'id di un selfie e ne ritorna tutte le informazioni
	 * 
	 * @param selfieId	id del selfie da cercare
	 * @return
	 */
	public static Selfie getPostById(int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// selfie da ritornare
		Selfie selfie = new Selfie();
		
		/*
		 * query che restituisce un post idententificato dall'id
		 */
		String singlePostString = 
					"SELECT "
				+ 		"SE.id_selfie, SE.picture, SE.description, SE.uploader "
				+ 	"FROM "
				+ 		"Selfie AS SE "
				+ 	"WHERE "
				+ 		"id_selfie = ?";
		// query formato SQL
		PreparedStatement singlePostSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			singlePostSQL = connect.prepareStatement(singlePostString);
			singlePostSQL.setInt(1, selfieId);		        
			ResultSet hashtagPostsRes = singlePostSQL.executeQuery();
			
			/* se il selfie è stato trovato */
			if (hashtagPostsRes.next()) 
			{		
				/* vengono presi tutti gli attributi del selfie e messi nel selfie */
				selfie.setId_selfie(hashtagPostsRes.getInt("id_selfie"));
				selfie.setPicture(hashtagPostsRes.getString("picture"));
				selfie.setDescription(hashtagPostsRes.getString("description"));   
				selfie.setUploader(hashtagPostsRes.getInt("uploader"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna il selfie
		return selfie;
		
	}
	
	
	/**
	 * ritorna tutti i selfie che ha caricato lo user
	 * 
	 * @param uploaderId	id dell'utente
	 * @return				lista di selfie
	 */
	public static List<Selfie> getSelfieByUploaderId(int uploaderId){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di users dove caricare i risultati
		List<Selfie> selfiesByUploader = new ArrayList<Selfie>();
		
		/*
		 * query che restituisce tutti i selfie di uno user
		 */
		String selfiesByUploaderString = 
				"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"Selfie "
						+ 	"WHERE "
						+ 		"uploader = ?";
		// query formato SQL
		PreparedStatement selfiesByUploaderSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			selfiesByUploaderSQL = connect.prepareStatement(selfiesByUploaderString);
			selfiesByUploaderSQL.setInt(1, uploaderId);	
			ResultSet selfiesByUploaderRes = selfiesByUploaderSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (selfiesByUploaderRes.next()) 
			{		
				// selfie di appoggio per caricare la lista
				Selfie selfie = new Selfie();
				
				/* vengono presi tutti gli attributi del selfie e messi in quello di appoggio */
				selfie.setId_selfie(selfiesByUploaderRes.getInt("id_selfie"));
				selfie.setUploader(selfiesByUploaderRes.getInt("uploader"));
				selfie.setDescription(selfiesByUploaderRes.getString("description"));
				selfie.setLocation(selfiesByUploaderRes.getString("location"));
				selfie.setDate(selfiesByUploaderRes.getTimestamp("date"));
				selfie.setPicture(selfiesByUploaderRes.getString("picture"));
				
				// il selfie di appoggio viene aggiunto alla lista
				selfiesByUploader.add(selfie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return selfiesByUploader;
	}
	
	/**
	 * prende in input una stringa (di un luogo) e ritorna tutti i selfie
	 * che hanno location uguale alla keyword
	 * 
	 * @param location	stringa rappresentante un luogo
	 * @return			lista di selfie location = keyword
	 */
	public static List<Selfie> getSelfieByLocation(String location){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> selfiesByLocation = new ArrayList<Selfie>();
		
		/*
		 * query che restituisce tutti i selfie caricati in un certo posto
		 */
		String selfiesByLocationString = 
				"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"Selfie "
						+ 	"WHERE "
						+ 		"location = ?";
		// query formato SQL
		PreparedStatement selfiesByLocationSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			selfiesByLocationSQL = connect.prepareStatement(selfiesByLocationString);
			selfiesByLocationSQL.setString(1, location);	
			ResultSet selfiesByLocationRes = selfiesByLocationSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (selfiesByLocationRes.next()) 
			{		
				// selfie di appoggio per caricare la lista
				Selfie selfie = new Selfie();
				
				/* vengono presi tutti gli attributi del selfie e messi in quello di appoggio */
				selfie.setId_selfie(selfiesByLocationRes.getInt("id_selfie"));
				selfie.setUploader(selfiesByLocationRes.getInt("uploader"));
				selfie.setDescription(selfiesByLocationRes.getString("description"));
				selfie.setLocation(selfiesByLocationRes.getString("location"));
				selfie.setDate(selfiesByLocationRes.getTimestamp("date"));
				selfie.setPicture(selfiesByLocationRes.getString("picture"));
				
				// il selfie di appoggio viene aggiunto alla lista
				selfiesByLocation.add(selfie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return selfiesByLocation;
	}

}


