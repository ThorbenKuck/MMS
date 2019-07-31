package com.github.thorbenkuck.mms;

public interface DependencyRepository {

	void add(Object object);

	<T> T get(Class<T> type);

}
