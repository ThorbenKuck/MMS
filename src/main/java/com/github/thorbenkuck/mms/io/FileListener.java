package com.github.thorbenkuck.mms.io;

import com.github.thorbenkuck.mms.Dispatcher;
import com.github.thorbenkuck.mms.core.Registration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public interface FileListener {

	static FileListener open(String path) throws IOException {
		return open(Paths.get(path));
	}

	static FileListener open(Path path) throws IOException {
		NativeFileListener nativeFileListener = new NativeFileListener(path);
		nativeFileListener.verifyPath();

		return nativeFileListener;
	}

	Registration<Dispatcher> registration();

	void listen() throws IOException;

	void listen(Function<Runnable, Thread> provider) throws IOException;

	void listen(ExecutorService executorService) throws IOException;

	void shutdown();
}
