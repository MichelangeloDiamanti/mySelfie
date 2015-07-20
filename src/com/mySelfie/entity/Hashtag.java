package com.mySelfie.entity;

public class Hashtag {

	int id_hashtag = -1;
	String name = null;
	
	public int getId_hashtag() {
		return id_hashtag;
	}
	public void setId_hashtag(int id_hashtag) {
		this.id_hashtag = id_hashtag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * compara due hashtag
	 */
    public boolean equals(Object obj) {
        /*
         * se l'oggetto in questione è lo stesso
         * l'esito è banalmente true
         */
    	if (obj == this) {
            return true;
        }
    	/*
    	 * se l'oggetto passato non è un'istanza di Hashtag
    	 * i due non sono confrontabili quindi torna false
    	 */
        if (!(obj instanceof Hashtag)) {
            return false;
        }
        /*
         * altrimenti si fa il casting a Hashtag
         * e si confrontano gli id.
         */
        Hashtag other = (Hashtag) obj;
        return this.id_hashtag == other.id_hashtag;
    }
	
}
