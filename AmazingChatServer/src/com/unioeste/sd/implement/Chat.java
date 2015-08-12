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
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private ChatInterface client;
	
	public Chat() throws RemoteException {
        super();
        this.serverMessages = new CopyOnWriteArrayList<MessageInterface>();
        this.log = new CopyOnWriteArrayList<String>();
        this.messages = new CopyOnWriteArrayList<MessageInterface>();
        this.users = new CopyOnWriteArrayList<UserInterface>();
       
    }
	
	@Override
	public synchronized boolean login(UserInterface user) throws RemoteException {
        if(!users.isEmpty()){
        	for(UserInterface login: users){
        		if(login.getName().equals(user.getName())){        			        			   
        			notifyAll();
            		return false;
        		}
        	}
        	MessageInterface serverMessage;
	        serverMessage = new Message(user);	        
	        Date date = new Date();
	        serverMessage.setType(MessageInterface.Type.LOGIN);
	        serverMessage.setDate(date);
	        serverMessage.setMessage("[SYSTEM] User [" + user.getName() + "] is now online - "+dateFormat.format(date));   
	        serverMessages.add(serverMessage);
	        notifyAll();
	        return true;
        }
        MessageInterface serverMessage;
        serverMessage = new Message(user);
        
        Date date = new Date();
        serverMessage.setType(MessageInterface.Type.LOGIN);
        serverMessage.setDate(date);
        serverMessage.setMessage("[SYSTEM] User [" + user.getName() + "] is now online - "+dateFormat.format(date));   
        serverMessages.add(serverMessage);
        notifyAll();
        return true;
	}

	@Override
	public synchronized void logout(UserInterface user) throws RemoteException {
		MessageInterface lastMessage = new Message(user);
		lastMessage.setType(MessageInterface.Type.SHUTDOWN);
		lastMessage.setMessage("[SERVER] - Server is Shutting Down. GoodBye!");
		Date date = new Date();
		lastMessage.setDate(date);
		Iterator<UserInterface> it = users.iterator();
		while(it.hasNext()){
			UserInterface target = it.next();
        	lastMessage.addTarget(target);
        }
		this.serverMessages.add(lastMessage);
		notifyAll();
	}

    @Override
    public synchronized void sendBroadcastMessage(MessageInterface serverMessage) throws RemoteException {
        Iterator<UserInterface> it = users.iterator();
		while(it.hasNext()){
			UserInterface user = it.next();
        	serverMessage.addTarget(user);
        }
		serverMessage.setType(MessageInterface.Type.BROADCAST);
		this.serverMessages.add(serverMessage);
		notifyAll();
    }

    
    @Override
    public synchronized boolean sendUnicastMessage(UserInterface target, MessageInterface serverMessage) throws RemoteException {
    	
        Iterator<UserInterface> it = users.iterator();
        while(it.hasNext()){
        	UserInterface user = it.next();
        	if(user.getName().equals(target.getName())){
        		serverMessage.addTarget(target);
                serverMessage.setType(MessageInterface.Type.UNICAST);
        		this.serverMessages.add(serverMessage);
        		notifyAll();
        		return true;
        	}
        }
        notifyAll();
		return false;
    }

	@Override
	public synchronized UserInterface[] getLoggedUsers() throws RemoteException {
		UserInterface[] userArray = new UserInterface[users.size()];
		userArray = (UserInterface[]) users.toArray(userArray);
		return (userArray);
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
	public synchronized void readServerMessages(UserInterface user) throws RemoteException{
		
		Iterator<MessageInterface> it = serverMessages.iterator();
		while(it.hasNext()){
			MessageInterface message = it.next();
			
			switch (message.getType()) {
			case BROADCAST:
				messages.add(message);
				if(!message.getUser().getName().equals("SERVER")){					
					System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
				}
				serverMessages.remove(message);
				break;
			case UNICAST:
				messages.add(message);
				if(!message.getUser().getName().equals("SERVER")){
					System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
				}
				serverMessages.remove(message);
				break;
			case LOGOUT:				
				System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
				users.remove(message.getUser());
				serverMessages.remove(message);
				message.setMessage("[SYSTEM]User [" + message.getUser().getName() + "] has left");
				sendBroadcastMessage(message);
				break;
			case SHUTDOWN:
				messages.add(message);
				serverMessages.remove(message);
				break;
			case WHOSTHERE:
				MessageInterface newMessage = new Message(message.getUser());
				newMessage.setType(MessageInterface.Type.UNICAST);
				Iterator<UserInterface> iTWho = users.iterator();
				String str = new String();
				while(iTWho.hasNext()){
					UserInterface who = iTWho.next();
					str+="["+who.getName()+"] ";
				}
				newMessage.setDate(message.getDate());
				System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
				newMessage.setMessage("[SERVER]-Usuarios on-line: "+str);
				if(message.getUser().getName().equals("SERVER")){
					System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
					System.out.println(newMessage.getMessage()+" - "+dateFormat.format(newMessage.getDate()));
				}else
					sendUnicastMessage(message.getUser(), newMessage);
				log.add("[WHO]["+message.getUser().getName()+"] - "+message.getMessage());
				serverMessages.remove(message);
				break;
			case LOGIN:
				this.users.add(message.getUser());
				System.out.println(message.getMessage());
				log.add("[LOGIN]["+message.getUser().getName()+"] - "+message.getMessage());
				serverMessages.remove(message);
				message.setMessage("[SYSTEM]User [" + message.getUser().getName() + "] is in the room");
				message.setType(MessageInterface.Type.BROADCAST);
				sendBroadcastMessage(message);
				break;
			default:
				break;
			}
		}
		notifyAll();
	}

	@Override
	public synchronized List<MessageInterface> getMessages(UserInterface user) throws RemoteException {
		// TODO Auto-generated method stub
		Iterator<MessageInterface> it = messages.iterator();
		List<MessageInterface> userMessages = new CopyOnWriteArrayList<MessageInterface>();
		while(it.hasNext()){
			MessageInterface message = it.next();
			switch (message.getType()) {
			case BROADCAST:
				List<UserInterface> targets = message.getTarget();				
				if (!targets.isEmpty()) {
					Iterator<UserInterface> itTarget = targets.iterator();
					while(itTarget.hasNext()){
					UserInterface target = itTarget.next();
						if(target.getName().equals(user.getName())){											
							userMessages.add(message);
							message.removeTarget(user);
							log.add("[READ]["+user.getName()+"] - "+message.getMessage());
							if(message.getTarget().isEmpty()){								
								messages.remove(message);
							}else{
								messages.remove(message);
								messages.add(message);
							}
						}
					}
				}
				break;
			case UNICAST:
				List<UserInterface> targets1 = message.getTarget();				
				if (!targets1.isEmpty()) {
					Iterator<UserInterface> itTarget = targets1.iterator();
					while(itTarget.hasNext()){
						UserInterface target = itTarget.next();
						if(target.getName().equals(user.getName())){	
							userMessages.add(message);
							log.add("[READ]["+user.getName()+"] - "+message.getMessage());
							messages.remove(message);
						}
					}
				}
				break;
			case SHUTDOWN:
				log.add("[SHUTDOWN]["+user.getName()+"] - "+message.getMessage());				
				messages.remove(message);
				userMessages.add(message);
			default:
				break;
			}
		}
		notifyAll();
		return userMessages;
	}

	@Override
	public synchronized void sendWho(MessageInterface serverMessage) throws RemoteException {
		// TODO Auto-generated method stub
		
        serverMessage.setType(MessageInterface.Type.WHOSTHERE);
        Date date = new Date();
        serverMessage.setDate(date);
        serverMessage.setMessage("WHO's THERE?");
		this.serverMessages.add(serverMessage);
		notifyAll();
	}

	@Override
	public synchronized void sendLogout(MessageInterface serverMessage) throws RemoteException {
		// TODO Auto-generated method stub
		serverMessage.setType(MessageInterface.Type.LOGOUT);
		Date date = new Date();
        serverMessage.setDate(date);
        serverMessage.setMessage("LOGOUT!");
		this.serverMessages.add(serverMessage);
		notifyAll();
	}
	
}
