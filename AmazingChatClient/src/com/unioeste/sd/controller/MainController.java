package com.unioeste.sd.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.unioeste.sd.ChatClient;
import com.unioeste.sd.facade.FacadeUser;
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
		Platform.exit();
	}

	public void initManager(final ChatClient chatClient) {
		this.client = chatClient;
		
		try {
			Collection<FacadeUser> loggedUsers = new ArrayList<FacadeUser>(Arrays.asList(this.client.getChat().getLoggedUsers()));
			loggedUsers.remove(chatClient.getUser());
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
								for(FacadeUser user : chatClient.getChatList()){
									try {contains = user.getName().equals(cell.getItem().getName());} catch (RemoteException e) {}
									if(contains) break;
								}
								
								if(!contains && event.getClickCount() > 1){
									chatClient.getChatList().add(cell.getItem());
									
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
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
