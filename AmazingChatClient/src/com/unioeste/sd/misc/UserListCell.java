package com.unioeste.sd.misc;

import java.rmi.RemoteException;

import com.unioeste.sd.facade.FacadeUser;

import javafx.scene.control.ListCell;

public class UserListCell extends ListCell<FacadeUser> {
	@Override
	public void updateItem(FacadeUser user, boolean empty) {
		super.updateItem(user, empty);
		
		if(empty) {
			setText(null);
			setGraphic(null);
		} else {
			try {setText(user.getName());} catch (RemoteException e) {e.printStackTrace();}
			setGraphic(null);
		}
	}
}
