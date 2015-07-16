package com.mySelfie.function;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.security.PasswordHash;

public class ModifyProfileUtils {
	
	/**
	 * Aggiorna il profilo di un utente con i parametri presi in input
	 * 
	 * @param me_id
	 * @param name
	 * @param surname
	 * @param city
	 * @param birthdate
	 * @param gender
	 * @param phone
	 * @param notes
	 * @param username
	 * @param password
	 * @param profilepic
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static void updateProfile(int me_id, String name, String surname, String city, String birthdate, String gender,
									 String phone, String notes, String username, String password, String profilepic) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		// ottengo la connessione al DB
		Connection connect = ConnectionManager.getConnection();	  	  
	    
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
		
		// se la nuova password non è inserita non và aggiornata
		if(!password.isEmpty() && !password.equals(""))
			updateUserQuery += ", password = ? ";
		// se la nuova immagine di profilo non è inserita non và aggiornata
		if(!profilepic.isEmpty() && !profilepic.equals(""))
			updateUserQuery += ", profilepic = ? ";
		// aggiunge la condizione where
		updateUserQuery += "where id_user = ? ";
		
		PreparedStatement updateUserSQL;
		try {
			// valorizza i campi con i valori ricevuti
			updateUserSQL = connect.prepareStatement(updateUserQuery);
			updateUserSQL.setString(1, name);
			updateUserSQL.setString(2, surname);
			updateUserSQL.setString(3, city);
			updateUserSQL.setString(4, birthdate);
			updateUserSQL.setString(5, gender);
			updateUserSQL.setString(6, phone);
			updateUserSQL.setString(7, notes);
			updateUserSQL.setString(8, username);
			
			/* valore che indica l'indice del campo da valorizzare
			 * è necessario perchè per esempio se non si vuole cambiare la password
			 * l'indice della profilepic deve essere 9, altrimenti 10 e cosi via...
			 */
			int m = 9;
			
			// se si vuole cambiare la password essa viene valorizzata nella query
			if(!password.isEmpty() && !password.equals(""))
			{
				updateUserSQL.setString(m, PasswordHash.createHash(password));
				m++;
			}
			// se si vuole cambiare l'immagine di profilo essa viene valorizzata nella query
			if(!profilepic.isEmpty() && !profilepic.equals(""))
			{
				updateUserSQL.setString(m, profilepic);
				m++;
			}
			
			// viene valorizzato l'id dello user nella clausola where
			updateUserSQL.setInt(m, me_id);

			// viene eseguita la query
			updateUserSQL.executeUpdate();	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

	}
	
}


