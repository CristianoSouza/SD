package com.unioeste.sd.facade;

import com.unioeste.sd.implement.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote{
	public boolean login(UserInterface user) throws RemoteException;
	public void logout(UserInterface user) throws RemoteException;
	public void sendBroadcastMessage(MessageInterface message,UserInterface from) throws RemoteException;
	public void sendUnicastMessage(UserInterface target,UserInterface from, MessageInterface message) throws RemoteException;
	public UserInterface[] getLoggedUsers() throws RemoteException;
	public void notifyChange() throws RemoteException;
	public ChatInterface getClient() throws RemoteException;
	public void setClient(ChatInterface client) throws RemoteException;
	public MessageInterface getMessage() throws RemoteException;
	public void readServerMessages() throws RemoteException;
	public void readMessages(UserInterface user) throws RemoteException;
}
