<form action="/mySelfie/homepage/signup" method="post" enctype="multipart/form-data" class="form-horizontal" onSubmit="return checkPwd()">
    
    <div class="form-group">
      <div class="col-md-8">
	    <input type="text" name="nickname" required="required" placeholder="nickname" class="form-control SUtxtbox" id="nickname"/>
      	<div id="nicknameAlert" class="col-xs-12"></div>
      </div>
    </div>
    
    
    <div class="form-group">
      <div class="col-md-8">
	    <input type="password" name="password" required="required" placeholder="password" class="form-control SUtxtbox" id="supassword"/>
      </div>
    </div>
    
   <div class="form-group">
      <div class="col-md-8">
      	<input type="password" name="checkPassword" required="required" placeholder="check password" class="form-control SUtxtbox" id="suchkpassword"/>
      </div>
    </div>

    <div class="form-group">
      <div class="col-md-8">
	    <input type="text" name="email" required="required" placeholder="e-mail" class="form-control SUtxtbox" id="email"/>
      </div>
    </div>

    <div class="form-group">
      <div class="col-md-8">
      	<label for="immagine_di_profilo">Profile pic:</label>
    	<input id="input-id" type="file" name="profilePic" required="required" data-show-upload="false" class="file" id="browsebtn" data-preview-file-type="text" >
      </div>
    </div>
    
    
	<div class="col-md-8 center-block">
    	<input type="submit" name="signUp" value="Sign Up" class="btn btn-primary center-block" id="signupbtn">
	</div>
</form>