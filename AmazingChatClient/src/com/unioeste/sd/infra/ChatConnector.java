package com.unioeste.sd.infra;

import com.unioeste.sd.exception.ChatException;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Error while establishing connection with server");
			alert.setContentText(e.getCause().getMessage());
			alert.showAndWait();
			
			throw new ChatException("The following problem has happend while establishing connection with server: " + e.getMessage());
		}
	}
}
