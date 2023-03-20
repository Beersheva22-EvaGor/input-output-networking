package telran.net.application;

import telran.io.util.Handler;
import telran.io.util.LoggerRecord;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;


public class TcpClientHandler implements Handler {
	Socket socket; 
	PrintStream output; 
	BufferedReader input; 	
	Consumer<String> publishResponse = s -> System.out.println(s);;
	
	public TcpClientHandler(Socket socket, PrintStream output, BufferedReader input) {
		this.socket = socket;
		this.output = output;
		this.input = input; 
	}
	
	@Override
	public void publish(LoggerRecord record) {
			output.printf("log#%s", toString(record));		
			String response;
			try {
				response = input.readLine();
				publishResponse.accept(response);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
	}
	
	@Override
	public void close() {
			try {
				socket.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
	}
	
	private String toString(LoggerRecord record) {
		return String.format("timestamp: %s, zoneId: %s, level: %s, loggerName: %s, message: %s\r\n", 
				record.timestamp.toString(), record.zoneId, record.level, record.loggerName, record.message);
	}
}