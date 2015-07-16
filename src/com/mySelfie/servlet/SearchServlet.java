package com.mySelfie.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mySelfie.entity.Hashtag;
import com.mySelfie.entity.Selfie;
import com.mySelfie.entity.User;
import com.mySelfie.function.HashtagUtils;
import com.mySelfie.function.SearchUtils;
import com.mySelfie.function.SelfieUtils;
import com.mySelfie.function.UserUtils;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // prende la keyword da ricercare dalla richiesta
		String keyword = request.getParameter("keyword");
        // controlla che non sia vuota
		if(keyword!=null && !keyword.trim().equals(""))
        {
			// se sono state digitate più parole le divido
			String[] words = keyword.split("\\s+");
			for (int i = 0; i < words.length; i++) {
				// rimpiazzo gli spazi bianchi di ogni parola
			    words[i] = words[i].replaceAll("[^\\w]", "");
			}
			
			// UTENTI
			
			
			// da tutti i risultati parziali ottenuti dobbiamo ricavare un'unica lista
			List<User> resultUserList = new ArrayList<User>();
			
			// aggiungo roba alla lista degli user risultanti
			// lo user che ha esattamente lo username ricercato
			if(UserUtils.getUserByUsername(keyword).getId_user() > 0)
				resultUserList.add(UserUtils.getUserByUsername(keyword));
			
			// lista di utenti il cui username contiene la keyword da ricercare
			for (User user : SearchUtils.searchUserByUsername(keyword)) {
				if (!resultUserList.contains(user)) {
					resultUserList.add(user);
				}
			}
			// lista di utenti il cui nome contiene la keyword da ricercare
			for (User user : SearchUtils.searchUserByName(keyword)) {
				if (!resultUserList.contains(user)) {
					resultUserList.add(user);
				}
			}
			// lista di utenti il cui cognome contiene la keyword da ricercare
			for (User user : SearchUtils.searchUserBySurname(keyword)) {
				if (!resultUserList.contains(user)) {
					resultUserList.add(user);
				}
			}
			// lista di utenti la cui città contiene la keyword da ricercare
			for (User user : SearchUtils.searchUserByCity(keyword)) {
				if (!resultUserList.contains(user)) {
					resultUserList.add(user);
				}
			}
			
			// se sono state digitate molteplici parole provo ad effettuare una ricerca nome-cognome
			if(words.length >= 2){
				// lista di utenti la cui città contiene la keyword da ricercare
				for (User user : SearchUtils.searchUserByNameAndSurname(words[0], words[1])) {
					if (!resultUserList.contains(user)) {
						resultUserList.add(user);
					}
				}
			}
			// imposta nella risposta gli user trovati
			request.setAttribute("userMatches", resultUserList);
			
			// SELFIE
			
			// da tutti i risultati parziali ottenuti dobbiamo ricavare un'unica lista
			List<Selfie> resultSelfieList = new ArrayList<Selfie>();
			
			// aggiungo roba alla lista dei selfie risultanti
			// i selfie caricati dallo user che ha esattamente lo username ricercato
			resultSelfieList.addAll(SelfieUtils.getSelfieByUploaderId(UserUtils.getUserByUsername(keyword).getId_user()));
			
			// scorre la lista di utenti il cui username contiene la keyword da ricercare
			for (User user : SearchUtils.searchUserByUsername(keyword)) {
				// per ogni utente trovato prende tutti i selfie che ha caricato
				int uploaderId = user.getId_user();
				for ( Selfie selfie : SelfieUtils.getSelfieByUploaderId(uploaderId))
				{
					if (!resultSelfieList.contains(selfie)) {
						resultSelfieList.add(selfie);
					}
				}
			}
			
			// lista di utenti la cui città contiene la keyword da ricercare
			for (Selfie selfie : SearchUtils.searchSelfieByLocation(keyword)) {
				if (!resultSelfieList.contains(selfie)) {
					resultSelfieList.add(selfie);
				}
			}
			// imposta nella risposta gli user trovati
			request.setAttribute("selfieMatches", resultSelfieList);
			
			// HASHTAGS
			
			// da tutti i risultati parziali ottenuti dobbiamo ricavare un'unica lista
			List<Hashtag> resultHashtagList = new ArrayList<Hashtag>();
			
			// aggiungo roba alla lista degli hashtags risultanti
			// gli hashtag che hanno come nome esattamente la keyword ricercata
			if(HashtagUtils.getHashTagByName(keyword).getId_hashtag() > 0)
				resultHashtagList.add(HashtagUtils.getHashTagByName(keyword));
			
			// la lista degli hashtag che CONTENGONO nel nome la keyword ricercata
			for (Hashtag hashtag : SearchUtils.searchHashTagByName(keyword)) {
				if (!resultHashtagList.contains(hashtag)) {
					resultHashtagList.add(hashtag);
				}
			}
			
			// imposta nella risposta gli user trovati
			request.setAttribute("hashTagMatches", resultHashtagList);
			
    		// viene renderizzata la pagina dei risultati
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/searchResults.jsp"); 
    		dispatcher.forward(request,response);
        } else { // se il termine di ricerca è vuoto
    		// viene renderizzata la homepage
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/protected/homepage.jsp"); 
    		dispatcher.forward(request,response);
		}
		
	}

}
