package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeMessageFactory;

public class MessageFactory extends UnicastRemoteObject implements FacadeMessageFactory {
	private static final long serialVersionUID = 1L;

	public MessageFactory() throws RemoteException {
		super();
	}

	@Override
	public FacadeMessage getMessageInstance() throws RemoteException {
		return new Message();
	}

}
