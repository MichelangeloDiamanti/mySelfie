package com.mySelfie.entity;

import java.util.Date;

public class Comment {

	private int id_comment = 0;
	private int id_user = 0;
	private int id_selfie = 0;
	private String text = null;
	private Date date = null;

	public int getId_comment() {
		return id_comment;
	}

	public void setId_comment(int id_comment) {
		this.id_comment = id_comment;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public int getId_selfie() {
		return id_selfie;
	}

	public void setId_selfie(int id_selfie) {
		this.id_selfie = id_selfie;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
