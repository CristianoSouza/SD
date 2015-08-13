package com.unioeste.sd.controller;

import java.net.Inet4Address;

import com.unioeste.sd.ChatClient;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.infra.ChatConnector;
import com.unioeste.sd.infra.Register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController implements Controller{
	@FXML
	private TextField username;
	
	private Register register;
	private ChatClient client;
	private ChatConnector connector;
	
	public LoginController() {
		this.register = new Register();
		this.connector = new ChatConnector();
	}
	
	public void initManager(final ChatClient client){
		this.client = client;
	}
	
	@FXML
	private void handleLoginButtonAction(ActionEvent event){
		if(!username.getText().isEmpty()){
			try{
				FacadeUser user = register.getUserInstance();
				user.setName(username.getText());
				user.setIp(Inet4Address.getLocalHost());
				this.client.setUser(user);
				
				FacadeChat chat = connector.connect(this.client.getUser());
				this.client.setChat(chat);
				this.client.setLastUpdate(chat.getLastUpdate());
				
				this.client.authenticated(username.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			username.setStyle("-fx-border-color: red;");
		}
	}
	
	@FXML
	private void handleTypedKey(KeyEvent event){
		if(event.getCode() == KeyCode.ENTER){
			handleLoginButtonAction(null);
		}
	}
}
