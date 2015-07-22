package com.unioeste.sd.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacadeUserFactory extends Remote{
	public FacadeUser getUserInstance() throws RemoteException;
}
