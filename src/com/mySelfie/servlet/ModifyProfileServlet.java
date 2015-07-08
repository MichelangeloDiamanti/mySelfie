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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;

import com.mySelfie.entity.User;
import com.mySelfie.function.ModifyProfileUtils;
import com.mySelfie.function.UserUtils;
import com.mySelfie.security.PasswordHash;

/**
 * Servlet implementation class ModifyProfileServlet
 */
@MultipartConfig	// Serve per supportare l'upload di files, form multipart
@WebServlet("/ModifyProfileServlet")
public class ModifyProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reqType = request.getParameter("reqType");
	
		
		switch(reqType)
        {
			//ricava tutte le informazioni dello user per mostrare sulla pagina per modificare il profilo
	        case "getInfo":
	    	{
	    		response.setContentType("text/plain");
		  		
	    		/* dalla sessione si ricava lo user */
	    		HttpSession session = request.getSession();
	    		User me = new User();
	      		me = (User) session.getAttribute("user");
	      			      			      		
	    		String INFOres = "";
	    		//crea una stringa con tutti gli attributi divisi dal carattere '%'
	    		//richiama i metodi get solo se gli attributi sono settati
	    		INFOres += (me.getName()==null) ? "name:" : "name:" + me.getName();
	    		INFOres += (me.getSurname()==null) ? "%surname:" : "%surname:" + me.getSurname();
	    		INFOres += (me.getCity()==null) ? "%city:" : "%city:" + me.getCity();
	    		INFOres += (me.getBirthdate()==null) ? "%birthdate:" : "%birthdate:" + me.getBirthdate().toString();
	    		INFOres += (me.getPhone()==null) ? "%phone:" : "%phone:" + me.getPhone();
	    		INFOres += (me.getNotes()==null) ? "%notes:" : "%notes:" + me.getNotes();
	    		INFOres += (me.getusername()==null) ? "%username:" : "%username:" + me.getusername();
	    		INFOres += (me.getGender()==null) ? "%gender:" : "%gender:" + me.getGender(); 					   		
	    					   
	    		response.getWriter().write(INFOres);
	       	}
	    	break;

	    	//vengono controllati i parametri passati e inseriti nel DB
	        case "submitInfo":
	    	{
	    		response.setContentType("text/plain");
	    		
	    		//prendo tutti i parametri della form (anche se sono vuoti)
	    		String name = request.getParameter("name");
	    		String surname = request.getParameter("surname");
	    		String city = request.getParameter("city");
	    		String birthdate = request.getParameter("birthdate");
	    		String gender = request.getParameter("gender");
	    		String phone = request.getParameter("phone");
	    		String notes = request.getParameter("notes");
	    		String username = request.getParameter("username");
	    		String oldpassword = request.getParameter("oldpassword");
	    		String newpassword = request.getParameter("newpassword");
	    		String checkpassword = request.getParameter("checkpassword");
	    		String uploadedFileName = "";

	    		
	    		//stringa di appoggio per la password hashtata dell' utente
	    		String realPassword = "";
	        	
	      		//parametri di risposta per l' URL (success/fail + eventuali motivi fallimento)
	    		String status = "success";
	    		String reason = "";
	    		
	    		
	        	/* dalla sessione si ricava l' id dello user e il suo username */
	    		HttpSession session = request.getSession();
	    		User me = new User();
	      		me = (User) session.getAttribute("user");
	      		int me_id = me.getId_user();
	      		String me_username = me.getusername();
	      		
	      		//ricava la password criptata
	    		realPassword = UserUtils.getHashedPassword(me_id);
	    		
	    		//controlli sugli input effettuati:
	    		
				//se la password è sbagliata
	        	try 
	        	{
					if(!PasswordHash.validatePassword(oldpassword, realPassword))
					{
						status = "fail";
						reason = "incorrect_password";							
					}
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e){e.printStackTrace();}
	        	
	        	//se lo username esiste gia 
		        try {
					if(UserUtils.exist(username) && (!username.equals(me_username)) || username.isEmpty() || username.equals(""))
					{
						status = "fail";
						reason = "invalid_username";							
					}
				} catch (NamingException e1) { e1.printStackTrace(); }
			        
		        //le password non corrispondono
		        if(!newpassword.equals(checkpassword))
				{
					status = "fail";
					reason = "passwords_dont_match";								
				}
			        

		        
		        //se i controlli hanno prodotto degli errori:
	        	if(status.equals("fail"))
	        	{
	        		response.sendRedirect("/mySelfie/protected/modifyProfile.jsp?status=" + status + "&reason=" + reason);
	        	}
	        	
	        	//se vengono superati tutti i controlli
	        	if(status.equals("success"))
	        	{
	        		//viene presa l' eventuale immagine di profilo inserita e viene salvato il nome
	        		Part filePart = request.getPart("profilepic");	
        			String fileName = getFileName(filePart);
        			
	        		//se l'immagine non è vuota viene salvata (controllo con la size)
	        		if(filePart.getSize() > 0)
	        		{
	        			        			
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
	    			    
	    			    int riW = (ptcW >= ptcH) ? 200 : (200*ptcW/ptcH);
	    			    int riH = (ptcW >= ptcH) ? (200*ptcH/ptcW) : 200;
	    			    		    			    
	    			    int rtype = (picToCompress.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : picToCompress.getType();
	    			    
	    			    BufferedImage Rimage = new BufferedImage(riW, riH, rtype);
	    	            Graphics2D g = Rimage.createGraphics();
	    	            g.drawImage(picToCompress, 0, 0, riW, riH, null);
	    	        	g.dispose();
	    	    	    
	    	        	file.delete();		      
	    	        	File cmprsdFile = File.createTempFile("567", fileExtension, uploads); 
	    	        	uploadedFileName = cmprsdFile.getName();
	    	        	
	    	        	//comprimo l' immagine
	    				BufferedImage Cimage = Rimage;
	    			    OutputStream os =new FileOutputStream(cmprsdFile);

	    			    Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
	    			    ImageWriter writer = (ImageWriter) writers.next();

	    			    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	    			    writer.setOutput(ios);

	    			    ImageWriteParam param = writer.getDefaultWriteParam();
	    			     
	    			    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    			    param.setCompressionQuality(0.3f);
	    			    writer.write(null, new IIOImage(Cimage, null, null), param);
	    			      
	    			    os.close();
	    			    ios.close();
	    			    writer.dispose();
	        		}   
	        		
	    			//eseguo la query per aggiornare il profilo
	        		try {
						ModifyProfileUtils.updateProfile(me_id, name, surname, city, birthdate, gender, phone, notes, username, newpassword, uploadedFileName);
					} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
						e.printStackTrace();
					}	   
  
	        		//aggiorna lo user della sessione con i nuovi parametri
	        		me.setName(name);
					me.setSurname(surname);
					me.setCity(city);
					try { me.setBirthdate(birthdate); } catch (ParseException e) { e.printStackTrace(); }
					me.setGender(gender);
					me.setPhone(phone);
					me.setNotes(notes);
					me.setusername(username);
					if(!uploadedFileName.isEmpty() && !uploadedFileName.equals("")) me.setProfilepic(uploadedFileName);
    					 
    			    session.setAttribute("user", me);
    	    	
    			    
    			    response.sendRedirect("/mySelfie/protected/modifyProfile.jsp?status=" + status + "&reason=" + reason);
	        	}
    	    		    		
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
