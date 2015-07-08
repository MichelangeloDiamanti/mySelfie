package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mySelfie.connection.ConnectionManager;

public class LikeUtils {

	/**
	 * Controlla se ad un utente piace un determinato selfie grazie
	 * ai loro ID
	 * 
	 * @param userId		// id dell'utente da controllare
	 * @param selfieId		// id del selfie da controllare
	 * @return
	 */
	public static boolean checkLike(int userId, int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// risultato del controllo
		boolean checkLike = false;
		
		/*
		 * query che controlla se allo user in input piace il selfie in input
		 */
		String checkLikeString = 
					"SELECT "
				+ 		"* "
				+ 	"FROM "
				+ 		"user_like_selfie "
				+ 	"WHERE "
				+ 		"id_user = ? AND "
				+ 		"id_selfie = ?";
		
		// query formato SQL
		PreparedStatement checkLikeSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			checkLikeSQL = connect.prepareStatement(checkLikeString);
			checkLikeSQL.setInt(1, userId);		         	        
			checkLikeSQL.setInt(2, selfieId);		         	        
			ResultSet checkLikeRes = checkLikeSQL.executeQuery();
			
			/* se c'è un risultato */
			if (checkLikeRes.next()) 
			{		
				// significa che allo user piace il selfie
				checkLike = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista dei selfie
		return checkLike;
	}
	
	/**
	 * Metodo che ritorna il numero di likes di un selfie a partire dal sui ID
	 * se non ci sono likes ritorna -1
	 * 
	 * @param selfieId	// id del selfie del quale contare il likes
	 * @return
	 */
	public static int getSelfieLikesCount(int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// numero di mi piace di un selfie
		int likesCount = -1;
		
		/*
		 * query che ritorna il numero di likes di una selfie
		 */
		String likesCountString = 
					"SELECT "
				+ 		"COUNT(*) AS likes "
				+ 	"FROM "
				+ 		"user_like_selfie "
				+ 	"WHERE "
				+ 		"id_selfie = ?";
		
		// query formato SQL
		PreparedStatement likesCountSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			likesCountSQL = connect.prepareStatement(likesCountString);
			likesCountSQL.setInt(1, selfieId);
			ResultSet likesCountRes = likesCountSQL.executeQuery();
			
			/* se c'è un risultato */
			if (likesCountRes.next()) 
			{		
				// significa che allo user piace il selfie
				likesCount = likesCountRes.getInt("likes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return likesCount;
	}
	
}
