package com.mySelfie.servlet;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.FollowUtils;
import com.mySelfie.function.PostUtils;
import com.mySelfie.function.UserUtils;

/**
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* dalla sessione si ricava l'id e lo username dello user */
		HttpSession session = request.getSession();
		User me = new User();
		me = (User) session.getAttribute("user");
  		String myUsername = me.getusername();
  		int myId = me.getId_user();
  		
		// ricava lo user al quale appartiene il profilo analizzando il path
		String user = request.getPathInfo().substring(1);

		// ricava l'id dello user al quale appartiene il profilo
		int id_followed = -1;
		try {
			id_followed = UserUtils.getId(user);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// prende il contesto del server
		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getContextPath();
		
		// rivata tutti i post dello user e li mette nella risposta
		String posts = PostUtils.getProfilePosts(user, contextPath);
		
		request.setAttribute("profilePosts", posts);
		// mette lo username a cui appartiene il profilo nella risposta
		request.setAttribute("profileOwner", user);
		// controlla se il profilo visitato è quello dello user che ha effettuato il login
		if(user.equals(myUsername)){
			request.setAttribute("myProfile", true);
		}
		else{
			request.setAttribute("myProfile", false);
		}
		
		// mette nella risposta l'id dell'utente a cui appartiene il profilo
		request.setAttribute("profileId", id_followed);
		
		// flag che indica se lo user attuale segue il proprietario del profilo visitato
		boolean following = FollowUtils.checkFollow(myId, id_followed);
		request.setAttribute("following", following);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/profile.jsp"); 
		dispatcher.forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
