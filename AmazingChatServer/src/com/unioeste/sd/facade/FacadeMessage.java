package com.unioeste.sd.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

import com.unioeste.sd.implement.Message.Type;

public interface FacadeMessage extends Remote{
	public void setUser(FacadeUser user) throws RemoteException;
	public FacadeUser getUser() throws RemoteException;
	public String getMessage() throws RemoteException;
	public void setMessage(String message) throws RemoteException;
	public Date getDate() throws RemoteException;
	public void setDate(Date date) throws RemoteException;
	public Type getType() throws RemoteException;
	public void setType(Type type) throws RemoteException;
}
