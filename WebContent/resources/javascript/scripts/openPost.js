function openIMG(i)
{

	//creo il div che oscura la pagina e lo inietto nell' html del container globale
	var oIMG = "<div class=\"shadow\" ></div>";
	
	document.getElementById('bcontainer').innerHTML += oIMG;

	//perndo l' attributo src dell' immagine (path + nome)
	var nameIMG = i.src;	
	//ottengo il nome dell' immagine a partire dall' ultimo /
	nameIMG = nameIMG.substring(nameIMG.lastIndexOf('/')+1);

	var queryType = "profilePost";
	
	//chiamata post con ajax per visualizzare il post 
	$.ajax(
	{
		method: "POST",
		url : '/mySelfie/protected/getPosts',
		data : 
		{ 
			reqType: "profilePost",
			queryType: queryType,
			nameIMG: nameIMG
		},
		success : function(responseText) 
		{
			//viene restituito l' HTML dei post, da poter iniettare nel div

			//rimuovo il precedente div shadow vuoto e ne creo un altro contenente l'html da iniettare
			$('.shadow').remove();
			oIMG = "<div class=\"shadow\" >";
			oIMG += responseText;
			oIMG += "</div>";
			document.getElementById('bcontainer').innerHTML += oIMG;
			
			resizeComments();
			
			bindClicks();
		}
	});
	
	

}

function bindClicks()
{
	$('.shadow').click(function(event)
	{
		$('.shadow').remove();
	});

	$('.post_container').click(function(event)
	{
		event.stopPropagation();
		
	});
	
	$(document).keyup(function(event) 
	{
		  if (event.keyCode == 27)
	      {
			  $('.shadow').remove();
	      }
	});
	
			
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
	

