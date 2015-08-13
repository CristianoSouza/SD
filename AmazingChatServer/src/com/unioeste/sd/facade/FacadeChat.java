package com.unioeste.sd.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface FacadeChat extends Remote{
	public void login(FacadeUser user) throws RemoteException;
	public void logout(FacadeUser user) throws RemoteException;
	public void sendBroadcastMessage(FacadeMessage message) throws RemoteException;
	public void sendUnicastMessage(FacadeUser target, FacadeMessage message) throws RemoteException;
	public FacadeUser[] getLoggedUsers() throws RemoteException;
	public FacadeMessage[] getMessagesAfter(FacadeUser user, Date date) throws RemoteException;
	public Date getLastUpdate() throws RemoteException;
}
