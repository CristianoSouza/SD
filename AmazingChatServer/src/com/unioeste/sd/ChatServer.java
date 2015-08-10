package com.unioeste.sd;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;
import com.unioeste.sd.implement.Chat;
import com.unioeste.sd.implement.Message;
import com.unioeste.sd.implement.User;

public class ChatServer extends Thread {
	
	ChatInterface chat;
	private boolean connected = true;
	UserInterface user;
	MessageInterface message;
	
	public ChatServer(){
		int threadCounter =0;
		try {
			user = new User();
			user.setName("SERVER");
			message = new Message(user);
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
            
            while(connected){
            	System.out.println("[SYSTEM]-Type: [1]to broadcast \t[2]to unicast \t[3]to WHO'S there \t[4]shutdown");
    	    	Scanner s = new Scanner(System.in);
    	    	String choice = s.nextLine();
    	    	switch (choice) {
				case "1":
					System.out.println("Write your broadcas message followed by [ENTER]");
					
						s = new Scanner(System.in);
		    	    	String str = s.nextLine();
		    	    	/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    	    	dateFormat.format(date);*/
		    	    	Date date = new Date();
		    	    	message.setDate(date);
						message.setMessage(str);				
						chat.sendBroadcastMessage(message);
						
					
					break;
				case "2":
					System.out.println("Write your Unicast message followed by [ENTER]");
					break;
				case "3":
					System.out.println("WHOSTHERE");
					break;
				case "4":
					System.out.println("SHUTTING DOWN");
					connected = false;
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
			
		
				try {
					
					MessageInterface received;
					ChatInterface client = chat.getClient();
					
					while(connected){
						chat.readServerMessages();
						
						
						/*switch (chat.getMessage().getType()) {
						case BROADCAST:
							
							break;
						case UNICAST:
							
							break;
						case SHUTDOWN:
							
							break;
						case WHOSTHERE:
							
							break;
						
						default:
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							System.out.println("["+received.getUser()+"] "+received.getMessage()+" - "+dateFormat.format(received.getDate()));
							break;
						}*/
						
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		//}
	};
}
