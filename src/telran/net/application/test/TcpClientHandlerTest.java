package telran.net.application.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

import org.junit.jupiter.api.*;
import telran.io.util.*;
import telran.net.application.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TcpClientHandlerTest {
	private static final int PORT = 4500;
	private static final String HOSTNAME = "localhost";
	static TcpClientHandler client;
	static Logger logger;
	static Logger logger1;

	@BeforeAll
	static void setUp() throws Exception {
		Socket socket = new Socket(HOSTNAME, PORT);
		PrintStream out = new PrintStream(socket.getOutputStream());
		client = new TcpClientHandler(socket, out, new BufferedReader(new InputStreamReader(socket.getInputStream())));
		logger = new Logger(client, "test-logger");
		logger.setLevel(Level.TRACE);	
		logger1 = new Logger(client, "test-logger1");
		logger1.setLevel(Level.TRACE);	
	}	

	private void logging() {
		logger.error("error message");
		logger.warn("warn message");
		logger.info("info message");
		logger.debug("debug message");
		logger.trace("trace message");
	}	
	
	private void logging1() {
		logger1.error("error message");
		logger1.warn("warn message");
		logger1.info("info message");
		logger1.debug("debug message");
		logger1.trace("trace message");
	}	

	@Test
	@Order(1)
	void sendLogs() {
		logging();
		logging1();
		logging1();
	}

	@Test
	void getLevels() {
		for (Level level : Level.values()) {
			client.getNumLevels(level);
		}		
	}

	@AfterAll
	public static void cleanUp() {
		client.close();
		System.out.println("Client's closed");
	}
}
