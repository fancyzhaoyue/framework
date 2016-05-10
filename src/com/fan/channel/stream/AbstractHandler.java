package com.fan.channel.stream;

import com.fan.service.common.Handler;

public abstract class AbstractHandler implements Handler{

	public final void handler() {
		
	}
	public abstract byte[] readStream();
}
