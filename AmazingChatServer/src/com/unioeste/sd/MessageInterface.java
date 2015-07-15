package com.unioeste.sd;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

import com.unioeste.sd.Message.Type;

public interface MessageInterface extends Remote{
	public void setUser(User user) throws RemoteException;
	public String getMessage() throws RemoteException;
	public void setMessage(String message) throws RemoteException;
	public Date getDate() throws RemoteException;
	public void setDate(Date date) throws RemoteException;
	public Type getType() throws RemoteException;
	public void setType(Type type) throws RemoteException;
}
