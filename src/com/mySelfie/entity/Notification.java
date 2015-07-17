package com.mySelfie.entity;

import java.sql.Timestamp;

public class Notification {

	private int id_notification = -1;
	private int id_user = -1;
	private String type = null;
	private String text = null;
	private Timestamp issue_date = null;
	private Timestamp seen_date = null;
	private int user_like_selfie = -1;
	private int user_tag_selfie = -1;
	private int user_follow_user = -1;

	public int getId_notification() {
		return id_notification;
	}

	public void setId_notification(int id_notification) {
		this.id_notification = id_notification;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(Timestamp issue_date) {
		this.issue_date = issue_date;
	}

	public Timestamp getSeen_date() {
		return seen_date;
	}

	public void setSeen_date(Timestamp seen_date) {
		this.seen_date = seen_date;
	}

	public int getUser_like_selfie() {
		return user_like_selfie;
	}

	public void setUser_like_selfie(int user_like_selfie) {
		this.user_like_selfie = user_like_selfie;
	}

	public int getUser_tag_selfie() {
		return user_tag_selfie;
	}

	public void setUser_tag_selfie(int user_tag_selfie) {
		this.user_tag_selfie = user_tag_selfie;
	}

	public int getUser_follow_user() {
		return user_follow_user;
	}

	public void setUser_follow_user(int user_follow_user) {
		this.user_follow_user = user_follow_user;
	}

}
