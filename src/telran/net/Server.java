package telran.net;

import java.net.ServerSocket;

public abstract class Server implements Runnable {
	
	protected Protocol protocol;
	protected int port;
	
	public Server(Protocol protocol, int port) throws Exception{
		this.protocol = protocol;
		this.port = port;
	}
	@Override
	abstract public void run();
}
