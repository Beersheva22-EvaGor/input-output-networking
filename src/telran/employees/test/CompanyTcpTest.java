package telran.employees.test;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import telran.employees.net.CompanyNetProxy;
import telran.net.TcpClient;

public class CompanyTcpTest extends CompanyTest {
	static CompanyNetProxy netProxy;
	@BeforeAll
	static void createProxy() throws Exception {
		netProxy = new CompanyNetProxy(new TcpClient("localhost", 4000));
	}
@BeforeEach
@Override
void setUp() throws Exception{
	company = netProxy;
	company.forEach(e -> company.removeEmployee(e.getId()));
	super.setUp();
}
@AfterAll
static void closeConnection() throws IOException {
	
	netProxy.close();
}
}