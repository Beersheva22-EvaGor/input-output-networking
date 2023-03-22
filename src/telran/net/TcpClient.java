package telran.net;

import java.io.*;
import java.net.*;

public class TcpClient implements NetworkClient {

	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public TcpClient(String hostname, int port) throws Exception {
		socket = new Socket(hostname, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public <T> T send(String type, Serializable requestData) {
		T responseObj = null;
		try {
			output.writeObject(new Request(type, requestData));
			responseObj = (T)input.readObject();
						
		} catch (Exception e) {
			new RuntimeException(e.getMessage());
		}
		
		return responseObj;
	}
	
	
}
