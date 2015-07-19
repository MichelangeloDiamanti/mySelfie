package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.entity.Notification;
import com.mySelfie.entity.User;

public class NotificationUtils {

	/**
	 * Prende in input uno user e ritorna il numero di notifiche non ancora
	 * lette
	 * 
	 * @param user	User a cui sono associate le notifiche
	 * @return		numero di notifiche non ancora lette
	 */
	public static int getUnseenUserNotificationsCount(User user) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// numero di notifiche trovate
		int res = 0;
		
		/*
		 * query che restituisce tutte le notifiche non lette di uno user
		 */
		String userNotificationsString = 
				"SELECT "
						+ 		"COUNT(*) AS notifications "
						+ 	"FROM "
						+ 		"user_notifications "
						+ 	"WHERE "
						+ 		"id_user = ? AND "
						+ 		"seen_date IS NULL";
		
		// query formato SQL
		PreparedStatement userNotificationsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userNotificationsSQL = connect.prepareStatement(userNotificationsString);
			userNotificationsSQL.setInt(1, user.getId_user());		        	        
			
			ResultSet userNotificationsRes = userNotificationsSQL.executeQuery();
			
			/* vengono scorse tutte le notifiche */
			if (userNotificationsRes.next()) 
			{		
				res = userNotificationsRes.getInt("notifications");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna il numero di notifiche trovate
		return res;
		
	}
	
	
	/**
	 * Prende in input uno user e ritorna tutte le notifiche non ancora
	 * lette
	 * 
	 * @param user	User a cui sono associate le notifiche
	 * @return		Lista di notifiche non lette
	 */
	public static List<Notification> getUnseenUserNotifications(User user) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di notifiche dove caricare i risultati
		List<Notification> userNotificationsList = new ArrayList<Notification>();

		/*
		 * query che restituisce tutte le notifiche non lette di uno user
		 */
		String userNotificationsString = 
				"SELECT "
			+ 		"* "
			+ 	"FROM "
			+ 		"user_notifications "
			+ 	"WHERE "
			+ 		"id_user = ? AND "
			+ 		"seen_date IS NULL "
			+ 	"ORDER BY "
			+ 		"issue_date DESC";
		
		// query formato SQL
		PreparedStatement userNotificationsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userNotificationsSQL = connect.prepareStatement(userNotificationsString);
			userNotificationsSQL.setInt(1, user.getId_user());		        	        
 
			ResultSet userNotificationsRes = userNotificationsSQL.executeQuery();
			
			/* vengono scorse tutte le notifiche */
			while (userNotificationsRes.next()) 
			{		
				// notifica di appoggio per caricare la lista
				Notification notification = new Notification();
				
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				notification.setId_notification(userNotificationsRes.getInt("id_notification"));
				notification.setId_user(userNotificationsRes.getInt("id_user"));
				notification.setType(userNotificationsRes.getString("type"));
				notification.setText(userNotificationsRes.getString("text"));
				notification.setIssue_date(userNotificationsRes.getTimestamp("issue_date"));
				notification.setSeen_date(userNotificationsRes.getTimestamp("seen_date"));

				// la notifica di appoggio viene messa nella lista
				userNotificationsList.add(notification);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
			
		//ritorna la lista dei selfie
		return userNotificationsList;

	}
	
	
	/**
	 * Prende in input uno user e ritorna tutte le notifiche non ancora
	 * lette
	 * 
	 * @param user	User a cui sono associate le notifiche
	 * @return		Lista di notifiche non lette
	 */
	public static List<Notification> getSeenUserNotifications(User user) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di notifiche dove caricare i risultati
		List<Notification> userNotificationsList = new ArrayList<Notification>();

		/*
		 * query che restituisce le ultime 25 notifiche non lette di uno user
		 * 
		 */
		String userNotificationsString = 
				"SELECT "
			+ 		"* "
			+ 	"FROM "
			+ 		"user_notifications "
			+ 	"WHERE "
			+ 		"id_user = ? AND "
			+ 		"seen_date IS NOT NULL "
			+ 	"ORDER BY "
			+ 		"issue_date DESC "
			+ 	"LIMIT 25";
		
