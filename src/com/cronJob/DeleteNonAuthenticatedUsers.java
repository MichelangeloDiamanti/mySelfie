package com.cronJob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mySelfie.connection.ConnectionManager;
import com.mySelfie.function.UserUtils;

public class DeleteNonAuthenticatedUsers implements ServletContextListener{
    private Thread t = null;
    public void contextInitialized(ServletContextEvent contextEvent) {
        t =  new Thread(){
            //task
            public void run(){                
                try {
                    while(true){

                    	//ricava tutti gli id degli utenti da cancellare
                    	List<Integer> expUsrId = UserUtils.getExpiredUsers();

                    	//ogni utente viene cancellato dal DBs
                    	for(int i = 0; i < expUsrId.size(); i++)
                		{
                    		// ottengo la connessione al DB
                    		Connection connect = ConnectionManager.getConnection();
                    		
                    		//id di appoggio
                    		int euid = expUsrId.get(i);
                    		
                    		//query che cancella l' user dal db
                        	String delExpUsrQuery = "DELETE FROM User WHERE id_user = ?";
                    		PreparedStatement delExpUsrSQL;
                       		try 
                       		{
                       			delExpUsrSQL = connect.prepareStatement(delExpUsrQuery);
                       			delExpUsrSQL.setInt(1, euid);
                    			delExpUsrSQL.execute();
                    		} catch (SQLException e) {e.printStackTrace();} 
                       		finally {try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }}

                		}
                    	//aspetta per 15 minuti
                    	Thread.sleep(900000);
                    }
                } catch (InterruptedException e) {}
            }            
        };
        t.start();
        contextEvent.getServletContext();
    }
    public void contextDestroyed(ServletContextEvent contextEvent) {
        // context is destroyed interrupts the thread
        t.interrupt();
    }            
}