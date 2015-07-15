package com.mySelfie.servlet;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;

import com.mySelfie.entity.User;
import com.mySelfie.exception.EmailInUseException;
import com.mySelfie.exception.UsernameInUseException;
import com.mySelfie.function.UserUtils;
import com.mySelfie.mail.SendMail;
import com.mySelfie.security.SecurityUtils;
import com.toastMessage.Message;

@MultipartConfig	// Serve per supportare l'upload di files, form multipart
@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        // Se il servlet è stato chiamato dalla form di registrazione
        if (request.getParameter("signUp") != null) {
        	
        	// messaggio da visualizzare a fine regisrazione, mostra l'esito allo user
    		Message message = new Message();
        	
        	boolean check = false;	// controllo parametri inseriti
        	
        	
        	String username = request.getParameter("username");
        	String password = request.getParameter("password");       	
        	String checkPassword = request.getParameter("checkPassword");       	
        	String email = request.getParameter("email");       	
        	//String profilePic = request.getParameter("profilePic");       	
        	
        	// controlla se i parametri inseriti sono validi
        	if(
        		// se nessun parametro è vuoto 
    			(username != null && !username.isEmpty()) &&
    			(password != null && !password.isEmpty()) &&
    			(checkPassword != null && !checkPassword.isEmpty()) &&
    			(email != null && !email.isEmpty()) &&
    			//(profilePic != null && !profilePic.isEmpty()) &&
        		(request.getParameter("password").equals(request.getParameter("checkPassword")))
        	   )
        	{
        		// il controllo ha successo
        		check = true;
        	}
        	
        	if(username.contains(" "))
        		check = false;
        		
        	
        	// se il controllo è andato a buon fine
        	if(check)
        	{
        		
	        	// SALVATAGGIO IMMAGINE NEL FILESYSTEM DEL SERVER
	        	
	        	// Ottiene l'immagine dalla form
	    	    Part filePart = request.getPart("profilePic");	// Prende l'immagine da <input type="file" name="immagineDiProfilo">
	    	    String fileName = getFileName(filePart);				// Ottiene il nome del file
	    	    
	    	    //InputStream fileContent = filePart.getInputStream();	// boh? Sarà il formato del file?? Manco viene usata...
	    	    	   
	    	    // Imposta il percorso dove salvare l'immagine
	    		String homeFolder = System.getProperty("user.home");
	    	    String uploadPath = homeFolder + "/mySelfie/resources/profilepics";
	    	    
	    	    // Istanzia un nuovo file nel path specificato
	    	    File uploads = new File(uploadPath);		
	    	    // Crea il nuovo file, assicurandosi che il nome sia univoco nella directory (prefisso, suffisso, directory)
	    	    String fileExtension = "." + FilenameUtils.getExtension(fileName);
	    	    File file = File.createTempFile("567", fileExtension, uploads); 
	    	    	    	    
	    	    //Salva l'immagine caricata 
	    	    try (InputStream input = filePart.getInputStream()) {  
	    	        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    	    }
	    	    
	    	    BufferedImage picToCompress = ImageIO.read(file);
	    	    
	    	    //ridimensiona l'immagine
			    int ptcW = picToCompress.getWidth();
			    int ptcH = picToCompress.getHeight();
			    
			    int riW = (ptcW >= ptcH) ? 256 : (256*ptcW/ptcH);
			    int riH = (ptcW >= ptcH) ? (256*ptcH/ptcW) : 256;
			    		    			    
			    int rtype = (picToCompress.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : picToCompress.getType();
			    
			    BufferedImage Rimage = new BufferedImage(riW, riH, rtype);
	            Graphics2D g = Rimage.createGraphics();
	            g.drawImage(picToCompress, 0, 0, riW, riH, null);
	        	g.dispose();
	    	    
	        	file.delete();		      
	        	File cmprsdFile = File.createTempFile("567", fileExtension, uploads); 
	        	String uploadedFileName = cmprsdFile .getName();
	        	
	        	//comprimo l' immagine
				BufferedImage Cimage = Rimage;
			    OutputStream os =new FileOutputStream(cmprsdFile);

			    Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
			    ImageWriter writer = (ImageWriter) writers.next();

			    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			    writer.setOutput(ios);

			    ImageWriteParam param = writer.getDefaultWriteParam();
			     
			    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			    param.setCompressionQuality(0.5f);
			    writer.write(null, new IIOImage(Cimage, null, null), param);
			      
			    os.close();
			    ios.close();
			    writer.dispose();
			    
			    
	    	    // SALVATAGGIO INFORMAZIONI INSERITE DALL'UTENTE NEL DB
	    	    	        	
	        	User user = new User();
	        	user.setusername(request.getParameter("username"));
	        	user.setPassword(request.getParameter("password"));
	        	user.setEmail(request.getParameter("email"));
	        	user.setProfilepic(uploadedFileName);
	        	
	        	// prova a registrare un nuovo utente, una possibile eccezione è: username in uso
	            try {
	                // salva le credenziali nel database
	                UserUtils.signUp(user);
	                // se è andata a buon fine la registrazione 
	                // viene mandata la mail per validare il nuovo account
	                String code = SendMail.sendSignupConfirmation(user.getusername(), user.getEmail());
	                // se il codice tornato non è nullo viene salvato nel DB
	                if(code!=null)
	                {
	                	// ricava l'id dello user appena registrato dal suo username
	                	int userId = UserUtils.getId(user.getusername());
	                	// imposta il nuovo codice nel DB
		                SecurityUtils.setUserValidationCode(userId, code);
	                }
	                // imposto un messaggio di successo
					message.setType("info");
					message.setTitle("You're almost there!");
					message.setBody("We sent a code to activate your account on the email address specified, please check your inbox.");
					request.setAttribute("toastMessage", message);
	            } catch ( EmailInUseException e) { // se la mail è in uso
	            	//	va quindi eliminata l'immagine di profilo caricata
	            	file.delete();
					message.setType("fail");
					message.setTitle("Registration failed");
					message.setBody("email address already in use");
					request.setAttribute("toastMessage", message);
				} catch (UsernameInUseException e) { // se lo username è in uso
	            	//	va quindi eliminata l'immagine di profilo caricata
	            	file.delete();
					message.setType("fail");
					message.setTitle("Registration failed");
					message.setBody("username already in use");
					request.setAttribute("toastMessage", message);
				} 
	            
	            // finita la registrazione viene renderizzata la homepage
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
	    		dispatcher.forward(request,response);
	          // Se le password non corrispondono viene visualizzato un messaggio di errore
        	} else {
				message.setType("fail");
				message.setTitle("Registration failed");
				message.setBody("invalid data");
				request.setAttribute("toastMessage", message);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
				dispatcher.forward(request,response);
        	}
        }
        
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
      	String reqType = request.getParameter("reqType");
        
    	switch(reqType)
        {
        	
        	// intercetta la richiesta ajax "controlla username"
        	case "checkUsr":
        	{
        		String username = request.getParameter("username").trim();
    			
    			boolean check = false;
    			String free = "";

    			// controlla che lo username sia libero
                check = UserUtils.usernameAvailable(username);
                if(check){
                	free = "true";
                }else{
                	free = "false";
                }
    			
    			response.setContentType("text/plain");
    			response.getWriter().write(free);
        	}
        	break;
        	// richiesta di controllare l'indirizzo email
        	case "checkEmail":
        	{
        		String email = request.getParameter("email").trim();
    			
    			boolean check = false;
    			String free = "";

    			// controlla che la email sia libera
                check = UserUtils.checkEmail(email);
                if(check){
                	free = "true";
                }else{
                	free = "false";
                }
    			
    			response.setContentType("text/plain");
    			response.getWriter().write(free);
        	}
        	break;
        }	
		
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