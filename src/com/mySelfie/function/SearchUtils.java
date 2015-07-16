package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.entity.Hashtag;
import com.mySelfie.entity.Selfie;
import com.mySelfie.entity.User;

public class SearchUtils {

	/**
	 * prende in input una keyword e restituisce una lista di utenti
	 * il cui username contiene la keyword
	 * 
	 * @param keyword	keyword da ricercare
	 * @return			lista di utenti il cui username contiene la key
	 */
	public static List<User> searchUserByUsername(String keyword){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di users dove caricare i risultati
		List<User> userMatches = new ArrayList<User>();
		
		/*
		 * query che restituisce tutti gli user che hanno lo username che contiene la keyword
		 */
		String userMatchesString = 
					"SELECT "
				+ 		"* "
				+ 	"FROM "
				+ 		"User "
				+ 	"WHERE "
				+ 		"username LIKE ?";
		// query formato SQL
		PreparedStatement userMatchesSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userMatchesSQL = connect.prepareStatement(userMatchesString);
			userMatchesSQL.setString(1, "%" + keyword + "%");			
			ResultSet userMatchesRes = userMatchesSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (userMatchesRes.next()) 
			{		
				// user di appoggio per caricare la lista
				User user = new User();
				
				/* vengono presi tutti gli attributi dello user e messi nello user di appoggio */
				user.setId_user(userMatchesRes.getInt("id_user"));
				user.setusername(userMatchesRes.getString("username"));
				user.setEmail(userMatchesRes.getString("email"));
				user.setPhone(userMatchesRes.getString("phone"));
				user.setName(userMatchesRes.getString("name"));
				user.setSurname(userMatchesRes.getString("surname"));
				user.setGender(userMatchesRes.getString("gender"));
				user.setNotes(userMatchesRes.getString("notes"));
				user.setCity(userMatchesRes.getString("city"));
				user.setProfilepic(userMatchesRes.getString("profilepic"));
				user.setBirthdate(userMatchesRes.getDate("birthdate"));
				
				// lo user di appoggio viene aggiunto alla lista
				userMatches.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista dei selfie
		return userMatches;
	}
	
	/**
	 * prende in input una keyword e restituisce una lista di utenti
	 * il cui nome contiene la keyword
	 * 
	 * @param keyword	keyword da ricercare
	 * @return			lista di utenti il cui nome contiene la key
	 */
	public static List<User> searchUserByName(String keyword){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di users dove caricare i risultati
		List<User> userMatches = new ArrayList<User>();
		
		/*
		 * query che restituisce tutti gli user che hanno lo username che contiene la keyword
		 */
		String userMatchesString = 
							"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"User "
						+ 	"WHERE "
						+ 		"name LIKE ?";
		// query formato SQL
		PreparedStatement userMatchesSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userMatchesSQL = connect.prepareStatement(userMatchesString);
			userMatchesSQL.setString(1, "%" + keyword + "%");	
			ResultSet userMatchesRes = userMatchesSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (userMatchesRes.next()) 
			{		
				// user di appoggio per caricare la lista
				User user = new User();
				
				/* vengono presi tutti gli attributi dello user e messi nello user di appoggio */
				user.setId_user(userMatchesRes.getInt("id_user"));
				user.setusername(userMatchesRes.getString("username"));
				user.setEmail(userMatchesRes.getString("email"));
				user.setPhone(userMatchesRes.getString("phone"));
				user.setName(userMatchesRes.getString("name"));
				user.setSurname(userMatchesRes.getString("surname"));
				user.setGender(userMatchesRes.getString("gender"));
				user.setNotes(userMatchesRes.getString("notes"));
				user.setCity(userMatchesRes.getString("city"));
				user.setProfilepic(userMatchesRes.getString("profilepic"));
				user.setBirthdate(userMatchesRes.getDate("birthdate"));
				
				// lo user di appoggio viene aggiunto alla lista
				userMatches.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return userMatches;
	}
	
	/**
	 * prende in input una keyword e restituisce una lista di utenti
	 * il cui cognome contiene la keyword
	 * 
	 * @param keyword	keyword da ricercare
	 * @return			lista di utenti il cui cognome contiene la key
	 */
	public static List<User> searchUserBySurname(String keyword){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di users dove caricare i risultati
		List<User> userMatches = new ArrayList<User>();
		
		/*
		 * query che restituisce tutti gli user che hanno lo username che contiene la keyword
		 */
		String userMatchesString = 
							"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"User "
						+ 	"WHERE "
						+ 		"surname LIKE ?";
		// query formato SQL
		PreparedStatement userMatchesSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userMatchesSQL = connect.prepareStatement(userMatchesString);
			userMatchesSQL.setString(1, "%" + keyword + "%");	
			ResultSet userMatchesRes = userMatchesSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (userMatchesRes.next()) 
			{		
				// user di appoggio per caricare la lista
				User user = new User();
				
				/* vengono presi tutti gli attributi dello user e messi nello user di appoggio */
				user.setId_user(userMatchesRes.getInt("id_user"));
				user.setusername(userMatchesRes.getString("username"));
				user.setEmail(userMatchesRes.getString("email"));
				user.setPhone(userMatchesRes.getString("phone"));
				user.setName(userMatchesRes.getString("name"));
				user.setSurname(userMatchesRes.getString("surname"));
				user.setGender(userMatchesRes.getString("gender"));
				user.setNotes(userMatchesRes.getString("notes"));
				user.setCity(userMatchesRes.getString("city"));
				user.setProfilepic(userMatchesRes.getString("profilepic"));
				user.setBirthdate(userMatchesRes.getDate("birthdate"));
				
				// lo user di appoggio viene aggiunto alla lista
				userMatches.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return userMatches;
	}
	
	/**
	 * prende in input nome e cognome e restituisce una lista di utenti
	 * che hanno nome contentente il name specificato e cognome contentente
	 * il surname specificato
	 * 
	 * @param name		nome da ricercare
	 * @param surname	cognome da ricercare
	 * @return			lista di utenti
	 */
	public static List<User> searchUserByNameAndSurname(String name, String surname){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di users dove caricare i risultati
		List<User> userMatches = new ArrayList<User>();
		
		/*
		 * query che restituisce tutti gli user che hanno lo username che contiene la keyword
		 */
		String userMatchesString = 
							"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"User "
						+ 	"WHERE "
						+ 		"name LIKE ? AND "
						+ 		"surname LIKE ?";
		// query formato SQL
		PreparedStatement userMatchesSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userMatchesSQL = connect.prepareStatement(userMatchesString);
			userMatchesSQL.setString(1, "%" + name + "%");	
			userMatchesSQL.setString(2, "%" + surname + "%");	
			ResultSet userMatchesRes = userMatchesSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (userMatchesRes.next()) 
			{		
				// user di appoggio per caricare la lista
				User user = new User();
				
				/* vengono presi tutti gli attributi dello user e messi nello user di appoggio */
				user.setId_user(userMatchesRes.getInt("id_user"));
				user.setusername(userMatchesRes.getString("username"));
				user.setEmail(userMatchesRes.getString("email"));
				user.setPhone(userMatchesRes.getString("phone"));
				user.setName(userMatchesRes.getString("name"));
				user.setSurname(userMatchesRes.getString("surname"));
				user.setGender(userMatchesRes.getString("gender"));
				user.setNotes(userMatchesRes.getString("notes"));
				user.setCity(userMatchesRes.getString("city"));
				user.setProfilepic(userMatchesRes.getString("profilepic"));
				user.setBirthdate(userMatchesRes.getDate("birthdate"));
				
				// lo user di appoggio viene aggiunto alla lista
				userMatches.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return userMatches;
	}
	
	/**
	 * prende in input una keyword e restituisce una lista di utenti
	 * la cui città contiene la keyword
	 * 
	 * @param keyword	keyword da ricercare
	 * @return			lista di utenti la cui città contiene la key
	 */
	public static List<User> searchUserByCity(String keyword){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di users dove caricare i risultati
		List<User> userMatches = new ArrayList<User>();
		
		/*
		 * query che restituisce tutti gli user che hanno lo username che contiene la keyword
		 */
		String userMatchesString = 
							"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"User "
						+ 	"WHERE "
						+ 		"city LIKE ?";
		// query formato SQL
		PreparedStatement userMatchesSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userMatchesSQL = connect.prepareStatement(userMatchesString);
			userMatchesSQL.setString(1, "%" + keyword + "%");	
			ResultSet userMatchesRes = userMatchesSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (userMatchesRes.next()) 
			{		
				// user di appoggio per caricare la lista
				User user = new User();
				
				/* vengono presi tutti gli attributi dello user e messi nello user di appoggio */
				user.setId_user(userMatchesRes.getInt("id_user"));
				user.setusername(userMatchesRes.getString("username"));
				user.setEmail(userMatchesRes.getString("email"));
				user.setPhone(userMatchesRes.getString("phone"));
				user.setName(userMatchesRes.getString("name"));
				user.setSurname(userMatchesRes.getString("surname"));
				user.setGender(userMatchesRes.getString("gender"));
				user.setNotes(userMatchesRes.getString("notes"));
				user.setCity(userMatchesRes.getString("city"));
				user.setProfilepic(userMatchesRes.getString("profilepic"));
				user.setBirthdate(userMatchesRes.getDate("birthdate"));
				
				// lo user di appoggio viene aggiunto alla lista
				userMatches.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return userMatches;
	}
	
	/**
	 * prende in input una stringa (di un luogo) e ritorna tutti i selfie
	 * che la cui location contiene la keyword
	 * 
	 * @param location	stringa rappresentante un luogo
	 * @return			lista di selfie location LIKE keyword
	 */
	public static List<Selfie> searchSelfieByLocation(String location){
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
						+ 		"location LIKE ?";
		// query formato SQL
		PreparedStatement selfiesByLocationSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			selfiesByLocationSQL = connect.prepareStatement(selfiesByLocationString);
			selfiesByLocationSQL.setString(1, "%" + location + "%");	
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
	
	
	/**
	 * prende in input una keyword username, ricava una lista di users
	 * i cui username contengono la keyword e ricava tutti i selfie di quegli users
	 * senza duplicati
	 * 
	 * @param uploader	keyword da ricercare
	 * @return			lista di selfie di tutti gli user i cui username contengono la keyword
	 */
	public static List<Selfie> searchSelfieByUploader(String uploader){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
	
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> selfiesByUploader = new ArrayList<Selfie>();
		
		// scorro la lista di utenti il cui username contiene la keyword
		for (User user : SearchUtils.searchUserByUsername(uploader)) {
			// ricavo l'id dell'untente corrente
			int uploaderId = UserUtils.getId(user.getusername());
			
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
					// viene preso l'id del selfie corrente
					selfie.setId_selfie(selfiesByUploaderRes.getInt("id_selfie"));
					// se il selfie corrente non è contenuto all'interno della lista viene aggiunto
					if (!selfiesByUploader.contains(selfie)) {
						/* vengono presi tutti gli attributi del selfie e messi in quello di appoggio */
						selfie.setUploader(selfiesByUploaderRes.getInt("uploader"));
						selfie.setDescription(selfiesByUploaderRes.getString("description"));
						selfie.setLocation(selfiesByUploaderRes.getString("location"));
						selfie.setDate(selfiesByUploaderRes.getTimestamp("date"));
						selfie.setPicture(selfiesByUploaderRes.getString("picture"));
						
						// il selfie di appoggio viene aggiunto alla lista
						selfiesByUploader.add(selfie);
					}
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// chiude la connessione
				try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		
		//ritorna la lista dei selfie
		return selfiesByUploader;
	}
	
	

	public static List<Hashtag> searchHashTagByName(String name){
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di selfie dove caricare i risultati
		List<Hashtag> hashTagsByName = new ArrayList<Hashtag>();
		
		/*
		 * query che restituisce gli hashtags che nel nome contengono 
		 * la keyword
		 */
		String hashTagsByNameString = 
				"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"Hashtag "
						+ 	"WHERE "
						+ 		"name LIKE ?";
		// query formato SQL
		PreparedStatement hashTagsByNameSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			hashTagsByNameSQL = connect.prepareStatement(hashTagsByNameString);
			hashTagsByNameSQL.setString(1, "%" + name + "%");	
			ResultSet hashTagsByNameRes = hashTagsByNameSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (hashTagsByNameRes.next()) 
			{		
				// selfie di appoggio per caricare la lista
				Hashtag hashtag = new Hashtag();
				
				/* vengono presi tutti gli attributi del selfie e messi in quello di appoggio */
				hashtag.setId_hashtag(hashTagsByNameRes.getInt("id_hashtag"));
				hashtag.setName(hashTagsByNameRes.getString("name"));
				
				// il selfie di appoggio viene aggiunto alla lista
				hashTagsByName.add(hashtag);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return hashTagsByName;
	}
	
	
}
