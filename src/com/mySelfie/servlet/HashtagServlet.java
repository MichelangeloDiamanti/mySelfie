package com.mySelfie.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.HashtagUtils;
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
		
		int last_index;
		if(request.getParameter("lastIndex")!=null)		
			last_index = Integer.parseInt(request.getParameter("lastIndex"));
		else
			last_index = 0;
		
		
		int id_ht = HashtagUtils.getHashtagId(hashtag);
		
		String HTMLres = "";
		
		if(id_ht!=-1)
		{
			/* dalla sessione si ricava l' id dello user */
			HttpSession session = request.getSession();
			User me = new User();
	  		me = (User) session.getAttribute("user");
	  		int me_id = me.getId_user();
	  		
	        ServletContext servletContext = getServletContext();
			String contextPath = servletContext.getContextPath();
			
			// istanzio una nuova data per passarla a getposts
			Date now = new Date();
			// converto la nuova data in formato sqlDatetime (da me brevettato top kek) 
			java.sql.Date sqlDate = new java.sql.Date(now.getTime());
			java.sql.Time sqlTime = new java.sql.Time(now.getTime());
			String sqlDateTime = sqlDate.toString() + " " + sqlTime.toString();
			
			// prendo tutti i post con l'hashtag, a partire dall'indice 0 e postati prima di ora
			HTMLres = PostUtils.getPosts("hashtag", contextPath, id_ht, me_id, last_index, sqlDateTime);
		}
		else
		{
			HTMLres = "<div class=\"empty\"><label class=\"empty_label\">There are no posts with that hashtag...</label></div>";
		}
		
		request.setAttribute("hashtagPosts", HTMLres);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/hashtag.jsp"); 
		dispatcher.forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String hashtag = request.getPathInfo().substring(1);
		hashtag = "#" + hashtag;
		
		int last_index;
		if(request.getParameter("lastIndex")!=null)		
			last_index = Integer.parseInt(request.getParameter("lastIndex"));
		else
			last_index = 0;
		
		
		int id_ht = HashtagUtils.getHashtagId(hashtag);
		
		String HTMLres = "";
		
		if(id_ht!=-1)
		{
			/* dalla sessione si ricava l' id dello user */
			HttpSession session = request.getSession();
			User me = new User();
	  		me = (User) session.getAttribute("user");
	  		int me_id = me.getId_user();
	  		
	        ServletContext servletContext = getServletContext();
			String contextPath = servletContext.getContextPath();
			
			// istanzio una nuova data per passarla a getposts
			Date now = new Date();
			// converto la nuova data in formato sqlDatetime (da me brevettato top kek) 
			java.sql.Date sqlDate = new java.sql.Date(now.getTime());
			java.sql.Time sqlTime = new java.sql.Time(now.getTime());
			String sqlDateTime = sqlDate.toString() + " " + sqlTime.toString();
			
			// prendo tutti i post con l'hashtag, a partire dall'indice 0 e postati prima di ora
			HTMLres = PostUtils.getPosts("hashtag", contextPath, id_ht, me_id, last_index, sqlDateTime);
			if(HTMLres.equals("<div class=\"empty\"><label class=\"empty_label\">There are no posts here...</label></div>")){
				HTMLres = "end";
			}
		}
		else
		{
			HTMLres = "<div class=\"empty\"><label class=\"empty_label\">There are no posts with that hashtag...</label></div>";
		}
		response.getWriter().write(HTMLres);
		
	}
	


}
