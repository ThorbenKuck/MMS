package com.github.thorbenkuck.mms.core;

import com.github.thorbenkuck.mms.Dispatcher;

public interface Pipeline extends Registration<Dispatcher> {

	static Pipeline create() {
		return new NativePipeline();
	}

	void apply(Object object);

}
