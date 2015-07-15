package com.unioeste.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.implement.Chat;

public class ChatServer {
	public static void main(String args[]) {
		int port = args[0] != null ? Integer.parseInt(args[0]) : 1099;
		String address = "rmi://localhost:" + port + "/ChatService";
		
		try {
			Registry r = LocateRegistry.createRegistry(port);
			ChatInterface chat = new Chat();
			
			r.rebind(address, chat);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
