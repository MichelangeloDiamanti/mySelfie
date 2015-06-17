package com.mySelfie.function;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.exception.NickNameInUseException;

public final class UserSignUp {

    public static void signUpQuery(PrintWriter out, Map<String, String> m) throws NamingException, NickNameInUseException {
        Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB

        try {
            // Get the context and create a connection
            context = new InitialContext();
            // Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
            connect = datasource.getConnection();
            
            // verifica che lo username non sia gia in uso
            String uniqueNickStr = "SELECT id_user FROM User WHERE nickname = ? ";
            PreparedStatement uniqueNickSQL = connect.prepareStatement(uniqueNickStr);
            uniqueNickSQL.setString(1, m.get("nickname"));
            ResultSet uniqueNickRes = uniqueNickSQL.executeQuery();
            
            boolean resEmpty = uniqueNickRes.next();
            
            // Se il nick è univoco
            if (!resEmpty)
            {
//	            // data di nascita passata come stringa, va convertita nel formato data di SQL
//	            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//	        	java.util.Date dataDiNascitaJava = null;
//	    		try {
//	    			dataDiNascitaJava = format.parse( m.get("dataDiNascita") );
//	    		} catch (ParseException e) {
//	    			// TODO Auto-generated catch block
//	    			e.printStackTrace();
//	    		}
//	        	java.sql.Date dataDiNascitaSql = new java.sql.Date( dataDiNascitaJava.getTime() );
//	            
	            // costruisce la query
	            String sql = "INSERT INTO User (nickname, password, email, profilepic ) "
	            		   + "VALUES (?, ?, ?, ?)";
	            PreparedStatement statement = connect.prepareStatement(sql);
	            
	            statement.setString(1, m.get("nickname"));
	            statement.setString(2, m.get("password"));
	            statement.setString(3, m.get("email"));
	            statement.setString(4, m.get("profilePic"));
	            
//	            statement.setDate(7, dataDiNascitaSql);
	            
	            // esegue la query
	            statement.executeUpdate();
            }
            else
            {
            	// se lo username è in uso viene generata un'eccezione
            	throw new NickNameInUseException();
            }
        } catch (SQLException e) { e.printStackTrace(out);
        } finally {
            // chiude la connessione
            try { connect.close(); } catch (SQLException e) { e.printStackTrace(out); }
        }
    }
    
    // la funzione controlla se lo username passato è in uso, in caso affermativo torna falso, altrimenti vero
    public static boolean checkUsername(String nickname) throws NamingException {
        Context context = null;			// contesto
        DataSource datasource = null;	// dove pescare i dati
        Connection connect = null;		// connessione al DB
        
        boolean resEmpty = false;		// true se lo username NON è in uso, falso altrimenti
        
        try{
            // Get the context and create a connection
            context = new InitialContext();
            // Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
            connect = datasource.getConnection();
            
            // verifica che lo username non sia gia in uso
            String uniqueNickStr = "SELECT id_user FROM User WHERE nickname = ? ";
            PreparedStatement uniqueNickSQL = connect.prepareStatement(uniqueNickStr);
            uniqueNickSQL.setString(1, nickname);
            ResultSet uniqueNickRes = uniqueNickSQL.executeQuery();
            
            resEmpty = !(uniqueNickRes.next());	// se c'è un risultato (nickname in uso) la 
            									// funzione uniqueNickRes.next() torna vero
            									// quindi negato torna falso
            
        }catch (SQLException e) { e.printStackTrace();
        }finally {
        // chiude la connessione
        try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
		return resEmpty;
    }
    
}
