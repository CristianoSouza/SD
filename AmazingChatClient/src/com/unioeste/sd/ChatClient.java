package com.unioeste.sd;

import java.io.IOException;

import com.unioeste.sd.controller.Controller;
import com.unioeste.sd.controller.LoginController;
import com.unioeste.sd.controller.MainController;
import com.unioeste.sd.facade.FacadeChat;
import com.unioeste.sd.facade.FacadeUser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ChatClient extends Application {
	private Stage stage;
	private Scene scene;
	private FacadeChat chat;
	private FacadeUser user;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		this.scene = new Scene(new StackPane());
		
		this.showScreen("view/Login.fxml", new LoginController());
		
		stage.setScene(scene);
	    stage.show();
	}
	
	public void authenticated(String sessionID) {
		showMainView();
	}

	public void logout() {
		showScreen("view/Login.fxml", new LoginController());
	}
	  
	public void showScreen(String resource, Controller controller) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Login.fxml"));
			scene.setRoot((Parent) loader.load());
			stage.sizeToScene();
			stage.centerOnScreen();
			LoginController controller = loader.<LoginController>getController();
			controller.initManager(this);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	private void showMainView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Main.fxml"));
			scene.setRoot((Parent) loader.load());
			stage.sizeToScene();
			stage.centerOnScreen();
			MainController controller = loader.<MainController>getController();
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
	
	public static void main(String args[]) {
		launch(args);	
	}
}
