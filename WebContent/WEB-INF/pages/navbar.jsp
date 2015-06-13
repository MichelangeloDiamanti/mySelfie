<div id="custom-bootstrap-menu" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">
        		<img id="logo" alt="Brand" src="${pageContext.request.contextPath}/resources/images/extendedLogo.png">
        	</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-menubuilder"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse navbar-menubuilder">
            <ul class="nav navbar-nav navbar-left">
                <li>
                	<div id="searchdiv">
						<input type="text" id="search"/>
						<button id="searchbtn">
							<img id="magnifierGlass" src="${pageContext.request.contextPath}/resources/images/search.png"></img>
						</button>
						<div id="separator"></div>
					</div>
                </li>
            </ul>
      			<div id="buttons">
        <ul class="nav navbar-nav navbar-right">
                <li>
                	<div id="updiv" class="iconsdiv">
						<div class="circle"></div>
						<img id="upload" class="icon" src="${pageContext.request.contextPath}/resources/images/upload.png"></img>
					</div>
                </li>
                <li>
                	<div id="homediv" class="iconsdiv">
						<div class="circle"></div>
						<img id="home" class="icon" src="${pageContext.request.contextPath}/resources/images/home.png"></img>
					</div>
                </li>       
                <li>
					<div id="profilepicdiv" style="background-image: url('${pageContext.request.contextPath}/resources/images/micheledindi.png')" ></div>
                </li>                           
            </ul>
            </div>
        </div>
    </div>
</div>