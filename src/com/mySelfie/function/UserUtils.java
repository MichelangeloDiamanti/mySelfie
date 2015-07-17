package com.mySelfie.function;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import com.mySelfie.entity.User;
import com.mySelfie.exception.EmailInUseException;
import com.mySelfie.exception.NoSuchUserException;
import com.mySelfie.exception.UsernameInUseException;
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
        
		// chiude la connessione
        try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		
		// torna l'esito del controllo
		return exst;
	}

	
	/**
	 * Prende in input un'oggetto user, controlla che lo username e la mail non siano
	 * gia in uso, quindi lo memorizza nel DB
	 * 
	 * @param user						user da registrare
	 * @return							id nuovo user se tutto è andato bene
	 * @throws EmailInUseException		se la mail è in uso
	 * @throws UsernameInUseException	se lo username è in uso
	 */
	public static int signUp(User user) throws EmailInUseException, UsernameInUseException {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// intero rappresentante l'id del nuovo user
		int idNewUser = -1;
		
		boolean checkUsername = UserUtils.usernameAvailable(user.getusername());
		// se lo username scelto è disponibile
		if(checkUsername)
		{
			boolean checkEmail = UserUtils.checkEmail(user.getEmail());
			// se la email fornita è libera
			if(checkEmail)
			{
				try {
					// ricava l'hash dalla password
					String hashedPassword = PasswordHash.createHash(user.getPassword());
					// costruisce la query per inserire un nuovo record
					String newUserQuery = 
								"INSERT INTO "
							+ 		"User (username, password, email, profilepic, registration_date ) "
							+ 	"VALUES "
							+ 		"(?, ?, ?, ?, now())";
					PreparedStatement newUserStatement = conn.prepareStatement(newUserQuery, Statement.RETURN_GENERATED_KEYS);
					newUserStatement.setString(1, user.getusername());
					newUserStatement.setString(2, hashedPassword);
					newUserStatement.setString(3, user.getEmail());
					newUserStatement.setString(4, user.getProfilepic());
					// esegue la query e si fa ritornare le righe modificate
					int affectedRows = newUserStatement.executeUpdate();
					// se non è stato possibile inserire il nuovo utente
					if (affectedRows == 0) {
						throw new SQLException("signup failed, no rows affected.");
					}
					// se il signup è andato a buon fine
					else {
						
						ResultSet generatedKeys = newUserStatement.getGeneratedKeys();
						// query che inserisce l' utente stesso tra gli utenti che segue
						if (generatedKeys.next())
						{
							// ricava l'id del nuovo utente
							idNewUser = generatedKeys.getInt(1);
							// costruisce ed esegue la query
							String ufuQuery = "INSERT INTO user_follow_user (id_follower, id_followed) VALUES (?, ?)";
							PreparedStatement ufuStatement = conn.prepareStatement(ufuQuery);
							ufuStatement.setInt(1, idNewUser);
							ufuStatement.setInt(2, idNewUser);
							ufuStatement.executeUpdate();
						}
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
		            // chiude la connessione
		            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		        }
			}
			else { // nel caso la mail fosse in uso
				throw new EmailInUseException("l'indirizzo email " + user.getEmail() + "risulta essere gia in uso");
			}
		}
		else
		{	// nel caso lo username fosse in uso
			throw new UsernameInUseException("lo username " + user.getusername() + "risulta essere gia in uso");
		}
		
		return idNewUser;
	}
	

	/**
	 * la funzione controlla se lo username passato è in uso, in caso
	 * affermativo torna falso, altrimenti vero
	 * 
	 * @param username
	 * @return
	 * @throws NamingException
	 */
	public static boolean usernameAvailable(String username){
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
		} catch (NamingException e) {
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
	 * Prende in input una mail e controlla se è in uso
	 * 
	 * @param email		email da controllare
	 * @return			TRUE se la mail NON è in uso, FALSE altrimenti.
	 */
	public static boolean checkEmail(String email)
	{
		Connection conn = ConnectionManager.getConnection();
		
		// true se la mail è libera
		boolean checkEmail = false;

		try {
			// verifica che la mail non sia gia in uso
			String checkEmailString = "SELECT id_user FROM User WHERE email = ? ";
			PreparedStatement checkEmailSQL;
			checkEmailSQL = conn.prepareStatement(checkEmailString);
			checkEmailSQL.setString(1, email);
			ResultSet checkEmailRes = checkEmailSQL.executeQuery();

			// se la mail non è presa imposta il flag a true
			if (!checkEmailRes.next())
				checkEmail = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		// return flag mail
		return checkEmail;
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


	/**
	 * prende in input uno username e ritorna informazioni riguardanti l'utente
	 * a cui appartiene
	 * 
	 * @param username
	 *            username da ricercare
	 * @return utente a cui appartiene lo username
	 */
	public static User getUserByUsername(String username) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();

		// dichiaro uno user da restituire
		User user = new User();
		/*
		 * query che restituisce tutti gli user che hanno lo username che
		 * contiene la keyword
		 */
		String userMatchesString = "SELECT " + "* " + "FROM " + "User "
				+ "WHERE " + "username = ?";
		// query formato SQL
		PreparedStatement userMatchesSQL;

		try {
			// imposto i parametri ed eseguo la query
			userMatchesSQL = connect.prepareStatement(userMatchesString);
			userMatchesSQL.setString(1, username);
			ResultSet userMatchesRes = userMatchesSQL.executeQuery();

			/* vengono scorsi tutti i selfie */
			if (userMatchesRes.next()) {

				/*
				 * vengono presi tutti gli attributi dello user e messi nello
				 * user di appoggio
				 */
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
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

		// ritorna l'utente trovato
		return user;
	}

	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce le note
	 * 
	 * @param userId
	 * @return
	 */
	public static String getUserNotesById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String notes = null;

		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String notesString = "SELECT " + "US.notes " + "FROM User AS US "
				+ "WHERE " + "US.id_user = ?";

		// query formato SQL
		PreparedStatement notesSQL;

		try {
			// imposto i parametri ed eseguo la query
			notesSQL = connect.prepareStatement(notesString);
			notesSQL.setInt(1, userId);
			ResultSet notesRes = notesSQL.executeQuery();

			/* se c'è un risultato */
			if (notesRes.next()) {
				// viene impostato lo username
				notes = notesRes.getString("notes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

		// ritorna la lista dei selfie
		return notes;
	}

	
	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce il nome
	 * 
	 * @param userId
	 * @return
	 */
	public static String getNameById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String name = null;
	
		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String nameString = "SELECT " + "US.name " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";
	
		// query formato SQL
		PreparedStatement nameSQL;
	
		try {
			// imposto i parametri ed eseguo la query
			nameSQL = connect.prepareStatement(nameString);
			nameSQL.setInt(1, userId);
			ResultSet nameRes = nameSQL.executeQuery();
	
			/* se c'è un risultato */
			if (nameRes.next()) {
				// viene impostato lo username
				name = nameRes.getString("name");
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
		return name;
	}
	
	
	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce il cognome
	 * 
	 * @param userId
	 * @return
	 */
	public static String getSurnameById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String surname = null;
	
		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String surnameString = "SELECT " + "US.surname " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";
	
		// query formato SQL
		PreparedStatement surnameSQL;
	
		try {
			// imposto i parametri ed eseguo la query
			surnameSQL = connect.prepareStatement(surnameString);
			surnameSQL.setInt(1, userId);
			ResultSet surnameRes = surnameSQL.executeQuery();
	
			/* se c'è un risultato */
			if (surnameRes.next()) {
				// viene impostato lo username
				surname = surnameRes.getString("surname");
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
		return surname;
	}
	


	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce il sesso
	 * 
	 * @param userId
	 * @return
	 */
	public static String getGenderById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String gender = null;
	
		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String genderString = "SELECT " + "US.gender " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";
	
		// query formato SQL
		PreparedStatement genderSQL;
	
		try {
			// imposto i parametri ed eseguo la query
			genderSQL = connect.prepareStatement(genderString);
			genderSQL.setInt(1, userId);
			ResultSet genderRes = genderSQL.executeQuery();
	
			/* se c'è un risultato */
			if (genderRes.next()) {
				// viene impostato lo username
				gender = genderRes.getString("gender");
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
		return gender;
	}
	
	
	
	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce la data di nascita
	 * 
	 * @param userId
	 * @return
	 */
	public static String getBirthdateById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String birthdate = null;
	
		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String birthdateString = "SELECT " + "US.birthdate " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";
	
		// query formato SQL
		PreparedStatement birthdateSQL;
	
		try {
			// imposto i parametri ed eseguo la query
			birthdateSQL = connect.prepareStatement(birthdateString);
			birthdateSQL.setInt(1, userId);
			ResultSet birthdateRes = birthdateSQL.executeQuery();
	
			/* se c'è un risultato */
			if (birthdateRes.next()) {
				// viene impostato lo username
				birthdate = birthdateRes.getString("birthdate");
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
		
		//va convertito perchè il tipo Date sennò da problemi
		if(birthdate==null)
			birthdate="";
		
		// ritorna la lista dei selfie
		return birthdate;
	}
	
	
	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce la citta
	 * 
	 * @param userId
	 * @return
	 */
	public static String getCityById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String city = null;
	
		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String cityString = "SELECT " + "US.city " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";
	
		// query formato SQL
		PreparedStatement citySQL;
	
		try {
			// imposto i parametri ed eseguo la query
			citySQL = connect.prepareStatement(cityString);
			citySQL.setInt(1, userId);
			ResultSet cityRes = citySQL.executeQuery();
	
			/* se c'è un risultato */
			if (cityRes.next()) {
				// viene impostato lo username
				city = cityRes.getString("city");
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
		return city;
	}
	
	
	/**
	 * Metodo che prende in input l'id di un utente e ne restituisce il telefono
	 * 
	 * @param userId
	 * @return
	 */
	public static String getPhoneById(int userId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// username da ritornare
		String phone = null;
	
		/*
		 * query che restituisce l'immagine di profilo di un utente grazie al
		 * suo id
		 */
		String phoneString = "SELECT " + "US.phone " + "FROM "
				+ "User AS US " + "WHERE " + "US.id_user = ?";
	
		// query formato SQL
		PreparedStatement phoneSQL;
	
		try {
			// imposto i parametri ed eseguo la query
			phoneSQL = connect.prepareStatement(phoneString);
			phoneSQL.setInt(1, userId);
			ResultSet phoneRes = phoneSQL.executeQuery();
	
			/* se c'è un risultato */
			if (phoneRes.next()) {
				// viene impostato lo username
				phone = phoneRes.getString("phone");
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
		return phone;
	}
	
	
	
}
