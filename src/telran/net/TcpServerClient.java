package telran.net;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.management.RuntimeErrorException;

public class TcpServerClient implements Runnable {

	public static final String MSG_SHUTDOWN = "shutdown has been raised";
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Protocol protocol;
	private static final int TIMEOUT = 5_000;
	private static final int TIMEOUT_QUEUE = 1;
	Instant start = Instant.now();

	public TcpServerClient(Socket socket, Protocol protocol) throws IOException {
		this.socket = socket;
		socket.setSoTimeout(TIMEOUT);
		this.protocol = protocol;
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());

	}

	@Override
	public void run() {
		boolean running = true;
		while (running && !TcpServer.isFlShutdown()) {
			try {
				if (checkNeedsClientToBeRestarted(start)) {
					break;
				}
				
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.reset();
				output.writeObject(response);

			} catch (EOFException e) {
				System.out.println("client closed connection");
				running = false;
			} catch (SocketTimeoutException s) {
				if (TcpServer.isFlShutdown()) {
					running = false;
				}
			} catch (Exception e) {
				running = false;
			}
		}
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkNeedsClientToBeRestarted(Instant start) throws SocketException {
		boolean res = false;
		if (ChronoUnit.SECONDS.between(start, Instant.now()) > TIMEOUT_QUEUE) {
			if (TcpServer.getQueue() > 0) {
				res = true;
			}
			start = Instant.now();
		}
		return res;
	}

}
