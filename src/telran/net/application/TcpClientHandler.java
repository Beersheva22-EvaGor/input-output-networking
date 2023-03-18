package telran.net.application;

import telran.io.util.Handler;
import telran.io.util.Level;
import telran.io.util.LoggerRecord;
import java.io.*;
import java.net.*;

public class TcpClientHandler implements Handler {
	Socket socket; 
	PrintStream output; 
	BufferedReader input; 	
	
	public TcpClientHandler(Socket socket, PrintStream output, BufferedReader input) {
		this.socket = socket;
		this.output = output;
		this.input = input;
	}
	
	@Override
	public void publish(LoggerRecord record) {
		String response;
		try {
			output.printf("log#%s", toString(record));		
			response = input.readLine();
			System.out.println(response);
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void getNumLevels(Level level) {
		try {
		output.printf("counter#%s\r\n", level.name());	
		System.out.println(input.readLine());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String toString(LoggerRecord record) {
		return String.format("timestamp: %s, zoneId: %s, level: %s, loggerName: %s, message: %s\r\n", 
				record.timestamp.toString(), record.zoneId, record.level, record.loggerName, record.message);
	}
}
