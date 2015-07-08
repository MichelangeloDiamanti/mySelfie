package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		// utente di appoggio per caricare la lista
		User user = new User();
		
		
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
	
}
