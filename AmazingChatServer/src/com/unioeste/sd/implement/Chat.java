package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

public class Chat extends UnicastRemoteObject implements ChatInterface {

	private static final long serialVersionUID = 1L;
	private List<UserInterface> users;
	private List<MessageInterface> messages,serverMessages;
	private List<String> log;
	private MessageInterface serverMessage;
	private ChatInterface client;
	
	public Chat() throws RemoteException {
        super();
        this.serverMessages = new CopyOnWriteArrayList<MessageInterface>();
        this.log = new CopyOnWriteArrayList<String>();
        this.messages = new CopyOnWriteArrayList<MessageInterface>();
        this.users = new CopyOnWriteArrayList<UserInterface>();
        this.serverMessage = new Message();
    }
	
	@Override
	public synchronized boolean login(UserInterface user) throws RemoteException {
        if(!users.isEmpty()){
        	for(UserInterface login: users){
        		if(login.getName().equals(user.getName())){        			        			   
            		notify();
            		return false;
        		}
        	}
	        serverMessage = new Message(user);
	        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        Date date = new Date();
	        serverMessage.setType(MessageInterface.Type.LOGIN);
	        serverMessage.setDate(date);
	        serverMessage.setMessage("[SYSTEM] User [" + user.getName() + "] is now online - "+dateFormat.format(date));   
	        serverMessages.add(serverMessage);
	        notify();
	        return true;
        }
        serverMessage = new Message(user);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        serverMessage.setType(MessageInterface.Type.LOGIN);
        serverMessage.setDate(date);
        serverMessage.setMessage("[SYSTEM] User [" + user.getName() + "] is now online - "+dateFormat.format(date));   
        serverMessages.add(serverMessage);
        notify();
        return true;
	}

	@Override
	public void logout(UserInterface user) throws RemoteException {
		System.out.println("[SYSTEM]User " + user.getName() + "has left");
		this.users.remove(user);
	}

    @Override
    public void sendBroadcastMessage(MessageInterface serverMessage, UserInterface from) throws RemoteException {
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("["+from.getName()+"] - "+serverMessage.getMessage()+" - "+dateFormat.format(serverMessage.getDate()));
        this.serverMessage = serverMessage;
        this.serverMessage.setUser(from);
        for(UserInterface user: users){
        	this.serverMessage.addTarget(user);
        }
        this.serverMessages.add(serverMessage);
        notify();
    }

    @Override
    public void sendUnicastMessage(UserInterface target, UserInterface from, MessageInterface serverMessage) throws RemoteException {

    }

	@Override
	public UserInterface[] getLoggedUsers() throws RemoteException {
		UserInterface[] userArray = new UserInterface[users.size()];
		userArray = (UserInterface[]) users.toArray(userArray);
		return (userArray);
	}

	@Override
	public void notifyChange() throws RemoteException {
		for(UserInterface listener : users){
			synchronized (listener) {
				listener.notify();
			}
		}
	}

	@Override
	public ChatInterface getClient() throws RemoteException {
		// TODO Auto-generated method stub
		return this.client;
	}

	@Override
	public void setClient(ChatInterface client) throws RemoteException {
		// TODO Auto-generated method stub
		this.client = client;
	}

	@Override
	public MessageInterface getMessage() throws RemoteException {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println("["+serverMessage.getUser().getName()+"] - "+serverMessage.getMessage()+" - "+dateFormat.format(serverMessage.getDate()));
		return this.serverMessage;
	}
	@Override
	public void readServerMessages() throws RemoteException{
		
		Iterator<MessageInterface> it = serverMessages.iterator();
		while(it.hasNext()){
			MessageInterface message = it.next();
			
			switch (message.getType()) {
			case BROADCAST:
				messages.add(message);
				System.out.println(message.getMessage());
				serverMessages.remove(message);
				break;
			case UNICAST:
				break;
			case SHUTDOWN:
				break;
			case WHOSTHERE:
				break;
			case LOGIN:
				this.users.add(message.getUser());
				System.out.println(message.getMessage());
				serverMessages.remove(message);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void readMessages(UserInterface user) throws RemoteException {
		// TODO Auto-generated method stub
		for(MessageInterface message: messages){
			switch (message.getType()) {
			case BROADCAST:
				List<UserInterface> targets = message.getTarget();				
				if (!targets.isEmpty()) {
					for(UserInterface target : targets){
						if(target.getName().equals(user.getName())){
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
							message.removeTarget(user);
							log.add("[READ]["+user.getName()+"] - "+message.getMessage());
							break;
						}
					}
				}
				break;

			default:
				break;
			}
		}
	}
	
}
