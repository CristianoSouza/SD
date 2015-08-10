package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

public class Message extends UnicastRemoteObject implements MessageInterface{

	
	
	private static final long serialVersionUID = 1L;
	private UserInterface user;
	private String message;
	private Date date;
	private Type type; 
	private Status status;
	private List<UserInterface> targets;
	
	public Message() throws RemoteException {
		super();
		this.targets = new ArrayList<UserInterface>();
        this.status = MessageInterface.Status.UNREAD;
        this.user = new User();
	}
	public Message(UserInterface user) throws RemoteException {
        super();
        this.user = user;
        this.targets = new ArrayList<UserInterface>();
        this.status = MessageInterface.Status.UNREAD;
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
	public List<UserInterface> getTarget() {
		return targets;
	}
	
	@Override
	public void setTarget(List<UserInterface> targets) {
		this.targets = targets;
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
	@Override
	public Status getStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return status;
	}
	@Override
	public void setStatus(Status status) throws RemoteException {
		// TODO Auto-generated method stub
		this.status = status;
	}
	public void addTarget(UserInterface target){
		this.targets.add(target);
	}
	public void removeTarget(UserInterface target){
		this.targets.remove(target);
	}
}
