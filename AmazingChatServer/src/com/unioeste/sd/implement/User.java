package com.unioeste.sd.implement;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unioeste.sd.facade.FacadeMessage;
import com.unioeste.sd.facade.FacadeUser;

public class User extends UnicastRemoteObject implements FacadeUser {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String status;
	private InetAddress ip; 

	public User() throws RemoteException {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public InetAddress getIp() {
		return ip;
	}

	@Override
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	@Override
	public void receive(FacadeMessage message) throws RemoteException {
		System.out.println(message.getUser() + ": " + message.getMessage());
	}
	
	@Override
	public String toString() {
		return name;		
	}
	
	public boolean equals(Object user){
		System.out.println("comparing objects");
		return this.name.equals(((User)user).getName());
	}
}
