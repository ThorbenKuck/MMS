package com.github.thorbenkuck.mms.core;

import com.github.thorbenkuck.mms.Dispatcher;

import java.util.ArrayDeque;
import java.util.Deque;

final class NativePipeline implements Pipeline {

	private final Deque<Dispatcher> core = new ArrayDeque<>();

	private Deque<Dispatcher> copyCore() {
		synchronized (core) {
			return new ArrayDeque<>(core);
		}
	}

	@Override
	public void addFirst(Dispatcher dispatcher) {
		synchronized (core) {
			core.addFirst(dispatcher);
		}
	}

	@Override
	public void addLast(Dispatcher dispatcher) {
		synchronized (core) {
			core.addLast(dispatcher);
		}
	}

	@Override
	public void apply(Object object) {
		Deque<Dispatcher> copy = copyCore();
		Class<?> type = object.getClass();

		copy.stream()
				.filter(dispatcher -> dispatcher.type() != null)
				.filter(dispatcher -> type.equals(dispatcher.type()))
				.forEachOrdered(dispatcher -> dispatcher.dispatch(object));
	}
}
