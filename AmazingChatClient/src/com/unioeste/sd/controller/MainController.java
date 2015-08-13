package com.unioeste.sd.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.unioeste.sd.ChatClient;
import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.implement.Message;
import com.unioeste.sd.misc.UserListCell;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class MainController implements Controller{
	@FXML
	private ListView<FacadeUser> userList;
	@FXML
	private TabPane chatPane;
	
	private ChatClient client;
	
	@FXML
	private void handleExitButtonAction(ActionEvent event){
		try {
			this.client.getChat().logout(this.client.getUser());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		Platform.exit();
	}

	public void initManager(final ChatClient chatClient) {
		this.client = chatClient;
		try {
			this.buildOnlineList(new ArrayList<FacadeUser>(Arrays.asList(this.client.getChat().getLoggedUsers())));
		} catch (RemoteException e) {
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
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		try {
			Message m = new Message();
			m.setMessage("Olá");
			client.getChat().sendBroadcastMessage(m);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
								
								try {
									Tab tab = FXMLLoader.load(getClass().getResource("../view/NewChat.fxml"));
									tab.setText(cell.getText());
									tab.setClosable(true);
									chatPane.getTabs().add(tab);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
				
				return cell;
			}
		});
	}
}
