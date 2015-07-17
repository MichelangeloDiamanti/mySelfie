package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.entity.User;

public class UsertagsUtils {

	/**
	 * prende in input l'id di un selfie e ne ricava tutti gli
	 * utente taggati
	 * 
	 * @param selfieId	selfie del quale prendere i tags
	 * @return
	 */
	public static List<User> getSelfieUserTags(int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// dichiaro una lista di utenti dove caricare i risultati
		List<User> selfieUserTagsList = new ArrayList<User>();
		
	
		/*
		 * query che restituisce tutti gli hashtags di un selfie
		 */
		String selfieUserTagsString = 
					"SELECT "
				+ 		"US.* "
				+ 	"FROM "
				+ 		"((Selfie AS SE INNER JOIN user_tag_selfie AS UTS ON SE.id_selfie = UTS.id_selfie) INNER JOIN "
				+ 		"User AS US ON UTS.id_user = US.id_user) "
				+ 	"WHERE "
				+ 		"SE.id_selfie= ? ";
		
		// query formato SQL
		PreparedStatement selfieUserTagsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			selfieUserTagsSQL = connect.prepareStatement(selfieUserTagsString);
			selfieUserTagsSQL.setInt(1, selfieId);
			ResultSet selfieUserTagsRes = selfieUserTagsSQL.executeQuery();
			
			/* vengono scorsi tutti gli hashtags */
			while (selfieUserTagsRes.next()) 
			{		
				// utente di appoggio per caricare la lista
				User user = new User();
				
				/* vengono presi tutti gli attributi dello user e messi nel'utente di appoggio */
            	user.setId_user(selfieUserTagsRes.getInt("id_user"));
            	user.setusername(selfieUserTagsRes.getString("username"));
            	user.setEmail(selfieUserTagsRes.getString("email"));
            	user.setPhone(selfieUserTagsRes.getString("phone"));
            	user.setName(selfieUserTagsRes.getString("name"));
            	user.setSurname(selfieUserTagsRes.getString("surname"));
            	user.setGender(selfieUserTagsRes.getString("gender"));
            	user.setNotes(selfieUserTagsRes.getString("notes"));
            	user.setCity(selfieUserTagsRes.getString("city"));
            	user.setProfilepic(selfieUserTagsRes.getString("profilepic"));
            	user.setBirthdate(selfieUserTagsRes.getDate("birthdate"));
				// l'hashtag di appoggio viene aggiunto alla lista
				selfieUserTagsList.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista di hashtags
		return selfieUserTagsList;

	}
	
	/**
	 * 
	 * @param username
	 * @param conn
	 * @return
	 * @throws NamingException
	 * 
	 *             Tagga uno user in una selfie (con connessione)
	 * 
	 */
	public static int userTagSelfie(int id_user, int id_selfie,
			Connection conn) throws NamingException {

		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// query in formato stringa e statement
		String userSelfieString = "INSERT INTO user_tag_selfie(id_user, id_selfie) VALUES (?, ?)";
		PreparedStatement userSelfieSQL;

		try {

			// prepara lo statement a partire dalla stringa
			userSelfieSQL = conn.prepareStatement(userSelfieString, Statement.RETURN_GENERATED_KEYS);
			// imposta i parametri nello statement
			userSelfieSQL.setInt(1, id_user);
			userSelfieSQL.setInt(2, id_selfie);
			// esegue la query
			int affectedRows = userSelfieSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException("tagging user failed, no rows affected.");
			}
			// altrimenti
			ResultSet generatedKeys = userSelfieSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id del record generato
				generatedId = generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatedId;
	}

	/**
	 * 
	 * @param username
	 * @param conn
	 * @return
	 * @throws NamingException
	 * 
	 *             Tagga uno user in una selfie (senza connessione)
	 * 
	 */
	public static boolean userTagSelfie(String username) throws NamingException {
		return false;

	}
	
	
	/**
	 * prende in input un'istanza di chiave primaria della tabella 
	 * user_tag_selfie e ritorna l'id dello user taggato
	 * 
	 * @param id_uts
	 * @return
	 */
	public static int getTaggedUser(int id_uts)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// id dell'untente taggato nel selfie
		int user_id = -1;
		
		try 
		{       
			
			// query che ritorna l'id dello user a cui fa riferimento la chiave
			String taggedUserString = 
						"SELECT "
					+ 		"id_user "
					+ 	"FROM "
					+ 		"user_tag_selfie "
					+ 	"WHERE "
					+ 		"id_uts = ?";
					
			PreparedStatement taggedUserSQL = connect.prepareStatement(taggedUserString);
			taggedUserSQL.setInt(1, id_uts);
			ResultSet taggedUserRes = taggedUserSQL.executeQuery(); 			
			
			if(taggedUserRes.next())
			{
				user_id = taggedUserRes.getInt("id_user");
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}  	 
		return user_id;
	}
	
	/**
	 * prende in input un'istanza di chiave primaria della tabella 
	 * user_tag_selfie e ritorna l'id del selfie
	 * 
	 * @param id_uls
	 * @return
	 */
	public static int getTaggedSelfie(int id_uls)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// id del selfie taggato
		int selfie_id = -1;
		
		try 
		{       
			
			// query che ritorna l'id del selfie taggato
			String taggedSelfieString = 
					"SELECT "
							+ 		"id_selfie "
							+ 	"FROM "
							+ 		"user_tag_selfie "
							+ 	"WHERE "
							+ 		"id_uts = ?";
			
			PreparedStatement taggedSelfieSQL = connect.prepareStatement(taggedSelfieString);
			taggedSelfieSQL.setInt(1, id_uls);
			ResultSet likeQueryRes = taggedSelfieSQL.executeQuery(); 			
			
			if(likeQueryRes.next())
			{
				selfie_id = likeQueryRes.getInt("id_selfie");
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}  	 
		return selfie_id;
	}
	
}
