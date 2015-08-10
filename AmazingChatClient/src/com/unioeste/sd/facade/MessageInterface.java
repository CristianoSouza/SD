package com.unioeste.sd.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;



public interface MessageInterface extends Remote{
	
	public static enum Type {
		UNICAST, BROADCAST, SHUTDOWN, WHOSTHERE, LOGIN
	}
	public static enum Status {
		UNREAD, READ
	}
	public void setUser(UserInterface user) throws RemoteException;
	public UserInterface getUser() throws RemoteException;
	public String getMessage() throws RemoteException;
	public void setMessage(String message) throws RemoteException;
	public Date getDate() throws RemoteException;
	public void setDate(Date date) throws RemoteException;
	public Type getType() throws RemoteException;
	public void setType(Type type) throws RemoteException;
	public Status getStatus() throws RemoteException;
	public void setStatus(Status status) throws RemoteException;
	public void setTarget(List<UserInterface> target) throws RemoteException;
	public List<UserInterface> getTarget() throws RemoteException;
	public void removeTarget(UserInterface target) throws RemoteException;
	public void addTarget(UserInterface target)throws RemoteException;
}
