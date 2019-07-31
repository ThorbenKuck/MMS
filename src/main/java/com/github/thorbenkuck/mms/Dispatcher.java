package com.github.thorbenkuck.mms;

public interface Dispatcher<T> {

	void dispatch(T t);

	Class<T> type();

}
