package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unioeste.sd.facade.ChatInterface;

public class Chat extends UnicastRemoteObject implements ChatInterface{

	private static final long serialVersionUID = 1L;

	protected Chat() throws RemoteException {
		super();
	}

}
