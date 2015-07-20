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
import com.mySelfie.entity.Comment;

public class CommentUtils {

	/**
	 * Prende in input l'id dell'utente che sta commentando, l'id del selfie su
	 * cui commentare e il testo del commento. Inserisce il tutto nel DB e
	 * ritorna true se il processo è andato a buon fine.
	 * 
	 * @param id_user
	 * @param id_selfie
	 * @param comment
	 * @param connect
	 * @return
	 */
	public static boolean addComment(int id_user, int id_selfie,
			String comment, Connection connect) throws SQLException {
		boolean result = false;
		// query in formato stringa e statement
		String newCommentString = "INSERT INTO Comment(id_user, id_selfie, text, date) VALUES (?, ?, ?, now())";
		PreparedStatement newCommentSQL;

		try {

			// prepara lo statement a partire dalla stringa
			newCommentSQL = connect.prepareStatement(newCommentString);
			// imposta i parametri nello statement
			newCommentSQL.setInt(1, id_user);
			newCommentSQL.setInt(2, id_selfie);
			newCommentSQL.setString(3, comment);
			// esegue la query
			int affectedRows = newCommentSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"couldn't add comment, no rows affected.");
			}
			// altrimenti
			else {
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Prende in input l'id dell'utente che sta commentando, l'id del selfie su
	 * cui commentare e il testo del commento. Inserisce il tutto nel DB e
	 * ritorna true se il processo è andato a buon fine.
	 * 
	 * SENZA CONNESSIONE
	 * 
	 * @param id_user
	 * @param id_selfie
	 * @param comment
	 * @return
	 */
	public static int addComment(int id_user, int id_selfie, String comment) throws SQLException {

		Context context = null; // contesto
		DataSource datasource = null; // dove pescare i dati
		Connection connect = null; // connessione al DB

		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		// query in formato stringa e statement
		String newCommentString = "INSERT INTO Comment(id_user, id_selfie, text, date) VALUES (?, ?, ?, now())";
		PreparedStatement newCommentSQL;

		try {

			// Get the context and create a connection
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in
			// 'WebContent/META-INF/context.xml'
			datasource = (DataSource) context
					.lookup("java:/comp/env/jdbc/mySelfie");
			connect = datasource.getConnection();

			// prepara lo statement a partire dalla stringa
			newCommentSQL = connect.prepareStatement(newCommentString, Statement.RETURN_GENERATED_KEYS);
			// imposta i parametri nello statement
			newCommentSQL.setInt(1, id_user);
			newCommentSQL.setInt(2, id_selfie);
			newCommentSQL.setString(3, comment);
			// esegue la query
			int affectedRows = newCommentSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"couldn't add comment, no rows affected.");
			}
		    // se il commento è stato inserito correttamente ricavo la chiave generata
			ResultSet generatedKeys = newCommentSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id del record generato
				generatedId = generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		return generatedId;
	}

	

	
	public static List<Comment> getSelfieComments(int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// dichiaro una lista di hashtags dove caricare i risultati
		List<Comment> selfieCommentsList = new ArrayList<Comment>();
				
		
		/*
		 * query che restituisce tutti gli hashtags di un selfie
		 */
		String selfieCommentsString = 
					"SELECT "
				+ 		"CO.* "
				+ 	"FROM "
				+ 		"Comment AS CO INNER JOIN "
				+ 		"Selfie AS SE ON CO.id_selfie = SE.id_selfie "
				+ 	"WHERE "
				+ 		"SE.id_selfie = ? "
				+ 	"ORDER BY "
				+ 		"CO.date ASC";

		// query formato SQL
		PreparedStatement selfieCommentsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			selfieCommentsSQL = connect.prepareStatement(selfieCommentsString);
			selfieCommentsSQL.setInt(1, selfieId);
			ResultSet selfieCommentsRes = selfieCommentsSQL.executeQuery();
			
			/* vengono scorsi tutti gli hashtags */
			while (selfieCommentsRes.next()) 
			{
				// hashtag di appoggio per caricare la lista
				Comment comment = new Comment();
				
				/* vengono presi tutti gli attributi del hashtag e messi nel hashtag di appoggio */				
				comment.setId_comment(selfieCommentsRes.getInt("id_comment"));
				comment.setId_user(selfieCommentsRes.getInt("id_user"));
				comment.setId_selfie(selfieCommentsRes.getInt("id_selfie"));
				comment.setText(selfieCommentsRes.getString("text"));
				comment.setDate(selfieCommentsRes.getTimestamp("date"));

				// l'hashtag di appoggio viene aggiunto alla lista
				selfieCommentsList.add(comment);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista di commenti
		return selfieCommentsList;

	}
	
	/**
	 * prende in input un'istanza di chiave primaria della tabella 
	 * Comment e ritorna l'id del commentatore
	 * 
	 * @param commentId		chiave primaria della tabella Comment
	 * @return				commentatore
	 */
	public static int getCommentator(int commentId)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// id del selfie piaciuto
		int commentatorId = -1;
		
		try 
		{       
			
			// query che ritorna l'id del selfie piaciuto
			String getCommentatorString = 
					"SELECT "
							+ 		"id_user "
							+ 	"FROM "
							+ 		"Comment "
							+ 	"WHERE "
							+ 		"id_comment = ?";
			
			PreparedStatement getCommentatorSQL = connect.prepareStatement(getCommentatorString);
			getCommentatorSQL.setInt(1, commentId);
			ResultSet getCommentatorRes = getCommentatorSQL.executeQuery(); 			
			
			if(getCommentatorRes.next())
			{
				commentatorId = getCommentatorRes.getInt("id_user");
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}  	 
		return commentatorId;
	}

	/**
	 * prende in input un'istanza di chiave primaria della tabella 
	 * Comment e ritorna l'id del selfie commentato
	 * 
	 * @param commentId		chiave primaria della tabella Comment
	 * @return				commentatore
	 */
	public static int getCommentedSelfie(int commentId)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// id del selfie piaciuto
		int commentedSelfieId = -1;
		
		try 
		{       
			
			// query che ritorna l'id del selfie piaciuto
			String getCommentedSelfieString = 
					"SELECT "
							+ 		"id_selfie "
							+ 	"FROM "
							+ 		"Comment "
							+ 	"WHERE "
							+ 		"id_comment = ?";
			
			PreparedStatement getCommentedSelfieSQL = connect.prepareStatement(getCommentedSelfieString);
			getCommentedSelfieSQL.setInt(1, commentId);
			ResultSet getCommentedSelfieRes = getCommentedSelfieSQL.executeQuery(); 			
			
			if(getCommentedSelfieRes.next())
			{
				commentedSelfieId = getCommentedSelfieRes.getInt("id_selfie");
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}  	 
		return commentedSelfieId;
	}
	
}
