package com.fan.service.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PooledExecutor implements Executor{
	public String name;
	public int minSize = 1;
	public int maxSize = 30;
	public int queueSize = 10;
	public int keepAliveTime = 300;
	public String blockedExecutionPolicy  = "run";
	public boolean alive;
	private int threadNumber;
	ThreadPoolExecutor executor;
	
	public synchronized void start() {
		if (alive) {
			return;
		}
		alive = true;
		BlockingQueue workQueue = null;
		if(queueSize > 0){
			workQueue = new ArrayBlockingQueue<>(queueSize);
		}else{
			workQueue = new SynchronousQueue();
		}
		ThreadFactory threadFactory = new ThreadFactory() {
			public Thread newThread(Runnable r) {
				return new Thread(r, (name == null) ? "ThreadPool" : name + "-" + threadNumber++);
			}
		};
		RejectedExecutionHandler handler = null;
		if("run".equals(blockedExecutionPolicy)){
			handler = new ThreadPoolExecutor.CallerRunsPolicy();
		}else if("abort".equals(blockedExecutionPolicy)){
			handler = new ThreadPoolExecutor.AbortPolicy();
		}else if("discard".equals(blockedExecutionPolicy)){
			handler = new ThreadPoolExecutor.DiscardPolicy();
		}else if("discardOldest".equals(blockedExecutionPolicy)){
			handler = new ThreadPoolExecutor.DiscardOldestPolicy();
		}
		executor = new ThreadPoolExecutor(minSize, maxSize, keepAliveTime, TimeUnit.SECONDS, workQueue, threadFactory, handler);
	}
	
	public void execute(Runnable r){
		if(executor == null){
			start();
		}
		executor.execute(r);
	}
}
