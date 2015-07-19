package com.mySelfie.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.Notification;
import com.mySelfie.entity.Selfie;
import com.mySelfie.entity.User;
import com.mySelfie.function.LikeUtils;
import com.mySelfie.function.NotificationUtils;
import com.mySelfie.function.SelfieUtils;

/**
 * Servlet implementation class NotificationsServlet
 */
@WebServlet("/NotificationsServlet")
public class NotificationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotificationsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ricavo lo user dalla sessione
  		HttpSession session = request.getSession();
  		User user = (User) session.getAttribute("user");
		
  		// ricavo la action dalla request
  		String action = request.getParameter("action");

  		if(action != null)
  		{
  	  		switch (action) {
  			case "getNotificationsCount":
  			{
  				String res = "none";
  				int notificationsCount = NotificationUtils.getUnseenUserNotificationsCount(user);
  				if (notificationsCount > 0)
  				{
  					res = "" + notificationsCount;
  				}

  				response.getWriter().write(res);
  			}
  			break;
  			case "getNotifications":
  			{
  				//prendo il context
  		  		ServletContext servletContext = getServletContext();
  				String contextPath = servletContext.getContextPath();
  				
	  	  		String HTMLres = "<ul id=\"newNotifications\">";
	  	  		
	  	  		//tutte le notifiche non lette
	  			for(Notification notification : NotificationUtils.getUnseenUserNotifications(user))
	  			{
	  				
	  				HTMLres += "<li><label class=\"ntfctn\">";
	  				
	  				if(notification.getType().equalsIgnoreCase("like"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-heart-empty notyphication\"></span>";

	  				if(notification.getType().equalsIgnoreCase("follow"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-eye-open notyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("tag"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-user notyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("comment"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-comment notyphication\"></span>";
	  				
	  				HTMLres += notification.getText();
	  				HTMLres += "</label>";
	  				

	  				if(notification.getType().equalsIgnoreCase("like"))
  					{
  						int idLikedSelfie = LikeUtils.getLikeSelfie(notification.getUser_like_selfie());
  						Selfie likedSelfie = SelfieUtils.getSelfieById(idLikedSelfie);
  						
  						HTMLres += "<img id=\"selfie-" + idLikedSelfie + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + likedSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
  					}
	  						
	  					
	  				if(notification.getType().equalsIgnoreCase("follow"))
	  				{

	  				}
	  				
	  				
	  				
	  				HTMLres += "</li>";						
	  						
	  				// segna le notifiche che verranno restituite come "lette"
	  				NotificationUtils.setSeenNotification(notification.getId_notification());
	  			}
	  			
	  			HTMLres += "</ul>"
	  					+  "<hr id=\"hrNotifications\">"
	  					+  "<ul id=\"oldNotifications\">";
	  					
	  			  			
				//tutte le notifiche lette
	  			for(Notification notification : NotificationUtils.getSeenUserNotifications(user))
	  			{

	  				HTMLres += "<li><label class=\"ntfctn\">";
	  				
	  				if(notification.getType().equalsIgnoreCase("like"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-heart-empty seenotyphication\"></span>";

	  				if(notification.getType().equalsIgnoreCase("follow"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-eye-open seenotyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("tag"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-user seenotyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("comment"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-comment seenotyphication\"></span>";
	  				
	  				HTMLres += notification.getText();
	  				HTMLres += "</label></li>";						
	  				
	  			}		
	  			
	  			HTMLres += "</ul>";
	  			
	  			response.getWriter().write(HTMLres);
  			}
  			break;
  			default:
  				break;
  			}
  		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated doPost method
	}

}
