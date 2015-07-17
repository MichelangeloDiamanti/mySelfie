package com.mySelfie.function;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.entity.Comment;
import com.mySelfie.entity.Hashtag;
import com.mySelfie.entity.Selfie;
import com.mySelfie.entity.User;

public class PostUtils {

	public static String getPosts(String queryType, String contextPath,  int id_req_obj, int me_id, int last_index, String max_date)
	{
		
	
		// dichiaro una lista di selfie dove caricare i risultati
		List<Selfie> followedUsersPostsList = new ArrayList<Selfie>(); 	

		//html risultante
		String HTMLres = "";

		if(queryType.equals("homepage"))
			followedUsersPostsList = SelfieUtils.getFollowedUsersPosts(me_id, last_index, max_date);

		if(queryType.equals("explore"))
			followedUsersPostsList = SelfieUtils.getExplorationPosts(me_id, last_index, max_date);
		
		if(queryType.equals("profilePost"))
			followedUsersPostsList.add(SelfieUtils.getPostById(id_req_obj));
				
		if(queryType.equals("hashtag"))
			followedUsersPostsList = SelfieUtils.getPostsByHashtag(id_req_obj, last_index, max_date);
		
		
		
		//scorre i selfie uno ad uno per creare l' html
		for(int i = 0; i < followedUsersPostsList.size(); i++)
		{
			// selfie di appoggio per caricare la lista
			Selfie selfie = new Selfie();
			
			selfie = followedUsersPostsList.get(i);
			
			/* tutti gli attributi da assegnare ai selfie */
	        int id_selfie = selfie.getId_selfie();
	        String picture = selfie.getPicture();
	        String description = selfie.getDescription();
	        int id_uploader = selfie.getUploader();
	        String username = UserUtils.getUsernameById(id_uploader);
	        String profilepic = UserUtils.getUserProfilepicById(id_uploader);
	        String location = selfie.getLocation();  
	        
	        /* si inizia a generare la stringa di risposta con l' HTML per visualizzare i post */
            HTMLres += "<table class=\"post_container";
            
            //in determinati casi la tabella va spostata leggermente a destra
            if(queryType.equals("homepage") || queryType.equals("hashtag") || queryType.equals("explore"))
            	HTMLres += " pstcntleft";
            
            HTMLres += "\">"
            		+ 	"<tr>"
            		+ 		"<th class=\"user_pic";
          
            //se va aperta una foto dalla pagina profilo, l' header non va colorato
            if(queryType.equals("profilePost"))
            			HTMLres += "_profile";

            HTMLres += "\"> "
            		+ "<a class=\"profile_link\" href=\"" + contextPath + "/protected/profile/" + username + "\">"
            		+ "<span class=\"profile_pic\" style=\"background-image: url('" + contextPath + "/protected/resources/profilepics/" + profilepic + "')\" ></span>"
            		+ "<label class=\"profile_name";
            	
            //se va aperta una foto dalla pagina profilo, il nome dell' uploader va colorato in bianco
            if(queryType.equals("profilePost"))
        		HTMLres += "_white";
            	
            //se va aperta una foto dalla home, il nome dell' uploader va colorato in blu
        	if(queryType.equals("homepage") || queryType.equals("hashtag") || queryType.equals("explore"))
    			HTMLres += "_blue";
            	
        	HTMLres += "\">" + username + "</label>"
        			+ "</a></th></tr><tr><td class=\"selfie_container\"><div class=\"selfie_wrapper\">"
        			+ "<img id=\"selfie-" + id_selfie + "\" class=\"selfie\" src=\"" + contextPath + "/protected/resources/selfies/compressedSize/"
        			+ picture + "\" />"
        			+ "<div class=\"selfie_tools\">";
	        
        	//controlla se lo user ha già messo mi piace alla foto, in tal caso il cuore va riempito
        	if(LikeUtils.checkLike(me_id, id_selfie)) 
 	        	HTMLres += "<span class=\"glyphicon glyphicon-heart hOn\" onClick=\"like(this, " + id_selfie + ")\">";
 	        else
 	        	HTMLres += "<span class=\"glyphicon glyphicon-heart-empty hOff\" onClick=\"like(this, " + id_selfie + ")\">";
 	        	
 	        HTMLres +=	"</span><label class=\"nlikes\">";
        	//conta il numero di like per la foto
 	        HTMLres += LikeUtils.getSelfieLikesCount(id_selfie);
 	        HTMLres	+= " Likes</label>"
    			+ "</div></div></td><td valign=\"top\" class=\"comments_container\"><div class=\"comment_wrapper\">";

 	        HTMLres += "<div class=\"comment_sections\">";
         	if(description!=null)
        	{
        		HTMLres += "<div class=\"comment_section_notes\"><p class=\"note\">"
        				+ description
        				+ "</p></div>";
        	}
        	
         	
         	// dichiaro una lista di hashtags dove caricare i risultati
    		List<Hashtag> hashtagList = HashtagUtils.getSelfieHashtags(id_selfie);
    		
    		if(hashtagList.size() > 0)
    			HTMLres += "<div class=\"comment_section_hashtags\"><p class=\"hashtag\">";
	        	
    		//scorre gli hashtag uno ad uno per creare l' html
			for(int j = 0; j < hashtagList.size(); j++)
			{
				// hashtag di appoggio per caricare la lista
	    		Hashtag hashtag = new Hashtag();

				hashtag = hashtagList.get(j);
			         	       
				HTMLres += "<a href=\"" + contextPath + "/protected/hashtag/" + hashtag.getName().substring(1) + "\" class=\"hashtag_link\" ";

				if(hashtag.getId_hashtag() == id_req_obj && queryType.equals("hashtag")) 
 	        		HTMLres += "style=\"font-weight: bold;\" ";
				
				HTMLres += "> " + hashtag.getName() + "</a>";
 	        }
		
			if(hashtagList.size() > 0)
				HTMLres += "</p></div>";
 	        
			
			
			// dichiaro una lista di utenti dove caricare i risultati
			List<User> selfieUserTagsList = UsertagsUtils.getSelfieUserTags(id_selfie);
			
    		if(selfieUserTagsList.size() > 0)
    			HTMLres += "<div class=\"comment_section_tags\"><p class=\"with\"> With - </p><p class=\"tag\">";

    		//scorre i tag uno ad uno per creare l' html
			for(int j = 0; j < selfieUserTagsList.size(); j++)
			{
				// utente di appoggio per caricare la lista
				User user = new User();
				
				user = selfieUserTagsList.get(j);
			         	       
				HTMLres += "<a href=\"" + contextPath + "/protected/profile/" + user.getusername() + "\" class=\"tag_link\">" + user.getusername() + " </a>";                 	

 	        }
		
			if(selfieUserTagsList.size() > 0)
				HTMLres += "</p></div>";
			
			//stampa la location se presente
			if(location != null && !location.equals(""))
			{
				HTMLres += "<div class=\"comment_section_location\"><span class=\"glyphicon glyphicon-map-marker locationlbl\"></span><p class=\"lctn\">"
						+  location
						+ "</p></div>";
			}
			
			HTMLres += "</div>";


			
			//viene settata un' altezza approssimativa per la sezione dei commenti
			int commentH = 0;
		    // Imposta il percorso dove salvare l'immagine
			String homeFolder = System.getProperty("user.home");    	    
			// leggo l'immagine da visualizzare
			BufferedImage buffSelfie = null;
			try {
				buffSelfie = ImageIO.read(new File(homeFolder + contextPath + "/resources/selfies/compressedSize/" + picture));
				//calcola un' altezza approssimativa per i commenti
				int bsW = buffSelfie.getWidth();
				int bsH = buffSelfie.getHeight();
				commentH = (700*bsH/bsW) -250;
			} catch (IOException e) { e.printStackTrace(); }	
			
		    
			
 	        HTMLres += "</div><div id=\"list_container-" + id_selfie + "\"  class=\"comments\" style=\"height: " + commentH + "px; \" >"
	        		+ 	"<ul id=\"comments_list-" + id_selfie + "\" class=\"comment_list\">";
 	        
 	        

 			// dichiaro una lista di commenti dove caricare i risultati
 			List<Comment> selfieCommentsList = CommentUtils.getSelfieComments(id_selfie);

 			//scorre i commenti uno ad uno per creare l' html
			for(int j = 0; j < selfieCommentsList.size(); j++)
			{
	 			// commento di appoggio per caricare la lista
	 			Comment comment = new Comment();
	 			
				comment = selfieCommentsList.get(j);
				
				// se viene trovato un hashtag in un commento, viene aggiunto un link
				Pattern pattern = Pattern.compile("#[A-Za-z]+");
				Matcher matcher = pattern.matcher(comment.getText());
				while (matcher.find()) 
				{
					String commentHashtag = "<a href=\"" + contextPath
										  + "/protected/hashtag/"
										  + matcher.group().substring(1)
										  + "\" class=\"hashtag_link\"> " + matcher.group()
										  + "</a> ";
					comment.setText( comment.getText().replaceAll(matcher.group(),commentHashtag));
				}
				
				HTMLres += "<li class=\"comment_user_container\">"
						+ "<a href=\""
						+ contextPath
						+ "/protected/profile/"
						+ UserUtils.getUsernameById(comment.getId_user())
						+ "\">"
						+ "<span class=\"profile_pic_comment\" style=\"background-image: url('"
						+ contextPath + "/protected/resources/profilepics/"
						+ UserUtils.getUserProfilepicById(comment.getId_user()) + "')\" ></span>"
						+ "<label class=\"profile_name_comment\">"
						+ UserUtils.getUsernameById(comment.getId_user()) + "</label>" + "</a></li>"
						+ "<li class=\"comment_container\">" + comment.getText()
						+ "<br><label class=\"commentDate\"> " + comment.getDate() + "</label>"
						+ "</li>";
			}
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
		
		//se l' html non viene riempito (non ci sono post da visualizzare) allora va restituito "failed"
		if(HTMLres.equals(""))
			HTMLres = "failed";
		
        return HTMLres;
	}
	
	
	public static String getComments(String contextPath, int id_selfie)
	{
		String HTMLres = "";
		
		// dichiaro una lista di commenti dove caricare i risultati
		List<Comment> selfieCommentsList = CommentUtils.getSelfieComments(id_selfie);

		//scorre i commenti uno ad uno per creare l' html
		for(int j = 0; j < selfieCommentsList.size(); j++)
		{
			// comment di appoggio per caricare la lista
 			Comment comment = new Comment();
 			
			comment = selfieCommentsList.get(j);
			
			// se viene trovato un hashtag in un commento, viene aggiunto un link
			Pattern pattern = Pattern.compile("#[A-Za-z]+");
			Matcher matcher = pattern.matcher(comment.getText());
			while (matcher.find()) 
			{
				String commentHashtag = "<a href=\"" + contextPath
									  + "/protected/hashtag/"
									  + matcher.group().substring(1)
									  + "\" class=\"hashtag_link\"> " + matcher.group()
									  + "</a> ";
				comment.setText( comment.getText().replaceAll(matcher.group(),commentHashtag));
			}
			
			HTMLres += "<li class=\"comment_user_container\">"
					+ "<a href=\""
					+ contextPath
					+ "/protected/profile/"
					+ UserUtils.getUsernameById(comment.getId_user())
					+ "\">"
					+ "<span class=\"profile_pic_comment\" style=\"background-image: url('"
					+ contextPath + "/protected/resources/profilepics/"
					+ UserUtils.getUserProfilepicById(comment.getId_user()) + "')\" ></span>"
					+ "<label class=\"profile_name_comment\">"
					+ UserUtils.getUsernameById(comment.getId_user()) + "</label>" + "</a></li>"
					+ "<li class=\"comment_container\">" + comment.getText()
					+ "<br><label class=\"commentDate\"> " + comment.getDate() + "</label>"
					+ "</li>";
		}
       	
    
        return HTMLres;
	}
	
	
	public static String getProfilePosts(String user, String contextPath)
	{
		
        /* html da restituire al client */
        String HTMLres = "";
            
        Connection connect = ConnectionManager.getConnection();
        
        try 
        {
			
	  		
	  		/* query che restituisce tutti i selfie da far visualizzare allo user */
	        String ppostsQuery = "SELECT id_selfie, picture FROM Selfie WHERE uploader = (SELECT id_user FROM User WHERE username = ?) ORDER BY date DESC";
	        PreparedStatement ppostsSQL = connect.prepareStatement(ppostsQuery);
	        ppostsSQL.setString(1, user);
	        ResultSet ppostsRes = ppostsSQL.executeQuery();
	     
	        /* vengono scorsi tutti i selfie */
            while (ppostsRes.next()) 
            {
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
				
            	HTMLres += "<div class=\"postContainer\"><img id=\"selfie-" + id_selfie + "\" class=\"" + picClass + "\" src=\"" + contextPath + "/protected/resources/selfies/compressedSize/" + picture + "\" data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\" /></div>";

            }
                        
        } catch (SQLException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		
		return HTMLres;
		
	}
	
	
	
}
