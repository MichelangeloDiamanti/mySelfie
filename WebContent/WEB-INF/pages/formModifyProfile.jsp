<form action="query/signup" method="post" class="form-horizontal" onSubmit="return checkPwd()">
  
	<label>Add personal informations:</label>
    
    <div class="form-group">
	    <div class="col-md-12">
	    <input type="text" name="name" placeholder="name" class="form-control"/>
	    </div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
	  	  <input type="text" name="surname" placeholder="surname" class="form-control"/>
      	</div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
	  	  <input type="text" id="city" name="città" placeholder="city" class="form-control"/>
      	</div>
    </div>



    <div class="form-group">
        <div class="col-md-12">
            <div class="form-group row">
                <div class="col-md-6">
	    			<input id="my-datepicker" type="text" class="form-control" placeholder="birthdate">
                </div>
                <div class="col-md-6">
					<input type="radio" name="gender" id="radioBtnM"/>
					<label for="radioBtnM">Male</label> 
					<br>
					<input type="radio" name="gender" id="radioBtnF"/>
					<label for="radioBtnF">Female</label>
                </div>

            </div>
        </div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
		    <textarea placeholder="notes" class="form-control" rows="3" style="resize: none;"></textarea>
      	</div>
    </div>
    
    <br>
    <label>Modify profile settings:</label>    
    	        
    <div class="form-group">
      <div class="col-md-12">
	    <input type="text" name="new nickname" placeholder="nickname" class="form-control SUtxtbox" id="nickname"/>
      </div>
    </div>
    <br>
    
    <div class="form-group">
      <div class="col-md-12">
	    <input type="password" name="vecchia_password" placeholder="old password" class="form-control SUtxtbox" id="mopassword"/>
      </div>
    </div>
    
    <div class="form-group">
      <div class="col-md-12">
	    <input type="password" name="new_password" placeholder="new password" class="form-control SUtxtbox" id="mnpassword"/>
      </div>
    </div>
    
   <div class="form-group">
      <div class="col-md-12">
      	<input type="password" name="conferma_password" placeholder="check new password" class="form-control SUtxtbox" id="mcpassword"/>
      </div>
    </div>
    
    <div class="form-group">
      <div class="col-md-12">
      	<label for="immagine_di_profilo">Profile pic:</label>
    	<input id="input-id" type="file" name="immagine_di_profilo" data-show-upload="false" class="file" data-preview-file-type="text" >
      </div>
    </div>
    
    <br>
	<div class="col-md-12 center-block">
    	<input type="submit" name="jdbc_query" value="Confirm" id="modifyBtn" class="btn btn-primary center-block">
	</div>
</form>