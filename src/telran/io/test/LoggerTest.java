package telran.io.test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import telran.io.util.Handler;
import telran.io.util.Level;
import telran.io.util.Logger;
import telran.io.util.SimpleStreamHandler;

class LoggerTest {
	String file = "LOG.txt";

	@Test
	void loggerTest() throws Exception {
		Path path = Path.of(file);
		Handler handler_concole = new SimpleStreamHandler(System.err);
		if (!Files.exists(path)) {
			Files.createFile(path);
		}

		try (PrintStream stream = new PrintStream(new FileOutputStream(file, false))) {
			Handler handler_file = new SimpleStreamHandler(stream);
			Logger LOG = new Logger(handler_file, "telran.Logger");
			LOG.addHandler(handler_concole);
			LOG.setLevel(Level.TRACE);
			LOG.error("ERROR_1");
			LOG.warn("WARN_1");
			LOG.info("INFO_1");
			LOG.debug("DEBUG_1");
			LOG.trace("TRACE_1");
		}
	}

}