		// query formato SQL
		PreparedStatement userNotificationsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userNotificationsSQL = connect.prepareStatement(userNotificationsString);
			userNotificationsSQL.setInt(1, user.getId_user());		        	        
 
			ResultSet userNotificationsRes = userNotificationsSQL.executeQuery();

			/* vengono scorse tutte le notifiche */
			while (userNotificationsRes.next()) 
			{		
				// notifica di appoggio per caricare la lista
				Notification notification = new Notification();
				
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				notification.setId_notification(userNotificationsRes.getInt("id_notification"));
				notification.setId_user(userNotificationsRes.getInt("id_user"));
				notification.setType(userNotificationsRes.getString("type"));
				notification.setText(userNotificationsRes.getString("text"));
				notification.setIssue_date(userNotificationsRes.getTimestamp("issue_date"));
				notification.setSeen_date(userNotificationsRes.getTimestamp("seen_date"));

				// la notifica di appoggio viene messa nella lista
				userNotificationsList.add(notification);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
			
		//ritorna la lista dei selfie
		return userNotificationsList;

	}
	
	
	/**
	 * Prende in input uno user e ritorna tutte le notifiche 
	 * 
	 * @param user	User a cui sono associate le notifiche
	 * @return		Lista di notifiche
	 */
	public static List<Notification> getAllUserNotifications(User user) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di notifiche dove caricare i risultati
		List<Notification> userNotificationsList = new ArrayList<Notification>();
		
		/*
		 * query che restituisce tutte le notifiche non lette di uno user
		 */
		String userNotificationsString = 
				"SELECT "
						+ 		"* "
						+ 	"FROM "
						+ 		"user_notifications "
						+ 	"WHERE "
						+ 		"id_user = ? "
						+ 	"ORDER BY "
						+ 		"issue_date DESC";
		
		// query formato SQL
		PreparedStatement userNotificationsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			userNotificationsSQL = connect.prepareStatement(userNotificationsString);
			userNotificationsSQL.setInt(1, user.getId_user());		        	        
			
			ResultSet userNotificationsRes = userNotificationsSQL.executeQuery();
			
			/* vengono scorse tutte le notifiche */
			while (userNotificationsRes.next()) 
			{		
				// notifica di appoggio per caricare la lista
				Notification notification = new Notification();
				
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				notification.setId_notification(userNotificationsRes.getInt("id_notification"));
				notification.setId_user(userNotificationsRes.getInt("id_user"));
				notification.setType(userNotificationsRes.getString("type"));
				notification.setText(userNotificationsRes.getString("text"));
				notification.setIssue_date(userNotificationsRes.getTimestamp("issue_date"));
				notification.setSeen_date(userNotificationsRes.getTimestamp("seen_date"));
				
				// la notifica di appoggio viene messa nella lista
				userNotificationsList.add(notification);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		//ritorna la lista dei selfie
		return userNotificationsList;
		
	}
	
	
	/**
	 * imposta una notifica come letta
	 * 
	 * @param notificationId	id della notifica da segnare come letta
	 * @return					id della notifica aggiornata
	 */
	public static int setSeenNotification(int notificationId)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// risultato dell'operazione
		int updatedId = -1;
		
