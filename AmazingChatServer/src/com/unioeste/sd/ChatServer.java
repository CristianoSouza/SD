package com.unioeste.sd;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.implement.Chat;
import com.unioeste.sd.implement.User;

public class ChatServer {
	public static void main(String args[]) {
		/*int port = args[0] != null ? Integer.parseInt(args[0]) : 1099;
		String address = "rmi://localhost:" + port + "/ChatService";*/
		
		try {
            //System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(65000);
            Chat obj = new Chat();
            Naming.rebind("rmi://192.168.0.117/ABCD", obj);

            System.out.println("[System] Chat Server is ready.");

            /*while(true){
                String msg=s.nextLine().trim();
                if (server.getLoggedUsers()!=null){
                    ChatInterface client=server.getClient();
                    msg="["+server.getName()+"] "+msg;
                    client.send(msg);
                }
            }*/
		} catch (RemoteException e) {
            System.out.println("Chat Server Failed");
            e.printStackTrace();
		} catch (MalformedURLException e) {
            System.out.println("Chat Server Failed");
            e.printStackTrace();
        }
    }
}
