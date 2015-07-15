package com.unioeste.sd;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Chat extends UnicastRemoteObject implements ChatInterface{

	private static final long serialVersionUID = 1L;

	protected Chat() throws RemoteException {
		super();
	}

}
