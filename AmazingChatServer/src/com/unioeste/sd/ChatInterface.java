package com.unioeste.sd;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote{
	public void addUser(String message) throws RemoteException;
	
}
