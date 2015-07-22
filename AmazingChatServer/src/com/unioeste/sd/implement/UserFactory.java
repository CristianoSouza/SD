package com.unioeste.sd.implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unioeste.sd.facade.FacadeUserFactory;
import com.unioeste.sd.facade.FacadeUser;

public class UserFactory extends UnicastRemoteObject implements FacadeUserFactory {

	private static final long serialVersionUID = 1L;

	public UserFactory() throws RemoteException {
		super();
	}

	@Override
	public FacadeUser getUserInstance() throws RemoteException {
		return new User();
	}

}
