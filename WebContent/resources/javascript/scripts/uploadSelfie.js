/* Popup stile bootstrap navigatore file    */
$("#sampleFile").fileinput({
	allowedFileTypes : [ "image" ],
	showPreview : false,
	showRemove : false,
	'showUpload' : false,
	'previewFileType' : 'any'
});



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
	xhr.open("POST", "/mySelfie/protected/upload", true);
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
				trueSize : [ trueWidth, trueHeight ],
				onSelect : setCoords,
				onChange : setCoords,
				onRelease: hideCrop
			});

			// mostro i pulsanti per croppare e per inviare
			
			$('#notesContainer').show();

		}

	};

});



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
	xhr.open("POST", "/mySelfie/protected/upload", true);
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

$('#cheeseBtn').click(function performAjaxSubmit() {
	// dichiaro una nuova formData
	var formdata = new FormData();

	// ricavo il valore della descrizione dell'immagine
	var description = $('#description').val();
	var hashtags = $('#hashtags').val();
	var usertags = $('#usertags').val();

	// passa tutti i parametri necessari per uplodare la selfie
	formdata.append("action", "uploadSelfie"); // action da svolgere
	formdata.append("hashtags", hashtags); // hashtags
	formdata.append("usertags", usertags); // usertags	        
	formdata.append("description", description); // descrizione
	formdata.append("image", $('#cropbox').attr('src')); // nome dell'immagine

	// mando la richiesta tramite POST
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/mySelfie/protected/upload", true);
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



function hideCrop()
{
	$('#cropBtn').hide();
}



function setCoords(c) {
	jQuery('#x1').val(c.x);
	jQuery('#y1').val(c.y);
	jQuery('#x2').val(c.x2);
	jQuery('#y2').val(c.y2);
	jQuery('#w').val(c.w);
	jQuery('#h').val(c.h);
	
	if($("#cropBtn").css("display") == "none")
	{
		$('#cropBtn').show();
	}
	$('#cropBtn').focus();
	
};
