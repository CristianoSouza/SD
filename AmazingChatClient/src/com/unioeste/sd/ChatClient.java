package com.unioeste.sd;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;
import com.unioeste.sd.implement.Chat;
import com.unioeste.sd.implement.Message;
import com.unioeste.sd.implement.User;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ChatClient {
	ChatInterface chat;
	boolean connected = true;
	UserInterface user;
	MessageInterface message;
	
    public ChatClient() {
    	
        try {
            chat = (ChatInterface) Naming.lookup("rmi://localhost/ABCD");                                   
            int threadCounter =0;
            int count=0;
            synchronized (chat){
            	do{
            		Scanner scanner=new Scanner(System.in);
                    if(count>0){
                    	System.out.println("[SYSTEM] - login name already exists. Chose another");
                    }
                    System.out.println("Enter a username to login and press Enter:");
                    
                    String username = scanner.nextLine();
                    user = new User();
                    user.setName(username);
                    message = new Message();
                    message.setUser(user);
                    count++;
            	}while(!chat.login(user));
            	
        	}
            threadCounter++;
    		Thread threadM = new Thread(messagerThread,"chatMessager "+threadCounter);
            threadM.start();
            System.out.println("[System] Client Messenger is running...");
           
            while(connected){
            	System.out.println("[SYSTEM] - Type \t [1]to broadcast \t[2]to unicast \t[3]to WHO'S there \t[4]shutdown");
    	    	Scanner s = new Scanner(System.in);
    	    	String choice = s.nextLine();
    	    	switch (choice) {
				case "1":
					System.out.println("Write your broadcast message followed by [ENTER]");
					
					
						s = new Scanner(System.in);
		    	    	String str = s.nextLine();
		    	    	
		    	    	Date date = new Date();
		    	    	message.setDate(date);
						message.setMessage(str);
						
						chat.sendBroadcastMessage(message);
						System.out.println("alguem");
						
					
					break;
				case "2":
					System.out.println("Write your unicast message followed by [ENTER]");
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
            
        }catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
Runnable messagerThread = new Runnable(){
		
		public void run(){
			
			synchronized (chat){	
				try {
					
					List<MessageInterface> received;
					
					while(connected){
						received = chat.getMessages(user);					
						Iterator<MessageInterface> it = received.iterator();
						while(it.hasNext()){
							MessageInterface message = it.next();
							switch (message.getType()) {
							case BROADCAST:
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								System.out.println("["+message.getUser().getName()+"] - "+message.getMessage()+" - "+dateFormat.format(message.getDate()));
								break;
							case UNICAST:
								
								break;
							case SHUTDOWN:
								
								break;
							case WHOSTHERE:
								
								break;
							
							default:
								
								System.out.println(message.getMessage());
								break;
							}						
						}
					
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 				
			}
		}
	};
}
