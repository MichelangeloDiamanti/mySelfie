package com.mySelfie.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.mySelfie.entity.User;
import com.mySelfie.security.SecurityUtils;
 
/*
	* Il file web.xml è configurato per reindirizzare verso questo sevlet tutte le richieste dirette a /protected/*
	* Questa classe implementa l'interfaccia filtro di Java, si comporta quindi come tale.
*/
public class UserCheckFilter implements Filter {
    private String LOGIN_ACTION_URI;
     
    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        LOGIN_ACTION_URI = fConfig.getInitParameter("loginActionURI");
    }
 
    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }
 
    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        
        //Dichiarazione variabili ottenute dal database
    	int userID=0;
        Date expireDate = null;
        int valid=0;
 
        boolean checkCookie = false;
        boolean checkSession = false;
        
        // salva l'URL di provenienza in modo da poter reindirizzare dopo il login
    	String requestURL = req.getRequestURL().toString();
   
    	// controlla se lo user che richiede la risorsa è autenticato
        User user = (User) session.getAttribute("user");
        // se non lo è viene settato il flag
        if (user == null && !LOGIN_ACTION_URI.equals(req.getRequestURI())){
            checkSession=false;
        }
        else { checkSession = true; }
        
      //       ---> CONTROLLO COOKIE <--- 
     // Controllo esistenza e validità cookie
    	if(checkSession==false) {
    		
	        Cookie[] cookies=req.getCookies();
	    	String key=null;
	    	if (cookies != null) {
	    		for (Cookie cookie : cookies) {
	    			if (cookie.getName().equals("UVC")) {
	    				key = URLDecoder.decode(cookie.getValue(), "UTF-8");
		    			//Date expireTime = null;
		    	    	// Connessione al database per controllare la validità del cookie
		    	    	Context context = null;			// contesto
		    	        DataSource datasource = null;	// dove pescare i dati
		    	        Connection connect = null;		// connessione al DB
		    	        try {
		    	            // Get the context and create a connection
		    	            context = new InitialContext();
		    	            // Prende le informazioni del database dal file sito in 'WebContent/META-INF/context.xml'
		    	            datasource = (DataSource) context.lookup("java:/comp/env/jdbc/mySelfie");
		    	            connect = datasource.getConnection();
		    	            // Query estrapola user,data e ora scadenza cookie
		    	            String checkCookieQuery = "SELECT id_user,expire_date,valid from Cookie where cookie_key=?";
		    	            PreparedStatement checkCookieSQL = connect.prepareStatement(checkCookieQuery);
		    	            checkCookieSQL.setString(1, key);
		    	            ResultSet checkCookieRes = checkCookieSQL.executeQuery();
		    	            
		    	            // Estrapolo informazioni dai risultati della query
		    	            while (checkCookieRes.next()) {
		    	            	userID = checkCookieRes.getInt("id_user");
		    	                expireDate = checkCookieRes.getDate("expire_date");
		    	                valid = checkCookieRes.getInt("valid");
		    	            }
		    	        }
		    	        catch (SQLException e) { 
		    	        	e.printStackTrace();
		    			} catch (NamingException e) {
		    				e.printStackTrace();
		    			}
		    	    
		    	        finally {
		    	            // chiude la connessione
		    	        	try { connect.close(); } catch (SQLException e) { e.printStackTrace(); }
		    	        }
		    	        
		    	        //get current date time with Date()
		    	 	   	Date todayDate = new Date();
		    	 	   	
		    	        if((expireDate.compareTo(todayDate)>0) && valid==1){
		    	        	checkCookie=true;
		    	        }
	    			
	    			} 
	    	    }
	    	}
	    	
	        
	   
    	}
    	
    	// se il cookie è valido istanzia una nuova sessione
    	if(checkCookie == true)
    	{
    		try {
				user = SecurityUtils.setSessionFromCookie(userID);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		session.setAttribute("user", user);
    		checkSession=true;
    	}

    	// se non c'è una sessione torna al login
    	if(checkSession == false) {
    		RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
	        // passa alla form l'url di provenienza come attributo
	        request.setAttribute("redURL", requestURL);
	        rd.forward(request, response);
	        return;
    	}
    	
    	// altrimenti lascia il compito alla classe filter java
        chain.doFilter(request, response);	
    }
}