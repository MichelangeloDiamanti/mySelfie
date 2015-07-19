package com.mySelfie.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.CommentUtils;
import com.mySelfie.function.LikeUtils;
import com.mySelfie.function.NotificationUtils;
import com.mySelfie.function.PostUtils;
import com.mySelfie.function.SelfieUtils;
import com.mySelfie.function.UsertagsUtils;


/**
 * Servlet implementation class PostServlet
 */
@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// stringa che indica l'azione da svolgere
		String reqType = request.getParameter("reqType");
		String queryType = "";    
		
		/* dalla sessione si ricava l' id dello user */
		HttpSession session = request.getSession();
		User me = new User();
  		me = (User) session.getAttribute("user");
  		int me_id = me.getId_user();
  				
		//prendo il context
  		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getContextPath();
		
		int last_index = 0; 
		String max_date = "";
		
		//id da utilizzare in base alle differenti richieste 
		int id_req_obj = -1;
		
  		if(reqType.equals("getPosts"))
  			queryType = request.getParameter("queryType");
  		
  		
  		if(queryType.equals("homepage") || queryType.equals("explore"))
  		{
  			id_req_obj = me_id;
  			last_index = Integer.parseInt(request.getParameter("lastIndex"));          	   
  			max_date = request.getParameter("date"); 
  		}
  		
		
		if(queryType.equals("profilePost"))
		{
			String idIMGstr = request.getParameter("idIMG");
    		idIMGstr = idIMGstr.substring(idIMGstr.lastIndexOf('-')+1);
    		int idIMG = Integer.parseInt(idIMGstr);
			id_req_obj = idIMG;
		}
		
	 		
    	switch(reqType)
        {
           	// intercetta la richiesta ajax per ricevere tutti i post
        	case "getPosts":
        	{
        		
        		response.setContentType("text/plain");
        		
        		String HTMLres = PostUtils.getPosts(queryType, contextPath, id_req_obj, me_id, last_index, max_date);

        		response.getWriter().write(HTMLres);
           	}
        	break;
           	
           	// intercetta la richiesta ajax per mettere/togliere i like ai post
        	case "like":
        	{
        		// id generato nella tabella user_like_selfie
        		int generatedId = -1;
        		String heart = request.getParameter("heart");
        		int idSelfie = Integer.parseInt(request.getParameter("selfie"));  	  		
        		generatedId = LikeUtils.likeSelfie(heart, me_id, idSelfie);
        		// se il like è stato messo correttamente
        		if (generatedId > 0)
        		{
        			// ricavo l'id dell'uploader del selfie
        			int uploaderId = SelfieUtils.getSelfieById(idSelfie).getUploader();
        			// se l'uploader del selfie non sono io
        			if(uploaderId != me_id){
            			// gli mando una notifica
            			NotificationUtils.setLikeOnUploadedSelfieNotification(uploaderId, generatedId);
        			}
        			// mando la notifica a tutti gli utenti taggati nel selfie
        			for(User user : UsertagsUtils.getSelfieUserTags(idSelfie))
        			{
        				NotificationUtils.setLikeOnTaggedSelfieNotification(user.getId_user(), generatedId);
        			}
        		}

        	}
        	break;
        	
        	// intercetta la richiesta ajax per postare un nuovo commento
        	case "postComment":
        	{
        		response.setContentType("text/plain");
        		
        		// vengono ricavati l'id del selfie da commentare e il testo del commento
        		String idSelfieStr = request.getParameter("idSelfie");
        		int idSelfie = Integer.parseInt(idSelfieStr);
        		String comment = request.getParameter("commentText");
        		
        		// se il commento non è vuoto (o composto solo da spazi bianchi)
        		if (!comment.trim().equals("")) {
            		// viene aggiunto il nuovo commento per mezzo della classe CommentUtils
            		try {
    					int generatedId = CommentUtils.addComment(me_id, idSelfie, comment);
    					// se la funzione ha avuto esito positivo
    	        		if (generatedId > 0) {
    	        			// ricavo l'id dell'uploader del selfie
    	        			int uploaderId = SelfieUtils.getSelfieById(idSelfie).getUploader();
    	        			if(uploaderId != me_id){ // non ricevo notifiche per commenti che metto ai miei selfie
        	        			// mando una notifica all'uploader del selfie
        	        			NotificationUtils.setCommentOnUploadedSelfieNotification(uploaderId, generatedId);	
    	        			}
    	        			// mando una notifica a tutti gli user taggati
    	        			for(User user : UsertagsUtils.getSelfieUserTags(idSelfie))
    	        			{
    	        				// evito di mandare la notifica all'uploader 2 volte se è taggato nella sua foto
    	        				if(user.getId_user() != uploaderId)
    	        				{
    	        					NotificationUtils.setCommentOnTaggedSelfieNotification(user.getId_user(), generatedId);
    	        				}
    	        			}
    		        		// viene ricavata la nuova lista dei commenti e mandata al client
    		        		String HTMLres = PostUtils.getComments(contextPath, idSelfie);
    		        		response.getWriter().write(HTMLres);
    					}
    	        		// se l'aggiunta del commento è fallita
    	        		else {
    		        		// viene inviato un messaggio di errore
    		        		String HTMLres = "fail";
    		        		response.getWriter().write(HTMLres);
    					}
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
				}
        		// se il commento è vuoto (o composto solo da spazi bianchi)
        		else {
        			// viene inviato un messaggio di errore
	        		String HTMLres = "fail";
	        		response.getWriter().write(HTMLres);
				}
        	}
        	break;

        }
		
	}

}
