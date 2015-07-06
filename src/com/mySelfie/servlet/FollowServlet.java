package com.mySelfie.servlet;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.FollowUtils;

/**
 * Servlet implementation class FollowServlet
 */
@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowServlet() {
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
		String action = request.getParameter("action");
        
		/* dalla sessione si ricava l' id dello user */
		HttpSession session = request.getSession();
		User me = new User();
  		me = (User) session.getAttribute("user");
  		int me_id = me.getId_user();
  		
    	switch(action)
        {
    		// gestisce la richiesta di seguire uno user
	    	case "followUser":
	    	{
	    		// prende l'ID dell'utente da seguire
	    		int id_to_follow = Integer.parseInt(request.getParameter("idToFollow"));
	    		response.setContentType("text/plain");	
	    		boolean result = false;
	    		try {
	    			// effettua la chiamata followUser grazie alla classe FollowUtils
					result = FollowUtils.followUser(me_id, id_to_follow);
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		// se il metodo non ha incontrato problemi
	    		if(result){
	    			// viene mandata la form per fare l'unfollow
	    			response.getWriter().write(
	    				  	"<form id=\"unfollow_form\" class=\"form-inline\" onsubmit=\"return false\">"
	    			    	+	"<button id=\"unfollow_btn\" style=\"position:relative; float:right;\">unfollow!</button>"
	    			    	+	"<input type=\"hidden\" name=\"profile_id\" value=\"" + id_to_follow + "\">"
	    			    	+	"</form>"
	    					);
	    		}
	    		// altrimenti
	    		else{
	    			// viene rimandata la form per fare la follow
	    			response.getWriter().write(
	    				  	"<form id=\"follow_form\" class=\"form-inline\" onsubmit=\"return false\">"
	    			    	+	"<button id=\"follow_btn\" style=\"position:relative; float:right;\">follow!</button>"
	    			    	+	"<input type=\"hidden\" name=\"profile_id\" value=\"" + id_to_follow + "\">"
	    			    	+	"</form>"
	    					);
	    		}
	       	}
	    	break;
	    	
	    	// gestisce la richiesta di seguire uno user
	    	case "unfollowUser":
	    	{
	    		// prende l'ID dell'utente da seguire
	    		int id_to_unfollow = Integer.parseInt(request.getParameter("idToUnfollow"));
	    		response.setContentType("text/plain");	
	    		boolean result = false;
	    		try {
	    			// effettua la chiamata unfollowUser grazie alla classe FollowUtils
	    			result = FollowUtils.unfollowUser(me_id, id_to_unfollow);
	    		} catch (NamingException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		// se il metodo non ha incontrato problemi
	    		if(result){
	    			// viene mandata la form per fare la follow
	    			response.getWriter().write(
	    				  	"<form id=\"follow_form\" class=\"form-inline\" onsubmit=\"return false\">"
	    			    	+	"<button id=\"follow_btn\" style=\"position:relative; float:right;\">follow!</button>"
	    			    	+	"<input type=\"hidden\" name=\"profile_id\" value=\"" + id_to_unfollow + "\">"
	    			    	+	"</form>"
	    					);
	    		}
	    		// altrimenti
	    		else{
	    			// viene rimandata la form per fare l'unfollow
	    			response.getWriter().write(
	    				  	"<form id=\"unfollow_form\" class=\"form-inline\" onsubmit=\"return false\">"
	    			    	+	"<button id=\"unfollow_btn\" style=\"position:relative; float:right;\">unfollow!</button>"
	    			    	+	"<input type=\"hidden\" name=\"profile_id\" value=\"" + id_to_unfollow + "\">"
	    			    	+	"</form>"
	    					);
	    		}
	    	}
	    	break;
        }
	}

}
