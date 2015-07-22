package com.unioeste.sd;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.unioeste.sd.facade.ChatInterface;
import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

public class ChatClient extends Application{
	private Stage primaryStage;
    private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ChatClient.class.getResource("view/gui.fxml"));
        rootLayout = (BorderPane) loader.load();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();		
	}
	
	public static void main(String args[]) {
//		int port = 1099;
//		String host = "localhost";
//		String chat_address = "rmi://localhost:" + port + "/ChatService";
//		String user_address = "rmi://localhost:" + port + "/UserService";
//		String message_address = "rmi://localhost:" + port + "/MessageService";
//		
//		try{
//			Registry registry = LocateRegistry.getRegistry(host);
//			ChatInterface chat = (ChatInterface) registry.lookup(chat_address);
//			UserInterface user = (UserInterface) registry.lookup(user_address);
//			MessageInterface message = (MessageInterface) registry.lookup(message_address);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		launch(args);
	}
}
