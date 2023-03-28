
package telran.employees.net.application;

import telran.employees.Company;
import telran.employees.ICompany;
import telran.employees.net.CompanyProtocol;
import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyTcpApplication {
public static void main(String[] args) throws Exception {
	Company company = new Company();
	company.restore("company.data");
	Protocol protocol = new CompanyProtocol(company);
	TcpServer server = new TcpServer(protocol, 4000);
	server.run();
}
}