package com.unioeste.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;
import com.unioeste.sd.implement.Chat;
import com.unioeste.sd.implement.Message;
import com.unioeste.sd.implement.User;

public class ChatServer {
	public static void main(String args[]) {
		int port = args[0] != null ? Integer.parseInt(args[0]) : 1099;
		String chat_address = "rmi://localhost:" + port + "/ChatService";
		String user_address = "rmi://localhost:" + port + "/UserService";
		String message_address = "rmi://localhost:" + port + "/MessageService";
		
		try {
			Registry r = LocateRegistry.createRegistry(port);
			
			ChatInterface chat = new Chat();
			r.rebind(chat_address, chat);
			
			UserInterface user = new User();
			r.rebind(user_address, user);
			
			MessageInterface message = new Message();
			r.rebind(message_address, message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
