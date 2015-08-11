package com.unioeste.sd.controller;

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
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MainController implements Controller{
	@FXML
	private ListView<FacadeUser> userList;
	
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
				public ListCell<FacadeUser> call(ListView<FacadeUser> param) {
					return new UserListCell();
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
