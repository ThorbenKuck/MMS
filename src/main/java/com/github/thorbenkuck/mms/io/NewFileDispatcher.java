package com.github.thorbenkuck.mms.io;

import com.github.thorbenkuck.mms.Dispatcher;

public interface NewFileDispatcher extends Dispatcher<NewFile> {

	@Override
	default Class<NewFile> type() {
		return NewFile.class;
	}
}
