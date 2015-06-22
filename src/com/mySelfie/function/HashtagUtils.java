package com.mySelfie.function;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class HashtagUtils {

	// controlla se un hashtag esiste nel DB (senza connessione)
	public static boolean exist(String tagString)
	{
		return false;
	}
	
	// controlla se un hashtag esiste nel DB (con connessione)
	public static boolean exist(String tagName, Connection conn)
	{
		// il risultato del controllo
		boolean exist = false;
		
		// query in formato stringa e statement
        String tagsQuery = "SELECT * FROM Hashtag WHERE name = ?";
        PreparedStatement tagsSQL;
		
        try {
			
        	// prepara lo statement a partire dalla stringa
			tagsSQL = conn.prepareStatement(tagsQuery);
			// imposta i parametri nello statement
	        tagsSQL.setString(1, tagName);
	        // esegue la query
	        ResultSet tagsRes = tagsSQL.executeQuery();
	        // controlla se c'è un risultato
	        if(tagsRes.next())
	        {
	        	// se esiste ritorna true
	        	exist = true;
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return del risultato
		return exist;
	}
	
	// controlla se un hashtag esiste nel DB (senza connessione)
	public static int getId(String tagString)
	{
		return 0;
	}
	
	// restituisce l'id dell'hashtag col nome passato
	public static int getId(String tagName, Connection conn)
	{
		int id_hashtag = 0;
		// query in formato stringa e statement
        String tagsQuery = "SELECT id_hashtag FROM Hashtag WHERE name = ?";
        PreparedStatement tagsSQL;
		
        try {
			
        	// prepara lo statement a partire dalla stringa
			tagsSQL = conn.prepareStatement(tagsQuery);
			// imposta i parametri nello statement
	        tagsSQL.setString(1, tagName);
	        // esegue la query
	        ResultSet tagsRes = tagsSQL.executeQuery();
	        // controlla se c'è un risultato
	        if(tagsRes.next())
	        {
	        	// se esiste ritorna true
	        	id_hashtag = tagsRes.getInt("id_hashtag");
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id_hashtag;
	}
	
	// aggiunge un hashtag ad una selfie (senza connessione) ritorna true se tutto è andato bene
	public static boolean hashtagInSelfie(int id_selfie, int id_hashtag)
	{
		return false;
	}
	
	// aggiunge un hashtag ad una selfie (con connessione) ritorna true se tutto è andato bene
	public static boolean hashtagInSelfie(int id_selfie, int id_hashtag, Connection conn)
	{
		boolean result = false;
		// query in formato stringa e statement
		String hashSelfieString = "INSERT INTO hashtag_in_selfie(id_selfie, id_hashtag) VALUES (?, ?)";
		PreparedStatement hashSelfieSQL;
		
		try {
			
			// prepara lo statement a partire dalla stringa
			hashSelfieSQL = conn.prepareStatement(hashSelfieString);
			// imposta i parametri nello statement
			hashSelfieSQL.setInt(1, id_selfie);
			hashSelfieSQL.setInt(2, id_hashtag);
			// esegue la query
			int affectedRows = hashSelfieSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
	        if (affectedRows == 0) {
	            throw new SQLException("Creating user failed, no rows affected.");
	        }
	        // altrimenti
	        else
	        {
	        	result = true;
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	// crea un nuovo hashtag (senza connessione) ritorna l'id dell'hashtag se tutto è andato bene
	public static boolean newHashTag(String tagName)
	{
		return false;
	}
	
	// crea un nuovo hashtag (con connessione) ritorna l'id dell'hashtag tutto è andato bene
	public static int newHashTag(String tagName, Connection conn)
	{
		int id_hashtag = 0;
		// query in formato stringa e statement
		String hashSelfieString = "INSERT INTO Hashtag(name) VALUES (?)";
		PreparedStatement hashSelfieSQL;
		
		try {
			
			// dichiara lo statement specificando che deve ritornare la chiave della tabella della riga modificata
			hashSelfieSQL = conn.prepareStatement(hashSelfieString, Statement.RETURN_GENERATED_KEYS);

			// imposta i parametri nello statement
			hashSelfieSQL.setString(1, tagName);
			
			// esegue la query
			int affectedRows = hashSelfieSQL.executeUpdate();
			// se non è stata modificata nessuna riga spara un'eccezione
			if (affectedRows == 0) {
				throw new SQLException("Creating hashtag failed, no rows affected.");
			}
			// altrimenti
			else
			{
				try (ResultSet generatedKeys = hashSelfieSQL.getGeneratedKeys()) 
				{
		        	if (generatedKeys.next()) {
		        		// imposta l'id dell'hashtag alla chiave ritornata
		            	id_hashtag = generatedKeys.getInt(1);
		        	}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id_hashtag;
	}
	
	
}
