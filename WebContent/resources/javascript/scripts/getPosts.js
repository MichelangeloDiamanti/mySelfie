$( document ).ready(function() 
{
	var queryType = "homepage";
	
	var postsContainer = document.createElement("div");
	document.getElementById("bcontainer").appendChild(postsContainer);	
	
	//postsContainer.innerHTML = "<img src=\"/mySelfie/resources/images/loading.gif\">";
	
	
	$.ajax(
	{
		method: "POST",
		url : '/mySelfie/protected/getPosts',
		data : 
		{ 
			queryType: "queryType"
		},
		success : function(responseText) 
		{

			
			postsContainer.innerHTML = responseText;
			
	
			$('.selfie').on('load change', resizeComments());

			$(".glyphicon-heart-empty").click(function()
			{
				$(this).addClass("glyphicon-heart");
				$(this).addClass("hOn");
				$(this).removeClass("glyphicon-heart-empty");
				$(this).removeClass("hOff");
			});

		}
	});

	
});



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

	
