package com.mySelfie.mail;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SendMail {
    
    /**
     * prende in input la mail dello user a cui mandare il messaggio di conferma 
     * e, dopo aver creato un token random e averlo incorporato in un link manda
     * il messaggio. 
     * 
     * @param recipientUsername			username del nuovo utente
     * @param recipientEmailAddress		mail del destinatario	
     * @return							il token generato se tutto è andato bene altrimenti null
     */
    public static String sendSignupConfirmation(String recipientUsername, String recipientEmailAddress) {

            
    	InitialContext initialContext;	// necessario a leggere le credenziali dal context
    	String token = null;			// token random verifica mail
    	
		try {
			
			/*
			 * prende le credenziali di accesso della email dal file context.xml
			 */
			initialContext = new InitialContext();
	    	Context environmentContext = (Context) initialContext.lookup("java:/comp/env");
	    	final String email = (String) environmentContext.lookup("myselfieConfirmationEmail");
	    	final String password = (String) environmentContext.lookup("myselfieConfirmationPassword");
	    	
	    	// impostazioni server mail google
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");
	        
	        // apre una sessione SMTP con autenticazione
	        Session session = Session.getInstance(props,
	          new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(email, password);
	            }
	          });

	        // manda il messaggio
	        try {
	        	
	        	// genera un numero random
	        	SecureRandom random = new SecureRandom();
	        	/*
	        	 *  genera una stringa randomica di lunghezza 160/5 = 32
	        	 *  160 --> è il numero in input di BigInteger rappresenta il numero di bits
	        	 *  		casuali da cui pescare
	        	 *  5 	--> è 2^5 il numero di bit passati in input al metodo toString
	        	 */
	        	token = new BigInteger(160, random).toString(32);
	            
	        	// dichiara un nuovo messaggio mime
	            Message message = new MimeMessage(session);
	            // imposta il sender
	            message.setFrom(new InternetAddress("myselfieconfirmation@gmail.com"));
	            // imposta il destinatario
	            message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(recipientEmailAddress));
	            // imposta l'oggetto della mail
	            message.setSubject("Myselfie - Confirm Account");
	            // imposta il testo della mail
	            message.setText(
	            		"Dear "
	            		+ recipientUsername
	            		+ "\n\nPlease follow this link to confirm your account: "
	            		+ "https://localhost:8443/mySelfie/validation/"
	            		+ token
	            		);
	            // manda il messaggio
	            Transport.send(message);

	        } catch (MessagingException e) {
	            throw new RuntimeException(e);
	        }
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
        // ritorna il token generato
        return token;
    }
    
    /**
     * prende in input la mail dello user a cui mandare il messaggio  
     * e, dopo aver creato un token random e averlo incorporato in un link manda
     * il messaggio. 
     * 
     * @param recipientUsername			username del destinatario
     * @param recipientEmailAddress		mail del destinatario	
     * @return							il token generato se tutto è andato bene altrimenti null
     * @throws MessagingException 
     */
    public static String sendCredentialsResetCode(String recipientUsername, String recipientEmailAddress) throws MessagingException {
    	
    	
    	InitialContext initialContext;	// necessario a leggere le credenziali dal context
    	String token = null;			// token random verifica mail
    	
    	try {
    		
    		/*
    		 * prende le credenziali di accesso della email dal file context.xml
    		 */
    		initialContext = new InitialContext();
    		Context environmentContext = (Context) initialContext.lookup("java:/comp/env");
    		final String email = (String) environmentContext.lookup("myselfieConfirmationEmail");
    		final String password = (String) environmentContext.lookup("myselfieConfirmationPassword");
    		
    		// impostazioni server mail google
    		Properties props = new Properties();
    		props.put("mail.smtp.auth", "true");
    		props.put("mail.smtp.starttls.enable", "true");
    		props.put("mail.smtp.host", "smtp.gmail.com");
    		props.put("mail.smtp.port", "587");
    		
    		// apre una sessione SMTP con autenticazione
    		Session session = Session.getInstance(props,
    				new javax.mail.Authenticator() {
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(email, password);
    			}
    		});
    			
			// genera un numero random
			SecureRandom random = new SecureRandom();
			/*
			 *  genera una stringa randomica di lunghezza 160/5 = 32
			 *  160 --> è il numero in input di BigInteger rappresenta il numero di bits
			 *  		casuali da cui pescare
			 *  5 	--> è 2^5 il numero di bit passati in input al metodo toString
			 */
			token = new BigInteger(160, random).toString(32);
			
			// dichiara un nuovo messaggio mime
			Message message = new MimeMessage(session);
			// imposta il sender
			message.setFrom(new InternetAddress("myselfieconfirmation@gmail.com"));
			// imposta il destinatario
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipientEmailAddress));
			// imposta l'oggetto della mail
			message.setSubject("Myselfie - Reset Credentials");
			// imposta il testo della mail
			message.setText(
					"Dear "
							+ recipientUsername
							+ "\n\nPlease follow this link to reset your credentials: "
							+ "https://localhost:8443/mySelfie/resetCredentials/"
							+ token
					);
			// manda il messaggio
			Transport.send(message);
    			
    	} catch (NamingException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
    	
    	// ritorna il token generato
    	return token;
    }
}