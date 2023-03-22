package telran.net.test;

import telran.net.TcpServer;

public class StartServer {

	public static void main(String[] args) throws Exception {
		TcpServer server = new TcpServer(new ProtocolCalculator(), TcpCalculatorTest.PORT);
		server.run();
		
	}

}
