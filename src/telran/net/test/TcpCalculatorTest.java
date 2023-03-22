package telran.net.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.net.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TcpCalculatorTest {
	private static final String HOSTNAME = "localhost";
	public static final int PORT = 4000;
	static TcpClient client ;

	int a = 20;
	int b = 10;
	Operands operands = new Operands(a, b);

	@Order(1)
	@Test
	void sendRightTest() throws Exception {
		client = new TcpClient(HOSTNAME, PORT);
		Response response = client.send(Calculator.PLUS.name(), operands);
		assertEquals(a + b, response.data);
		assertEquals(ResponseCode.OK, response.code);
		response = client.send(Calculator.MINUS.name(), operands);
		assertEquals(a - b, response.data);
		assertEquals(ResponseCode.OK, response.code);
		response = client.send(Calculator.MULTIPLY.name(), operands);
		assertEquals(a * b, response.data);
		assertEquals(ResponseCode.OK, response.code);
	}
	
	@Test
	void sendWrongRequestTest() throws Exception{
		Response response = client.send("POW", operands);
		assertEquals(ResponseCode.WRONG_REQUEST, response.code);
	}
	
	@Test
	void sendWrongDataTest() throws Exception{
		Response response = client.send("DIVIDE", "smth");
		assertEquals(ResponseCode.WRONG_DATA, response.code);
	}
	
	@AfterAll
	static void closing() throws IOException {
		client.close();
	}

}