        try 
        {      

	        String seenNotificationString = 
	        				"UPDATE "
	        			+ 		"user_notifications "
	        			+ 	"SET "
	        			+ 		"seen_date = now() "
	        			+ 	"WHERE "
	        			+ 		"id_notification = ?";
	        
	        PreparedStatement seenNotificationSQL = connect.prepareStatement(seenNotificationString, Statement.RETURN_GENERATED_KEYS);
	        // imposto l'id della notifica da impostare come letta
	        seenNotificationSQL.setInt(1, notificationId);

		    int affectedRows = seenNotificationSQL.executeUpdate();
		    if (affectedRows == 0)
		    	throw new SQLException("Inserting notification failed, no rows affected");		        
		    
			// se la notifica è stata aggiornata correttamente ricavo la chiave generata
			ResultSet generatedKeys = seenNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				updatedId = generatedKeys.getInt(1);
			}
			
        } catch (SQLException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        //ritorna l'id della nuova notifica
        return updatedId;
	}
	
	/**
	 * crea una notifica 'like' all'interno del DB
	 * 
	 * @param userId	id dello user a cui è indirizzata la notifica
	 * @param uls		chiave primaria della tabella user_like_selfie
	 * @return			id della notifica creata
	 */
	public static int setLikeOnUploadedSelfieNotification(int userId, int uls)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// ottengo l'id dello user a cui è piaciuto il selfie
		int userLikeId = LikeUtils.getLikeUser(uls);
    	//ottengo lo username dello user a cui è piaciuto il selfie
    	String userLikeUsername = UserUtils.getUsernameById(userLikeId);
		
        try 
        {      

	        String likeNotificationString = 
	        			"INSERT INTO "
	        		+ 		"user_notifications (id_user, type, text, issue_date, user_like_selfie ) "
	        		+ 	"VALUES "
	        		+ 		"( ? , ? , ? , NOW() , ? ) ";
	        
	        PreparedStatement likeNotificationSQL = connect.prepareStatement(likeNotificationString, Statement.RETURN_GENERATED_KEYS);
	        // imposto lo user a cui è diretta la notifica
	        likeNotificationSQL.setInt(1, userId);
	        // imposto il tipo di notifica su like
	        likeNotificationSQL.setString(2, "like");
	        // imposto il messaggio da far vedere
	        likeNotificationSQL.setString(3, userLikeUsername + " liked your selfie");
	        // imposto l'id del record corrispondente nella tabella user_like_selfie
	        likeNotificationSQL.setInt(4, uls);

		    int affectedRows = likeNotificationSQL.executeUpdate();
		    if (affectedRows == 0)
		    	throw new SQLException("Inserting notification failed, no rows affected");		        
		    
			// se la notifica è stata inserita correttamente ricavo la chiave generata
			ResultSet generatedKeys = likeNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				generatedId = generatedKeys.getInt(1);
			}
			
        } catch (SQLException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        //ritorna l'id della nuova notifica
        return generatedId;
	}
	
	/**
	 * crea una notifica 'like' all'interno del DB
	 * 
	 * @param userId	id dello user a cui è indirizzata la notifica
	 * @param uls		chiave primaria della tabella user_like_selfie
	 * @return			id della notifica creata
	 */
	public static int setLikeOnTaggedSelfieNotification(int userId, int uls)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// ottengo l'id dello user a cui è piaciuto il selfie
		int userLikeId = LikeUtils.getLikeUser(uls);
		//ottengo lo username dello user a cui è piaciuto il selfie
		String userLikeUsername = UserUtils.getUsernameById(userLikeId);
		
		try 
		{      
			
			String likeNotificationString = 
					"INSERT INTO "
							+ 		"user_notifications (id_user, type, text, issue_date, user_like_selfie ) "
							+ 	"VALUES "
							+ 		"( ? , ? , ? , NOW() , ? ) ";
			
			PreparedStatement likeNotificationSQL = connect.prepareStatement(likeNotificationString, Statement.RETURN_GENERATED_KEYS);
			// imposto lo user a cui è diretta la notifica
			likeNotificationSQL.setInt(1, userId);
			// imposto il tipo di notifica su like
			likeNotificationSQL.setString(2, "like");
			// imposto il messaggio da far vedere
			likeNotificationSQL.setString(3, userLikeUsername + " liked a selfie you're tagged in");
			// imposto l'id del record corrispondente nella tabella user_like_selfie
			likeNotificationSQL.setInt(4, uls);
			
			int affectedRows = likeNotificationSQL.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException("Inserting notification failed, no rows affected");		        
			
			// se la notifica è stata inserita correttamente ricavo la chiave generata
			ResultSet generatedKeys = likeNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				generatedId = generatedKeys.getInt(1);
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		//ritorna l'id della nuova notifica
		return generatedId;
	}
	
	/**
	 * crea una notifica 'tag' all'interno del DB
	 * 
	 * @param userId	id dell'utente a cui è indirizzata la notifica
	 * @param uts		chiave primaria della tabella user_tag_selfie
	 * @return			chiave del record appena generato
	 */
	public static int setTagNotification(int userId, int uts)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// ottengo l'id del selfie taggato
		int selfieTagId = UsertagsUtils.getTaggedSelfie(uts);
		//ottengo l'id dell'uploader del selfie (ovvero chi ha taggato)
		int taggerId = SelfieUtils.getSelfieById(selfieTagId).getUploader();
		// ottengo lo username di chi ha taggato il selfie
		String taggerUsername = UserUtils.getUsernameById(taggerId);
		
		try 
		{      
			
			String likeNotificationString = 
					"INSERT INTO "
							+ 		"user_notifications (id_user, type, text, issue_date, user_tag_selfie ) "
							+ 	"VALUES "
							+ 		"( ? , ? , ? , NOW() , ? ) ";
			
			PreparedStatement likeNotificationSQL = connect.prepareStatement(likeNotificationString, Statement.RETURN_GENERATED_KEYS);
			// imposto lo user a cui è diretta la notifica
			likeNotificationSQL.setInt(1, userId);
			// imposto il tipo di notifica su tag
			likeNotificationSQL.setString(2, "tag");
			// imposto il messaggio da far vedere
			likeNotificationSQL.setString(3, taggerUsername + " tagged you in a selfie");
			// imposto l'id del record corrispondente nella tabella user_tag_selfie
			likeNotificationSQL.setInt(4, uts);
			
			int affectedRows = likeNotificationSQL.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException("Inserting notification failed, no rows affected");		        
			
			// se la notifica è stata inserita correttamente ricavo la chiave generata
			ResultSet generatedKeys = likeNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				generatedId = generatedKeys.getInt(1);
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		//ritorna l'id della nuova notifica
		return generatedId;
	}
	
	/**
	 * crea una notifica 'follow' all'interno del DB
	 * 
	 * @param userId	id dell'utente a cui è indirizzata la notifica
	 * @param uts		chiave primaria della tabella user_follow_user
	 * @return			chiave del record appena generato
	 */
	public static int setFollowNotification(int userId, int ufu)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// ottengo l'id del nuovo seguace
		int followerId = FollowUtils.getFollowerUser(ufu);
		// ottengo lo username del seguace
		String followerUsername = UserUtils.getUsernameById(followerId);
		
		try 
		{      
			
			String likeNotificationString = 
					"INSERT INTO "
							+ 		"user_notifications (id_user, type, text, issue_date, user_follow_user) "
							+ 	"VALUES "
							+ 		"( ? , ? , ? , NOW() , ? ) ";
			
			PreparedStatement likeNotificationSQL = connect.prepareStatement(likeNotificationString, Statement.RETURN_GENERATED_KEYS);
			// imposto lo user a cui è diretta la notifica
			likeNotificationSQL.setInt(1, userId);
			// imposto il tipo di notifica su tag
			likeNotificationSQL.setString(2, "follow");
			// imposto il messaggio da far vedere
			likeNotificationSQL.setString(3, followerUsername + " is following you");
			// imposto l'id del record corrispondente nella tabella user_tag_selfie
			likeNotificationSQL.setInt(4, ufu);
			
			int affectedRows = likeNotificationSQL.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException("Inserting notification failed, no rows affected");		        
			
			// se la notifica è stata inserita correttamente ricavo la chiave generata
			ResultSet generatedKeys = likeNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				generatedId = generatedKeys.getInt(1);
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		//ritorna l'id della nuova notifica
		return generatedId;
	}
	

	/**
	 * crea una notifica 'comment' all'interno del DB
	 * 
	 * @param userId		id dell'utente a cui è indirizzata la notifica
	 * @param commentId		chiave primaria della tabella comment
	 * @return				chiave del record appena generato
	 */
	public static int setCommentOnUploadedSelfieNotification(int userId, int commentId)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// ottengo l'id del commentatore
		int commentatorId = CommentUtils.getCommentator(commentId);
		// ottengo lo username del seguace
		String commentatorUsername = UserUtils.getUsernameById(commentatorId);
		
		try 
		{      
			
			String commentNotificationString = 
					"INSERT INTO "
							+ 		"user_notifications (id_user, type, text, issue_date, comment) "
							+ 	"VALUES "
							+ 		"( ? , ? , ? , NOW() , ? ) ";
			
			PreparedStatement commentNotificationSQL = connect.prepareStatement(commentNotificationString, Statement.RETURN_GENERATED_KEYS);
			// imposto lo user a cui è diretta la notifica
			commentNotificationSQL.setInt(1, userId);
			// imposto il tipo di notifica su tag
			commentNotificationSQL.setString(2, "comment");
			// imposto il messaggio da far vedere
			commentNotificationSQL.setString(3, commentatorUsername + " left a comment on your selfie");
			// imposto l'id del record corrispondente nella tabella Comment
			commentNotificationSQL.setInt(4, commentId);
			
			int affectedRows = commentNotificationSQL.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException("Inserting notification failed, no rows affected");		        
			
			// se la notifica è stata inserita correttamente ricavo la chiave generata
			ResultSet generatedKeys = commentNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				generatedId = generatedKeys.getInt(1);
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		//ritorna l'id della nuova notifica
		return generatedId;
	}
	
	/**
	 * crea una notifica 'comment' all'interno del DB
	 * 
	 * @param userId		id dell'utente a cui è indirizzata la notifica
	 * @param commentId		chiave primaria della tabella comment
	 * @return				chiave del record appena generato
	 */
	public static int setCommentOnTaggedSelfieNotification(int userId, int commentId)
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// chiave generata, risultato dell'operazione
		int generatedId = -1;
		
		// ottengo l'id del commentatore
		int commentatorId = CommentUtils.getCommentator(commentId);
		// ottengo lo username del seguace
		String commentatorUsername = UserUtils.getUsernameById(commentatorId);
		
		try 
		{      
			
			String commentNotificationString = 
					"INSERT INTO "
							+ 		"user_notifications (id_user, type, text, issue_date, comment) "
							+ 	"VALUES "
							+ 		"( ? , ? , ? , NOW() , ? ) ";
			
			PreparedStatement commentNotificationSQL = connect.prepareStatement(commentNotificationString, Statement.RETURN_GENERATED_KEYS);
			// imposto lo user a cui è diretta la notifica
			commentNotificationSQL.setInt(1, userId);
			// imposto il tipo di notifica su tag
			commentNotificationSQL.setString(2, "comment");
			// imposto il messaggio da far vedere
			commentNotificationSQL.setString(3, commentatorUsername + " left a comment on a selfie you're tagged in");
			// imposto l'id del record corrispondente nella tabella Comment
			commentNotificationSQL.setInt(4, commentId);
			
			int affectedRows = commentNotificationSQL.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException("Inserting notification failed, no rows affected");		        
			
			// se la notifica è stata inserita correttamente ricavo la chiave generata
			ResultSet generatedKeys = commentNotificationSQL.getGeneratedKeys();
			if (generatedKeys.next())
			{
				// ricava l'id della nuova notifica
				generatedId = generatedKeys.getInt(1);
			}
			
		} catch (SQLException e) { e.printStackTrace();
		} finally {
			// chiude la connessione
			try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		//ritorna l'id della nuova notifica
		return generatedId;
	}
	
	
}
