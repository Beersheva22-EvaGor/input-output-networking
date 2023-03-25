package telran.net.company;

import telran.net.*;

public class RunUdpServer implements RunUtil {

	public static void main(String[] args) throws Exception {
		Server server = new UdpServer(new CompanyProtocol(), PORT);
		server.run();
	}
}
