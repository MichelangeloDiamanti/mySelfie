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

public class FollowUtils {

	/**
	 * controlla se il primo utente segue il secondo
	 * 
	 * @param id_follower
	 * @param id_followed
	 * @param conn
	 * @return
	 */
	public static boolean checkFollow(int id_follower, int id_followed,
			Connection conn) {
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String checkFollowString = "SELECT * FROM user_follow_user WHERE id_follower=? AND id_followed=?";
		PreparedStatement checkFollowSQL;

		try {

			// prepara lo statement a partire dalla stringa
			checkFollowSQL = conn.prepareStatement(checkFollowString);
			// imposta i parametri nello statement
			checkFollowSQL.setInt(1, id_follower);
			checkFollowSQL.setInt(2, id_followed);
			// esegue la query
			ResultSet checkRes = checkFollowSQL.executeQuery();
			// se c'è un risultato signigica che il primo utente segue il
			// secondo
			if (checkRes.next()) {
				// in questo caso si imposta il risultato a true
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * controlla se il primo utente segue il secondo
	 * 
	 * SENZA CONNESSIONE
	 * 
	 * @param id_follower
	 * @param id_followed
	 * @return
	 */
	public static boolean checkFollow(int id_follower, int id_followed) {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String checkFollowString = "SELECT * FROM user_follow_user WHERE id_follower=? AND id_followed=?";
		PreparedStatement checkFollowSQL;

		try {

			// prepara lo statement a partire dalla stringa
			checkFollowSQL = conn.prepareStatement(checkFollowString);
			// imposta i parametri nello statement
			checkFollowSQL.setInt(1, id_follower);
			checkFollowSQL.setInt(2, id_followed);
			// esegue la query
			ResultSet checkRes = checkFollowSQL.executeQuery();
			// se c'è un risultato signigica che il primo utente segue il
			// secondo
			if (checkRes.next()) {
				// in questo caso si imposta il risultato a true
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
	 * prende in input l'id del follower e quello del followed e crea una
	 * connessione nel database, ritorna l'id del nuovo record 
	 * se tutto è andato a buon fine
	 * 
	 * @param id_follower
	 * @param id_followed
	 * @return
	 * @throws NamingException
	 */
	public static int followUser(int id_follower, int id_followed,
			Connection conn) throws NamingException {
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		// query in formato stringa e statement
		String followUserString = "INSERT INTO user_follow_user(id_follower, id_followed) VALUES (?, ?)";
		PreparedStatement followUserSQL;

		try {

			// prepara lo statement a partire dalla stringa
			followUserSQL = conn.prepareStatement(followUserString, Statement.RETURN_GENERATED_KEYS);
			// imposta i parametri nello statement
			followUserSQL.setInt(1, id_follower);
			followUserSQL.setInt(2, id_followed);
			// esegue la query
			int affectedRows = followUserSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"following user failed, no rows affected.");
			}
			// altrimenti
			ResultSet generatedKeys = followUserSQL.getGeneratedKeys();
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
	 * prende in input l'id del follower e quello del followed e crea una
	 * connessione nel database, ritorna true se tutto è andato a buon fine
	 * 
	 * SENZA CONNESSIONE
	 * 
	 * @param id_follower
	 * @param id_followed
	 * @return
	 * @throws NamingException
	 */
	public static int followUser(int id_follower, int id_followed)
			throws NamingException {

		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		// query in formato stringa e statement
		String followUserString = "INSERT INTO user_follow_user(id_follower, id_followed) VALUES (?, ?)";
		PreparedStatement followUserSQL;

		try {

			// prepara lo statement a partire dalla stringa
			followUserSQL = conn.prepareStatement(followUserString, Statement.RETURN_GENERATED_KEYS);
			// imposta i parametri nello statement
			followUserSQL.setInt(1, id_follower);
			followUserSQL.setInt(2, id_followed);
			// esegue la query
			int affectedRows = followUserSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"following user failed, no rows affected.");
			}
			// altrimenti
			ResultSet generatedKeys = followUserSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id del record generato
				generatedId = generatedKeys.getInt(1);
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
		return generatedId;
	}

	/**
	 * prende in input l'id del follower e quello del followed ed elimina la
	 * connessione nel database, ritorna true se tutto è andato a buon fine
	 * 
	 * @param id_follower
	 * @param id_followed
	 * @param conn
	 * @return
	 * @throws NamingException
	 */
	public static boolean unfollowUser(int id_follower, int id_followed,
			Connection conn) throws NamingException {
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String followUserString = "DELETE FROM user_follow_user WHERE id_follower=? AND id_followed=?;";
		PreparedStatement followUserSQL;

		try {

			// prepara lo statement a partire dalla stringa
			followUserSQL = conn.prepareStatement(followUserString);
			// imposta i parametri nello statement
			followUserSQL.setInt(1, id_follower);
			followUserSQL.setInt(2, id_followed);
			// esegue la query
			int affectedRows = followUserSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"unfollowing user failed, no rows affected.");
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
	 * prende in input l'id del follower e quello del followed ed elimina la
	 * connessione nel database, ritorna true se tutto è andato a buon fine
	 * 
	 * SENZA CONNESSIONE
	 * 
	 * @param id_follower
	 * @param id_followed
	 * @return
	 * @throws NamingException
	 */
	public static boolean unfollowUser(int id_follower, int id_followed)
			throws NamingException {

		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String followUserString = "DELETE FROM user_follow_user WHERE id_follower=? AND id_followed=?;";
		PreparedStatement followUserSQL;

		try {

			// prepara lo statement a partire dalla stringa
			followUserSQL = conn.prepareStatement(followUserString);
			// imposta i parametri nello statement
			followUserSQL.setInt(1, id_follower);
			followUserSQL.setInt(2, id_followed);
			// esegue la query
			int affectedRows = followUserSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"unfollowing user failed, no rows affected.");
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
	 * ritorna una lista contenente gli id degli utenti seguiti dallo user
	 * passato in input
	 * 
	 * @param id_user
	 * @param conn
	 * @return
	 */
	public static List<Integer> getFollowerdIds(int id_user, Connection conn) {
		// lista degli id delle persone seguite
		List<Integer> followedIds = new ArrayList<Integer>();
		// query in formato stringa e statement
		String followedIdsString = "SELECT id_followed FROM user_follow_user WHERE id_follower=?";
		PreparedStatement followedIdsSQL;
		try {

			// prepara lo statement a partire dalla stringa
			followedIdsSQL = conn.prepareStatement(followedIdsString);
			// imposta i parametri nello statement
			followedIdsSQL.setInt(1, id_user);
			// esegue la query
			ResultSet followedRes = followedIdsSQL.executeQuery();

			/* vengono scorsi tutti i followed */
			while (followedRes.next()) {
				followedIds.add(followedRes.getInt("id_followed"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return followedIds;
	}

	/**
	 * ritorna una lista contenente gli id degli utenti seguiti dallo user
	 * passato in input
	 * 
	 * SENZA CONNESSIONE
	 * 
	 * @param id_user
	 * @return
	 */
	public static List<Integer> getFollowerdIds(int id_user) {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// lista degli id delle persone seguite
		List<Integer> followedIds = new ArrayList<Integer>();
		// query in formato stringa e statement
		String followedIdsString = "SELECT id_followed FROM user_follow_user WHERE id_follower=?";
		PreparedStatement followedIdsSQL;
		try {

			// prepara lo statement a partire dalla stringa
			followedIdsSQL = conn.prepareStatement(followedIdsString);
			// imposta i parametri nello statement
			followedIdsSQL.setInt(1, id_user);
			// esegue la query
			ResultSet followedRes = followedIdsSQL.executeQuery();

			/* vengono scorsi tutti i followed */
			while (followedRes.next()) {
				followedIds.add(followedRes.getInt("id_followed"));
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
		return followedIds;
	}
	
	
	/**
	 * prende in input un'istanza di chiave primaria della tabella 
	 * user_tag_selfie e ritorna l'id dello user seguace
	 * 
	 * @param id_ufu
	 * @return
	 */
	public static int getFollowerUser(int id_ufu)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// id dell'untente seguace
		int user_id = -1;
		
		try 
		{       
			
			// query che ritorna l'id dello user a cui fa riferimento la chiave
			String followerUserString = 
						"SELECT "
					+ 		"id_follower "
					+ 	"FROM "
					+ 		"user_follow_user "
					+ 	"WHERE "
					+ 		"id_ufu = ?";
					
			PreparedStatement followerUserSQL = connect.prepareStatement(followerUserString);
			followerUserSQL.setInt(1, id_ufu);
			ResultSet followerUserRes = followerUserSQL.executeQuery(); 			
			
			if(followerUserRes.next())
			{
				user_id = followerUserRes.getInt("id_follower");
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
	 * user_tag_selfie e ritorna l'id dello user seguito
	 * 
	 * @param id_ufu
	 * @return
	 */
	public static int getFollowedUser(int id_ufu)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// id dell'untente seguace
		int user_id = -1;
		
		try 
		{       
			
			// query che ritorna l'id dello user a cui fa riferimento la chiave
			String followerUserString = 
						"SELECT "
					+ 		"id_followed "
					+ 	"FROM "
					+ 		"user_follow_user "
					+ 	"WHERE "
					+ 		"id_ufu = ?";
					
			PreparedStatement followerUserSQL = connect.prepareStatement(followerUserString);
			followerUserSQL.setInt(1, id_ufu);
			ResultSet followerUserRes = followerUserSQL.executeQuery(); 			
			
			if(followerUserRes.next())
			{
				user_id = followerUserRes.getInt("id_followed");
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}  	 
		return user_id;
	}

}
