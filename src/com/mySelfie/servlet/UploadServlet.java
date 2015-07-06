package com.mySelfie.servlet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.mySelfie.entity.Selfie;
import com.mySelfie.entity.User;
import com.mySelfie.function.SelfieUtils;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String ajaxUpdateResult = ""; // la risposta da mandare al client
		// mappa che conterrà tutti i campi della form inviata
		Map<String, String> formFields = new HashMap<>();
		// il nome del file che viene uplodato, sarà valorizzato al parsing
		// della request
		String fileName = null;
		// il contenuto del file che viene uplodato, sarà valorizzato al parsing
		// della request
		InputStream content = null;

		// Imposta il percorso dove salvare l'immagine
		String homeFolder = System.getProperty("user.home");
		// Immagini temporanee (da troncare etc...)
		String tmpUploadPath = homeFolder + "/mySelfie/resources/tmp";
		// Immagini persistenti compresse
		String uploadCPath = homeFolder + "/mySelfie/resources/selfies/compressedSize";
		// Immagini persistenti originali
		String uploadOPath = homeFolder + "/mySelfie/resources/selfies/originalSize";


		try {

			// parso la richiesta in una lista di file
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);

			// scorre la lista
			for (FileItem item : items) {

				// per ogni elemento che rappresenta il campo di una form
				if (item.isFormField()) {

					// lo salvo nella mappa dei formFields
					formFields.put(item.getFieldName(), item.getString());

					// se non è un formfield (è un'immagine)
				} else {

					// ricavo il nome del file
					fileName = item.getName();
					// ed il suo contenuto
					content = item.getInputStream();

				}

			}

			// imposto il tipo di risposta
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");

			// intercetto il tipo di azione richiesta
			switch (formFields.get("action")) {
			// in questo caso devo solamente fare l'upload dell'immagine e
			// ritornare l'URL
			case "showImage":
				// Istanzia un nuovo file nel path specificato
				File uploads = new File(tmpUploadPath);
				// ricava l'estensione del file uplodato
				String fileExtension = "."
						+ FilenameUtils.getExtension(fileName);
				// Crea il nuovo file, assicurandosi che il nome sia univoco
				// nella directory (prefisso, suffisso, directory)
				File file = File.createTempFile("567", fileExtension, uploads);
				file.deleteOnExit();
				
				try {
					// istazio un nuovo stream di output dal file creato
					OutputStream os = new FileOutputStream(file);
					// buffer, serve a leggere l'immagine
					byte[] buffer = new byte[1024];
					int bytesRead;

					// scrive l'immagine nello stream di output, a pezzi di 1024
					// bytes finchè il file non è stato completamente letto
					while ((bytesRead = content.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					content.close();
					// chiude il flusso di output
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// leggo l'immagine che ho salvato
				BufferedImage bimg = ImageIO.read(file);
				// ricavo la larghezza e l'altezza
				int width = bimg.getWidth();
				int height = bimg.getHeight();
				
				// ritorna una img la cui src è l'URL a cui il file uplodato è accessibile e i suoi valori di altezza e larghezza
				// necessari a jcrop se l'immagine viene scalata dal browser
				ajaxUpdateResult = "<img id=\"cropbox\" class=\"img-responsive\" src=\"/mySelfie/protected/resources/tmp/"+ file.getName() + "\">"
						+ "<input type=\"hidden\" name=\"trueHeight\" id=\"trueHeight\" value=\"" + height + "\"/>"
						+ "<input type=\"hidden\" name=\"trueWidth\" id=\"trueWidth\" value=\"" + width + "\"/>";

				break;

			case "cropImage":
				
				// prende i parametri cartesiani con cui troncare l'immagine
				int x1 = Math.round(Float.parseFloat(formFields.get("x1")));
				int y1 = Math.round(Float.parseFloat(formFields.get("y1")));
				// int x2= Integer.parseInt(formFields.get("x2"));	// a quanto pare sti due sono inutili
				// int y2= Integer.parseInt(formFields.get("y2"));
				int w = Math.round(Float.parseFloat(formFields.get("w")));
				int h = Math.round(Float.parseFloat(formFields.get("h")));

				// ricavo il nome dell'immagine e la sua estensione
				String imageName = FilenameUtils.getName(formFields.get("image"));
				String imageExt = FilenameUtils.getExtension(formFields.get("image"));
				
				// leggo l'immagine da troncare
				BufferedImage image = ImageIO.read(new File(tmpUploadPath + "/" + imageName));

				// cancello l'immagine (quella troncata sarà salvata su un nuovo file)
				File deleteFile = new File(tmpUploadPath + "/" + imageName);
				deleteFile.delete();

				// tronco l'immagine con le coordinate specificate
				BufferedImage out = image.getSubimage(x1, y1, w, h);

				// Istanzia un nuovo file nel path specificato
				File cropLoads = new File(tmpUploadPath);
				File croppedImage = File.createTempFile("567", "." + imageExt, cropLoads);
				croppedImage.deleteOnExit();
				
				// scrivo l'immagine nel nuovo file, in modo tale che, quando rispondo al client
				// l'URL della src sarà cambiato, quindi il browser sarà costretto a refreshare l'immagine
				// serve a non far usare la versione cashata (vecchia)
				ImageIO.write(out, imageExt, croppedImage);

				// preparo la risposta da mandare al browser
				ajaxUpdateResult = "<IMG id=\"cropbox\" class=\"img-responsive\" src=\"/mySelfie/protected/resources/tmp/"
						+ croppedImage.getName() + "\">";

				break;
				
			case "uploadSelfie":
				
				// ottengo la descrizione dell'immagine dal campo della form
				String upImageDesc = formFields.get("description");
				
				// ottengo gli hashtags dell'immagine dal campo della form				
				String hashtagString = formFields.get("hashtags");
				// elimina tutti gli spazi vuoti
				hashtagString = hashtagString.replaceAll("\\s+", "");
				
				// imposto il delimitatore con cui separare i vari tags (cancelletto)
				String delims = "#";
				// creo un vettore di stringhe contenente i tags separati
				String[] hashtagStrings = hashtagString.split(delims);
				
				// diachiaro una lista di stringhe dove inserire gli hastags effettivi
				ArrayList<String> hashtags = new ArrayList<String>();

				// itero tutti gli hashtags
				for (String hashtagstring : hashtagStrings) {
					// quelli che non sono vuoti li metto in una lista di stringhe 
					// rimettendoci i il cancelletto tolto dalla funzione split
					if(!hashtagstring.equals("")) hashtags.add("#" + hashtagstring);
				}
				
				// ottengo gli usertags dell'immagine dal campo della form e trimmo la stringa
				String usertagString = formFields.get("usertags");
				// sostituisce i molteplici spazi vuoti con uno solo
				usertagString = usertagString.replaceAll("\\s+", " ");
				
				// imposto il delimitatore con cui separare i vari tags (cancelletto)
				delims = " ";
				// creo un vettore di stringhe contenente i tags separati
				String[] usertagStrings = usertagString.split(delims);
				
				// diachiaro una lista di stringhe dove inserire gli usertags effettivi
				ArrayList<String> usertags = new ArrayList<String>();

				// itero tutti gli usertags
				for (String usertagstring : usertagStrings) {
					// quelli che non sono vuoti li metto nella lista
					if(!usertagstring.equals("")) usertags.add(usertagstring);
				}
				
				// ricavo il nome dell'immagine e la sua estensione
				String upImageName = FilenameUtils.getName(formFields.get("image"));
				String upImageExt = FilenameUtils.getExtension(formFields.get("image"));
				
				// ricavo lo user dalla sessione
	      		HttpSession session = request.getSession();
	      		User user = (User) session.getAttribute("user");
				
	      		
				// ricavo la data attuale e la trasformo in formato SQL
	      		Date utilDate = new Date();
	      		java.sql.Timestamp dateTime = new java.sql.Timestamp(utilDate.getTime());
	      		
				// leggo l'immagine da salvare
				BufferedImage upBuffImage = ImageIO.read(new File(tmpUploadPath + "/" + upImageName));		 
				 
				// cancello l'immagine temporanea
				File deleteTmpFile = new File(tmpUploadPath + "/" + upImageName);
				deleteTmpFile.delete();		      

	            
				// creo un nuovo file nella cartella selfie/originalSize
				File persUploadsO = new File(uploadOPath);
				File upImageO = File.createTempFile("567", "." + upImageExt, persUploadsO);
				// scrivo l'immagine nel nuovo file persistente
				ImageIO.write(upBuffImage, upImageExt, upImageO);
				
				
				// creo un nuovo file nella cartella selfie/compressedSize
				File upImage = new File (uploadCPath + "/" + upImageO.getName());
				
				
				//ridimensiona l'immagine
			    int ubiW = upBuffImage.getWidth();
			    int ubiH = upBuffImage.getHeight();
					   
			    int riW = (ubiW > 1000) ? 1000 : ubiW;
			    int riH = (ubiH*riW)/ubiW;
			    			    
			    int rtype = (upBuffImage.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : upBuffImage.getType();
			    
			    BufferedImage Rimage = new BufferedImage(riW, riH, rtype);
	            Graphics2D g = Rimage.createGraphics();
	            g.drawImage(upBuffImage, 0, 0, riW, riH, null);
	        	g.dispose();
	            
			
				//comprimo l' immagine
				BufferedImage Cimage = Rimage;
			    OutputStream os =new FileOutputStream(upImage);

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
			     
			     
			    
			    
			    
			      

				
				// istanzio una nuova selfie e la valorizzo con le informazioni ricavate
				Selfie selfie = new Selfie();
				
				selfie.setDescription(upImageDesc);
				selfie.setUploader(user.getId_user());
				selfie.setDate(dateTime);
				selfie.setPicture(upImage.getName());
				
				SelfieUtils.uploadSelfie(selfie, hashtags, usertags);
				
				ajaxUpdateResult = "/mySelfie/protected/homepage.jsp";
				
				break;

			}

		} catch (FileUploadException e) {

			throw new ServletException("Parsing file upload failed.", e);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mando la risposta al browser
		response.getWriter().print(ajaxUpdateResult);
	}

}
