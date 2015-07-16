// variabile usata per tener traccia dei post visualizzati
var lastIndex = 0;
//variabile che indica il tipo di query da eseguire
var queryType = "homepage";

// la data viene passata come parametro della query che prende nuovi post
// serve ad evitare che i selfie uplodati dopo il caricamento della pagina
// vengano mostrati più volte a causa dello spostamento dell'indice
var date = new Date();
date.setSeconds( date.getSeconds() + 10);
date = date.getFullYear() + '-' +
        ('00' + (date.getMonth() + 1)).slice(-2) + '-' +
        ('00' + date.getDate()).slice(-2) + ' ' +
        ('00' + date.getHours()).slice(-2) + ':' +
        ('00' + date.getMinutes()).slice(-2) + ':' +
        ('00' + date.getSeconds()).slice(-2);

// booleano che indica se i selfie con quell'hashtag sono terminati
var end = false;



$( document ).ready(function() 
{	
	//seleziona la tab home
	$('#tabH').addClass("seltab");

	//se l' argomento specificato nell' url è explore
	var exp = window.location.search.substring(1);
	if(exp=="queryType=explore")
	{
		queryType = "explore";
		
		//deseleziona la tab home e seleziona la tab explore
		$('#tabH').removeClass("seltab");
		$('#tabE').addClass("seltab");

	}

	
	getMorePosts(lastIndex, queryType);

	$(window).scroll(function() { //detect page scroll
	    if (((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 20) && !end) {
	    	lastIndex += 10;
	    	getMorePosts(lastIndex, queryType);
	    }
	});

});


function getMorePosts(index, queryType){

	var postsContainer = document.createElement("div");
	document.getElementById("bcontainer").appendChild(postsContainer);		
	postsContainer.innerHTML = "<img class=\"loadingGif\" src=\"/mySelfie/resources/images/loadingIMG.gif\" >";
		
	//chiamata post con ajax per visualizzare i post 
	// se non si è raggiunta la fine dei post
	if(!end){
		$.ajax(
		{
			method: "POST",
			url : '/mySelfie/protected/getPosts',
			data : 
			{ 
				reqType: "getPosts",
				queryType: queryType,
				lastIndex: index,
				date: date
			},
			success : function(responseText) 
			{
				// se i post sono finiti il server ritorna la stringa formattata There are no posts here...
				// quindi se la risposta è diversa vengono caricati i nuovi selfie
				if(responseText != "failed")
				{
				
					//viene restituito l' HTML dei post, da poter iniettare nel div
					postsContainer.innerHTML = responseText;
					// viene bindato la funzione post_comment al click del bottone passando come parametro
					// l'elemento cliccato
					$('.form-comment').on('submit', function(){
						post_comment(this);
					});
					
					//una volta che le immagini si sono caricate, è possibile ridimensionare i commenti
					$('.selfie').on('load change', function()
					{
						resizeComments();
						
					});
				}
				// se la risposta del server è "end" viene mostrata una scritta
				// e impostato il flag end a true
				else
				{
					end = true;
					postsContainer.innerHTML = 
						"<div class=\"empty\">" +
							"<label class=\"empty_label\">You reached the end</label>" +
						"</div>";
				}
	
			}
		});
	}
}

function resizeComments()
{	
	/* prende tutti i contenitori dei commenti */
	var comment = document.getElementsByClassName("comments");
	/* prende tutti i contenitori delle immagini */
	var selfie = document.getElementsByClassName("selfie_wrapper");
	/* prende tutti i contenitori delle note */
	var notes = document.getElementsByClassName("comment_sections");

	/* scorre tutti i commenti */
	var i=0;
	for (i = 0; i < comment.length; i++) 
	{ 
			/* setta l' altezza dei commenti */
			var sh = selfie[i].offsetHeight;   	//altezza della foto
			var nh = notes[i].offsetHeight;	   	//altezza delle note
			var ch = 50;						//altezza input
			comment[i].style.height = (sh - nh - ch) + "px";
	}	
}




function like(heart, id_selfie)
{
	//viene controllato se il like è già presente
	var h = "";
	if(heart.className == "glyphicon glyphicon-heart-empty hOff")
		h = "empty"; 
	else if(heart.className == "glyphicon glyphicon-heart hOn")
		h = "full"; 
	
	//viene modificato il campo nel DB con ajax
	$.ajax(
	{
		method: "POST",
		url : '/mySelfie/protected/likePosts',
		data : 
		{ 
			reqType: "like",
			heart: h,
			selfie: id_selfie
		},
		success : function() 
		{		
			//se il cuore era vuoto va riempito e incrementato il numero di likes
			if(h=="empty")
			{
				heart.className = "glyphicon glyphicon-heart hOn";
				var lblikes = heart.parentNode.childNodes[1].innerHTML;
				var nl = parseInt(lblikes.substring(0, lblikes.indexOf(' '))) + 1;
				heart.parentNode.childNodes[1].innerHTML = nl + " Likes";			
			}
			//se il cuore era pieno va svuotato e decrementato il numero di likes
			else if(h=="full")
			{
				heart.className = "glyphicon glyphicon-heart-empty hOff";
				var lblikes = heart.parentNode.childNodes[1].innerHTML;
				var nl = parseInt(lblikes.substring(0, lblikes.indexOf(' '))) - 1;
				heart.parentNode.childNodes[1].innerHTML = nl + " Likes";
			}
			
			
		}
	});
	
}
	


//binda il caricamento delle immagini home o explore al click del menu
$(document).ready(function()
{
	
	//click sul menu sinistro (home)
	$('#tabH').click(function()
	{
		//aggiorna la data
		var nowDate = new Date();
		nowDate.setSeconds( nowDate.getSeconds() + 10);
		nowDate = nowDate.getFullYear() + '-' +
		        ('00' + (nowDate.getMonth() + 1)).slice(-2) + '-' +
		        ('00' + nowDate.getDate()).slice(-2) + ' ' +
		        ('00' + nowDate.getHours()).slice(-2) + ':' +
		        ('00' + nowDate.getMinutes()).slice(-2) + ':' +
		        ('00' + nowDate.getSeconds()).slice(-2);
		date = nowDate;
		//rimouve tutti i post gia caricati
		$( ".post_container" ).remove();
		$( ".empty_label" ).remove();
		//azzera l' indice e l' end
		lastIndex=0;
		end = false;
		
		//prende i post (eventualmente aggiornati)
		getMorePosts(lastIndex, "homepage");	
	});
	
	
	//click sul menu sinistro (expolre)
	$('#tabE').click(function()
	{
		//aggiorna la data
		var nowDate = new Date();
		nowDate.setSeconds( nowDate.getSeconds() + 10);
		nowDate = nowDate.getFullYear() + '-' +
		        ('00' + (nowDate.getMonth() + 1)).slice(-2) + '-' +
		        ('00' + nowDate.getDate()).slice(-2) + ' ' +
		        ('00' + nowDate.getHours()).slice(-2) + ':' +
		        ('00' + nowDate.getMinutes()).slice(-2) + ':' +
		        ('00' + nowDate.getSeconds()).slice(-2);
		date = nowDate;
		//rimouve tutti i post gia caricati
		$( ".post_container" ).remove();
		$( ".empty_label" ).remove();
		//azzera l' indice e l' end
		lastIndex=0;
		end = false;				
		//prende i post (eventualmente aggiornati)
		getMorePosts(lastIndex, "explore");
	});

	
});