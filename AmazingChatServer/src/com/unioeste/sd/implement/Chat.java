package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

public class Chat extends UnicastRemoteObject implements ChatInterface {

	private static final long serialVersionUID = 1L;
	private List<UserInterface> users;

	public Chat() throws RemoteException {
        super();

        this.users = new ArrayList<UserInterface>();
    }

	@Override
	public void login(UserInterface user) throws RemoteException {
        this.users.add(user);

		System.out.println("User " + user.getName() + " is now logged in");

	}

	@Override
	public void logout(UserInterface user) throws RemoteException {
		System.out.println("User " + user.getName() + "has left");
		this.users.remove(user);
	}

    @Override
    public void sendBroadcastMessage(MessageInterface message, UserInterface from) throws RemoteException {
        System.out.println("["+from.getName()+"] "+message.getMessage());
        Iterator<UserInterface> usernames = users.iterator();
        while(usernames.hasNext()){
            User user =(User) usernames.next();
            MessageInterface m = (MessageInterface) users.get(users.indexOf(usernames.next()));
            if (user.getName().equals(from.getName())){continue;}

            try{
                m.setMessage("\n[" + from.getName() +"] "+message.getMessage());
            }catch(Exception e){e.printStackTrace();}
        }
    }

    @Override
    public void sendUnicastMessage(UserInterface target, UserInterface from, MessageInterface message) throws RemoteException {

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
