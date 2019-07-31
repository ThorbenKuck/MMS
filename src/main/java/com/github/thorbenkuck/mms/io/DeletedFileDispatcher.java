package com.github.thorbenkuck.mms.io;

import com.github.thorbenkuck.mms.Dispatcher;

public interface DeletedFileDispatcher extends Dispatcher<DeletedFile> {

	@Override
	default Class<DeletedFile> type() {
		return DeletedFile.class;
	}
}
