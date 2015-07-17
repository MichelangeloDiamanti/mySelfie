package com.mySelfie.servlet;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mySelfie.entity.User;
import com.mySelfie.function.FollowUtils;
import com.mySelfie.function.PostUtils;
import com.mySelfie.function.UserUtils;

/**
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* dalla sessione si ricava l'id e lo username dello user */
		HttpSession session = request.getSession();
		User me = new User();
		me = (User) session.getAttribute("user");
  		String myUsername = me.getusername();
  		int myId = me.getId_user();
  		  		
  		
  		// prende il contesto del server
  		ServletContext servletContext = getServletContext();
  		String contextPath = servletContext.getContextPath();
  			
  		
		// ricava lo user al quale appartiene il profilo analizzando il path
		String user = request.getPathInfo().substring(1);

		//se l' utente esiste
		try 
		{
			if(UserUtils.exist(user))
			{
				// ricava tutti i post dello user e li mette nella risposta
				String posts = PostUtils.getProfilePosts(user, contextPath);
						
				// ricava l'id dello user al quale appartiene il profilo
				int id_followed = -1;
					id_followed = UserUtils.getId(user);
					
				// flag che indica se lo user attuale segue il proprietario del profilo visitato
				boolean follow = FollowUtils.checkFollow(myId, id_followed);
				
				String userProfilePic = UserUtils.getUserProfilepicById(id_followed);
				int followers = UserUtils.getCountFollowers(id_followed);
				int following = UserUtils.getCountFollowing(id_followed);
				int nposts = UserUtils.getCountPosts(id_followed);
				String uNotes = UserUtils.getUserNotesById(id_followed);
				String uName = UserUtils.getNameById(id_followed);
				String uSurname = UserUtils.getSurnameById(id_followed);
				String uGender = UserUtils.getGenderById(id_followed);
				String uBirthdate = UserUtils.getBirthdateById(id_followed);
				String uCity = UserUtils.getCityById(id_followed);
				String uPhone = UserUtils.getPhoneById(id_followed);
				
				//flag che controlla se almeno un' informazione personale viene mostrata
				boolean inf = false;
				if( (!uNotes.equals("") && uNotes!=null) ||
					(!uName.equals("") && uName!=null) ||
					(!uSurname.equals("") && uSurname!=null) ||
					(!uBirthdate.equals("") && uBirthdate!=null) ||
					(!uCity.equals("") && uCity!=null) ||
					(!uPhone.equals("") && uPhone!=null))
					inf = true;
					
					
					
				// controlla se il profilo visitato è quello dello user che ha effettuato il login
				if(user.equals(myUsername)){
					request.setAttribute("myProfile", true);
				}
				else{
					request.setAttribute("myProfile", false);
				}		
				//html con tutti i post dell' utente
				request.setAttribute("profilePosts", posts);
				// mette lo username a cui appartiene il profilo nella risposta
				request.setAttribute("profileOwner", user);
				// mette nella risposta l'id dell'utente a cui appartiene il profilo
				request.setAttribute("profileId", id_followed);
				//flag che indica se l' utente viene seguito o no
				request.setAttribute("follow", follow);
				//nome dell' immagine di profilo
				request.setAttribute("profilePic", userProfilePic);
				//numero di followers, following e posts
				request.setAttribute("followers", followers);
				request.setAttribute("following", following);
				request.setAttribute("nposts", nposts);
				
				//infomazioni personali
				request.setAttribute("profileInfoFlag", inf);
				request.setAttribute("profileNotes", uNotes);
				request.setAttribute("profileName", uName);
				request.setAttribute("profileSurname", uSurname);
				request.setAttribute("profileGender", uGender);
				request.setAttribute("profileBirthdate", uBirthdate);
				request.setAttribute("profileCity", uCity);
				request.setAttribute("profilePhone", uPhone);
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/profile.jsp"); 
				dispatcher.forward(request,response);
			}
			//se l' utente non esiste
			else
			{
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/pageNotFound.jsp"); 
				dispatcher.forward(request,response);
			}
		} catch (NamingException e) {e.printStackTrace();}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
