package com.mySelfie.function;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.exception.usernameInUseException;

public final class UserUtils {

	/**
	 * controlla se lo user esiste (con connessione)
	 * 
	 * @param username
	 * @param conn
	 * @return
	 * @throws NamingException
	 */
	public static boolean exist(String username, Connection conn)
			throws NamingException {

		// true se l'utente esiste
		boolean exist = false;

		// verifica che lo username non sia gia in uso
		String userExistStr = "SELECT id_user FROM User WHERE username = ? ";
		PreparedStatement userExistSQL;
		try {
			userExistSQL = conn.prepareStatement(userExistStr);
			userExistSQL.setString(1, username);
			ResultSet userExistRes = userExistSQL.executeQuery();

			// se lo user esiste imposta il flag a true
			if (userExistRes.next())
				exist = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return exist;
	}

	/**
	 * controlla se lo user esiste (senza connessione)
	 * 
	 * @param username
	 * @return
	 * @throws NamingException
	 */
	public static boolean exist(String username) throws NamingException {

		return false;
	}

	/**
	 * registra un nuovo utente
	 * 
	 * @param out
	 * @param m
	 * @throws NamingException
	 * @throws usernameInUseException
	 */
	public static void signup(PrintWriter out, Map<String, String> m)
			throws NamingException, usernameInUseException {
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
			connect = datasource.getConnection();

			// verifica che lo username non sia gia in uso
			String uniqueNickStr = "SELECT id_user FROM User WHERE username = ? ";
			PreparedStatement uniqueNickSQL = connect
					.prepareStatement(uniqueNickStr);
			uniqueNickSQL.setString(1, m.get("username"));
			ResultSet uniqueNickRes = uniqueNickSQL.executeQuery();

			boolean resEmpty = uniqueNickRes.next();

			// Se il nick è univoco
			if (!resEmpty) {
				// // data di nascita passata come stringa, va convertita nel
				// formato data di SQL
				// DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				// java.util.Date dataDiNascitaJava = null;
				// try {
				// dataDiNascitaJava = format.parse( m.get("dataDiNascita") );
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// java.sql.Date dataDiNascitaSql = new java.sql.Date(
				// dataDiNascitaJava.getTime() );
				//
				// costruisce ed esegue la query per inserire un nuovo record user
				String newUserQuery = "INSERT INTO User (username, password, email, profilepic ) VALUES (?, ?, ?, ?)";
				PreparedStatement newUserStatement = connect.prepareStatement(newUserQuery, Statement.RETURN_GENERATED_KEYS);
				newUserStatement.setString(1, m.get("username"));
				newUserStatement.setString(2, m.get("password"));
				newUserStatement.setString(3, m.get("email"));
				newUserStatement.setString(4, m.get("profilePic"));
				int affectedRows = newUserStatement.executeUpdate();
				
				int idNewUser= -1;
				ResultSet generatedKeys = newUserStatement.getGeneratedKeys();
	            if (generatedKeys.next()) 
		           	idNewUser = generatedKeys.getInt(1);
	            else
					throw new usernameInUseException();
				
		            	
				// costruisce ed esegue la query che inserisce l' utente setesso tra gli utenti che segue
				String ufuQuery = "INSERT INTO user_follow_user (id_follower, id_followed) VALUES (?, ?)";
				PreparedStatement ufuStatement = connect.prepareStatement(ufuQuery);
				ufuStatement.setInt(1, idNewUser);
				ufuStatement.setInt(2, idNewUser);
				ufuStatement.executeUpdate();

				
			} else {
				// se lo username è in uso viene generata un'eccezione
				throw new usernameInUseException();
			}
		} catch (SQLException e) {
			e.printStackTrace(out);
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace(out);
			}
		}
	}

	/**
	 * la funzione controlla se lo username passato è in uso, in caso
	 * affermativo torna falso, altrimenti vero
	 * 
	 * @param username
	 * @return
	 * @throws NamingException
	 */
	public static boolean usernameAvailable(String username)
			throws NamingException {
		Context context = null; // contesto
		DataSource datasource = null; // dove pescare i dati
		Connection connect = null; // connessione al DB

		boolean resEmpty = false; // true se lo username NON è in uso, falso
									// altrimenti

		try {
			// Get the context and create a connection
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in
			// 'WebContent/META-INF/context.xml'
			datasource = (DataSource) context
					.lookup("java:/comp/env/jdbc/mySelfie");
			connect = datasource.getConnection();

			// verifica che lo username non sia gia in uso
			String uniqueNickStr = "SELECT id_user FROM User WHERE username = ? ";
			PreparedStatement uniqueNickSQL = connect
					.prepareStatement(uniqueNickStr);
			uniqueNickSQL.setString(1, username);
			ResultSet uniqueNickRes = uniqueNickSQL.executeQuery();

			resEmpty = !(uniqueNickRes.next()); // se c'è un risultato (username
												// in uso) la
												// funzione uniqueNickRes.next()
												// torna vero
												// quindi negato torna falso

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

		return resEmpty;
	}

	/**
	 * la funzione controlla se lo username passato è in uso, in caso
	 * affermativo torna falso, altrimenti vero (con connessione)
	 * 
	 * @param username
	 * @param conn
	 * @return
	 * @throws NamingException
	 */
	public static boolean usernameAvailable(String username, Connection conn)
			throws NamingException {

		// true se lo username NON è in uso, falso altrimenti
		boolean resEmpty = false;

		// verifica che lo username non sia gia in uso
		String uniqueNickStr = "SELECT id_user FROM User WHERE username = ? ";
		PreparedStatement uniqueNickSQL;
		try {
			uniqueNickSQL = conn.prepareStatement(uniqueNickStr);
			uniqueNickSQL.setString(1, username);
			ResultSet uniqueNickRes = uniqueNickSQL.executeQuery();

			// se c'è un risultato (username in uso) la funzione
			// uniqueNickRes.next() torna vero quindi negato torna falso
			resEmpty = !(uniqueNickRes.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resEmpty;
	}

	/**
	 * @param username
	 * @param conn
	 * @return
	 * @throws NamingException
	 * 
	 *             ritorna l'id dell'utente a partire dal username (con
	 *             connessione)
	 */
	public static int getId(String username, Connection conn)
			throws NamingException {
		// true se lo username NON è in uso, falso altrimenti
		int id_user = 0;

		// verifica che lo username non sia gia in uso
		String userIdString = "SELECT id_user FROM User WHERE username = ? ";
		PreparedStatement userIdSQL;
		try {
			userIdSQL = conn.prepareStatement(userIdString);
			userIdSQL.setString(1, username);
			ResultSet userIdRes = userIdSQL.executeQuery();

			// se c'è un risultato
			if (userIdRes.next()) {
				// imposta l'id dell'utente
				id_user = userIdRes.getInt("id_user");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ritorna l'id dell'utente
		return id_user;
	}

	/**
	 * @param username
	 * @param conn
	 * @return
	 * @throws NamingException
	 * 
	 *             ritorna l'id dell'utente a partire dal username (senza
	 *             connessione)
	 */
	public static int getId(String username) throws NamingException {
		return 0;
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
	public static boolean userTagSelfie(int id_user, int id_selfie,
			Connection conn) throws NamingException {

		boolean result = false;
		// query in formato stringa e statement
		String userSelfieString = "INSERT INTO user_tag_selfie(id_user, id_selfie) VALUES (?, ?)";
		PreparedStatement userSelfieSQL;

		try {

			// prepara lo statement a partire dalla stringa
			userSelfieSQL = conn.prepareStatement(userSelfieString);
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

}
