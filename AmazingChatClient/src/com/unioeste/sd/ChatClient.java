package com.unioeste.sd;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.unioeste.sd.controller.Controller;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatClient extends Application {
	private Stage stage;
	private Scene scene;
	private FacadeChat chat;
	private FacadeUser user;
	private List<FacadeUser> havingChat;
	private Date lastUpdate;
	private Date lastReceivedMessageDate;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.havingChat = new ArrayList<FacadeUser>();
		this.stage = primaryStage;
		
		primaryStage.setOnCloseRequest(new javafx.event.EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.out.println("Closing client ...");
				Platform.exit();
				try {chat.logout(user);} catch (RemoteException e) {}
				System.exit(0);
			}
		});
		
		this.scene = new Scene(new StackPane());
		
		this.showScreen("view/Login.fxml");
		
		stage.setScene(scene);
	    stage.show();
	}
	
	public void authenticated(String sessionID) {
		showScreen("view/Main.fxml");
	}

	public void logout() {
		showScreen("view/Login.fxml");
	}
	  
	public void showScreen(String resource) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
			scene.setRoot((Parent) loader.load());
			stage.sizeToScene();
			stage.centerOnScreen();
			Controller controller = loader.getController();
			controller.initManager(this);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public FacadeChat getChat() {
		return this.chat;
	}
	
	public void setChat(FacadeChat chat) {
		this.chat = chat;
	}
	
	public FacadeUser getUser() {
		return this.user;
	}
	
	public void setUser(FacadeUser user) {
		this.user = user;
	}
	
	public List<FacadeUser> getChatList(){
		return this.havingChat;
	}
	
	public Date getLastUpdate(){
		return this.lastUpdate;
	}
	
	public void setLastUpdate(Date lastUpdate){
		this.lastUpdate = lastUpdate;
	}
	
	public Date getLastReceivedMessageDate(){
		return this.lastReceivedMessageDate;
	}
	
	public void setLastReceivedMessageDate(Date date){
		this.lastReceivedMessageDate = date;
	}
	
	public static void main(String args[]) {
		launch(args);	
	}
}
