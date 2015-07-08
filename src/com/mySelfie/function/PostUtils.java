package com.mySelfie.function;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.entity.Selfie;
import com.mySelfie.connection.ConnectionManager;

public class PostUtils {

	public static String getPosts(String reqType, String contextPath,  int id_req_obj, int id_usr, int last_index, String max_date)
	{
		
		Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB
		
        /* html da restituire al client */
        String HTMLres = "";
        
       
        try 
        {
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
	        datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	        connect = datasource.getConnection();	  	
	  		
	  		/* query che restituisce tutti i selfie da far visualizzare allo user */
	        String postsQuery = "";
	        
	        
	        if(reqType.equals("homepage"))	        	
	        {
	        	postsQuery = 
	        				"SELECT "
	        			+ 		"SE.id_selfie, SE.picture, SE.description, US.username, US.profilepic "
	        			+ 	"FROM"
	        			+ 		"((Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) "
	        			+ 		"INNER JOIN user_follow_user AS UFU ON US.id_user = UFU.id_followed) "
	        			+ 	"WHERE "
	        			+ 		"UFU.id_follower= ? AND "
	        			+ 		"SE.uploader=UFU.id_followed AND "
	        			+		"SE.date < ? "
	        			+ 	"ORDER BY "
	        			+ 		"SE.date DESC "
	        			+ 	"LIMIT "
	        			+ 		"?,10";
	        }
	        if(reqType.equals("hashtag"))	        	
	        {
	        	postsQuery = 
	        				"SELECT "
	        			+ 		"SE.id_selfie, SE.picture, SE.description, US.username, US.profilepic "
	        			+ 	"FROM "
	        			+ 		"(Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) "
	        			+ 	"WHERE "
	        			+ 		"id_selfie = ANY(SELECT id_selfie FROM hashtag_in_selfie WHERE id_hashtag= ? ) AND "
	        			+		"SE.date < ? "
	        			+ 	"ORDER BY "
	        			+ 		"SE.date DESC "
	        			+ 	"LIMIT "
	        			+ 		"?,10";
	        }
	        if(reqType.equals("profilePost"))	        	
	        {
	        	postsQuery = 
	        				"SELECT "
	        			+ 		"SE.id_selfie, SE.picture, SE.description, US.username, US.profilepic "
	        			+ 	"FROM "
	        			+ 		"(Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) "
	        			+ 	"WHERE "
	        			+ 		"id_selfie = ? AND "
	        			+		"SE.date < ? "
	        			+ 	"LIMIT "
	        			+ 		"?,10";
	        }
	        
	        PreparedStatement postsSQL = connect.prepareStatement(postsQuery);
 	        postsSQL.setInt(1, id_req_obj);		        
 	        postsSQL.setString(2, max_date);
 	        postsSQL.setInt(3, last_index); 	        
	        ResultSet postsRes = postsSQL.executeQuery();
	 	        
	        /* tutti gli attributi da assegnare ai selfie */
	        int id_selfie = 0;
	        int likes = 0;
	        String picture = "";
	        String description = "";
	        String username = "";
	        String profilepic = "";
	        String hashtag = "";
	        String tag = "";
	        
	        /* flag che indica se non ci sono foto da visualizzare */
			boolean emptyFlag = false;
			
			/* vengono scorsi tutti i selfie */
            while (postsRes.next()) 
            {
            	emptyFlag = true;
            	String comment_sections = "";
            	
            	/* vengono presi tutti gli attributi del selfie restituiti dal DB */
            	id_selfie = postsRes.getInt("id_selfie");
            	picture = postsRes.getString("picture");
            	description = postsRes.getString("description");   
               	username = postsRes.getString("username");
            	profilepic = postsRes.getString("profilepic");
            
            	/* si inizia a generare la stringa di risposta con l' HTML per visualizzare i post */
            	HTMLres += "<table class=\"post_container\">"
            			+ 	"<tr>"
            			+ 		"<th class=\"user_pic";
            	
            	if(reqType.equals("profilePost"))
            			HTMLres += "_profile";

            	HTMLres += "\"> "
            			+ "<a class=\"profile_link\" href=\"" + contextPath + "/protected/profile/" + username + "\">"
            			+ "<span class=\"profile_pic\" style=\"background-image: url('" + contextPath + "/protected/resources/profilepics/" + profilepic + "')\" ></span>"
            			+ "<label class=\"profile_name";
            	
            	if(reqType.equals("profilePost"))
        			HTMLres += "_white";
            	
            	if(reqType.equals("homepage"))
        			HTMLres += "_blue";
            	
            	HTMLres += "\">" + username + "</label>"
            			+ "</a></th></tr><tr><td class=\"selfie_container\"><div class=\"selfie_wrapper\">"
            			//+ "<img class=\"selfie\" src=\"" + contextPath + "/resources/images/loadingIMG.gif\" data-src=\"" + contextPath + "/protected/resources/selfies/" + picture + "\" />"
            			+ "<img id=\"selfie-" + id_selfie + "\" class=\"selfie\" src=\"" + contextPath + "/protected/resources/selfies/compressedSize/" + picture + "\" />"
            			+ "<div class=\"selfie_tools\">";
            	
            	
            	/* viene controllato se l'utente ha già messo "mi piace" al selfie */
            	String likeQuery = "SELECT * FROM user_like_selfie WHERE id_user = ? AND id_selfie = ?";
     	        PreparedStatement likeSQL = connect.prepareStatement(likeQuery);
     	        likeSQL.setInt(1, id_usr);
     	        likeSQL.setInt(2, id_selfie);
    	        ResultSet likeRes = likeSQL.executeQuery();
     	        if(likeRes.next()) 
     	        	HTMLres += "<span class=\"glyphicon glyphicon-heart hOn\" onClick=\"like(this, " + id_selfie + ")\">";
     	        else
     	        	HTMLres += "<span class=\"glyphicon glyphicon-heart-empty hOff\" onClick=\"like(this, " + id_selfie + ")\">";
     	        	
     	          	        	
     	        HTMLres +=	"</span><label class=\"nlikes\">";
            		
            	/* vanno contati i likes per il selfie corrente */
            	String likesQuery = "SELECT COUNT(*) AS likes FROM user_like_selfie WHERE id_selfie = ?";
     	        PreparedStatement likesSQL = connect.prepareStatement(likesQuery);
     	        likesSQL.setInt(1, id_selfie);
     	        ResultSet likesRes = likesSQL.executeQuery();
     	        while (likesRes.next()) 
     	        {
     	        	likes = likesRes.getInt("likes");
     	        	HTMLres += Integer.toString(likes);
     	        }		
            			            			
            	HTMLres	+= " Likes</label>"
            			+ "</div></div></td><td valign=\"top\" class=\"comments_container\"><div class=\"comment_wrapper\">";
            	
            	comment_sections += "<div class=\"comment_sections\">";
                  	
            	if(description!=null)
            	{
            		comment_sections += "<div class=\"comment_section_notes\"><p class=\"note\">"
            						 + description
            						 + "</p></div>";
            	}
            	     	       		
            	/* si ricavano gli hashtag del selfie corrente */
    			String hashtagsQuery = "SELECT HT.name, HT.id_hashtag FROM ((Selfie AS SE INNER JOIN hashtag_in_selfie AS HIS ON SE.id_selfie = HIS.id_selfie) INNER JOIN Hashtag AS HT ON HIS.id_hashtag = HT.id_hashtag) WHERE SE.id_selfie= ?";
     	        PreparedStatement hashtagsSQL = connect.prepareStatement(hashtagsQuery);
     	        hashtagsSQL.setInt(1, id_selfie);
     	        ResultSet hashtagsRes = hashtagsSQL.executeQuery();
     	      
     	        if(hashtagsRes.next())
     	        	comment_sections += "<div class=\"comment_section_hashtags\"><p class=\"hashtag\">";
     	        hashtagsRes.previous();
     	       
     	        while (hashtagsRes.next()) 
     	        {
     	        	hashtag = hashtagsRes.getString("name");
     	        	int hasht_id = hashtagsRes.getInt("id_hashtag");
     	        	
     	        	comment_sections += "<a href=\"" + contextPath + "/protected/hashtag/" + hashtag.substring(1) + "\" class=\"hashtag_link\" ";
     	        	
     	        	if(hasht_id==id_req_obj && reqType.equals("hashtag")) comment_sections += "style=\"font-weight: bold;\" ";

     	        	comment_sections += "> " + hashtag + "</a>";
     	        }
     	        
     	        if(hashtagsRes.previous())
     	        	comment_sections += "</p></div>";
     	        
            	
            	/* si ricavano i tag del selfie corrente */
    			String tagsQuery = "SELECT US.username FROM ((Selfie AS SE INNER JOIN user_tag_selfie AS UTS ON SE.id_selfie = UTS.id_selfie) INNER JOIN User AS US ON UTS.id_user = US.id_user) WHERE SE.id_selfie= ? ";
     	        PreparedStatement tagsSQL = connect.prepareStatement(tagsQuery);
     	        tagsSQL.setInt(1, id_selfie);
     	        ResultSet tagsRes = tagsSQL.executeQuery();
     	        
     	        if(tagsRes.next())
     	        	comment_sections += "<div class=\"comment_section_tags\"><p class=\"with\"> With - </p><p class=\"tag\">";  	        
     	        tagsRes.previous();
     	        
     	        while (tagsRes.next()) 
     	        {
     	        	tag = tagsRes.getString("username");
     	        	comment_sections += "<a href=\"" + contextPath + "/protected/profile/" + tag + "\" class=\"tag_link\">" + tag + " </a>";
     	        }
            	
     	        if(tagsRes.previous())
     	        	comment_sections += "</p></div>";
     	       
     	        comment_sections += "</div>";
     	       
     	        HTMLres += comment_sections;
     	        	
     	        HTMLres += "</div><div id=\"list_container-" + id_selfie + "\"  class=\"comments\">"
     	        		+ 	"<ul id=\"comments_list-" + id_selfie + "\" class=\"comment_list\">";
     	        
     	        // carica tutti i commenti relativi al selfie in questione grazie ad una funzione contenuta nella classe CommentUtils
     	        HTMLres += CommentUtils.getComments(id_selfie, connect, contextPath);

     	        /* aggiunge la sezione dei commenti, passando l'id del selfie su cui commentare
     	         * la form ha return false al submit perchè viene gestito tramite ajax in comments.js
     	         */
		        HTMLres += "</ul>"
		        		+ 	"</div>"
		        		+ 		"<form method=\"POST\" onsubmit=\"return false\"class=\"form-comment form-inline\">"
		        		+ 			"<div class=\"comment_input\">"
		        		+ 				"<input type=\"text\" name=\"comment_txt\" class=\"comment_textbox\" placeholder=\"Write your comment here...\"/>"
		        		+ 				"<button type=\"submit\" name=\"comment_btn\" id=\"comment-" + id_selfie + "\" class=\"comment_btn\">"
		        		+ 					"<span class=\"glyphicon glyphicon-comment\"></span>"
		        		+ 				"</button>"
		        		+ 			"</div>"
		        		+		"</form>"
		        		+ 	"</div>"
		        		+ "</td></tr></table>";

            }

            /* se non sono stati trovati selfie, viene stampato un messaggio all' utente */
 	        if(!emptyFlag)
 	        {
 	        	HTMLres = "<div class=\"empty\"><label class=\"empty_label\">There are no posts here...</label></div>";
 	        }
 	        
// 	       HTMLres += "<div id=\"bottomDiv\" style=\"height:100px; border:1px solid black\"></div>";
// 	       HTMLres += "<button>MORE!</button>";
// 	       HTMLres += "<input type=\"hidden\"/>";
 	       
 	        
        } catch (SQLException | NamingException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
        
        return HTMLres;
	}
	
	/**
	 * Prende in input l'id dello user attuale, e carica 10 post a partire dall'ultimo
	 * visualizzato, per questo necessita di un indice che ne tiene traccia.
	 * Si serve della data per caricare solo posts antecedenti al caricamento della pagina
	 * 
	 * @param userId 		id dello user attuale
	 * @param lastIndex		indice che tiene traccia dei post visualizzati
	 * @param maxDate		valore che indica il datetime di caricamento della pagina
	 * @return 
	 */
	public static List<Selfie> getFollowedUsersPosts(int userId, int lastIndex, String maxDate) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> followedUsersPostsList = new ArrayList<Selfie>();
		// selfie di appoggio per caricare la lista
		Selfie selfie = new Selfie();
		
		/*
		 * query che restituisce tutti i post pubblicati dagli utenti seguiti dallo user
		 */
		String followedUsersPostsString = 
				"SELECT "
			+ 		"SE.id_selfie, SE.picture, SE.description, SE.uploader "
			+ 	"FROM "
			+ 		"(Selfie AS SE INNER JOIN user_follow_user AS UFU ON SE.uploader = UFU.id_followed) "
			+ 	"WHERE "
			+ 		"UFU.id_follower = ? AND "
			+ 		"SE.uploader=UFU.id_followed AND "
			+ 		"SE.date < ? "
			+ 	"ORDER BY "
			+ 		"SE.date DESC "
			+ 	"LIMIT "
			+ 		"?,10";
		
		// query formato SQL
		PreparedStatement followedUsersPostsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			followedUsersPostsSQL = connect.prepareStatement(followedUsersPostsString);
			followedUsersPostsSQL.setInt(1, userId);		        
			followedUsersPostsSQL.setString(2, maxDate);
			followedUsersPostsSQL.setInt(3, lastIndex); 	        
			ResultSet followedUsersPostsRes = followedUsersPostsSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (followedUsersPostsRes.next()) 
			{		
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				selfie.setId_selfie(followedUsersPostsRes.getInt("id_selfie"));
				selfie.setPicture(followedUsersPostsRes.getString("picture"));
				selfie.setDescription(followedUsersPostsRes.getString("description"));   
				selfie.setUploader(followedUsersPostsRes.getInt("uploader"));
				// il selfie di appoggio viene aggiunto alla lista
				followedUsersPostsList.add(selfie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista dei selfie
		return followedUsersPostsList;

	}
	
	/**
	 * Prende in input l'id dell'hashtag da ricercare nei selfie, e carica 10 post a partire dall'ultimo
	 * visualizzato, per questo necessita di un indice che ne tiene traccia.
	 * Si serve della data per caricare solo posts antecedenti al caricamento della pagina
	 * 
	 * @param hashtagId 	id dell'hashtag da cercare
	 * @param lastIndex		indice che tiene traccia dei post visualizzati
	 * @param maxDate		valore che indica il datetime di caricamento della pagina
	 * @return 
	 */
	public static List<Selfie> getPostsByHashtag(int hashtagId, int lastIndex, String maxDate) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> hashtagPostsList = new ArrayList<Selfie>();
		// selfie di appoggio per caricare la lista
		Selfie selfie = new Selfie();
		
		/*
		 * query che restituisce tutti i post che hanno un determinato hashtag
		 */
		String hashtagPostsString = 
					"SELECT "
				+ 		"SE.id_selfie, SE.picture, SE.description, SE.uploader "
				+ 	"FROM "
				+ 		"Selfie AS SE "
				+ 	"WHERE "
				+ 		"id_selfie = ANY(SELECT id_selfie FROM hashtag_in_selfie WHERE id_hashtag= ? ) AND "
				+ 		"SE.date < ?"
				+ 	"ORDER BY "
				+ 		"SE.date DESC "
				+ 	"LIMIT "
				+ 		"?,10";
		// query formato SQL
		PreparedStatement hashtagPostsSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			hashtagPostsSQL = connect.prepareStatement(hashtagPostsString);
			hashtagPostsSQL.setInt(1, hashtagId);		        
			hashtagPostsSQL.setString(2, maxDate);
			hashtagPostsSQL.setInt(3, lastIndex); 	        
			ResultSet hashtagPostsRes = hashtagPostsSQL.executeQuery();
			
			/* vengono scorsi tutti i selfie */
			while (hashtagPostsRes.next()) 
			{		
				/* vengono presi tutti gli attributi del selfie e messi nel selfie di appoggio */
				selfie.setId_selfie(hashtagPostsRes.getInt("id_selfie"));
				selfie.setPicture(hashtagPostsRes.getString("picture"));
				selfie.setDescription(hashtagPostsRes.getString("description"));   
				selfie.setUploader(hashtagPostsRes.getInt("uploader"));
				// il selfie di appoggio viene aggiunto alla lista
				hashtagPostsList.add(selfie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna la lista dei selfie
		return hashtagPostsList;
		
	}
	
	/**
	 * Prende in input l'id di un selfie e ne ritorna tutte le informazioni
	 * 
	 * @param selfieId	id del selfie da cercare
	 * @return
	 */
	public static Selfie getPostById(int selfieId) {
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();
		// selfie da ritornare
		Selfie selfie = new Selfie();
		
		/*
		 * query che restituisce un post idententificato dall'id
		 */
		String singlePostString = 
					"SELECT "
				+ 		"SE.id_selfie, SE.picture, SE.description, SE.uploader "
				+ 	"FROM "
				+ 		"Selfie AS SE "
				+ 	"WHERE "
				+ 		"id_selfie = ?";
		// query formato SQL
		PreparedStatement singlePostSQL;
		
		try {
			// imposto i parametri ed eseguo la query
			singlePostSQL = connect.prepareStatement(singlePostString);
			singlePostSQL.setInt(1, selfieId);		        
			ResultSet hashtagPostsRes = singlePostSQL.executeQuery();
			
			/* se il selfie è stato trovato */
			if (hashtagPostsRes.next()) 
			{		
				/* vengono presi tutti gli attributi del selfie e messi nel selfie */
				selfie.setId_selfie(hashtagPostsRes.getInt("id_selfie"));
				selfie.setPicture(hashtagPostsRes.getString("picture"));
				selfie.setDescription(hashtagPostsRes.getString("description"));   
				selfie.setUploader(hashtagPostsRes.getInt("uploader"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		//ritorna il selfie
		return selfie;
		
	}
	
	public static void likeSelfie(String heart, int me_id, int idSelfie)
	{
		Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB     
       
        try 
        {
        	//connessione al DB
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
	        datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	        connect = datasource.getConnection();
	        

	        // se il cuore è vuoto, va riempito mettendo un nuovo record nel DB
	        if(heart.equals("empty"))
	        {	        	
		        String likeQuery = "INSERT INTO user_like_selfie (id_user, id_selfie) VALUES ( ? , ? ) ";
		        PreparedStatement likeSQL = connect.prepareStatement(likeQuery, Statement.RETURN_GENERATED_KEYS);
			    likeSQL.setInt(1, me_id);
			    likeSQL.setInt(2, idSelfie);
			    int affectedRows = likeSQL.executeUpdate();
			    if (affectedRows == 0)
			    	throw new SQLException("Like failed, no rows affected");		        
	        }
	        
	        // se il cuore è pieno, va svuotato rimuovendo il record dal DB
	        else if(heart.equals("full"))
	        {
	        	String dislikeQuery = "DELETE FROM user_like_selfie WHERE id_user = ? AND id_selfie =  ? ";
	        	PreparedStatement dislikeSQL = connect.prepareStatement(dislikeQuery);
	        	dislikeSQL.setInt(1, me_id);
	        	dislikeSQL.setInt(2, idSelfie);
	        	dislikeSQL.execute();
	        }
		   

        } catch (SQLException | NamingException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }  	    
	}
	
	public static String getProfilePosts(String user, String contextPath)
	{
		Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB

        /* html da restituire al client */
        String HTMLres = "";
               
        try 
        {
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
	        datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	        connect = datasource.getConnection();	  	
	  		
	  		/* query che restituisce tutti i selfie da far visualizzare allo user */
	        String ppostsQuery = "SELECT id_selfie, picture FROM Selfie WHERE uploader = (SELECT id_user FROM User WHERE username = ?) ORDER BY date DESC";
	        PreparedStatement ppostsSQL = connect.prepareStatement(ppostsQuery);
	        ppostsSQL.setString(1, user);
	        ResultSet ppostsRes = ppostsSQL.executeQuery();
	     
	        /* flag che indica se non ci sono foto da visualizzare */
			boolean emptyFlag = false;
			
			/* vengono scorsi tutti i selfie */
            while (ppostsRes.next()) 
            {
            	emptyFlag = true;
     
            	String picture = ppostsRes.getString("picture");
            	int id_selfie = ppostsRes.getInt("id_selfie");
            	
            	//ricava la home dell'utente (dove si suppone siano salvate le risorse protette)
        		String homeFolder = System.getProperty("user.home");
        		
            	File file = new File(homeFolder + "/mySelfie/resources/selfies/compressedSize/" + picture);
            	// leggo l'immagine che ho salvato
				BufferedImage bimg;
				int width=0;
				int height=0;
				
				try {
					bimg = ImageIO.read(file);
					// ricavo la larghezza e l'altezza
					width = bimg.getWidth();
					height = bimg.getHeight();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String picClass = (width >= height) ? "thumbnailL" : "thumbnailP";
				
            	HTMLres += "<div class=\"postContainer\"><img id=\"selfie-" + id_selfie + "\" class=\"" + picClass + "\" src=\"" + contextPath + "/protected/resources/selfies/compressedSize/" + picture + "\" onClick=\"openIMG(this)\" /></div>";

            }
            
            /* se non sono stati trovati selfie, viene stampato un messaggio all' utente */
 	        if(!emptyFlag)
 	        {
 	        	HTMLres = "<div class=\"empty\"><label class=\"empty_label\">There are no posts here...</label></div>";
 	        }
            
        } catch (SQLException | NamingException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		
		return HTMLres;
		
	}
	
	public static int getHashtagId(String hashtag)
	{
		Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB

        int id_ht = -1;

        try 
        {
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
	        datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	        connect = datasource.getConnection();	  	
	  		
	  		/* query che restituisce tutti i selfie da far visualizzare allo user */
	        String hidQuery = "SELECT id_hashtag FROM Hashtag WHERE name = ?";
	        PreparedStatement hidSQL = connect.prepareStatement(hidQuery);
	        hidSQL.setString(1, hashtag);
	        ResultSet hidRes = hidSQL.executeQuery();
	    
	    	/* vengono scorsi tutti i selfie */
            while (hidRes.next()) 
            {
               id_ht = hidRes.getInt("id_hashtag");
            }
            
            
        } catch (SQLException | NamingException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		
		return id_ht;
		
	}
	
	
}
