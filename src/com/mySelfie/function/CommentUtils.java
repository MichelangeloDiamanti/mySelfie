package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static boolean addComment(int id_user, int id_selfie, String comment) throws SQLException {

		Context context = null; // contesto
		DataSource datasource = null; // dove pescare i dati
		Connection connect = null; // connessione al DB

		boolean result = false;
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
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		return result;
	}

	/**
	 * Prende in input l'id di un selfie, una connessione al DB e il contextpath
	 * restituisce tutti i commenti in formato HTML parsabili all'interno di una
	 * sezione commenti
	 * 
	 * @param id_selfie
	 * @param connect
	 * @param contextPath
	 * @return
	 */

	public static String getComments(int id_selfie, Connection connect,
			String contextPath) {
		/*
		 * vengono presi tutti i commenti (e utente che ha commentato) del
		 * selfie corrente
		 */
		String commentQuery = "SELECT CO.text, US.profilepic, US.username FROM ((Comment AS CO INNER JOIN Selfie AS SE ON CO.id_selfie = SE.id_selfie) INNER JOIN User AS US ON CO.id_user = US.id_user) WHERE SE.id_selfie = ? ORDER BY CO.date ASC";
		PreparedStatement commentSQL;
		String commentText = "";
		String commentUser = "";
		String commentUserPic = "";
		String HTMLres = "";

		try {
			commentSQL = connect.prepareStatement(commentQuery);
			commentSQL.setInt(1, id_selfie);
			ResultSet commentRes = commentSQL.executeQuery();

			while (commentRes.next()) {
				commentText = commentRes.getString("text");
				commentUser = commentRes.getString("username");
				commentUserPic = commentRes.getString("profilepic");

				// se viene trovato un hashtag in un commento, viene aggiunto un
				// link
				Pattern pattern = Pattern.compile("#[A-Za-z]+");
				Matcher matcher = pattern.matcher(commentText);

				while (matcher.find()) {
					String commentHashtag = "<a href=\"" + contextPath
							+ "/protected/hashtag/"
							+ matcher.group().substring(1)
							+ "\" class=\"hashtag_link\"> " + matcher.group()
							+ "</a> ";
					commentText = commentText.replaceAll(matcher.group(),
							commentHashtag);
				}

				HTMLres += "<li class=\"comment_user_container\">"
						+ "<a href=\""
						+ contextPath
						+ "/protected/profile/"
						+ commentUser
						+ "\">"
						+ "<span class=\"profile_pic_comment\" style=\"background-image: url('"
						+ contextPath + "/protected/resources/profilepics/"
						+ commentUserPic + "')\" ></span>"
						+ "<label class=\"profile_name_comment\">"
						+ commentUser + "</label>" + "</a></li>"
						+ "<li class=\"comment_container\">" + commentText
						+ "</li>";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return HTMLres;
	}

	/**
	 * Prende in input l'id di un selfie, una connessione al DB e il contextpath
	 * restituisce tutti i commenti in formato HTML parsabili all'interno di una
	 * sezione commenti
	 * 
	 * SENZA CONNESSIONE
	 * 
	 * @param id_selfie
	 * @param contextPath
	 * @return
	 */

	public static String getComments(int id_selfie, String contextPath) {

		Context context = null; // contesto
		DataSource datasource = null; // dove pescare i dati
		Connection connect = null; // connessione al DB

		/*
		 * vengono presi tutti i commenti (e utente che ha commentato) del
		 * selfie corrente
		 */
		String commentQuery = "SELECT CO.text, US.profilepic, US.username FROM ((Comment AS CO INNER JOIN Selfie AS SE ON CO.id_selfie = SE.id_selfie) INNER JOIN User AS US ON CO.id_user = US.id_user) WHERE SE.id_selfie = ? ORDER BY CO.date ASC";
		PreparedStatement commentSQL;
		String commentText = "";
		String commentUser = "";
		String commentUserPic = "";
		String HTMLres = "";

		try {

			// Get the context and create a connection
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in
			// 'WebContent/META-INF/context.xml'
			datasource = (DataSource) context
					.lookup("java:/comp/env/jdbc/mySelfie");
			connect = datasource.getConnection();

			commentSQL = connect.prepareStatement(commentQuery);
			commentSQL.setInt(1, id_selfie);
			ResultSet commentRes = commentSQL.executeQuery();

			while (commentRes.next()) {
				commentText = commentRes.getString("text");
				commentUser = commentRes.getString("username");
				commentUserPic = commentRes.getString("profilepic");

				// se viene trovato un hashtag in un commento, viene aggiunto un
				// link
				Pattern pattern = Pattern.compile("#[A-Za-z]+");
				Matcher matcher = pattern.matcher(commentText);

				while (matcher.find()) {
					String commentHashtag = "<a href=\"" + contextPath
							+ "/protected/hashtag/"
							+ matcher.group().substring(1)
							+ "\" class=\"hashtag_link\"> " + matcher.group()
							+ "</a> ";
					commentText = commentText.replaceAll(matcher.group(),
							commentHashtag);
				}

				HTMLres += "<li class=\"comment_user_container\">"
						+ "<a href=\""
						+ contextPath
						+ "/protected/profile/"
						+ commentUser
						+ "\">"
						+ "<span class=\"profile_pic_comment\" style=\"background-image: url('"
						+ contextPath + "/protected/resources/profilepics/"
						+ commentUserPic + "')\" ></span>"
						+ "<label class=\"profile_name_comment\">"
						+ commentUser + "</label>" + "</a></li>"
						+ "<li class=\"comment_container\">" + commentText
						+ "</li>";
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

		return HTMLres;
	}
	

	
	public static List<Comment> getSelfieComments(int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// dichiaro una lista di hashtags dove caricare i risultati
		List<Comment> selfieCommentsList = new ArrayList<Comment>();
		// hashtag di appoggio per caricare la lista
		Comment comment = new Comment();
		
		
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
				/* vengono presi tutti gli attributi del hashtag e messi nel hashtag di appoggio */				
				comment.setId_comment(selfieCommentsRes.getInt("id_comment"));
				comment.setId_user(selfieCommentsRes.getInt("id_user"));
				comment.setId_selfie(selfieCommentsRes.getInt("id_selfie"));
				comment.setText(selfieCommentsRes.getString("text"));
				comment.setDate(selfieCommentsRes.getDate("date"));
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

}
