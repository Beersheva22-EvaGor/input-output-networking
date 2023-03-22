package telran.net.application;

import java.util.HashMap;
import telran.io.util.Level;

import java.io.*;
import java.net.*;
public class ServerLogAppl {
static HashMap<String, Integer> logCounters = new HashMap<>();
public static final String OK = "ok";
public static final int PORT = 3000;
public static final String LOG_TYPE = "log";
public static final String COUNTER_TYPE = "counter";
private static final String WRONG_LEVEL = "Wrong Level in logger record";
	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Logger Server is listening on port " + PORT);
		while(true) {
			Socket socket = serverSocket.accept();
			runProtocol(socket);
		}

	}
	private static void runProtocol(Socket socket) {
		try {
			BufferedReader input = 
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream output = new PrintStream(socket.getOutputStream());
			while(true) {
				String request = input.readLine();
				if (request == null) {
					break;
				}
				String response = getResponse(request);
				output.println(response);
				
			}
		} catch (IOException e) {
			System.out.println("client has closed connection improperly");
		}
		
		
	}
	private static String getResponse(String request) {
		String[] tokens = request.split("#");
		String response = "";
		if (tokens.length != 2) {
			response = "Wrong request: should be <type>#<string>";
		} else {
			response = switch(tokens[0]) {
			case LOG_TYPE -> processLogRequest(tokens[1]);
			case COUNTER_TYPE -> processCounterRequest(tokens[1]); 
			default -> String.format("Wrong request type: should be either %s or %s", LOG_TYPE, COUNTER_TYPE);
			};
		}
//		
		return response;
	}
	private static String processCounterRequest(String requestData) {
		
		return "" + logCounters.getOrDefault(requestData.toUpperCase(), 0);
	}
	private static String processLogRequest(String requestData) {
		Level level = getLevel(requestData);
		String res = WRONG_LEVEL;
		if (level != null) {
			res = OK;
			logCounters.merge(level.toString(), 1, (a,b) -> a + b);
		}
		return res;
	}
	private static Level getLevel(String requestData) {
		Level levels[] = Level.values();
		int index = 0;
		while(index < levels.length && !requestData.contains(levels[index].toString())) {
			index++;
		}
		return index < levels.length ? levels[index] : null;
	}

}