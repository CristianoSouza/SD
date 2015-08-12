package com.unioeste.sd.facade;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacadeUser extends Remote{
	public String getName() throws RemoteException;
	public void setName(String name) throws RemoteException;
	public String getStatus() throws RemoteException;
	public void setStatus(String status) throws RemoteException;
	public InetAddress getIp() throws RemoteException;
	public void setIp(InetAddress inetAddress) throws RemoteException;
	public void receive(FacadeMessage message) throws RemoteException;
}
