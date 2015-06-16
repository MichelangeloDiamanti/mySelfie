package com.mySelfie.function;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.entity.User;

public final class checkLogIn {
	
    public static User checkLoginQuery(String username, String password) throws NamingException
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
            String userQuery = "SELECT * FROM User WHERE nickname = ? AND password = ?";
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
            	user.setNickname(queryRes.getString("nickname"));
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
}
