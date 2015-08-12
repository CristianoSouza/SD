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
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
					Date date = new Date();
					message.setDate(date);
					message.setMessage(str);				
					chat.sendBroadcastMessage(message);


					break;
				case "2":
					System.out.println("Write your unicast message followed by [ENTER]");
					s = new Scanner(System.in);
	    	    	String str2 = s.nextLine();
	    	    	
	    	    	System.out.println("Write your target name followed by [ENTER]");
					s = new Scanner(System.in);
	    	    	String strTarget = s.nextLine();
	    	    	
	    	    	message = new Message(user);
	    	    	Date date2 = new Date();
	    	    	message.setDate(date2);
					message.setMessage(str2);
					UserInterface target = new User();
					target.setName(strTarget);					
					
					if(!chat.sendUnicastMessage(target, message)){
						System.out.println("[SYSTEM] - User is not online. Try again!");
					}
					break;
				case "3":					
					message =new Message(user);
					chat.sendWho(message);				
					System.out.println("WHOSTHERE");
					
					break;
				case "4":
					
					message = new Message(user);
					chat.logout(user);
					connected = false;
					System.out.println("[SYSTEM] - Bye!");
					break;
				default:
					System.out.println("[SYSTEM]-Please chose one of the options followed by [ENTER]");
					break;
				}
			}
			Thread.sleep(5000);
			System.exit(0);
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
				while(connected){
					chat.readServerMessages(user);					
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}
	};
}
