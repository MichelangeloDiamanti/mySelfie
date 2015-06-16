package com.mySelfie.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
 
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
        
        // salva l'URL di provenienza in modo da poter reindirizzare dopo il login
    	String requestURL = req.getRequestURL().toString();
    	// controlla se lo user che richiede la risorsa è autenticato
        User user = (User) session.getAttribute("user");
         
        // se non lo è viene reindirizzata la pagina di login
        if (user == null && !LOGIN_ACTION_URI.equals(req.getRequestURI())){
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