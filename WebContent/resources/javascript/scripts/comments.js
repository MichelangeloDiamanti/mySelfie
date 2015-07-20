/**
 * prende in input la form che contiene in commento da postare
 * ne estrae gli elementi significativi e li invia tramite
 * AJAX al server per essere processati. 
 * Il server ritorna la lista dei commenti aggiornata che viene
 * mostrata.
 * 
 * @param form
 */

function post_comment(form){
	// prende tutti gli elementi della form
	var inputs = form.elements;
	// seleziona la textbox del commento
	var commentInput = inputs["comment_txt"];
	
	// ricava il testo del commento
	var commentText = commentInput.value;
	
	// se il commento inserito non è vuoto (o composto solo da spazi bianchi)
	if (commentText.trim()!="") {
		//	seleziona il bottone submit il cui id contiene quello del selfie da commentare
		var commentBtn = inputs["comment_btn"];
		
		// ricava l'id del selfie da commentare dal bottone con id="comment_btn-<id_selfie>"
		var idCommentBtn = commentBtn.id;
		var idSelfie = idCommentBtn.substring(idCommentBtn.lastIndexOf('-')+1);
		
		//chiamata post con ajax per postare il nuovo commento
		$.ajax(
		{
			method: "POST",
			url : '/mySelfie/protected/postComment',
			data : 
			{ 
				reqType: "postComment",
				idSelfie: idSelfie,
				commentText: commentText
			},
			success : function(responseText) 
			{
				// se l'aggiunta del commento fallisce il server risponde "fail"
				if (responseText != "fail") {
					// viene ricavato il DOM contenente tutti i commenti
					var comments_list_id = "#comments_list-" + idSelfie;
					// viene mostrata la lista aggiornata dei commenti
					$(comments_list_id).html(responseText);
					// viene pulito l'input dell'utente nella textbox
					commentInput.value = "";
					// scrolla alla fine della lista dei commenti
					var list_container_id = "#list_container-" + idSelfie;
					$(list_container_id).animate({ scrollTop: $(list_container_id)[0].scrollHeight}, 1000);
				}
				// se l'aggiunta del commento fallisce...
				else {
					// do something
					// viene pulito l'input dell'utente nella textbox
					commentInput.value = "";
				}

			}
		});		
	}
}