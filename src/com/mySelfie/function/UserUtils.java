package com.mySelfie.function;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.exception.NoSuchUserException;
import com.mySelfie.exception.usernameInUseException;
import com.mySelfie.security.PasswordHash;

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

		boolean exst = false;

		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// controllo se lo user esiste usando il metodo con connessione
		exst = UserUtils.exist(username, connect);
		// torna l'esito del controllo
		return exst;
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
				// ricava l'hash dalla password
				String hashedPassword = PasswordHash.createHash(m
						.get("password"));

				// costruisce ed esegue la query per inserire un nuovo record
				// user
				String newUserQuery = "INSERT INTO User (username, password, email, profilepic ) VALUES (?, ?, ?, ?)";
				PreparedStatement newUserStatement = connect.prepareStatement(
						newUserQuery, Statement.RETURN_GENERATED_KEYS);
				newUserStatement.setString(1, m.get("username"));
				newUserStatement.setString(2, hashedPassword);
				newUserStatement.setString(3, m.get("email"));
				newUserStatement.setString(4, m.get("profilePic"));
				int affectedRows = newUserStatement.executeUpdate();

				// se non è stato possibile inserire il nuovo utente
				if (affectedRows == 0) {
					throw new SQLException("signup failed, no rows affected.");
				}
				// se il signup è andato a buon fine
				else {

					int idNewUser = -1;
					ResultSet generatedKeys = newUserStatement
							.getGeneratedKeys();
					if (generatedKeys.next())
						idNewUser = generatedKeys.getInt(1);
					else
						throw new usernameInUseException();

					// costruisce ed esegue la query che inserisce l' utente
					// setesso tra gli utenti che segue
					String ufuQuery = "INSERT INTO user_follow_user (id_follower, id_followed) VALUES (?, ?)";
					PreparedStatement ufuStatement = connect
							.prepareStatement(ufuQuery);
					ufuStatement.setInt(1, idNewUser);
					ufuStatement.setInt(2, idNewUser);
					ufuStatement.executeUpdate();
				}

			} else {
				// se lo username è in uso viene generata un'eccezione
				throw new usernameInUseException();
			}
		} catch (SQLException e) {
			e.printStackTrace(out);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		int id_user = -1;

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
	public static int getId(String username) {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// id da ritornare inizialmente impostato a -1 (inesistente)
		int id_user = -1;

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
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// ritorna l'id dell'utente
		return id_user;
	}
	
	/**
	 * Passo in input una email e ritorna l'id dell'untente a cui è associata
	 * 
	 * @param email				mail dello user
	 * @return					id dello user, -1 se non esiste
	 * @throws NamingException	
	 */
	public static int getIdByEmail(String email) throws NoSuchUserException{
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// id da ritornare inizialmente impostato a -1 (inesistente)
		int id_user = -1;
		
		// query che torna l'id dello username con la mail passata in input
		String userIdString = "SELECT id_user FROM User WHERE email = ? ";
		PreparedStatement userIdSQL;
		try {
			userIdSQL = conn.prepareStatement(userIdString);
			userIdSQL.setString(1, email);
			ResultSet userIdRes = userIdSQL.executeQuery();
			
			// se c'è un risultato
			if (userIdRes.next()) {
				// imposta l'id dell'utente
				id_user = userIdRes.getInt("id_user");
			}
			// se non c'è uno user con quel nome spara un'eccezione
			else
			{
				throw new NoSuchUserException("There is no user with the email specified.");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// ritorna l'id dell'utente
		return id_user;
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

	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce lo username
	 * 
	 * @param userId
	 * @return
	 */
	public static String getUsernameById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String username = null;

		/*
		 * query che restituisce lo username di un utente grazie all'id
		 */
		String usernameString = "SELECT " + "US.username " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";

		// query formato SQL
		PreparedStatement usernameSQL;

		try {
			// imposto i parametri ed eseguo la query
			usernameSQL = connect.prepareStatement(usernameString);
			usernameSQL.setInt(1, userId);
			ResultSet usernameRes = usernameSQL.executeQuery();

			/* se c'è un risultato */
			if (usernameRes.next()) {
				// viene impostato lo username
				username = usernameRes.getString("username");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// ritorna la lista dei selfie
		return username;
	}

	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce l'immagine
	 * di profilo
	 * 
	 * @param userId
	 * @return
	 */
	public static String getUserProfilepicById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String profilepic = null;

		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String profilepicString = "SELECT " + "US.profilepic " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";

		// query formato SQL
		PreparedStatement profilepicSQL;

		try {
			// imposto i parametri ed eseguo la query
			profilepicSQL = connect.prepareStatement(profilepicString);
			profilepicSQL.setInt(1, userId);
			ResultSet profilepicRes = profilepicSQL.executeQuery();

			/* se c'è un risultato */
			if (profilepicRes.next()) {
				// viene impostato lo username
				profilepic = profilepicRes.getString("profilepic");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// ritorna la lista dei selfie
		return profilepic;
	}

	/**
	 * ricava la password criptata di uno user passato tramite id
	 * 
	 * @param me_id
	 * @return
	 */
	public static String getHashedPassword(int me_id) {
		// password da ritornare
		String Hpass = null;
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// ricava la password criptata
		String passQuery = "SELECT password FROM User WHERE id_user = ? ";
		PreparedStatement passSQL;
		try {
			passSQL = connect.prepareStatement(passQuery);
			passSQL.setInt(1, me_id);
			ResultSet passRes = passSQL.executeQuery();

			while (passRes.next())
				Hpass = passRes.getString("password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return Hpass;

	}
	
	public static boolean setNewPassword(int userId, String password) throws SQLException {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String setNewPasswordString = "UPDATE User SET password = ? where id_user = ?";
		PreparedStatement setNewPasswordSQL;

		try {

			// prepara lo statement a partire dalla stringa
			setNewPasswordSQL = conn.prepareStatement(setNewPasswordString);
			// imposta i parametri nello statement
			setNewPasswordSQL.setString(1, password);
			setNewPasswordSQL.setInt(2, userId);
			// esegue la query
			int affectedRows = setNewPasswordSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"Resetting credentials failed, no rows affected.");
			}
			// altrimenti
			else {
				result = true;
			}
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static boolean setValid(int userId) {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String setValidString = "UPDATE User SET valid = 1 where id_user = ?";
		PreparedStatement setValidSQL;

		try {

			// prepara lo statement a partire dalla stringa
			setValidSQL = conn.prepareStatement(setValidString);
			// imposta i parametri nello statement
			setValidSQL.setInt(1, userId);
			// esegue la query
			int affectedRows = setValidSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"validating user account failed, no rows affected.");
			}
			// altrimenti
			else {
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * conta i follower di uno user tramite id
	 * 
	 * @param me_id
	 * @return
	 */
	public static int getCountFollowers(int me_id) {
		int followers = 0;

		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// ricava la password criptata
		String cfQuery = "SELECT COUNT(*) AS followers FROM user_follow_user WHERE id_followed = ? ";
		PreparedStatement cfSQL;
		try {
			cfSQL = connect.prepareStatement(cfQuery);
			cfSQL.setInt(1, me_id);
			ResultSet cfRes = cfSQL.executeQuery();

			while (cfRes.next())
				followers = cfRes.getInt("followers");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// ogni utente segue anche se stesso, quindi non va contato
		return followers - 1;
	}

	/**
	 * conta gli utenti seguiti da uno user tramite id
	 * 
	 * @param me_id
	 * @return
	 */
	public static int getCountFollowing(int me_id) {
		int following = 0;

		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// ricava la password criptata
		String cfQuery = "SELECT COUNT(*) AS following FROM user_follow_user WHERE id_follower = ? ";
		PreparedStatement cfSQL;
		try {
			cfSQL = connect.prepareStatement(cfQuery);
			cfSQL.setInt(1, me_id);
			ResultSet cfRes = cfSQL.executeQuery();

			while (cfRes.next())
				following = cfRes.getInt("following");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return following - 1;
	}

	/**
	 * conta i post di uno user tramite id
	 * 
	 * @param me_id
	 * @return
	 */
	public static int getCountPosts(int me_id) {
		int posts = 0;

		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// ricava la password criptata
		String cpQuery = "SELECT COUNT(*) AS posts FROM Selfie WHERE uploader = ? ";
		PreparedStatement cpSQL;
		try {
			cpSQL = connect.prepareStatement(cpQuery);
			cpSQL.setInt(1, me_id);
			ResultSet cpRes = cpSQL.executeQuery();

			while (cpRes.next())
				posts = cpRes.getInt("posts");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return posts;
	}
	
	
	
	/**
	 * ritorna una lista di user che devono ancora essere autenticati
	 * presenti nel DB da almeno un giorno
	 * 
	 * @return 
	 */
	public static List<Integer> getExpiredUsers() {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di id dove caricare i risultati
		List<Integer> expUsrId = new ArrayList<Integer>();
		
		/*
		 * query che restituisce tutti i post pubblicati dagli utenti seguiti dallo user
		 */
		String expUsrIdString = "SELECT id_user FROM User WHERE valid=0 AND registration_date < NOW()-INTERVAL 1 DAY";
		// query formato SQL
		PreparedStatement expUsrIdSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			expUsrIdSQL = connect.prepareStatement(expUsrIdString);
			ResultSet expUsrIdRes = expUsrIdSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (expUsrIdRes.next()) 
			{		
				expUsrId.add(expUsrIdRes.getInt("id_user"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
					
		//ritorna la lista degli utenti
		return expUsrId;

	}
	
	

}
