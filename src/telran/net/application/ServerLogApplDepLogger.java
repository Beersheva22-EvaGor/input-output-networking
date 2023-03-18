package telran.net.application;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import telran.io.util.*;

public class ServerLogApplDepLogger {

	private static int PORT = 4500;
	static HashMap<String, HashMap<String, Integer>> levels = new HashMap<>();

	private static HashMap<String, Integer> fillMap(){
		HashMap<String, Integer> l = new HashMap<>();
		for (Level level : Level.values()) {
			l.put(level.name(), 0);
		}
		return l;
	}

	public static void main(String[] args) {
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
		String loggerName = getArgument(input, "loggerName");
		String level = getArgument(input, "level");
		HashMap<String, Integer> levelsByLogger = levels.getOrDefault(loggerName, new HashMap<>());
		Integer counter = levelsByLogger.get(level);
		levelsByLogger.put(level, ++counter);
	}

	private static String getArgument(String input, String arg) {
		Matcher match = Pattern.compile("arg: (.*?),").matcher(input);
		match.find();
		return  match.group(1);
	}

}
