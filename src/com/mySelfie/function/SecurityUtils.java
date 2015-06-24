package com.mySelfie.function;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
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






import com.mySelfie.entity.User;
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
            String userQuery = "SELECT * FROM User WHERE username = ? AND password = ?";
            PreparedStatement userSQL = connect.prepareStatement(userQuery);
            userSQL.setString(1, username);
            userSQL.setString(2, password);
            ResultSet queryRes = userSQL.executeQuery();
            
            boolean usrExists = queryRes.next();
            
            // se c'è un risultato per la query
            if (usrExists)
            {
            	// vengono valorizzati i vari attributi dell'istanza dell'utente
            	user.setId_user(queryRes.getInt("id_user"));
            	user.setusername(queryRes.getString("username"));
            	user.setPassword(queryRes.getString("password"));
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
	            	String addCookieQuery = "INSERT INTO Cookie(id_user,cookie_key) values (?,?)";
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

}
