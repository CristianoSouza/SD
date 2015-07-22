package com.unioeste.sd.infra;

import com.unioeste.sd.exception.ChatException;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;

public class ChatConnector {
	private Register register;
	
	public ChatConnector(){
		this.register = new Register();
	}
	
	public void connect(FacadeUser user) throws ChatException{
		try{
			FacadeChat chat = register.getChatInstance();
			chat.login(user);
			
			FacadeUser users[] = chat.getLoggedUsers();
			
			for(FacadeUser u : users){
				System.out.println(u.getName());
			}
			
//			MessageInterface message = (MessageInterface) registry.lookup(message_address);
		} catch(Exception e) {
			throw new ChatException("A problem has happend while establishing connection with server.");
		}
	}
}
