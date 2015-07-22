package com.unioeste.sd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClient extends Application{
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ChatClient.class.getResource("view/Login.fxml"));
        rootLayout = (BorderPane) loader.load();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Amazing chat - Login");
        primaryStage.show();		
	}
	
	public static void main(String args[]) {
		launch(args);	
	}
}
