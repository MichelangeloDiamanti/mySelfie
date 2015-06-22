package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
	            		
	            		System.out.println(usertag);
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

}
