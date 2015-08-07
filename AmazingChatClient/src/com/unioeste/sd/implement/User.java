package com.unioeste.sd.implement;

import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unioeste.sd.facade.MessageInterface;
import com.unioeste.sd.facade.UserInterface;

public class User extends UnicastRemoteObject implements UserInterface {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String status;
	private Inet4Address ip; 

	public User(String name) throws RemoteException {
        super();
        this.name = name;
    }
	
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
	public Inet4Address getIp() {
		return ip;
	}

	@Override
	public void setIp(Inet4Address ip) {
		this.ip = ip;
	}

	@Override
	public void receive(MessageInterface message) throws RemoteException {
		System.out.println(message.getUser() + ": " + message.getMessage());
	}
}
