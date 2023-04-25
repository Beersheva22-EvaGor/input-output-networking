package telran.net;

import java.io.*;
import java.net.*;

public class TcpClient implements NetworkClient {
	private static final int N_ATTEMPTION = 2;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	String hostname;
	int port; 

	public TcpClient(String hostname, int port) throws Exception {
		this.hostname = hostname;
		this.port = port;
		init();
	}

	private void init() throws Exception {
		socket = new Socket(hostname, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		socket.close();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String type, Serializable requestData) {
		T res = null;
		int attemption = 0;
		while (attemption < N_ATTEMPTION + 1) {
		Request request = new Request(type, requestData);
		try {
			output.writeObject(request);
			Response response = (Response) input.readObject();
			if (response.code != ResponseCode.OK) { 
				throw new Exception(response.data.toString());
			}
			res = (T) response.data;
			attemption = N_ATTEMPTION +1;
		} catch (SocketException e) {
			System.out.println("Socket was closed: " + e.getMessage());
			try {
				restart();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			attemption ++;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		}
		return res;

	}

	private void restart() throws Exception {
		socket.close();
		init();
	}


}