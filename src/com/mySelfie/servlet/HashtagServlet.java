package com.mySelfie.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.PostUtils;

/**
 * Servlet implementation class HashtagServlet
 */
@WebServlet("/HashtagServlet")
public class HashtagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HashtagServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String hashtag = request.getPathInfo().substring(1);
		hashtag = "#" + hashtag;
							
		int id_ht = PostUtils.getHashtagId(hashtag);
		
		String posts = "";
		
		if(id_ht!=-1)
		{
			/* dalla sessione si ricava l' id dello user */
			HttpSession session = request.getSession();
			User me = new User();
	  		me = (User) session.getAttribute("user");
	  		int me_id = me.getId_user();
	  		
	        ServletContext servletContext = getServletContext();
			String contextPath = servletContext.getContextPath();
			
			posts = PostUtils.getPosts("hashtag", contextPath, id_ht, me_id);
		}
		else
		{
			posts = "<div class=\"empty\"><label class=\"empty_label\">There are no posts with that hashtag...</label></div>";
		}
		
		request.setAttribute("hashtagPosts", posts);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/hashtag.jsp"); 
		dispatcher.forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
