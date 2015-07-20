$(document).ready(function()
{
	$('.tab').click(function()
	{
		$("#leftMenu > li").removeClass("seltab");
		$(this).addClass("seltab");
	});
});



//binda il caricamento delle immagini home o explore al click del menu
$(document).ready(function()
{
	//click sul menu sinistro (home)
	$('#tabH').click(function()
	{
		window.location = "/mySelfie/";
	});
	
	
	//click sul menu sinistro (expolre)
	$('#tabE').click(function()
	{
		window.location = "/mySelfie/?queryType=explore";
	});
	
	
});

//funzione che ricava il numero di notifiche
//non visualizzate appena viene caricato il menu
$(document).ready(function()
{
	var href = window.location.pathname;
	href = href.substr(href.lastIndexOf('/') + 1); 

	//se la pagina è quella delle notifiche, non va eseguita la chiamata ajax
	if(href != "notifications.jsp")
	{
		$.ajax({
			method: "GET",
			url : '/mySelfie/protected/getNotifications',
			data : 
			{ 
				action: "getNotificationsCount"
			},
			success : function(notificationsCount) {
				//Update your dashboard gauge
				if(notificationsCount != "none")
				{
					$('#notificationsCount').html(notificationsCount);
					$('#notificationsCount').css("display","block");					
				}	
			},
			dataType : "html"
		});
	}
});


//funzione di polling che ricava ogni 20 secondi il numero di notifiche
//non visualizzate
(function poll() {
	setTimeout(function() {
		$.ajax({
			method: "GET",
			url : '/mySelfie/protected/getNotifications',
			data : 
			{ 
				action: "getNotificationsCount"
			},
			success : function(notificationsCount) {
				//Update your dashboard gauge
				if(notificationsCount != "none")
				{
					$('#notificationsCount').html(notificationsCount);
					$('#notificationsCount').css("display","block");					
				}
//					toastr.info("You have " + notificationsCount + " unread notifications", 'Wow!');

				//Setup the next poll recursively
				poll();
			},
			dataType : "html"
		});
	}, 15000);
})();



