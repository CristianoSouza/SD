package com.unioeste.sd.controller;

import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.infra.ChatConnector;
import com.unioeste.sd.infra.Register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField username;
	
	private ChatConnector connector;
	private Register register;
	
	public LoginController() {
		this.register = new Register();
	}
	
	@FXML
	private void handleLoginButtonAction(ActionEvent event){
		System.out.println(username.getText() + " is logged in.");
		
		if(!username.getText().isEmpty()){
			Stage stage = (Stage) username.getScene().getWindow();
			stage.close();
			
			try{
				FacadeUser user = register.getUserInstance();
				user.setName(username.getText());
				
				connector = new ChatConnector();
				connector.connect(user);
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
