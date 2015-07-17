package com.mySelfie.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.mySelfie.entity.Notification;
import com.mySelfie.entity.User;
import com.mySelfie.function.NotificationUtils;

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
	  	  		String res = "";
	  	  		
	  			for(Notification notification : NotificationUtils.getAllUserNotifications(user))
	  			{
	  				res += notification.getText();
	  				// segna le notifiche che verranno restituite come "lette"
	  				NotificationUtils.setSeenNotification(notification.getId_notification());
	  			}
	  			
	  			response.getWriter().write(res);
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
