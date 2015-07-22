package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

public class Chat extends UnicastRemoteObject implements ChatInterface {

	private static final long serialVersionUID = 1L;
	private List<UserInterface> users;

	public Chat() throws RemoteException {
		super();
		System.out.println("Initializing server ...");
		this.users = new ArrayList<UserInterface>();
	}

	@Override
	public void login(UserInterface user) throws RemoteException {
		System.out.println("User " + user.getName() + " is now logged in");
		this.users.add(user);
	}

	@Override
	public void logout(UserInterface user) throws RemoteException {
		System.out.println("User " + user.getName() + "has left");
		this.users.remove(user);
	}

	@Override
	public void sendBroadcastMessage(MessageInterface message) throws RemoteException {
		System.out.println("Sending broadcast message");
		for(UserInterface user : this.users){
			user.receive(message);
		}		
	}

	@Override
	public void sendUnicastMessage(UserInterface target, MessageInterface message) throws RemoteException {
		System.out.println("Sending unicast message");
		target.receive(message);
	}

	@Override
	public UserInterface[] getLoggedUsers() throws RemoteException {
		return (UserInterface[]) this.users.toArray();
	}

	@Override
	public void notifyChange() throws RemoteException {
		for(UserInterface listener : users){
			synchronized (listener) {
				listener.notify();
			}
		}
	}

}
