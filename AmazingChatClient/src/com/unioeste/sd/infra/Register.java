package com.unioeste.sd.infra;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeMessageFactory;
import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.facade.FacadeUserFactory;

public class Register {
	private int port;
	private String host;
	private String chat_address;
	private String user_address;
	private String message_address;
	private Registry registry;
	
	public Register(){
		port = 1099;
		host = "localhost";
		chat_address = "rmi://localhost:" + port + "/ChatService";
		user_address = "rmi://localhost:" + port + "/UserFactoryService";
		message_address = "rmi://localhost:" + port + "/MessageFactoryService";
		
		try {
			registry = LocateRegistry.getRegistry(host);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public FacadeChat getChatInstance() throws Exception {
		return (FacadeChat) registry.lookup(chat_address); 
	}
	
	public FacadeUser getUserInstance() throws Exception {
		return ((FacadeUserFactory) registry.lookup(user_address)).getUserInstance();
	}
	
	public FacadeMessage getMessageInstance() throws Exception {
		return ((FacadeMessageFactory) registry.lookup(message_address)).getMessageInstance();
	}
}
