package telran.employees.net.application;

import telran.employees.ICompany;
import java.util.Scanner;

import telran.employees.Company;
import telran.employees.net.CompanyProtocol;
import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyTcpApplication {
	private static final String FILE_NAME = "company.data";

	public static void main(String[] args) throws Exception {
		ICompany company = new Company();
		company.restore(FILE_NAME);
		Protocol protocol = new CompanyProtocol(company);
		TcpServer server = new TcpServer(protocol, 4000);
		Thread thread = new Thread(server);
		thread.start();
		Scanner scanner = new Scanner(System.in);
		
		boolean running = true;
		while (running) {
			System.out.println("To exit from the application input 'shutdown'");
			String line = scanner.nextLine();
			if (line.equals("shutdown")) { 
				server.shutdown();
				thread.join();
				company.save(FILE_NAME);
				System.out.println("Closing TCP application");
				running = false;
			}
		}
	}

}