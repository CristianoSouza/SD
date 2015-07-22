package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeUser;

public class Chat extends UnicastRemoteObject implements FacadeChat {

	private static final long serialVersionUID = 1L;
	private List<FacadeUser> users;

	public Chat() throws RemoteException {
		super();
		System.out.println("Initializing server ...");
		this.users = new ArrayList<FacadeUser>();
	}

	@Override
	public void login(FacadeUser user) throws RemoteException {
		System.out.println("User " + user.getName() + " is now logged in");
		this.users.add(user);
	}

	@Override
	public void logout(FacadeUser user) throws RemoteException {
		System.out.println("User " + user.getName() + "has left");
		this.users.remove(user);
	}

	@Override
	public void sendBroadcastMessage(FacadeMessage message) throws RemoteException {
		System.out.println("Sending broadcast message");
		for(FacadeUser user : this.users){
			user.receive(message);
		}		
	}

	@Override
	public void sendUnicastMessage(FacadeUser target, FacadeMessage message) throws RemoteException {
		System.out.println("Sending unicast message");
		target.receive(message);
	}

	@Override
	public FacadeUser[] getLoggedUsers() throws RemoteException {
		return (FacadeUser[]) this.users.toArray(new FacadeUser[this.users.size()]);
	}

	@Override
	public void notifyChange() throws RemoteException {
		for(FacadeUser listener : users){
			synchronized (listener) {
				listener.notify();
			}
		}
	}

}
