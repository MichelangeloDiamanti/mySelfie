package com.mySelfie.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
        
    	switch(reqType)
        {
           	// intercetta la richiesta ajax per ricevere tutti i post
        	case "getPosts":
        	{
        		response.setContentType("text/plain");

        		String queryType = request.getParameter("queryType");
        		
        		ServletContext servletContext = getServletContext();
        		String contextPath = servletContext.getContextPath();
      
          	    /* dalla sessione si ricava l' id dello user */
        		HttpSession session = request.getSession();
        		User me = new User();
    	  		me = (User) session.getAttribute("user");
    	  		int me_id = me.getId_user();
    	  		
        		String HTMLres = PostUtils.getPosts(queryType, contextPath, me_id);

        		response.getWriter().write(HTMLres);
           	}
        	break;
           	
        	// intercetta la richiesta ajax per ricevere un post
        	case "profilePost":
        	{
        		response.setContentType("text/plain");

        		String queryType = request.getParameter("queryType");
        		String nameIMG =  request.getParameter("nameIMG");
        		
        		ServletContext servletContext = getServletContext();
        		String contextPath = servletContext.getContextPath();
        		
        		int idIMG = PostUtils.getImgIdByName(nameIMG);
        		String HTMLres = PostUtils.getPosts(queryType, contextPath, idIMG);

        		response.getWriter().write(HTMLres);
           	}
        	break;
        	
           	// intercetta la richiesta ajax per mettere/togliere i like ai post
        	case "like":
        	{
        		String heart = request.getParameter("heart");
        		int idSelfie = Integer.parseInt(request.getParameter("selfie"));
        				
        		HttpSession session = request.getSession();
    	        User me = new User();
    	  		me = (User) session.getAttribute("user");
    	  		int me_id = me.getId_user();
    	  	
    	  		
        		PostUtils.likeSelfie(heart, me_id, idSelfie);

        	}
        	break;

        }
		
	}

}
