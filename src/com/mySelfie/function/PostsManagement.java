package com.mySelfie.function;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.mySelfie.entity.User;

public class PostsManagement {

	public static String getPosts(String reqType, String contextPath, HttpSession session)
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

	        /* dalla sessione si rivava l' id dello user */
	    	User me = new User();
	  		me = (User) session.getAttribute("user");
	  		int me_id = me.getId_user();
	  	
	  		
	  		/* query che restituisce tutti i selfie da far visualizzare allo user */
	        String postsQuery = "SELECT SE.id_selfie, SE.picture, SE.description, US.nickname, US.profilepic FROM ((Selfie AS SE INNER JOIN User as US ON SE.uploader=US.id_user) INNER JOIN user_follow_user AS UFU ON US.id_user = UFU.id_followed) WHERE UFU.id_follower= ? AND SE.uploader=UFU.id_followed ORDER BY SE.date ASC";
	        PreparedStatement postsSQL = connect.prepareStatement(postsQuery);
	        postsSQL.setInt(1, me_id);
	        ResultSet postsRes = postsSQL.executeQuery();
	   
	        /* tutti gli attributi da assegnare ai selfie */
	        int id_selfie = 0;
	        int likes = 0;
	        String picture = "";
	        String description = "";
	        String nickname = "";
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
            
            	/* vengono presi tutti gli attributi del selfie restituiti dal DB */
            	id_selfie = postsRes.getInt("id_selfie");
            	picture = postsRes.getString("picture");
            	description = postsRes.getString("description");   
            	if(description == null)
            		description = "";
            	
            	nickname = postsRes.getString("nickname");
            	profilepic = postsRes.getString("profilepic");
            
            	/* si inizia a generare la stringa di risposta con l' HTML per visualizzare i post */
            	HTMLres += "<table class=\"post_container\"><tr><th class=\"user_pic\"> "
            			+ "<a href=\"" + contextPath + "/profile/" + nickname + ".jsp\">"
            			+ "<span class=\"profile_pic\" style=\"background-image: url('" + contextPath + "/protected/resources/profilepics/" + profilepic + "')\" ></span>"
            			+ "<label class=\"profile_name\">" + nickname + "</label>"
            			+ "</a></th></tr><tr><td class=\"selfie_container\"><div class=\"selfie_wrapper\">"
            			+ "<img class=\"selfie\" src=\"" + contextPath + "/protected/resources/selfies/" + picture + "\"/>"
            			+ "<div class=\"selfie_tools\"><span class=\"glyphicon glyphicon-heart-empty hOff\"></span><label class=\"nlikes\">";
            		
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
            			+ "</div></div></td><td valign=\"top\" class=\"comments_container\"><div class=\"comment_wrapper\"><div class=\"comment_sections\"><div class=\"comment_section_notes\"><p class=\"note\">"
            			+ description
            			+ "</p></div><div class=\"comment_section_hashtags\"><p class=\"hashtag\">";
            			
            	/* si ricavano gli hashtag del selfie corrente */
    			String hashtagsQuery = "SELECT HT.name FROM ((Selfie AS SE INNER JOIN hashtag_in_selfie AS HIS ON SE.id_selfie = HIS.id_selfie) INNER JOIN Hashtag AS HT ON HIS.id_hashtag = HT.id_hashtag) WHERE SE.id_selfie= ?";
     	        PreparedStatement hashtagsSQL = connect.prepareStatement(hashtagsQuery);
     	        hashtagsSQL.setInt(1, id_selfie);
     	        ResultSet hashtagsRes = hashtagsSQL.executeQuery();
     	        while (hashtagsRes.next()) 
     	        {
     	        	hashtag = hashtagsRes.getString("name");
     	        	HTMLres += "<a href=\"\" class=\"hashtag_link\"> " + hashtag + "</a>";
     	        }
     	        
            	
            	
            	HTMLres += "</p></div><div class=\"comment_section_tags\">";
            	
            	/* si ricavano i tag del selfie corrente */
    			String tagsQuery = "SELECT US.nickname FROM ((Selfie AS SE INNER JOIN user_tag_selfie AS UTS ON SE.id_selfie = UTS.id_selfie) INNER JOIN User AS US ON UTS.id_user = US.id_user) WHERE SE.id_selfie= ? ";
     	        PreparedStatement tagsSQL = connect.prepareStatement(tagsQuery);
     	        tagsSQL.setInt(1, id_selfie);
     	        ResultSet tagsRes = tagsSQL.executeQuery();
     	        
     	        if(tagsRes.next())
     	        	HTMLres += "<p class=\"with\"> With - </p><p class=\"tag\">";
     	        else
     	        	HTMLres += "<p class=\"tag\">";
     	        
     	        tagsRes.previous();
     	        
     	        while (tagsRes.next()) 
     	        {
     	        	
     	        	tag = tagsRes.getString("nickname");
     	        	HTMLres += "<a href=\"\" class=\"tag_link\">" + tag + "</a>";
     	        }
            	
     	        HTMLres += "</p></div></div><div class=\"comments\"><ul class=\"comment_list\"><li class=\"comment_user_container\">";

     	       /* vengono presi tutti i commenti (e utente che ha commentato) del selfie corrente */
    			String commentQuery = "SELECT CO.text, US.profilepic, US.nickname FROM ((Comment AS CO INNER JOIN Selfie AS SE ON CO.id_selfie = SE.id_selfie) INNER JOIN User AS US ON CO.id_user = US.id_user) WHERE SE.id_selfie = ? ORDER BY CO.date ASC";
     	        PreparedStatement commentSQL = connect.prepareStatement(commentQuery);
     	        commentSQL.setInt(1, id_selfie);
     	        ResultSet commentRes = commentSQL.executeQuery();
     	        while (commentRes.next()) 
     	        {
     	        	commentText = commentRes.getString("text");
     	        	commentUser = commentRes.getString("nickname");
     	        	commentUserPic = commentRes.getString("profilepic");
  
     	        	HTMLres += "<a href=\"" + contextPath + "/profile/" + commentUser + ".jsp\">"
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
	
	
	
	
}