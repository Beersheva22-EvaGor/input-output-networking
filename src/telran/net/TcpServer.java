package telran.net;

import java.net.*;

public class TcpServer extends Server {

	private ServerSocket serverSocket;
	
	public TcpServer(Protocol protocol, int port) throws Exception {
		super(protocol, port);
		serverSocket = new ServerSocket(port);
	}


	@Override
	public void run() {
		System.out.println("Tcp server listening on port " + this.port);
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				TcpServerClient serverClient = new TcpServerClient(socket, protocol);
				serverClient.run();

			} catch (Exception e) {
				break;
			}
		}
	}

}
