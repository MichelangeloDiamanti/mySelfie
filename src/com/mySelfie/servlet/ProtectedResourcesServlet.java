package com.mySelfie.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ProtectedResourcesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProtectedResourcesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//    il servlet riceve le richieste di leggere un file contenuto in un path relativo composto nel seguente modo:
//    -	nome della cartella in cui è contenuta la risorsa (a partire da "resources" escluso) esempio "/profilepics"
//    -	nome del file da accedere
//    quindi per esempio se dovessimo accedere all'immagine di profilo "pippo.png" scriveremmo "/profilepics/pippo.png"
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    	{
//    		ricava la home dell'utente (dove si suppone siano salvate le risorse protette)
    		String homeFolder = System.getProperty("user.home");
//			ottiene una stringa composta dal path relativo del file a cui si vuole accedere + il nome del file
    	    String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
//			viene istanziato un nuovo file nell'appropriata directory
    	    File file = new File( homeFolder + "/mySelfie/resources/", filename);
//    	    vengono impostati gli header della risposta HTTP da inviare (MIME risposta, lunghezza file...)
    	    response.setHeader("Content-Type", getServletContext().getMimeType(filename));
    	    response.setHeader("Content-Length", String.valueOf(file.length()));
    	    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
//    	    viene inviata la risposta
    	    Files.copy(file.toPath(), response.getOutputStream());
    	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
