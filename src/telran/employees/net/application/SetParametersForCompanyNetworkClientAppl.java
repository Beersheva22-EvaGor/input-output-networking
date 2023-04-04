package telran.employees.net.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SetParametersForCompanyNetworkClientAppl {

	private static final String FILE_NAME = "company.config";

	private static void setProperties(String filename) throws IOException {
		// create an instance of Properties
		Properties p = new Properties();

		// add properties to it
		p.setProperty("hostname", "localhost");
		p.setProperty("clientName", "Tcp");
		p.setProperty("port", "4000");
		p.setProperty("departments", "QA, Development, Audit, Management, Accounting");
		// store the properties to a file
		p.store(new FileWriter(filename), "Parameters for Tcp/Udp company client application");
	}

	public static void main(String[] args) throws IOException {
		File file = new File(FILE_NAME);
		file.createNewFile();
		
		setProperties(FILE_NAME);
	}
}
