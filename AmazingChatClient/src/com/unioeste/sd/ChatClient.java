package com.unioeste.sd;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;
import com.unioeste.sd.implement.Chat;
import com.unioeste.sd.implement.Message;
import com.unioeste.sd.implement.User;

import java.rmi.Naming;
import java.util.Date;
import java.util.Scanner;

public class ChatClient {
	ChatInterface server;
	boolean connected = true;
	UserInterface user;
	MessageInterface message;
	
    public ChatClient() {
    	
        try {
            server = (ChatInterface) Naming.lookup("rmi://localhost/ABCD");             
            
            
            
            int count=0;
            synchronized (server){
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
            	}while(!server.login(user));
            	
        	}
            System.out.println("[System] Client Messenger is running");
            System.out.println("[SYSTEM] - Type \t [1]to broadcast \t[2]to unicast \t[3]to WHO'S there \t[4]shutdown");
            while(connected){            	
    	    	Scanner s = new Scanner(System.in);
    	    	String choice = s.nextLine();
    	    	switch (choice) {
				case "1":
					System.out.println("Write your broadcast message followed by [ENTER]");
					synchronized (server){
						s = new Scanner(System.in);
		    	    	String str = s.nextLine();
		    	    	/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    	    	dateFormat.format(date);*/
		    	    	Date date = new Date();
		    	    	message.setDate(date);
						message.setMessage(str);
						message.setType(Message.Type.BROADCAST);
						server.sendBroadcastMessage(message, user);
					}
					break;
				case "2":
					System.out.println("Write your broadcas message followed by [ENTER]");
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
            
        }catch (Exception e) {
            System.out.println("Some Error has occurred: " + e);
            e.printStackTrace();
        }
    }
}
