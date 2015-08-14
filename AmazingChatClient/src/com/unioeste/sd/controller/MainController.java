package com.unioeste.sd.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import com.unioeste.sd.ChatClient;
import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.infra.Register;
import com.unioeste.sd.misc.UserListCell;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class MainController implements Controller{
	@FXML private ListView<FacadeUser> userList;
	@FXML private TextArea textToSend;
	@FXML private TextArea broadcastPanel;
	@FXML private TabPane chatPane;
	private ChatClient client;
	private Register register;
	private SimpleDateFormat formatter;
	
	public MainController() {
		this.register = new Register();
		this.formatter = new SimpleDateFormat("hh:mm:ss");
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
	
	@FXML
	private void handleExitButtonAction(ActionEvent event){
		try {
			this.client.getChat().logout(this.client.getUser());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		Platform.exit();
		System.exit(0);
	}
	
	@FXML
	private void handleTextToSendKeyPressed(KeyEvent event){
		if(event.getCode() == KeyCode.ENTER){
			handleSendButtonAction(null);
			event.consume();
		}
	}
	
	@FXML
	private void handleSendButtonAction(ActionEvent event){
		try{
			FacadeMessage message = this.buildMessage(textToSend.getText());		
			client.getChat().sendBroadcastMessage(message);
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		textToSend.clear();
	}

	public void initManager(final ChatClient chatClient) {
		this.client = chatClient;
		try {
			this.buildOnlineList(new ArrayList<FacadeUser>(Arrays.asList(this.client.getChat().getLoggedUsers())));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			FacadeMessage message = this.buildMessage("Hi! I'm online.");
			this.client.getChat().sendBroadcastMessage(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.listen();
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
						if(client.getLastUpdate().compareTo(client.getChat().getLastUpdate()) < 0){
							System.out.println("Rebuilding user list");
							client.setLastUpdate(client.getChat().getLastUpdate());
							buildOnlineList(new ArrayList<FacadeUser>(Arrays.asList(client.getChat().getLoggedUsers())));
						}
						
						FacadeMessage[] broadcastMessages = client.getChat().getBroadcastMessagesAfter(client.getLastReceivedMessageDate());
						
						for(FacadeMessage message : broadcastMessages){
							String date = formatter.format(message.getDate());
							broadcastPanel.appendText("[" + date + "]" + message.getUser().getName() + ": " + message.getMessage() + "\n");
						}
						
						client.setLastReceivedMessageDate(new Date());
						
						FacadeUser[] onGoingChat = client.getChat().getOnGoingChatWith(client.getUser());
						
						for(FacadeUser user : onGoingChat){
							if(!client.getChatList().contains(user)){
								createTab(user);
								client.getChatList().add(user);
							}
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void buildOnlineList(Collection<FacadeUser> loggedUsers){
		loggedUsers.remove(this.client.getUser());
		ObservableList<FacadeUser> items = FXCollections.observableArrayList(loggedUsers);
		
		userList.setItems(items);
		userList.setCellFactory(new Callback<ListView<FacadeUser>, ListCell<FacadeUser>>() {
			@Override
			public ListCell<FacadeUser> call(ListView<FacadeUser> list) {
				UserListCell cell = new UserListCell();
				cell.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event) {
						boolean contains = false;
						if(cell.getText() != null){
							for(FacadeUser user : client.getChatList()){
								try {contains = user.getName().equals(cell.getItem().getName());} catch (RemoteException e) {}
								if(contains) break;
							}
							
							if(!contains && event.getClickCount() > 1){
								client.getChatList().add(cell.getItem());
								
								try {
									System.out.println(
										"opening new connection with " + cell.getText() + 
										" over IP: " + cell.getItem().getIp().getHostAddress()
									);
								} catch (RemoteException e) {
									e.printStackTrace();
								}
								
								createTab(cell.getItem());
							}
						}
					}
				});
				
				return cell;
			}
		});
	}

	private void createTab(FacadeUser user){
		System.out.println("Trying to create tab");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewChat.fxml"));
			Tab tab = loader.load();
			
			UnicastController controller = loader.<UnicastController>getController();
			controller.setUser(user);
			controller.initManager(client);
			
			tab.setText(user.getName());
			tab.setClosable(true);
			
			tab.setOnCloseRequest(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					client.getChatList().remove(user);
				}
			});
			
			chatPane.getTabs().add(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}