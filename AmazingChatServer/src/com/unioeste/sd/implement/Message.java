package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;

import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeUser;

public class Message extends UnicastRemoteObject implements FacadeMessage{

	public static enum Type {
		UNICAST, BROADCAST
	}
	
	private static final long serialVersionUID = 1L;
	private FacadeUser user;
	private String message;
	private Date date;
	private Type type; 
	
	public Message() throws RemoteException {
		super();
	}
	
	@Override
	public FacadeUser getUser() {
		return user;
	}
	
	@Override
	public void setUser(FacadeUser user) {
		this.user = user;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}
}
