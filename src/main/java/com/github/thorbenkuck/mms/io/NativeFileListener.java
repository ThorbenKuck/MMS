package com.github.thorbenkuck.mms.io;

import com.github.thorbenkuck.mms.Dispatcher;
import com.github.thorbenkuck.mms.core.Pipeline;
import com.github.thorbenkuck.mms.core.Registration;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.nio.file.StandardWatchEventKinds.*;

final class NativeFileListener implements FileListener {

	private final Path parentFolder;
	private final WatchService watchService;
	private final Pipeline actions = Pipeline.create();
	private MainLoop mainLoop;
	private WatchKey watchKey;

	NativeFileListener(Path parentFolder) throws IOException {
		this.parentFolder = parentFolder;

		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			throw new IOException("Could not set up a WatchService for the FileSystem!", e);
		}
	}

	void verifyPath() throws IOException {
		File target = parentFolder.toFile();
		if(!target.isDirectory()) {
			throw new IOException("Only folders may be watched for changes!");
		}

		if(!target.canRead()) {
			throw new IOException("Read rights are required to watch a directory!");
		}
	}

	private WatchKey register() throws IOException {
		return parentFolder.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	}

	@Override
	public Registration<Dispatcher> registration() {
		return actions;
	}

	@Override
	public void listen() throws IOException {
		listen(IOExecutorService.access());
	}

	@Override
	public synchronized void listen(Function<Runnable, Thread> provider) throws IOException {
		if(mainLoop != null) {
			throw new IOException("Already listening");
		}
		watchKey = register();
		mainLoop = new MainLoop(watchService);
		Thread thread = provider.apply(mainLoop);
		thread.start();
	}

	@Override
	public synchronized void listen(ExecutorService executorService) throws IOException {
		if(mainLoop != null) {
			throw new IOException("Already listening");
		}
		watchKey = register();
		mainLoop = new MainLoop(watchService);
		executorService.execute(mainLoop);
	}

	@Override
	public synchronized void shutdown() {
		if(mainLoop != null) {
			mainLoop.stop();
			mainLoop = null;
		}

		if(watchKey != null){
			watchKey.cancel();
		}
	}

	private class MainLoop implements Runnable {

		private final WatchService watchservice;
		private boolean running = true;

		private MainLoop(WatchService watchservice) {
			this.watchservice = watchservice;
		}

		private synchronized boolean isRunning() {
			return running;
		}

		private synchronized void stop() {
			running = false;
		}

		@Override
		public void run() {
			while(isRunning()) {
				WatchKey watchKey;
				try {
					watchKey = watchservice.take();
				} catch (InterruptedException e) {
					stop();
					continue;
				}

				for(WatchEvent<?> event : watchKey.pollEvents()) {
					if(event.kind() == OVERFLOW) {
						continue;
					}

					WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
					Path target = parentFolder.resolve(watchEvent.context());

					Object pipelineEvent = null;

					if(event.kind() == ENTRY_CREATE) {
						try {
							pipelineEvent = NewFile.wrap(target);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(event.kind() == ENTRY_MODIFY) {
						try {
							pipelineEvent = UpdatedFile.wrap(target);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(event.kind() == ENTRY_DELETE) {
						try {
							pipelineEvent = DeletedFile.wrap(target);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if(pipelineEvent != null) {
						actions.apply(pipelineEvent);
					}
				}

				boolean valid = watchKey.reset();
				if(!valid) {
					stop();
				}
			}
		}
	}
}
