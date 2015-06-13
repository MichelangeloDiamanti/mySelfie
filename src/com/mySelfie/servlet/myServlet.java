package com.mySelfie.servlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;

import com.mySelfie.exception.NickNameInUseException;
import com.mySelfie.function.UserSignUp;

@MultipartConfig	// Serve per supportare l'upload di files, form multipart
@SuppressWarnings("serial")
public class myServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Se il servlet è stato chiamato dalla form di registrazione
        if (request.getParameter("signUp") != null) {
        	
        	String status = "";		// stato della registrazione
        	String reason = "";		// info addizionali sullo stato della registrazione
        	
        	// controlla se le password coincidono
        	if(request.getParameter("password").equals(request.getParameter("checkPassword")))
        	{
        		
	        	// SAVLATAGGIO IMMAGINE NEL FILESYSTEM DEL SERVER
	        	
	        	// Ottiene l'immagine dalla form
	    	    Part filePart = request.getPart("profilePic");	// Prende l'immagine da <input type="file" name="immagineDiProfilo">
	    	    String fileName = getFileName(filePart);				// Ottiene il nome del file
	    	    
	    	    //InputStream fileContent = filePart.getInputStream();	// boh? Sarà il formato del file?? Manco viene usata...
	    	    	   
	    	    // Imposta il percorso dove salvare l'immagine
	    	    String homeFolder = System.getProperty("user.dir");
	    	    String uploadPath = homeFolder + "/mySelfie/data/profilePics";
	    	    
	    	    // Istanzia un nuovo file nel path specificato
	    	    File uploads = new File(uploadPath);		
	    	    // Crea il nuovo file, assicurandosi che il nome sia univoco nella directory (prefisso, suffisso, directory)
	    	    String fileExtension = "." + FilenameUtils.getExtension(fileName);
	    	    File file = File.createTempFile("567", fileExtension, uploads); 
	    	    String uploadedFileName = file.getName();
	    	    
	    	    //Salva l'immagine caricata 
	    	    try (InputStream input = filePart.getInputStream()) {  
	    	        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    	    }
	    	    
	    	    
	    	    // SALVATAGGIO INFORMAZIONI INSERITE DALL'UTENTE NEL DB
	    	    
	    	    // Immagazzina le informazioni inserite in una mappa per passarle alla classe responsabile dell'inserimento nel DB
	        	Map<String, String> m = new HashMap<String, String>();
	        	
	        	m.put("nickname", request.getParameter("nickname"));
	        	m.put("password", request.getParameter("password"));       	
	        	m.put("checkPassword", request.getParameter("checkPassword"));
	        	m.put("email", request.getParameter("email"));	 
	        	m.put("profilePic", uploadedFileName);
	        	
	        	// prova a registrare un nuovo utente, una possibile eccezione è: username in uso
	            try {
	                // salva le credenziali nel database
	                UserSignUp.signUpQuery(out, m);
	                // se è andata a buon fine la registrazione viene impostato il messaggio a ok
	                status = "success";
	                reason = "goodInput";
	            } catch (NamingException e) {
	                e.printStackTrace();
	            } catch (NickNameInUseException e) {
	            	//	se lo username non è disponibile non viene effettuata la registrazione
	            	//	va quindi eliminata l'immagine di profilo caricata
	            	file.delete();
	            	status = "fail";
	            	reason = "nickNameInUseException";
				}
	            
	            // finita la registrazione viene renderizzata la homepage
	            response.sendRedirect("/mySelfie/index.jsp?status=" + status + "&reason=" + reason);
	          // Se le password non corrispondono viene visualizzato un messaggio di errore
        	} else {
        		status = "fail";
        		reason = "passwordDontMatch";
        		response.sendRedirect("/mySelfie/index.jsp?status=" + status + "&reason=" + reason);
        	}
        }
        else System.out.println("i'm not the Servlet you're looking for");  
    }
    
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	// Ricava il nome del file dalla form
	private static String getFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
	        }
	    }
	    return null;
	}
}