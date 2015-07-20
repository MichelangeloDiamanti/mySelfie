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
import com.mySelfie.function.CommentUtils;
import com.mySelfie.function.FollowUtils;
import com.mySelfie.function.LikeUtils;
import com.mySelfie.function.NotificationUtils;
import com.mySelfie.function.SelfieUtils;
import com.mySelfie.function.UserUtils;
import com.mySelfie.function.UsertagsUtils;

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
  				
	  	  		String HTMLres = "<table id=\"newNotificationsTable\">";
	  	  		
	  	  		//tutte le notifiche non lette
	  			for(Notification notification : NotificationUtils.getUnseenUserNotifications(user))
	  			{
	  				int idNotifier = NotificationUtils.getNotifierUserIdByNotification(notification.getId_notification(), notification.getType());

	  				int idTarget = -1;
	  					  				
	  				HTMLres += "<tr class=\"notificationsTR\"><td class=\"typeTD\">";
	  				
	  				if(notification.getType().equalsIgnoreCase("like"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-heart-empty notyphication\"></span>";

	  				if(notification.getType().equalsIgnoreCase("follow"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-eye-open notyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("tag"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-user notyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("comment"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-comment notyphication\"></span>";
	  				
	  				HTMLres += "</td><td class=\"thumbnailTD\">";
	  				
	  				HTMLres += "<a href=\"" + contextPath + "/protected/profile/" + UserUtils.getUsernameById(idNotifier) + "\">"
	  						+  "<img class=\"profileThumbnail\" src=\"" + contextPath + "/protected/resources/profilepics/" + UserUtils.getUserProfilepicById(idNotifier) + "\">"
	  						+  "</a></td><td class=\"textTD\">";
					
					HTMLres += "<label class=\"ntfctn\">" + notification.getText() + "</label>";
					HTMLres += "</td><td class=\"thumbnailTD\">";
					
	  				if(notification.getType().equalsIgnoreCase("like"))
  					{	
  						idTarget = LikeUtils.getLikeSelfie(notification.getUser_like_selfie());
  						Selfie targetSelfie = SelfieUtils.getSelfieById(idTarget);
  						HTMLres += "<img id=\"selfie-" + idTarget + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + targetSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
  					} 				
	  				if(notification.getType().equalsIgnoreCase("tag"))
	  				{
	  					idTarget = UsertagsUtils.getTaggedSelfie(notification.getUser_tag_selfie()); 
	  					Selfie targetSelfie = SelfieUtils.getSelfieById(idTarget);
	  					HTMLres += "<img id=\"selfie-" + idTarget + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + targetSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
	  				}
	  				if(notification.getType().equalsIgnoreCase("comment"))
	  				{
	  					idTarget = CommentUtils.getCommentedSelfie(notification.getComment()); 
	  					Selfie targetSelfie = SelfieUtils.getSelfieById(idTarget);
	  					HTMLres += "<img id=\"selfie-" + idTarget + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + targetSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
	  				}
	  				if(notification.getType().equalsIgnoreCase("follow"))
	  				{
	  					idTarget = FollowUtils.getFollowedUser(notification.getUser_follow_user());
	  					HTMLres += "<a href=\"" + contextPath + "/protected/profile/" + UserUtils.getUsernameById(idTarget) + "\">"
		  						+  "<img class=\"profileThumbnail\" src=\"" + contextPath + "/protected/resources/profilepics/" + UserUtils.getUserProfilepicById(idTarget) + "\">"
		  						+  "</a>";
	  				}
					HTMLres += "</td></tr>";		
	  					  				
	  			}
	  			
	  			HTMLres += "</table>"
	  					+  "<hr id=\"hrNotifications\">"
	  					+  "<table id=\"oldNotificationsTable\">";
	  					
	  			  			
				//tutte le notifiche lette
	  			for(Notification notification : NotificationUtils.getSeenUserNotifications(user))
	  			{
	  				int idNotifier = NotificationUtils.getNotifierUserIdByNotification(notification.getId_notification(), notification.getType());

	  				int idTarget = -1;
	  					  				
	  				HTMLres += "<tr class=\"notificationsTR\"><td class=\"typeTD\">";
	  				
	  				if(notification.getType().equalsIgnoreCase("like"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-heart-empty seenotyphication\"></span>";

	  				if(notification.getType().equalsIgnoreCase("follow"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-eye-open seenotyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("tag"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-user seenotyphication\"></span>";
	  				
	  				if(notification.getType().equalsIgnoreCase("comment"))
	  					HTMLres += "<span class=\"glyphicon glyphicon-comment seenotyphication\"></span>";
	  				
	  				HTMLres += "</td><td class=\"thumbnailTD\">";
	  				
	  				HTMLres += "<a href=\"" + contextPath + "/protected/profile/" + UserUtils.getUsernameById(idNotifier) + "\">"
	  						+  "<img class=\"profileThumbnail\" src=\"" + contextPath + "/protected/resources/profilepics/" + UserUtils.getUserProfilepicById(idNotifier) + "\">"
	  						+  "</a></td><td class=\"textTD\">";
					
					HTMLres += "<label class=\"ntfctn\">" + notification.getText() + "</label>";
					HTMLres += "</td><td class=\"thumbnailTD\">";
					
	  				if(notification.getType().equalsIgnoreCase("like"))
  					{	
  						idTarget = LikeUtils.getLikeSelfie(notification.getUser_like_selfie());
  						Selfie targetSelfie = SelfieUtils.getSelfieById(idTarget);
  						HTMLres += "<img id=\"selfie-" + idTarget + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + targetSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
  					} 				
	  				if(notification.getType().equalsIgnoreCase("tag"))
	  				{
	  					idTarget = UsertagsUtils.getTaggedSelfie(notification.getUser_tag_selfie()); 
	  					Selfie targetSelfie = SelfieUtils.getSelfieById(idTarget);
	  					HTMLres += "<img id=\"selfie-" + idTarget + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + targetSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
	  				}
	  				if(notification.getType().equalsIgnoreCase("comment"))
	  				{
	  					idTarget = CommentUtils.getCommentedSelfie(notification.getComment()); 
	  					Selfie targetSelfie = SelfieUtils.getSelfieById(idTarget);
	  					HTMLres += "<img id=\"selfie-" + idTarget + "\" class=\"notificationThumbnail\" src='" + contextPath + "/protected/resources/selfies/compressedSize/" + targetSelfie.getPicture() + "' data-toggle=\"modal\" data-target=\"#modalTable\" onClick=\"openIMG(this)\">";	  					
	  				}
	  				if(notification.getType().equalsIgnoreCase("follow"))
	  				{
	  					idTarget = FollowUtils.getFollowedUser(notification.getUser_follow_user());
	  					HTMLres += "<a href=\"" + contextPath + "/protected/profile/" + UserUtils.getUsernameById(idTarget) + "\">"
		  						+  "<img class=\"profileThumbnail\" src=\"" + contextPath + "/protected/resources/profilepics/" + UserUtils.getUserProfilepicById(idTarget) + "\">"
		  						+  "</a>";
	  				}
					HTMLres += "</td></tr>";	
	  			}		
	  			
	  			HTMLres += "</table>";
	  			
	  			//setta tutte le notifiche non lette a lette
	  			for(Notification notification : NotificationUtils.getUnseenUserNotifications(user))
	  			{
	  				// segna le notifiche che verranno restituite come "lette"
	  				NotificationUtils.setSeenNotification(notification.getId_notification());
	  			}
	  			
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
