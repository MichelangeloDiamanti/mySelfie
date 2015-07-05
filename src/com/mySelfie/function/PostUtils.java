package com.mySelfie.function;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostUtils {

	public static String getPosts(String reqType, String contextPath,  int id_req_obj, int id_usr)
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
	        	postsQuery = "SELECT SE.id_selfie, SE.picture, SE.description, US.username, US.profilepic FROM ((Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) INNER JOIN user_follow_user AS UFU ON US.id_user = UFU.id_followed) WHERE UFU.id_follower= ? AND SE.uploader=UFU.id_followed ORDER BY SE.date DESC";
	        }
	        if(reqType.equals("hashtag"))	        	
	        {
	        	postsQuery = "SELECT SE.id_selfie, SE.picture, SE.description, US.username, US.profilepic FROM (Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) WHERE id_selfie = ANY(SELECT id_selfie FROM hashtag_in_selfie WHERE id_hashtag= ? ) ORDER BY SE.date DESC";
	        }
	        if(reqType.equals("profilePost"))	        	
	        {
	        	postsQuery = "SELECT SE.id_selfie, SE.picture, SE.description, US.username, US.profilepic FROM (Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) WHERE id_selfie = ? ";
	        }
	        
	        PreparedStatement postsSQL = connect.prepareStatement(postsQuery);
 	        postsSQL.setInt(1, id_req_obj);		        
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
	        String commentText = "";
	        String commentUser = "";
	        String commentUserPic = "";
	        
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
            	HTMLres += "<table class=\"post_container\"><tr><th class=\"user_pic";
            	
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
            			+ "<img id=\"selfie-" + id_selfie + "\" class=\"selfie\" src=\"" + contextPath + "/protected/resources/selfies/" + picture + "\" />"
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
     	        	
     	        HTMLres += "</div><div class=\"comments\"><ul class=\"comment_list\"><li class=\"comment_user_container\">";
     	       
     	       /* vengono presi tutti i commenti (e utente che ha commentato) del selfie corrente */
    			String commentQuery = "SELECT CO.text, US.profilepic, US.username FROM ((Comment AS CO INNER JOIN Selfie AS SE ON CO.id_selfie = SE.id_selfie) INNER JOIN User AS US ON CO.id_user = US.id_user) WHERE SE.id_selfie = ? ORDER BY CO.date ASC";
     	        PreparedStatement commentSQL = connect.prepareStatement(commentQuery);
     	        commentSQL.setInt(1, id_selfie);
     	        ResultSet commentRes = commentSQL.executeQuery();
     	        while (commentRes.next()) 
     	        {
     	        	commentText = commentRes.getString("text");
     	        	commentUser = commentRes.getString("username");
     	        	commentUserPic = commentRes.getString("profilepic");
     
     	        	
     	        	//se viene trovato un hashtag in un commento, viene aggiunto un link
     	        	Pattern pattern = Pattern.compile("#[A-Za-z]+");
     	        	Matcher matcher = pattern.matcher(commentText);
     	        	
     	        	while (matcher.find()) 
     	        	{
     	                String commentHashtag = "<a href=\"" + contextPath + "/protected/hashtag/" + matcher.group().substring(1) + "\" class=\"hashtag_link\"> " + matcher.group() + "</a> ";
     	                commentText = commentText.replaceAll(matcher.group() , commentHashtag);
     	        	}
     	        	     	        	
     	        	HTMLres += "<a href=\"" + contextPath + "/protected/profile/" + commentUser + "\">"
                			+ "<span class=\"profile_pic_comment\" style=\"background-image: url('" + contextPath + "/protected/resources/profilepics/" + commentUserPic + "')\" ></span>"
                			+ "<label class=\"profile_name_comment\">" + commentUser + "</label>"
                			+ "</a></li>"
                			+ "<li class=\"comment_container\">"
                			+ commentText
                			+ "</li>";
     	        }
     	    
     	        HTMLres += "</ul></div><div class=\"comment_input\"><input type=\"text\" class=\"comment_textbox\" placeholder=\"Write your comment here...\"/><button type=\"button\" class=\"comment_btn\"><span class=\"glyphicon glyphicon-comment\"></span></button></div></div></td></tr></table>";
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
        		
            	File file = new File(homeFolder + "/mySelfie/resources/selfies/" + picture);
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
				
            	HTMLres += "<div class=\"postContainer\"><img id=\"selfie-" + id_selfie + "\" class=\"" + picClass + "\" src=\"" + contextPath + "/protected/resources/selfies/" + picture + "\" onClick=\"openIMG(this)\" /></div>";

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
