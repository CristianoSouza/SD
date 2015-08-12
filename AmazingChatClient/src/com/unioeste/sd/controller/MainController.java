package com.unioeste.sd.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.unioeste.sd.ChatClient;
import com.unioeste.sd.exception.ChatException;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;
import com.unioeste.sd.infra.ChatConnector;
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
	private ChatConnector connector;
	
	public MainController() {
		this.connector = new ChatConnector();
	}
	
	@FXML
	private void handleExitButtonAction(ActionEvent event){
		Platform.exit();
	}

	public void initManager(final ChatClient chatClient) {
		this.client = chatClient;
		
		try {
			FacadeChat chat = connector.connect(this.client.getUser());
			this.client.setChat(chat);			
			
		} catch (ChatException e) {
			e.printStackTrace();
		}
		
		try {
			Collection<FacadeUser> loggedUsers = new ArrayList<FacadeUser>(Arrays.asList(this.client.getChat().getLoggedUsers()));
			ObservableList<FacadeUser> items = FXCollections.observableArrayList(loggedUsers);
			
			userList.setItems(items);
			userList.setCellFactory(new Callback<ListView<FacadeUser>, ListCell<FacadeUser>>() {
				@Override
				public ListCell<FacadeUser> call(ListView<FacadeUser> list) {
					UserListCell cell = new UserListCell();
					cell.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
						public void handle(MouseEvent event) {
							if(cell.getText() != null && event.getClickCount() > 1){
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
									tab.setClosable(true);
									chatPane.getTabs().add(tab);
								} catch (IOException e) {
									e.printStackTrace();
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
