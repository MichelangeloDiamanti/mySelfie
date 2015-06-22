<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />       
		<link href="${pageContext.request.contextPath}/resources/css/fileInput/fileinput.css" media="all" rel="stylesheet" type="text/css" />       
		<link href="${pageContext.request.contextPath}/resources/css/jcrop/jquery.Jcrop.css" media="all" rel="stylesheet" type="text/css" />       
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	
	<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
    
	<script src="${pageContext.request.contextPath}/resources/javascript/jcrop/jquery.Jcrop.js"></script>
	
	<!-- appena viene scelta la selfie da uplodare essa viene caricata nella pagina -->
	<script type="text/javascript">
	
	    $('#sampleFile').change(function performAjaxSubmit() {
			
	    	// prendo il file uplodato
	        var sampleFile = document.getElementById("sampleFile").files[0];
	
	    	// dichiaro una nuova formData
	        var formdata = new FormData();
	        
	    	// ci metto due attributi: azione da svolgere e file
	        formdata.append("action", "showImage");
	        formdata.append("sampleFile", sampleFile);
	
	        // mando la richiesta tramite POST
	        var xhr = new XMLHttpRequest();       
	        xhr.open("POST","/mySelfie/protected/upload", true);
	        xhr.send(formdata);
			
	        // quando ottengo la risposta
	        xhr.onload = function(e) {
	        	
				// controllo il codice (200 OK)
	            if (this.status == 200) {
	
					// il server mi risponde con l'immagine uplodata e
					// la sua dimenzione (width, height), le carico
	               $('#uploadedIMG').html(
	            	
	            		   this.responseText
	            		   
	               );
	               
					// ottengo le dimenzioni dell'immagine
	               var trueWidth = $('#trueWidth').val();
	               var trueHeight = $('#trueHeight').val();
	               
	               // bindo Jcrop alla nuova immagine
	               $("#cropbox").Jcrop({
	            	   trueSize: [trueWidth,trueHeight],
	                   onSelect: setCoords,
	                   onChange: setCoords
	               });
	               
	               // mostro i pulsanti per croppare e per inviare
	               $('#cropBtn').show();
	               $('#cheeseBtn').show();
	
	            }
	
	        };                    
	
	    });  
	
	</script>
	
	<!-- Se l'utente vuole troncare l'immagine -->
	<script type="text/javascript">
	
	    $('#cropBtn').click(function performAjaxSubmit() {
	
	    	// dichiaro una nuova formData
	        var formdata = new FormData();
	        
	    	// metto nella formData l'azione da compiere (cropImage)
	        formdata.append("action", "cropImage");
	    	
	    	// le coordinate da croppare (prese dagli input hidden valorizzati da Jcrop)
	        formdata.append("x1", $('#x1').val());
	        formdata.append("y1", $('#y1').val());
	        formdata.append("x2", $('#x2').val());
	        formdata.append("y2", $('#y2').val());
	        formdata.append("w", $('#w').val());
	        formdata.append("h", $('#h').val());
			
	        // il nome dell'immagine uplodata
	        formdata.append("image", $('#cropbox').attr('src'));
	
	     	// mando la richiesta tramite POST
	        var xhr = new XMLHttpRequest();       
	        xhr.open("POST","/mySelfie/protected/upload", true);
	        xhr.send(formdata);
	     	
	        // quando ottengo la risposta
	        xhr.onload = function(e) {
	        	
	        	// controllo il codice (200 OK)
	            if (this.status == 200) {
					
	            	// ricarico l'immagine nel div
 	               $('#uploadedIMG').html(this.responseText);
 	               $('#cropBtn').hide();
	            }
	
	        };                    
	
	    });  
	
	</script>
	
	<!-- quando lo user vuole uplodare l'immagine su mySelfie -->
	<script type="text/javascript">
	
	    $('#cheeseBtn').click(function performAjaxSubmit() {
	    	// dichiaro una nuova formData
	        var formdata = new FormData();
	     	
	    	// ricavo il valore della descrizione dell'immagine
	        var description = $('#description').val();
	        var tags = $('#tags').val();	        
	    	
	        // passa tutti i parametri necessari per uplodare la selfie
	        formdata.append("action", "uploadSelfie");	// action da svolgere
	        formdata.append("tags", tags);	// descrizione
	        formdata.append("description", description);	// descrizione
	        formdata.append("image", $('#cropbox').attr('src'));	// nome dell'immagine
			
	     	// mando la richiesta tramite POST
	        var xhr = new XMLHttpRequest();       
	        xhr.open("POST","/mySelfie/protected/upload", true);
	        xhr.send(formdata);
	     	
	     	// quando ottengo la risposta
	        xhr.onload = function(e) {
	        	// controllo il codice (200 OK)
	            if (this.status == 200) {
					
	            	// reindirizzo l'utente alla homepage
					window.location = this.responseText;
	            	
	            }
	
	        };                    
	
	    });  
	
	</script>

	<!-- ogni volta che la selezione cambia vengono aggiornate le coordinate -->
    <script type="text/javascript">
      function setCoords(c)
      {
        jQuery('#x1').val(c.x);
        jQuery('#y1').val(c.y);
        jQuery('#x2').val(c.x2);
        jQuery('#y2').val(c.y2);
        jQuery('#w').val(c.w);
        jQuery('#h').val(c.h);
       };
	</script>

     
    
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		
		<!-- prima form, appena carica l'immagine viene submittata -->
		<form id="form1">    
		    <input id="sampleFile" name="sampleFile" type="file" /> <br/>
		</form>
		
		<!-- seconda form, viene submittata quando l'utente inserisce tutti i dati -->
		<form onSubmit="return false">
			<label for="comment">Select a picture:</label>
			
			<div id="uploadedIMG">
					
			</div>
			
			<!-- coordinate dell'immagine da troncare -->
			<input type="hidden" name="x1" id="x1"/>
			<input type="hidden" name="y1" id="y1"/>
			<input type="hidden" name="x2" id="x2"/>
			<input type="hidden" name="y2" id="y2"/>
			<input type="hidden" name="w" id="w"/>
			<input type="hidden" name="h" id="h"/>
			
			<br>

			<input type="submit" name="crop" value="crop" class="btn btn-primary" id="cropBtn" style="display: none">
			
			<br>
			<div class="form-group">
			  <label for="tags">Hashtags:</label>
			  <input type="text" name="tags" id="tags"/>
			</div>
			
			<div class="form-group">
			  <label for="comment">Description:</label>
			  <textarea class="form-control" rows="5" id="description" style="resize: none"></textarea>
			</div>
			<div class="col-md-12 center-block">
			   	<input type="submit" name="cheese" value="cheese!" class="btn btn-primary center-block" id="cheeseBtn" style="display: none">
			</div>
			
		</form>
    
    	<br>
    
    </jsp:body>
        
</t:template>