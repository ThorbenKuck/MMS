package com.github.thorbenkuck.mms.io;

import java.io.IOException;
import java.nio.file.Path;

public final class NewFile {

	private final Path target;

	private NewFile(Path target) {
		this.target = target;
	}

	public static NewFile wrap(Path path) throws IOException {
		if(path.toFile().isDirectory()) {
			throw new IOException("Only files may be added");
		}

		return new NewFile(path);
	}

	public Path getTarget() {
		return target;
	}

	public Path getParentFolder() {
		return target.getParent();
	}
}
