package com.mySelfie.entity;

import java.sql.Timestamp;

public class Selfie {
	
	private int id_selfie = 0;
	private int uploader = 0;
	private String description = null;
    private String location = null;
    private Timestamp date = null;
    private String picture = null;
	
	public int getId_selfie() {
		return id_selfie;
	}
	public void setId_selfie(int id_selfie) {
		this.id_selfie = id_selfie;
	}
	public int getUploader() {
		return uploader;
	}
	public void setUploader(int uploader) {
		this.uploader = uploader;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

}
