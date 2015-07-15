package com.mySelfie.security;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.entity.User;
import com.mySelfie.exception.InvalidResetCodeException;
import com.mySelfie.exception.NoSuchUserException;
import com.mysql.jdbc.Statement;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public final class SecurityUtils {
	
    public static User checkLogin(String username, String password) throws NamingException
    {
    	User user = new User();			// istanzio un nuovo utente vuoto
        Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB

        try 
        {
            // Get the context and create a connection
            context = new InitialContext();
            // Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
            connect = datasource.getConnection();
            
            // verifica che username e password inseriti facciano riferimento ad uno user valido
            
            // ottiene la password dell'utente che sta effettuando il login (protetta da hash)
            String userQuery = "SELECT * FROM User WHERE username = ?";
            PreparedStatement userSQL = connect.prepareStatement(userQuery);
            userSQL.setString(1, username);
            ResultSet queryRes = userSQL.executeQuery();
            
            boolean usrExists = queryRes.next();
            
            // se lo username inserito esiste all'interno del DB
            if (usrExists)
            {
            	
            	// viene controllata che la password inserita sia corretta
                String hashedPassword = queryRes.getString("password");
                // controlla se la password inserita, hashata con il salt salvato all'interno del DB
                // corrisponde con quella usata per registrarsi
                boolean passwordIsValid = PasswordHash.validatePassword(password, hashedPassword);
            	
                // la query per questo campo torna 1 se l'account è stato attivato tramite mail
                int accountValid = queryRes.getInt("valid");
                // se è attivo allora il controllo da true, false altrimenti
                boolean accountIsActivated = (accountValid == 1) ? true : false;
                
                //se la password è valida
                if(passwordIsValid && accountIsActivated)
                {
	            	// vengono valorizzati i vari attributi dell'istanza dell'utente
	            	user.setId_user(queryRes.getInt("id_user"));
	            	user.setusername(queryRes.getString("username"));
	            	user.setEmail(queryRes.getString("email"));
	            	user.setPhone(queryRes.getString("phone"));
	            	user.setName(queryRes.getString("name"));
	            	user.setSurname(queryRes.getString("surname"));
	            	user.setGender(queryRes.getString("gender"));
	            	user.setNotes(queryRes.getString("notes"));
	            	user.setCity(queryRes.getString("city"));
	            	user.setProfilepic(queryRes.getString("profilepic"));
	            	user.setBirthdate(queryRes.getDate("birthdate"));
	            	// viene impostato l'utente a valido
	            	user.setValid(true);
            	}
            }
            else 
            {
            	// altrimenti l'untente non è valido
            	user.setValid(false);
			}
           
           
        } catch (SQLException e) { e.printStackTrace();
        // catch password hash
        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
        // viene restituito l'utente
        return user;
    }

    // Genera una nuova sessione a partire dallo userID proveniente dal cookie
    public static User setSessionFromCookie(int userID) throws NamingException {
    	User user = new User();			// istanzio un nuovo utente vuoto
        Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB

        try 
        {
            // Get the context and create a connection
            context = new InitialContext();
            // Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
            connect = datasource.getConnection();
            
            // verifica che username e password inseriti facciano riferimento ad uno user valido
            String userQuery = "SELECT * FROM User WHERE id_user = ?";
            PreparedStatement userSQL = connect.prepareStatement(userQuery);
            userSQL.setInt(1, userID);
            ResultSet queryRes = userSQL.executeQuery();
            boolean usrExists = queryRes.next();
            
            // se c'è un risultato per la query
            if (usrExists)
            {
            	// vengono valorizzati i vari attributi dell'istanza dell'utente
            	user.setId_user(queryRes.getInt("id_user"));
            	user.setusername(queryRes.getString("username"));
            	user.setEmail(queryRes.getString("email"));
            	user.setPhone(queryRes.getString("phone"));
            	user.setName(queryRes.getString("name"));
            	user.setSurname(queryRes.getString("surname"));
            	user.setGender(queryRes.getString("gender"));
            	user.setNotes(queryRes.getString("notes"));
            	user.setCity(queryRes.getString("city"));
            	user.setProfilepic(queryRes.getString("profilepic"));
            	user.setBirthdate(queryRes.getDate("birthdate"));
            	// viene impostato l'untente a valido
            	user.setValid(true);
            }
            else 
            {
            	// altrimenti l'untente non è valido
            	user.setValid(false);
			}
           
           
        } catch (SQLException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
        // viene restituito l'utente
        return user;
    }
    	
    public static String generateKey() {
    	// Genera nuova chiave
    	SecretKey secretKey = null;
    	String stringKey = null;
    	try {
    		secretKey = KeyGenerator.getInstance("HmacSHA512").generateKey();
    	} 
    	catch (NoSuchAlgorithmException e) {
    				e.printStackTrace();
    	}
    	if (secretKey != null) stringKey = Base64.encode(secretKey.getEncoded(), Base64.BASE64DEFAULTLENGTH);
    	return stringKey;
    }
    
    
    public static HttpServletResponse generateCookie (HttpServletResponse response, int userID) throws UnsupportedEncodingException, NamingException {
			
    		// Variabili
    		String stringKey=null;
			int affectedRows=0;
			
			// Connessione al database
			Context context = null;			// contesto
	        DataSource datasource = null;	// dove pescare i dati
	        Connection connect = null;		// connessione al DB
	        try {
	            // Get the context and create a connection
	            context = new InitialContext();
	            // Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
	            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	            connect = datasource.getConnection();
	            do{
	            	stringKey = generateKey();
	            	String addCookieQuery = "INSERT INTO Cookie(id_user,cookie_key,expire_date,valid) values (?,?,DATE_ADD(CURDATE(), INTERVAL 365 DAY),1)";
	            	PreparedStatement addCookieSQL = connect.prepareStatement(addCookieQuery,Statement.RETURN_GENERATED_KEYS);
	            	addCookieSQL.setInt(1, userID);
	            	addCookieSQL.setString(2, stringKey);
	            	affectedRows = addCookieSQL.executeUpdate();
	            }
	            while(affectedRows==0);
	        }
	        catch (SQLException e) { 
	        	e.printStackTrace();
			}
	    
	        finally {
	            // chiude la connessione
	        	try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
	        }
	        
	        // Genera cookie
		  	Cookie userValidCookie = new Cookie ("UVC", URLEncoder.encode(stringKey, "UTF-8"));
			userValidCookie.setMaxAge(60*60*24*365);
			response.addCookie(userValidCookie);
	
    	return response;
    }
    
    public static boolean destroyCookie (Cookie cookie) throws SQLException, UnsupportedEncodingException {
    	Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB
        boolean cookieStatus = false;
    
            // Get the context and create a connection
            try {
				context = new InitialContext();
				// Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
	            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	            connect = datasource.getConnection();
	            
	            int affectedRows=0;
	            
	            // Setta il valore di validità del cookie a 0
	            String cookieDestroyString = "UPDATE Cookie SET valid=0 WHERE cookie_key=?";
	            PreparedStatement cookieDestroySQL = connect.prepareStatement(cookieDestroyString,Statement.RETURN_GENERATED_KEYS);
	            cookieDestroySQL.setString(1, URLDecoder.decode(cookie.getValue(), "UTF-8"));
	            affectedRows = cookieDestroySQL.executeUpdate();
	            if(affectedRows > 0) cookieStatus=true;
			} catch (NamingException e) {
				e.printStackTrace();
			}
    	return cookieStatus;
    	
    }
    
    /**
     * Prende in input l'id di uno user ed il codice generato in fase di registrazione
     * per validare la mail e lo inserisce nalla tabella del DB
     * 
     * @param userId	id utente
     * @param code		codice
     * @return			true se l'operazione va a buon fine
     */
    public static boolean setUserValidationCode(int userId, String code)
    {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String userValidationString = "INSERT INTO user_validation(id_user, code) VALUES (?, ?)";
		PreparedStatement userValidationSQL;

		try {

			// prepara lo statement a partire dalla stringa
			userValidationSQL = conn.prepareStatement(userValidationString);
			// imposta i parametri nello statement
			userValidationSQL.setInt(1, userId);
			userValidationSQL.setString(2, code);
			// esegue la query
			int affectedRows = userValidationSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"setting validation code failed, no rows affected.");
			}
			// altrimenti
			else {
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
    }
    
    /**
     * Prende in input un codice di validazione account generato dalla registrazione
     * e vede se corrisponde ad un account esistente. in caso affermativo ritorna l'id
     * del possessore dell'account altrimenti -1
     * 
     * @param code	codice di validazione
     * @return		id utente se esiste altrimenti -1
     */
	public static int checkUserValidationCode(String code) 
	{
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		int userId = -1;
		// query in formato stringa e statement
		String validationCodeString = "SELECT id_user FROM user_validation WHERE code = ?";
		PreparedStatement validationCodeSQL;

		try {

			// prepara lo statement a partire dalla stringa
			validationCodeSQL = conn.prepareStatement(validationCodeString);
			// imposta i parametri nello statement
			validationCodeSQL.setString(1, code);
			// esegue la query
			ResultSet checkValidationRes = validationCodeSQL.executeQuery();
			// se c'è un risultato signigica che il primo utente segue il
			// secondo
			if (checkValidationRes.next()) {
				// in questo caso si imposta l'id dell'utente con quello ricavato
				userId = checkValidationRes.getInt("id_user");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userId;
	}
	
	/**
	 * prende in input l'id dello user che vuole resettare le credenziali ed un codice
	 * generato precedentemente e imposta crea un record nella tabella di reset delle credenziali
	 * 
	 * @param userId	id dello user che vuole resettare la password
	 * @param code		codice di reset
	 * @return			true se tutto è andato a buon fine, false altrimenti
	 */
	public static boolean setUserCredentialsResetCode(int userId, String code){
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String resetCredentialsString = 
					"INSERT INTO "
				+ 		"user_reset_credentials(id_user, code, valid, issue_date) "
				+ 	"VALUES "
				+ 		"(?, ?, 1, now())";
		PreparedStatement resetCredentialsSQL;

		try {

			// prepara lo statement a partire dalla stringa
			resetCredentialsSQL = conn.prepareStatement(resetCredentialsString);
			// imposta i parametri nello statement
			resetCredentialsSQL.setInt(1, userId);
			resetCredentialsSQL.setString(2, code);
			// esegue la query
			int affectedRows = resetCredentialsSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"setting a reset code failed, no rows affected.");
			}
			// altrimenti
			else {
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
    /**
     * Prende in input un codice di reset e vede se corrisponde ad un account esistente. 
     * In caso affermativo ritorna l'id del possessore dell'account altrimenti -1
     * 
     * @param code	codice di validazione
     * @return		id utente se esiste altrimenti -1
     * @throws InvalidResetCodeException 
     */
	public static int checkUserCredentialsResetCode(String code) throws NoSuchUserException, InvalidResetCodeException
	{
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		int userId = -1;
		// query in formato stringa e statement
		String passwordResetCodeString = "SELECT id_user, valid FROM user_reset_credentials WHERE code = ?";
		PreparedStatement passwordResetCodeSQL;

		try {

			// prepara lo statement a partire dalla stringa
			passwordResetCodeSQL = conn.prepareStatement(passwordResetCodeString);
			// imposta i parametri nello statement
			passwordResetCodeSQL.setString(1, code);
			// esegue la query
			ResultSet passwordResetCodeRes = passwordResetCodeSQL.executeQuery();
			// se c'è un risultato signigica che il codice di reset è valido
			if (passwordResetCodeRes.next()) {
				// in questo caso si imposta l'id dell'utente con quello ricavato
				userId = passwordResetCodeRes.getInt("id_user");
				int codeValid = passwordResetCodeRes.getInt("valid");
				// controlla che il codice sia valido
				if(codeValid == 0) 
					throw new InvalidResetCodeException("The code you supplied for resetting credentials doesn't appear to be valid.");
			}
			else {
				throw new NoSuchUserException("The code you supplied for resetting credentials doesn't belong to any user.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userId;
	}
	
	/**
	 * prende in input un codice di reset password e lo invalida
	 * 
	 * @param code
	 * @return
	 * @throws SQLException
	 */
    public static boolean invalidateUserCredentialsResetCode(String code) throws SQLException {
		// ottiene la connessione al database
		Connection conn = ConnectionManager.getConnection();
		// risultato dell'operazione
		boolean result = false;
		// query in formato stringa e statement
		String invalidateCodeString = 
					"UPDATE "
				+ 		"user_reset_credentials "
				+ 	"SET "
				+ 		"valid = 0, "
				+		"used_date = now() "
				+ 	"WHERE "
				+ 		"code = ?";
		PreparedStatement invalidateCodeSQL;

		try {

			// prepara lo statement a partire dalla stringa
			invalidateCodeSQL = conn.prepareStatement(invalidateCodeString);
			// imposta i parametri nello statement
			invalidateCodeSQL.setString(1, code);
			// esegue la query
			int affectedRows = invalidateCodeSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException(
						"couldnt invalidate reset code, no rows affected.");
			}
			// altrimenti
			else {
				result = true;
			}
		} finally {
			try {
				// chiude la connessione
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
    }
	
}
