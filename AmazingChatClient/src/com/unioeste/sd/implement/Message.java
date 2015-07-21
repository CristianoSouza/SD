package com.unioeste.sd.implement;

import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;

public class Message extends UnicastRemoteObject implements MessageInterface{

	public static enum Type {
		UNICAST, BROADCAST
	}
	
	private static final long serialVersionUID = 1L;
	private UserInterface user;
	private String message;
	private Date date;
	private Type type; 
	
	public Message() throws RemoteException {
		super();
	}

    public Message(UserInterface user) throws RemoteException {
        super();
        this.user = user;
    }
	
	@Override
	public UserInterface getUser() {
		return user;
	}
	
	@Override
	public void setUser(UserInterface user) {
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
