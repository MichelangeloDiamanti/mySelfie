package com.mySelfie.servlet;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.checkLogIn;
 
public class UserValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    	
    	response.setContentType("text/plain");
    	
    	
    	// viene salvato l'url richiesto in modo da poter essere reindirizzati dopo il login 
    	String requestURL = request.getParameter("redURL");
    	// se l'url richiesto è vuoto significa che la form di login non è stata presentata in risposta ad una richiesta passata
    	// per il filtro di controllo ma è stata acceduta direttamente dall'utente viene quindi impostato un url di redirect standard
    	if(requestURL.equals("")) requestURL = "http://localhost:8080/mySelfie/protected/homepage.jsp";
    	
    	// vengono presi i parametri necessari per il login dalla form
        String usr = request.getParameter("username");
        String pwd = request.getParameter("password");
        
        // viene istanziato un nuovo utente null
        User user = null;
      	
      	try 
      	{
      		// viene controllato se esiste un utente con le credenziali fornite
      		user = checkLogIn.checkLoginQuery(usr, pwd);
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
      		// viene trasmesso al browser l'url a cui deve andare
      		response.getWriter().write(requestURL);
		}
      	else
      	{
      		// altrimenti viene comunicato che il login è fallito
      		response.getWriter().write("loginFAIL");
      	}
    }
}