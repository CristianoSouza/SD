package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeUser;

public class Chat extends UnicastRemoteObject implements FacadeChat {

	private static final long serialVersionUID = 1L;
	private List<FacadeUser> users;
	private HashMap<FacadeUser, List<FacadeMessage>> messagesBuffer;
	private FacadeUser broadcastUser;
	private Date lastUpdate;

	public Chat() throws RemoteException {
		super();
		System.out.println("Initializing server ...");
		this.users = new ArrayList<FacadeUser>();
		
		this.messagesBuffer = new HashMap<FacadeUser, List<FacadeMessage>>();
		this.broadcastUser = new User();
		this.messagesBuffer.put(broadcastUser, new ArrayList<FacadeMessage>());
		
		this.lastUpdate = new Date();
	}

	@Override
	public void login(FacadeUser user) throws RemoteException {
		boolean contains = false;
		
		for(FacadeUser u : this.users){
			contains = u.getName().equals(user.getName());
			
			if(contains) break;
		}
		
		if(!contains){
			System.out.println("User " + user.getName() + " is now online");
			this.users.add(user);
			this.lastUpdate = new Date();
		} else {
			throw new RemoteException("This user already online");
		}
	}

	@Override
	public void logout(FacadeUser user) throws RemoteException {
		System.out.println("User " + user.getName() + "has left");
		this.users.remove(user);
		this.lastUpdate = new Date();
	}

	@Override
	public void sendBroadcastMessage(FacadeMessage message) throws RemoteException {
		System.out.println("Sending broadcast message");
		this.messagesBuffer.get(broadcastUser).add(message);
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
	public Date getLastUpdate() throws RemoteException {
		return this.lastUpdate;
	}

	@Override
	public FacadeMessage[] getMessagesAfter(FacadeUser user, Date date) throws RemoteException {
		List<FacadeMessage> messages = this.messagesBuffer.get(user);
		LinkedList<FacadeMessage> newMessasges = new LinkedList<FacadeMessage>();
		
		ListIterator<FacadeMessage> iterator = messages.listIterator(messages.size());
		
		while(iterator.hasPrevious()){
			FacadeMessage message = iterator.previous();
			
			if(message.getDate().compareTo(date) <= 0){
				break;
			} else {
				newMessasges.addFirst(message);
			}
		}
		
		return newMessasges.toArray(new FacadeMessage[newMessasges.size()]);
	}
}
