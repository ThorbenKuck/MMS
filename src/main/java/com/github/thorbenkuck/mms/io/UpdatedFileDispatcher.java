package com.github.thorbenkuck.mms.io;

import com.github.thorbenkuck.mms.Dispatcher;

public interface UpdatedFileDispatcher extends Dispatcher<UpdatedFile> {

	@Override
	default Class<UpdatedFile> type() {
		return UpdatedFile.class;
	}
}
