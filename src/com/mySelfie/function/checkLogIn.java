package com.mySelfie.function;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public final class checkLogIn {

    public static String checkLoginQuery(String username, String password) throws NamingException
    {
    	String controlResult = "";
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
            
            // verifica che lo username esista
            String idusrQuery = "SELECT id_user FROM User WHERE nickname = ? ";
            PreparedStatement idusrSQL = connect.prepareStatement(idusrQuery);
            idusrSQL.setString(1, username);
            ResultSet idusrRes = idusrSQL.executeQuery();
            
            boolean usrExists = idusrRes.next();
              
            //se lo username esiste
            if (usrExists)
            {
            	int user_id = idusrRes.getInt("id_user");
            	            	
            	//ricava la password per confrontarla
            	String pwdQuery = "SELECT password FROM User WHERE id_user = ? ";
                PreparedStatement pwdSQL = connect.prepareStatement(pwdQuery);
                pwdSQL.setInt(1, user_id);
                ResultSet pwdRes = pwdSQL.executeQuery();
                                
                String actualPwd = "";
                while (pwdRes.next()) 
                	actualPwd = pwdRes.getString("password");
                
                
            	
                //se le password corrispondono
                if(actualPwd.equals(password))
                {
                	controlResult = "loginOK";
                }
                else
                {
                	controlResult = "wrongPassword";
                }
            }
            else
            {
            	controlResult = "invalidUsername";
            }
           
           
        } catch (SQLException e) { e.printStackTrace();
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
        return controlResult;
    }
}
