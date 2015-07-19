$(document).ready(function(){ $('#tabN').addClass("seltab"); });

$(document).ready(function() {
	$.ajax({
		method: "GET",
		url : '/mySelfie/protected/getNotifications',
		data : 
		{ 
			action: "getNotifications"
		},
		success : function(notifications) {
			$('#oldAndNewNotifications').html(notifications);
		},
		dataType : "html"
	});
});

