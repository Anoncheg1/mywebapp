package com.foo.mywebapp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Contact {
	private int id;
	private String fio;
	private Date birthday;
	private String phone;
	private String address;

	public Contact() {};
	public Contact(int id, String fio, Date birthday, String phone, String address) {	
		this.id = id;
		this.fio = fio;
		this.birthday = birthday;
		this.phone = phone;
		this.address = address;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Contact))
			return false;
		Contact n = (Contact) o;
		return (n.id == this.id);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	  
	// getters/setters
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	/**
	 * @TODO Formatter
	 * @return
	 */
	public Date getBirthday() {
		
		if (birthday == null)
			return new Date();
		return birthday; //ZK support only java.util.Date 
	}

	/**
	 * @TODO Validator
	 * @param birthday
	 */
	public void setBirthday(Date birthday){
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}