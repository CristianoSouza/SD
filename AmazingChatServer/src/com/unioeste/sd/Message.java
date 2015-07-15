package com.unioeste.sd;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;

public class Message extends UnicastRemoteObject implements MessageInterface{

	public static enum Type {
		UNICAST, BROADCAST
	}
	
	private static final long serialVersionUID = 1L;
	private User user;
	private String message;
	private Date date;
	private Type type; // false for unicast and true to multicast 
	
	protected Message() throws RemoteException {
		super();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
