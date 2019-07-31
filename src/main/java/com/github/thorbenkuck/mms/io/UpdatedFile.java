package com.github.thorbenkuck.mms.io;

import java.io.IOException;
import java.nio.file.Path;

public final class UpdatedFile {

	private final Path target;

	private UpdatedFile(Path target) {
		this.target = target;
	}

	public static UpdatedFile wrap(Path path) throws IOException {
		if(path.toFile().isDirectory()) {
			throw new IOException("Only files may be added");
		}

		return new UpdatedFile(path);
	}

	public Path getTarget() {
		return target;
	}

	public Path getParentFolder() {
		return target.getParent();
	}
}
