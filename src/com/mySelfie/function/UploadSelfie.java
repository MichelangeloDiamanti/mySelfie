package com.mySelfie.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mySelfie.entity.Selfie;

public class UploadSelfie {

	public static void upload(Selfie selfie) throws NamingException {
		Context context = null; // contesto
		DataSource datasource = null; // dove pescare i dati
		Connection connect = null; // connessione al DB

		try {
			// Get the context and create a connection
			context = new InitialContext();
			// Prende le informazioni del database dal file sito in
			// 'WebContent/META-INF/context.xml'
			datasource = (DataSource) context
					.lookup("java:/comp/env/jdbc/mySelfie");
			connect = datasource.getConnection();

			String sql = "INSERT INTO Selfie (uploader, description, date, picture) "
					+ "VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connect.prepareStatement(sql);

			statement.setInt(1, selfie.getUploader());
			statement.setString(2, selfie.getDescription());
			statement.setTimestamp(3, selfie.getDate());
			statement.setString(4, selfie.getPicture());

			// esegue la query
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// chiude la connessione
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
