package com.unioeste.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeMessageFactory;
import com.unioeste.sd.facade.FacadeUserFactory;
import com.unioeste.sd.implement.Chat;
import com.unioeste.sd.implement.MessageFactory;
import com.unioeste.sd.implement.UserFactory;

public class ChatServer {
	public static void main(String args[]) {
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 1099;
		String chat_address = "rmi://localhost:" + port + "/ChatService";
		String user_address = "rmi://localhost:" + port + "/UserFactoryService";
		String message_address = "rmi://localhost:" + port + "/MessageFactoryService";
		
		try {
			Registry r = LocateRegistry.createRegistry(port);
			
			FacadeChat chat = new Chat();
			r.rebind(chat_address, chat);
			
			FacadeUserFactory userFactory = new UserFactory();
			r.rebind(user_address, userFactory);
			
			FacadeMessageFactory messageFactory = new MessageFactory();
			r.rebind(message_address, messageFactory);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
