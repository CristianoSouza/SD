package com.unioeste.sd.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacadeMessageFactory extends Remote {
	public FacadeMessage getMessageInstance() throws RemoteException;
}
