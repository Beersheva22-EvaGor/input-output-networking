package telran.employees.net.application;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

import telran.employees.apllication.controller.CompanyControllerItems;
import telran.employees.apllication.controller.graphics.Frame;
import telran.employees.net.CompanyNetProxy;
import telran.net.NetworkClient;
import telran.view.InputOutput;


public class CompanyNetworkClientApplication {
	private static final String BASE_PACKAGE = "telran.net";
	static InputOutput IO = new Frame("Employees' menu");
	static String hostname;
	static String clientName;
	static int port;
	static List<String> departments;
	
	private static String[] clientsName= new String[] {"Tcp", "Udp"};
	
	public static void main(String[] args) throws Exception {		
		if (args.length == 0) {
			IO.writeString("Must be one argument (path to config file)");
		} else {
	        try (FileReader reader = new FileReader(args[0])){
	        	Properties p = new Properties();
	        	p.load(reader);
	        	getParameters(p);	        	
	        	
	        	NetworkClient client = createClient(clientName, hostname, port);
	        	
	        	CompanyNetProxy companyNetProxy = new CompanyNetProxy(client);
	    		CompanyControllerItems.mainMenu(IO, companyNetProxy, departments).perform(IO);
	    		IO.writeLine("Thank you and goodbuy!");
	    		companyNetProxy.close();
 	        } catch(FileNotFoundException e) {
	        	IO.writeLine("Config file not found. Check existance and path");
	        }
	        catch (IllegalArgumentException e) {
	        	IO.writeLine(e.getMessage());
	        }
	        
		}

	}

	private static NetworkClient createClient(String clientName, String hostname, int port) throws Exception {
		@SuppressWarnings("unchecked")
		Class<NetworkClient> client = (Class<NetworkClient>) Class.forName(BASE_PACKAGE +"." + clientName);
		Constructor<NetworkClient> constructor = client.getConstructor(String.class, int.class);
		return constructor.newInstance(hostname, port);
	}
	
	private static void getParameters(Properties p)  {
		hostname = p.getProperty("hostname", "localhost");
		
		clientName = p.getProperty("clientName");
		if (Arrays.stream(clientsName).anyMatch(n -> clientName == n)) {
			throw new IllegalArgumentException("Client's name is wrong");
		}
		clientName += "Client";
		
		port = Integer.parseInt(p.getProperty("port"));
		departments = Arrays.stream(p.getProperty("departments", "dep1").split(",")).toList();
		departments.forEach(d -> d.trim());
	}

}
