package com.github.thorbenkuck.mms.io;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class IOExecutorService {

	private static ExecutorService executorService;

	static ExecutorService access() {
		verify();
		return executorService;
	}

	private static void verify() {
		if(executorService == null) {
			synchronized (IOExecutorService.class) {
				// sanity check to compensate
				// race conditions
				if (executorService == null) {
					executorService = Executors.newCachedThreadPool();
				}
			}
		}
	}

}
