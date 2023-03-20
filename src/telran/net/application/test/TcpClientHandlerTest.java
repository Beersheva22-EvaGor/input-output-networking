package telran.net.application.test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.io.BufferedReader;
import java.io.IOException;
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
	static Socket socket;
	static BufferedReader in;
	static PrintStream out;
	static Logger logger;
	static Logger logger1;

	@BeforeAll
	static void setUp() throws Exception {
		socket = new Socket(HOSTNAME, PORT);
		out = new PrintStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		client = new TcpClientHandler(socket, out, in);
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
	void getLevels() throws IOException {
		for (Level level : Level.values()) {
			out.printf("counter#%s\r\n", level.name());
			System.out.println(in.readLine());
		}	
		
		out.printf("counter#%s\r\n", "trace");
		System.out.println(in.readLine());			
	}
	

	@AfterAll
	public static void cleanUp() throws IOException {
		client.close();
		System.out.println("Client's closed");
	}
}
