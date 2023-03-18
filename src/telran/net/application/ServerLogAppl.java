package telran.net.application;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import telran.io.util.*;

public class ServerLogAppl {

	private static int PORT = 4500;
	static HashMap<String, Integer> levels = new HashMap<>();

	private static void fillMap(){
		for (Level level : Level.values()) {
			levels.put(level.name(), 0);
		}
	}

	public static void main(String[] args) {
		fillMap();
		try (ServerSocket serverSocket = new ServerSocket(PORT);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine;
			String outputLine = "";

			while ((inputLine = in.readLine()) != null) {
				outputLine = logProtocol(inputLine);
				out.println(outputLine);
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	private static String logProtocol(String input) {
		if (input.startsWith("log")) {
			increaseLevelCounter(input);
			return "OK";
		} else {
			String level = input.replaceAll("counter#", "");
			return String.format("Number of logs on level %s is %d", level, levels.get(level));
		}
	}

	private static void increaseLevelCounter(String input) {
		Matcher match = Pattern.compile("level: (.*?),").matcher(input);
		match.find();
		String level = match.group(1);
		Integer counter = levels.get(level);
		levels.put(level, ++counter);
	}

}
