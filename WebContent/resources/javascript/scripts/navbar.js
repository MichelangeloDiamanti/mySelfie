// click sul pulsante di logout
// è necessario fare una post all'user validator passando come action logout
$('#logOutBtn').click(function() {
	$.ajax({
		method : "POST",
		url : '/mySelfie/userValidator',
		data : {
			action : "logout"
		},
		success : function(responseText) {
			window.location = "/mySelfie/";
		}
	});
});


$('#hiddenMenuBtn').click(function() 
{
	$("#hiddenMenuContainer").slideToggle( "fast" );	
});



$(document).click(function(e) {
	
		
	  if ($("#hiddenMenuContainer").is(":visible") &&
		  $(e.target).attr("id") != "hiddenMenuBtn"  &&
		  $(e.target).closest('#hiddenMenuContainer').length === 0) 
	  {
		  $( "#hiddenMenuContainer" ).slideUp( "fast" );		
	  }
});


// funzione di polling che ricava ogni 20 secondi il numero di notifiche
// non visualizzate
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
					toastr.info("You have " + notificationsCount + " unread notifications", 'Wow!');

				//Setup the next poll recursively
				poll();
			},
			dataType : "html"
		});
	}, 20000);
})();

$('#showNotifications').click(function() {
	$.ajax({
		method: "GET",
		url : '/mySelfie/protected/getNotifications',
		data : 
		{ 
			action: "getNotifications"
		},
		success : function(notifications) {
			//Update your dashboard gauge
			toastr.info(notifications, 'Notifications:');
		},
		dataType : "html"
	});
});

