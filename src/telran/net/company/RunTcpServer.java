package telran.net.company;
import telran.net.*;

public class RunTcpServer implements RunUtil{

	public static void main(String[] args) throws Exception {
		Server server = new TcpServer(new CompanyProtocol(), PORT);
		server.run();
	}

}
