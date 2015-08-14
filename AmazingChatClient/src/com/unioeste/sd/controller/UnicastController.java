package com.unioeste.sd.controller;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.unioeste.sd.ChatClient;
import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.infra.Register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class UnicastController implements Controller{
	@FXML TextArea unicastPanel;
	@FXML TextArea textToSend;
	private ChatClient client;
	private FacadeUser currentUser;
	private Register register;
	private Date lastMessage;
	private SimpleDateFormat formatter;
	
	public UnicastController() {
		this.register = new Register();
		this.lastMessage = new Date(0);
		this.formatter = new SimpleDateFormat("hh:mm:ss");
	}
	
	@FXML
	public void handleSendButtonAction(ActionEvent event){
		try{
			FacadeMessage message = this.buildMessage(textToSend.getText());		
			client.getChat().sendUnicastMessage(currentUser, message);;
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		textToSend.clear();
	}
	
	@FXML
	private void handleTextToSendKeyPressed(KeyEvent event){
		if(event.getCode() == KeyCode.ENTER){
			handleSendButtonAction(null);
			event.consume();
		}
	}
	
	private FacadeMessage buildMessage(String message){
		FacadeMessage m = null;
		
		try {
			m = this.register.getMessageInstance();
			m.setUser(client.getUser());
			m.setMessage(message);
			m.setDate(new Date());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return m;
	}
	
	public void setUser(FacadeUser user){
		this.currentUser = user;
		try {
			System.out.println("Current user: " + currentUser.getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					try {
						FacadeMessage[] messages = client.getChat().getMessagesAfter(currentUser, lastMessage);
						
						for(FacadeMessage message : messages){
							String date = formatter.format(message.getDate());
							unicastPanel.appendText("[" + date + "]" + message.getUser().getName() + ": " + message.getMessage() + "\n");
						}
						
						client.setLastReceivedMessageDate(new Date());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void initManager(ChatClient chatClient) {
		this.client = chatClient;	
		this.listen();
	}
}
