package com.unioeste.sd.facade;

import com.unioeste.sd.implement.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatInterface extends Remote{
	public boolean login(UserInterface user) throws RemoteException;
	public void logout(UserInterface user) throws RemoteException;
	public void sendBroadcastMessage(MessageInterface message) throws RemoteException;
	public boolean sendUnicastMessage(UserInterface target, MessageInterface message) throws RemoteException;
	public UserInterface[] getLoggedUsers() throws RemoteException;
	public ChatInterface getClient() throws RemoteException;
	public void setClient(ChatInterface client) throws RemoteException;
	//public MessageInterface getMessage() throws RemoteException;
	public void readServerMessages(UserInterface user) throws RemoteException;
	//public void readMessages(UserInterface user) throws RemoteException;
	public List<MessageInterface> getMessages(UserInterface user)throws RemoteException;
	public void sendWho(MessageInterface serverMessage)throws RemoteException;
	public void sendLogout(MessageInterface serverMessage) throws RemoteException;
}
