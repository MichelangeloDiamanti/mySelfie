package com.mySelfie.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.UserUtils;
import com.mySelfie.security.SecurityUtils;
import com.toastMessage.Message;
 
public class UserValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * questa funzione gestisce la validazione dell'account dell'utente, ricava dalla request il token inviato
	 * per email e, se corrisponde ad un account lo valida.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ricava il token dall'url
		String code = request.getPathInfo().substring(1);
		Message message = new Message();
		
		// se il codice non è nullo
		if(code!=null)
		{
			// ricavo l'id dell'utente a cui è associato il codice
			int userId = SecurityUtils.checkUserValidationCode(code);
			// se l'utente esiste
			if(userId >= 0)
			{
				// valido il suo account
				UserUtils.setValid(userId);
				// imposto un messaggio di successo da far vedere tramite toast
				message.setType("success");
				message.setTitle("Validation Succeeded");
				message.setBody("Congratulations, you can now log in.");
				request.setAttribute("toastMessage", message);
			}
			else
			{
				// imposto un messaggio di errore da far vedere tramite toast
				message.setType("fail");
				message.setTitle("Validation Failed");
				message.setBody("Sorry something went wrong, try again.");
				request.setAttribute("toastMessage", message);
			}
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
		dispatcher.forward(request,response);
	}



	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    	
    	response.setContentType("text/plain");
    	String action = request.getParameter("action");
    	
    	switch (action) {
		case "login":
		{
			// viene salvato l'url richiesto in modo da poter essere reindirizzati dopo il login 
	    	String requestURL = request.getParameter("redURL");
	    	// se l'url richiesto è vuoto significa che la form di login non è stata presentata in risposta ad una richiesta passata
	    	// per il filtro di controllo ma è stata acceduta direttamente dall'utente viene quindi impostato un url di redirect standard
	    	if(requestURL.equals("")) requestURL = "/mySelfie/protected/homepage.jsp";
	    	
	    	// vengono presi i parametri necessari per il login dalla form
	        String usr = request.getParameter("username");
	        String pwd = request.getParameter("password");
	        String checkStat = request.getParameter("remMe");
	        
	        // viene istanziato un nuovo utente null
	        User user = null;
	      	
	      	try 
	      	{
	      		// viene controllato se esiste un utente con le credenziali fornite
	      		user = SecurityUtils.checkLogin(usr, pwd);
	        } 
	      	catch (NamingException e) 
	      	{
	             e.printStackTrace();
	      	}
	      	// se l'utente ritornato è valido        	
	      	if(user.isValid())
			{
	      		// viene istanziata una nuova sessione
	      		HttpSession session = request.getSession();
	      		session.setAttribute("user", user);
	      		
	      		 // Keep Me Logged In //
	      		// Controlo checkbox
	      		boolean rememberMe = false;
	      		if(checkStat!=null && checkStat.equalsIgnoreCase("on")) {
	      			rememberMe=true;
	      		}
	      		// Se l'utente desidera rimanere loggato viene generato
	      		// un cookie
 	      		if(rememberMe) {
	      			int usId = user.getId_user();
 	      			try {
						response=SecurityUtils.generateCookie(response, usId);
					} catch (NamingException e) {
						e.printStackTrace();
					}
	      		}
	      		// viene trasmesso al browser l'url a cui deve andare
	      		response.getWriter().write(requestURL);
			}
	      	else
	      	{
	      		// altrimenti viene comunicato che il login è fallito
	      		response.getWriter().write("loginFAIL");
	      	}
	      	break;
		}
		
		case "logout":
		{
	        // Invalida la sessione
			HttpSession session = request.getSession();
	        session.invalidate();
	        // Invalida il cookie
	        Cookie[] cookies=request.getCookies();
	    	if (cookies != null) {
	    		for (Cookie cookie : cookies) {
	    			if (cookie.getName().equals("UVC")) {
	    				cookie.setMaxAge(0);
	    				boolean cookieStatus=false;
						try {
							cookieStatus = SecurityUtils.destroyCookie(cookie);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    				if(!cookieStatus) System.out.println("Cookie couldn't be destroyed!");
	    			} 
	    	    }
	    	}
	        // Redirect al login
	        response.sendRedirect("/mySelfie/");
	        break;
		}
		default:
			break;
		}
    	
    }
}