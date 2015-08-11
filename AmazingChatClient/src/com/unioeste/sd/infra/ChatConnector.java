package com.unioeste.sd.infra;

import com.unioeste.sd.exception.ChatException;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;

public class ChatConnector {
	private Register register;
	
	public ChatConnector(){
		this.register = new Register();
	}
	
	public FacadeChat connect(FacadeUser user) throws ChatException{
		try{
			FacadeChat chat = register.getChatInstance();
			chat.login(user);
			
			return chat;
		} catch(Exception e) {
			throw new ChatException("A problem has happend while establishing connection with server.");
		}
	}
}
