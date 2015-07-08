package com.mySelfie.entity;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class User {
	private int id_user = 0;
	private String username = null;
    private String password = null;
    private String name = null;
    private String email = null;
    private String phone = null;
    private String surname = null;
    private String gender = null;
    private String notes = null;
    private String city = null;
    private String profilepic = null;
    private Date birthdate = null;
       
    private boolean isValid = false;
     
    public User()
    {
    	
    }

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setBirthdate(String birthdate) throws ParseException  {
		//converto la stringa in Data
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd"); 
		Date bd = df.parse(birthdate);    					  
		java.sql.Date dateSQL = new java.sql.Date(bd.getTime());
		//setto l' attributo birthdate con la data convertita
		this.birthdate = dateSQL;
	
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
    
}