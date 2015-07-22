package com.unioeste.sd.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
	@FXML
	private void handleExitButtonAction(ActionEvent event){
		Platform.exit();
	}
}
