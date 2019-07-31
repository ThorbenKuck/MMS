package com.github.thorbenkuck.mms.io;

import java.io.IOException;
import java.nio.file.Path;

public final class DeletedFile {

	private final Path target;

	private DeletedFile(Path target) {
		this.target = target;
	}

	public static DeletedFile wrap(Path path) throws IOException {
		if(path.toFile().isDirectory()) {
			throw new IOException("Only files may be added");
		}

		return new DeletedFile(path);
	}

	public Path getTarget() {
		return target;
	}

	public Path getParentFolder() {
		return target.getParent();
	}
}
