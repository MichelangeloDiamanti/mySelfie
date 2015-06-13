package com.mySelfie.function;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;


/**
 * Servlet implementation class UploadServlet
 */
@MultipartConfig
public class FileUploadFSServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
	    Part filePart = request.getPart("dataFile"); // Retrieves <input type="file" name="file">
	    String fileName = getFileName(filePart);
	    InputStream fileContent = filePart.getInputStream();
	    	    
	    String homeFolder = System.getProperty("user.dir");
	    String uploadPath = homeFolder + "/mySelfie/data";

	    File uploads = new File(uploadPath);
	    File file = new File(uploads, fileName); // Or File.createTempFile("somefilename-", ".ext", uploads) if you want to autogenerate an unique name.

	    try (InputStream input = filePart.getInputStream()) {  // How to obtain part is answered in http://stackoverflow.com/a/2424824
	        Files.copy(input, file.toPath());
	    }
	}

	private static String getFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}

}