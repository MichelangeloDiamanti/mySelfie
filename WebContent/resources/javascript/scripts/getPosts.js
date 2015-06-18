$( document ).ready(function() 
{
	var queryType = "homepage";
//immagine loading
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
			var postsContainer = document.createElement("div");
			postsContainer.innerHTML = responseText;
			document.getElementById("bcontainer").appendChild(postsContainer);	
			
			
			$('.comments').each(function(i, comments) 
			{	    
				var sh = 0;
				var nh = 0;
				var ch = 50;
				
				$('.selfie_wrapper').each(function(j, selfie) 
				{	
					if(i==j)
						sh = $(selfie).height();
				});				    
				
				$('.comment_sections').each(function(k, notes) 
				{	
					if(i==k)
						nh = $(notes).height();
				});				 
							
				$(comments).height(sh - nh -ch);
				
			});


			$(".glyphicon-heart-empty").click(function()
			{
				$(this).addClass("glyphicon-heart");
				$(this).addClass("hOn");
				$(this).removeClass("glyphicon-heart-empty");
				$(this).removeClass("hOff");
			});
			$(".glyphicon-heart").click(function()
					{
				$(this).addClass("glyphicon-heart-empty");
				$(this).addClass("hOff");
				$(this).removeClass("glyphicon-heart");
				$(this).removeClass("hOn");
					});


		}
	});


	
	
});



	
