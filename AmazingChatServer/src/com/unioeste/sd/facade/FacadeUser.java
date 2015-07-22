package com.unioeste.sd.facade;

import java.net.Inet4Address;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacadeUser extends Remote{
	public String getName() throws RemoteException;
	public void setName(String name) throws RemoteException;
	public String getStatus() throws RemoteException;
	public void setStatus(String status) throws RemoteException;
	public Inet4Address getIp() throws RemoteException;
	public void setIp(Inet4Address ip) throws RemoteException;
	public void receive(FacadeMessage message) throws RemoteException;
}
