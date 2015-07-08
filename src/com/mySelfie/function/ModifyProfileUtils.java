package com.mySelfie.function;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.security.PasswordHash;

public class ModifyProfileUtils {

	//ricava la password criptata di uno user passato tramite id
	public static String getHashedPassword(int me_id)
	{
		String Hpass = "";
		
		//apro una connessione al DB
		Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB
		try
		{	
            context = new InitialContext();
			datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	        connect = datasource.getConnection();	  
	        
	        // ricava la password criptata
			String passQuery = "SELECT password FROM User WHERE id_user = ? ";
			PreparedStatement passSQL = connect.prepareStatement(passQuery);
			passSQL.setInt(1, me_id);
			ResultSet passRes = passSQL.executeQuery();		        
			
			while (passRes.next()) 
				Hpass = passRes.getString("password");
		}
		catch (SQLException | NamingException e) { e.printStackTrace(); } 
    	finally 
    	{
    		// chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
		
		return Hpass;
		
	}
	
	public static void updateProfile(int me_id, String name, String surname, String city, String birthdate, String gender,
									 String phone, String notes, String username, String password, String profilepic) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		//apro una connessione al DB
		Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB
		try
		{	
            context = new InitialContext();
            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
	        connect = datasource.getConnection();	  
	    
	        // aggiorna i campi dello user
			String updateUserQuery = "UPDATE User SET "
								   + "name = ? , "
								   + "surname = ? , "
								   + "city = ? , "
								   + "birthdate = ? , "
								   + "gender = ? , "
								   + "phone = ? , "
								   + "notes = ? , "
								   + "username = ? ";
			
			if(!password.isEmpty() && !password.equals(""))
				updateUserQuery += ", password = ? ";
			
			if(!profilepic.isEmpty() && !profilepic.equals(""))
				updateUserQuery += ", profilepic = ? ";
			
			updateUserQuery += "where id_user = ? ";

			
			PreparedStatement updateUserSQL = connect.prepareStatement(updateUserQuery);
			updateUserSQL.setString(1, name);
			updateUserSQL.setString(2, surname);
			updateUserSQL.setString(3, city);
			updateUserSQL.setString(4, birthdate);
			updateUserSQL.setString(5, gender);
			updateUserSQL.setString(6, phone);
			updateUserSQL.setString(7, notes);
			updateUserSQL.setString(8, username);
			
			int m = 9;
			
			if(!password.isEmpty() && !password.equals(""))
			{
				updateUserSQL.setString(m, PasswordHash.createHash(password));
				m++;
			}
			if(!profilepic.isEmpty() && !profilepic.equals(""))
			{
				updateUserSQL.setString(m, profilepic);
				m++;
			}
			updateUserSQL.setInt(m, me_id);
			
			updateUserSQL.executeUpdate();		        

		}
		catch (SQLException | NamingException e) { e.printStackTrace(); } 
    	finally 
    	{
    		// chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
	}
	
}


