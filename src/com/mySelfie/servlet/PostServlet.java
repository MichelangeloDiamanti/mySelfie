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

import com.mySelfie.function.CommentUtils;
import com.mySelfie.function.PostUtils;
import com.mySelfie.entity.User;


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
		
		String reqType = request.getParameter("reqType");
        
		/* dalla sessione si ricava l' id dello user */
		HttpSession session = request.getSession();
		User me = new User();
  		me = (User) session.getAttribute("user");
  		int me_id = me.getId_user();
  		
  		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getContextPath();
  		
    	switch(reqType)
        {
           	// intercetta la richiesta ajax per ricevere tutti i post
        	case "getPosts":
        	{
        		response.setContentType("text/plain");

        		String queryType = request.getParameter("queryType");          	   
    	  		
        		String HTMLres = PostUtils.getPosts(queryType, contextPath, me_id, me_id);

        		response.getWriter().write(HTMLres);
           	}
        	break;
           	
        	// intercetta la richiesta ajax per ricevere un post
        	case "profilePost":
        	{
        		response.setContentType("text/plain");

        		String queryType = request.getParameter("queryType");
        		String idIMGstr = request.getParameter("idIMG");
        		idIMGstr = idIMGstr.substring(idIMGstr.lastIndexOf('-')+1);
        		int idIMG = Integer.parseInt(idIMGstr);
        	
        		String HTMLres = PostUtils.getPosts(queryType, contextPath, idIMG, me_id);

        		response.getWriter().write(HTMLres);
           	}
        	break;
        	
           	// intercetta la richiesta ajax per mettere/togliere i like ai post
        	case "like":
        	{
        		String heart = request.getParameter("heart");
        		int idSelfie = Integer.parseInt(request.getParameter("selfie"));
        				    	  		
        		PostUtils.likeSelfie(heart, me_id, idSelfie);

        	}
        	break;
        	// intercetta la richiesta ajax per postare un nuovo commento
        	case "postComment":
        	{
        		// vengono ricavati l'id del selfie da commentare e il testo del commento
        		String idSelfieStr = request.getParameter("idSelfie");
        		int idSelfie = Integer.parseInt(idSelfieStr);
        		String comment = request.getParameter("commentText");
        		
        		// se il commento non è vuoto (o composto solo da spazi bianchi)
        		if (!comment.trim().equals("")) {
            		// viene aggiunto il nuovo commento per mezzo della classe CommentUtils
            		try {
    					boolean result = CommentUtils.addComment(me_id, idSelfie, comment);
    					// se la funzione ha avuto esito positivo
    	        		if (result) {
    		        		// viene ricavata la nuova lista dei commenti e mandata al client
    		        		String HTMLres = CommentUtils.getComments(idSelfie, contextPath);
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
