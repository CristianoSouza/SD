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

public class ChatServer extends Thread {
	
	Chat chat;
	private boolean connected = true;
	
	public ChatServer(){
		int threadCounter =0;
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			chat = new Chat();
			Naming.rebind("rmi://localhost/ABCD", chat);
			System.out.println("[SYSTEM] Chat Server is ready.");
            synchronized (chat){
        		if (chat.getLoggedUsers()!=null){
        			System.out.println("No Users Online");
        			chat.wait();
    			}  		
        	}
            threadCounter++;
    		Thread threadM = new Thread(messagerThread,"messager "+threadCounter);
            threadM.start();
            System.out.println("[SYSTEM] - Type \t [1]to broadcast \t[2]to unicast \t[3]to WHO'S there \t[4]connected");
            while(connected){            	
    	    	Scanner s = new Scanner(System.in);
    	    	String choice = s.nextLine();
    	    	switch (choice) {
				case "1":
					System.out.println("BROADCAST");
					break;
				case "2":
					System.out.println("UNICAST");
					break;
				case "3":
					System.out.println("WHOSTHERE");
					break;
				case "4":
					System.out.println("connected");
					connected = true;
					break;
				default:
					System.out.println("[SYSTEM]-Please chose one of the options followed by [ENTER]");
					break;
				}
            }
		} catch (RemoteException e) {
            System.out.println("Chat Server Failed");
            e.printStackTrace();
		} catch (MalformedURLException e) {
            System.out.println("Chat Server Failed");
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Runnable messagerThread = new Runnable(){
		
		public void run(){
			
			synchronized (chat){	
				try {
					
					String received;
					ChatInterface client = chat.getClient();
					
					while(connected){
						
						System.out.println(chat.getMessage().getMessage());
						chat.wait();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	};
}
