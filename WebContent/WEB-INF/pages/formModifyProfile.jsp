<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="/mySelfie/protected/modifyProfile" method="post" enctype="multipart/form-data" class="form-horizontal" onSubmit="return checkPwd()">
  
	<label class="subtitle" >Add personal informations:</label>
    
    <div class="form-group">
	    <div class="col-md-12">
	    <label>name:</label>
	    <input type="text" id="name" name="name" placeholder="name" class="form-control"/>
	    </div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
    	  <label>surname:</label>
	  	  <input type="text" id="surname" name="surname" placeholder="surname" class="form-control"/>
      	</div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
    	  <label>city:</label>
	  	  <input type="text" id="city" name="city" placeholder="city" class="form-control"/>
      	</div>
    </div>



    <div class="form-group">
        <div class="col-md-12">
            <div class="form-group row">
                <div class="col-md-6">
                	<label>birthdate:</label>
	    			<input id="my-datepicker" name="birthdate" type="text" class="form-control" placeholder="birthdate">
                </div>
                <div class="col-md-6">
                	<label>gender:</label><br>
					<input type="radio" name="gender" value="M" id="radioBtnM"/>
					<label style="color: #888;" for="radioBtnM">Male</label> 
					<br>
					<input type="radio" name="gender" value="F" id="radioBtnF"/>
					<label style="color: #888;" for="radioBtnF">Female</label>
                </div>

            </div>
        </div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
    	  <label>phone-number:</label>
	  	  <input type="text" id="phone" name="phone" placeholder="phone-number" class="form-control"/>
      	</div>
    </div>
    
    <div class="form-group">
    	<div class="col-md-12">
	    	<label>notes:</label>
		    <textarea placeholder="notes" id="notes" name="notes" class="form-control" rows="3" style="resize: none;"></textarea>
      	</div>
    </div>
    
    <br>
    <hr class="mpdivisor">
    <label class="subtitle" >Modify profile settings:</label>    
    	        
    <div class="form-group">
      <div class="col-md-12">
      	<label>new username:</label>
	    <input type="text" name="username" placeholder="username" class="form-control SUtxtbox" id="username"/>
	    <div id="usernameAlert" class="col-md-12"></div>
      </div>
    </div>
    <br>
        
    <div class="form-group">
      <div class="col-md-12">
      	<label>new password:</label>
	    <input type="password" name="newpassword" placeholder="new password" class="form-control SUtxtbox" id="mnpassword"/>
      </div>
    </div>
    
   <div class="form-group">
      <div class="col-md-12">
      	<label>check new password:</label>
      	<input type="password" name="checkpassword" placeholder="check new password" class="form-control SUtxtbox" id="mcpassword"/>
      </div>
    </div>
    
    <br>
    <div class="form-group">
      <div class="col-md-11">
       	<label for="immagine_di_profilo">Profile pic:</label>
    	<input id="input-id" type="file" name="profilepic" data-show-upload="false" class="file" data-preview-file-type="text" >
      </div>

      <div class="col-md-1">
      	<span id="mprofilepicdiv" style="background-image: url('${pageContext.request.contextPath}/protected/resources/profilepics/<c:out value="${sessionScope.user.profilepic}" />')" ></span>
   	  </div>
    </div>
    <br>
        
    <div class="form-group">
      <div class="col-md-12">
      	<label><span id="okpass" class="glyphicon glyphicon-ok"></span>insert password to confirm:</label>
	    <input type="password" name="oldpassword" placeholder="password" class="form-control SUtxtbox" id="mopassword" required="required" />
      </div>
    </div>
    
    <br>
    
    <input type="hidden" name="reqType" value="submitInfo">
	
	<div class="col-md-12 center-block">
    	<input type="submit" name="jdbc_query" value="Confirm" id="modifyBtn" class="btn btn-primary center-block">
	</div>
</form>