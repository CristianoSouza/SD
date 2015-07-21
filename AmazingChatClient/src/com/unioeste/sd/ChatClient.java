package com.unioeste.sd;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;
import com.unioeste.sd.implement.Message;
import com.unioeste.sd.implement.User;

import java.rmi.Naming;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            ChatInterface server = (ChatInterface) Naming.lookup("rmi://192.168.0.117/ABCD");

            Scanner scanner=new Scanner(System.in);
            System.out.println("[System] Client Messenger is running");
            System.out.println("Enter a username to login and press Enter:");
            String username = scanner.nextLine();
            UserInterface user = new User(username);
            MessageInterface message = new Message(user);
            message.setMessage("Just Connected");
            server.login(user);
            server.sendBroadcastMessage(message,user);
            for(;;){
                String aa = scanner.nextLine();
                message.setMessage(aa);
                server.sendBroadcastMessage(message,user);
            }
        }catch (Exception e) {
            System.out.println("Hello Client exception: " + e);
            e.printStackTrace();
        }
    }
}
