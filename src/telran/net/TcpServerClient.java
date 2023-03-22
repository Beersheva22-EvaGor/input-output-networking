package telran.net;

import java.io.*;
import java.net.*;

import javax.management.RuntimeErrorException;

public class TcpServerClient implements Runnable {

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Protocol protocol;

	public TcpServerClient(Socket socket, Protocol protocol) throws IOException {
		this.socket = socket;
		this.protocol = protocol;
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		while (true) {
			try {
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.writeObject(response);
			} catch (EOFException e) {			
				// Implement Logging 
				System.out.println("client closed connection");
				throw new RuntimeException("client closed connection");
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new RuntimeException(e.toString());
			}
		}
		
	}

}
