package com.mySelfie.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mySelfie.exception.InvalidResetCodeException;
import com.mySelfie.exception.NoSuchUserException;
import com.mySelfie.function.UserUtils;
import com.mySelfie.mail.SendMail;
import com.mySelfie.security.PasswordHash;
import com.mySelfie.security.SecurityUtils;
import com.toastMessage.Message;

/**
 * Servlet implementation class ResetCredentialsServlet
 */
@WebServlet("/ResetCredentialsServlet")
public class ResetCredentials extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetCredentials() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//istruzioni per l'utente
		Message message = new Message();
		// ricava il codice di reset dall'url
		String code = request.getPathInfo().substring(1);
		
		try {
			// controlla che il codice sia valido
			SecurityUtils.checkUserCredentialsResetCode(code);
			// lo mette nella risposta
			request.setAttribute("secret", code);
			// imposto un messaggio di successo da far vedere tramite toast
			message.setType("info");
			message.setTitle("Information");
			message.setBody("Enter your new credentials");
			request.setAttribute("toastMessage", message);
			// mando la risposta al client
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/newcredentials.jsp"); 
			dispatcher.forward(request,response);
		} catch (NoSuchUserException | InvalidResetCodeException e) {
			// imposto un messaggio di errore da far vedere tramite toast
			message.setType("fail");
			message.setTitle("Invalid Code");
			message.setBody("The code you supplied doesn't appear to be valid, please try again.");
			request.setAttribute("toastMessage", message);
			// mando la risposta al client
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
			dispatcher.forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		switch (action) {
			/*
			 * qualcuno richiede di cambiare password, dopo essere stato controllato
			 * bisogna mandare la mail con il codice per reimpostarla
			 */
			case "sendMail":
			{
				//esito della funzione da far visualizzare allo user
				Message message = new Message();
				
				// ricavo la mail dalla request
				String email = request.getParameter("email");
				if(email!=null)
				{
					try {
						// ricavo l'id dello user a cui è associata la mail
						int userId = UserUtils.getIdByEmail(email);
						// ricavo lo username per incorporarlo nel messaggio di reset
						String username = UserUtils.getUsernameById(userId);
						// mando la mail e mi faccio ritornare il codice di reset
						String resetCode = SendMail.sendCredentialsResetCode(username, email);		
						// imposta il codice di reset generato in fase di invio della mail
						SecurityUtils.setUserCredentialsResetCode(userId, resetCode);
						// imposto un messaggio di successo da far vedere tramite toast
						message.setType("info");
						message.setTitle("Mail sent");
						message.setBody("You've been sent a code to reset your credentials, please check your inbox.");
						request.setAttribute("toastMessage", message);
						
					} catch (NoSuchUserException e) { // se nessuno user ha quell'indirizzo email
						// imposto un messaggio di errore da far vedere tramite toast
						message.setType("fail");
						message.setTitle("Uh-oh, something went wrong!");
						message.setBody("There is no user for the email address specified, please check your input.");
						request.setAttribute("toastMessage", message);
					} catch (MessagingException e) { // non è stato possibile mandare la mail
						// imposto un messaggio di errore da far vedere tramite toast
						message.setType("fail");
						message.setTitle("Uh-oh, something went wrong!");
						message.setBody("There was a problem sending the email, please try again later");
						request.setAttribute("toastMessage", message);
					}
				}
				
				// mando la risposta al client
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
				dispatcher.forward(request,response);
			}
			break;
			/*
			 * si proviene dalla form con un codice di reset, bisogna controllarlo
			 * e se valido cambiare la password dell'utente e mandare un messaggio
			 * di risposta
			 */
			case "newCredentials":
			{
				
	        	String password = request.getParameter("password");       	
	        	String confirmPassword = request.getParameter("confirmPassword");       	    	
				String secret = request.getParameter("secret");
				
	        	//esito della funzione da far visualizzare allo user
				Message message = new Message();
	        	
				// controlla se i parametri inseriti sono validi
	        	if(
	        		// se nessun parametro è vuoto 
	    			(password != null && !password.isEmpty()) &&
	    			(confirmPassword != null && !confirmPassword.isEmpty()) &&
	        		(password.equals(confirmPassword))
	        	   )
	        	{
					
					String code = request.getParameter("secret");
					if(code != null)
					{
						try {
							// controlla che il codice sia valido e ricava l'id dell'untente che richiede il cambio password
							int userId = SecurityUtils.checkUserCredentialsResetCode(code);
							// ottiene la nuova password dalla request
							String newPassword = request.getParameter("password");
							// cambia la password con quella in input hashata
							UserUtils.setNewPassword(userId, PasswordHash.createHash(newPassword));
							// invalida il codice usato
							SecurityUtils.invalidateUserCredentialsResetCode(code);
							// imposto un messaggio di successo da far vedere tramite toast
							message.setType("success");
							message.setTitle("Reset Succeeded");
							message.setBody("You can now log in with the new credentials, have fun!");
							request.setAttribute("toastMessage", message);
						} catch (NoSuchUserException | InvalidResetCodeException e) { // se il codice fornito non è valido
							// imposto un messaggio di errore da far vedere tramite toast
							message.setType("fail");
							message.setTitle("Invalid Code");
							message.setBody("The code you supplied doesn't appear to be valid, please try again.");
							request.setAttribute("toastMessage", message);
						} catch (SQLException e) { // non è stato possibile aggiornare le credenziali nel DB
							// imposto un messaggio di errore da far vedere tramite toast
							message.setType("fail");
							message.setTitle("Sorry, Something Went Wrong");
							message.setBody("We weren't able to update your credentials, try again later.");
							request.setAttribute("toastMessage", message);
							e.printStackTrace();
						} catch ( NoSuchAlgorithmException | InvalidKeySpecException e) { // fail hashing password
							e.printStackTrace();
						} 
					}
					
					// mando la risposta al client
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
					dispatcher.forward(request,response);
				} else {
//					message.setType("fail");
//					message.setTitle("Reset failed");
//					message.setBody("The information you supplied are not valid");
//					request.setAttribute("toastMessage", message);
//					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/resetCredentials/" + secret); 
//					dispatcher.forward(request,response);
					
					// viene reindirizzata la stessa form
					response.sendRedirect(response.encodeRedirectURL("/mySelfie/resetCredentials/" + secret));
					
	        	}
			}
			break;
		}

	}

}
