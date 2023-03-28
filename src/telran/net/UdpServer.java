package telran.net;

import java.net.*;
import static telran.net.UdpUtils.*;

public class UdpServer extends Server{

	private DatagramSocket socket;
	public UdpServer(Protocol protocol, int port) throws Exception {
		super(protocol, port);
		socket = new DatagramSocket(port);
	}


	@Override
	public void run() {
		System.out.println("UDP server running on port " + port);
		try {
			byte [] bufferReceive = new byte[MAX_BUFFER_LENGTH];
			byte [] bufferSend = null;
			while(true) {
				DatagramPacket packetReceive =
						new DatagramPacket(bufferReceive, MAX_BUFFER_LENGTH);
				socket.receive(packetReceive);
				Request request = (Request) toSerializable(packetReceive.getData(),
						packetReceive.getLength());
				Response response = protocol.getResponse(request);
				bufferSend = toBytesArray(response);
				DatagramPacket packetSend = new DatagramPacket(bufferSend, bufferSend.length,
						packetReceive.getAddress(), packetReceive.getPort());
				socket.send(packetSend);
			}
			
		} catch(Exception e) {
			throw new RuntimeException(e.toString());
		}

	}

}
