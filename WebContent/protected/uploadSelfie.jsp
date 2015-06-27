<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />       
		<link href="${pageContext.request.contextPath}/resources/css/uploadSelfie.css" media="all" rel="stylesheet" type="text/css" />       
		<link href="${pageContext.request.contextPath}/resources/css/fileInput/fileinput.css" media="all" rel="stylesheet" type="text/css" />       
		<link href="${pageContext.request.contextPath}/resources/css/jcrop/jquery.Jcrop.css" media="all" rel="stylesheet" type="text/css" />       
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	
	<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
    
	<script src="${pageContext.request.contextPath}/resources/javascript/jcrop/jquery.Jcrop.js"></script>
	
	<script src="${pageContext.request.contextPath}/resources/javascript/fileInput/fileinput.js"></script>
			
	<script src="${pageContext.request.contextPath}/resources/javascript/scripts/uploadSelfie.js"></script>
			


     
    
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		
		
		<label id="upHeader">Select a picture:</label>
		
		
		<!-- seconda form, viene submittata quando l'utente inserisce tutti i dati -->
		<form onSubmit="return false">
			
			<table id="unploadTable">
				
				<tr>
					<th id="fileInputTH">
						<!-- prima form, appena carica l'immagine viene submittata -->
						<form id="form1">  
							<div class="col-md-6" id="sampleFileContainer">  
		    					<input id="sampleFile" name="sampleFile" type="file" required="required"  />
		   					</div>
		   					<br><br>
						</form>
					</th>										
				</tr>
				
				<tr>
					<td id="imgTD">
						<div id="uploadedIMG" tabindex="1">
					
						</div>
						<input type="submit" name="crop" value="crop" class="btn btn-primary btn-xs" id="cropBtn">
					</td>
					
					<td	id="notesTD">
					
						<div id="notesContainer">
						
							<div class="form-group">
						  		<label for="comment">Description:</label>
				  				<textarea class="form-control" rows="2" name="description" id="description"></textarea>
							</div>
				
							<div class="form-group">
								<label for="hashtags">Hashtags:</label><br>
				  				<input type="text" class="form-control" name="hashtags" id="hashtags"/>
							</div>
				
							<div class="form-group">
				  				<label for="usertags">Usertags:</label><br>
				  				<input type="text" class="form-control" name="usertags" id="usertags"/>
							</div>
				
							<div class="col-md-12 center-block">
				   				<input type="submit" name="cheese" value="cheese!" class="btn btn-primary center-block" id="cheeseBtn">
							</div>

							<img id="loadingBar" src="${pageContext.request.contextPath}/resources/images/loadingBar.gif"></img>						
						</div>
						
					</td>
				</tr>
				
			</table>
			
			
			

			
			
			<!-- coordinate dell'immagine da troncare -->
			<input type="hidden" name="x1" id="x1"/>
			<input type="hidden" name="y1" id="y1"/>
			<input type="hidden" name="x2" id="x2"/>
			<input type="hidden" name="y2" id="y2"/>
			<input type="hidden" name="w" id="w"/>
			<input type="hidden" name="h" id="h"/>
			
			
		</form>
    
    	<br>

    </jsp:body>
        
</t:template>