package com.fan.service.common;

public interface Server {
	
	public void start();
	public void restart();
	public void shutdown();
	public boolean isAlive();
	
}
