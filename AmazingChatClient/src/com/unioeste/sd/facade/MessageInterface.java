package com.unioeste.sd.facade;

import com.unioeste.sd.implement.Message.Type;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface MessageInterface extends Remote{
	public void setUser(UserInterface user) throws RemoteException;
	public UserInterface getUser() throws RemoteException;
	public String getMessage() throws RemoteException;
	public void setMessage(String message) throws RemoteException;
	public Date getDate() throws RemoteException;
	public void setDate(Date date) throws RemoteException;
	public Type getType() throws RemoteException;
	public void setType(Type type) throws RemoteException;
}
