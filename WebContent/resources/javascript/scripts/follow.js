// la funzione serve a bindare gli eventi follow e unfollow
function bindFollow(){
	// al submit della form follow
	$('#follow_form').on('submit', function(){
		// vengono presi gli elementi della form
		var inputs = this.elements;
		// prende l'id dello user a cui appartiene il profilo da un input nascosto
		var profileId = inputs["profile_id"].value;
		//chiamata post con ajax per seguire lo user
		$.ajax(
		{
			method: "POST",
			url : '/mySelfie/protected/followUser',
			data : 
			{ 
				action: "followUser",
				idToFollow: profileId
			},
			success : function(responseText) 
			{
				// il server ritorna la form unfollow che viene mostrata al posto di quella follow
				$('#follow_form').replaceWith(responseText);
				// viene effettuato il rebind degli eventi
				bindFollow();
			}
		});
	});
	
	// al submit della form unfollow
	$('#unfollow_form').on('submit', function(){
		// vengono presi gli elementi della form
		var inputs = this.elements;
		// prende l'id dello user a cui appartiene il profilo da un input nascosto
		var profileId = inputs["profile_id"].value;
		//chiamata post con ajax per smettere di seguire lo user
		$.ajax(
				{
					method: "POST",
					url : '/mySelfie/protected/unfollowUser',
					data : 
					{ 
						action: "unfollowUser",
						idToUnfollow: profileId
					},
					success : function(responseText) 
					{
						// il server ritorna la form follow che viene mostrata al posto di quella unfollow
						$('#unfollow_form').replaceWith(responseText);
						// viene effettuato il rebind degli eventi
						bindFollow();
					}
				});
	});
}

// al caricamento del document vengono bindati gli eventi
$( document ).ready(function() {
    bindFollow();
});