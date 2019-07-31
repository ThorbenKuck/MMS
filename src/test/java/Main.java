import com.github.thorbenkuck.mms.Dispatcher;
import com.github.thorbenkuck.mms.core.Registration;
import com.github.thorbenkuck.mms.io.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException {
		File file = new File("test");
		if(!file.exists() || !file.isDirectory()) {
			if(!file.mkdirs()) {
				throw new IllegalStateException("Could not create the folder!");
			}
		}
		FileListener fileListener = FileListener.open(Paths.get(file.toURI()));

		Registration<Dispatcher> registration = fileListener.registration();

		registration.addFirst((NewFileDispatcher) newFile -> System.out.println("New File found: " + newFile.getTarget()));
		registration.addFirst((UpdatedFileDispatcher) newFile -> System.out.println("Updated File found: " + newFile.getTarget()));
		registration.addFirst((DeletedFileDispatcher) newFile -> System.out.println("Deleted File found: " + newFile.getTarget()));

		fileListener.listen();
	}

}
