package telran.net.company;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import telran.employees.*;
import telran.net.*;

public class CompanyClientAdapter implements ICompany, RunUtil{
	private static final long serialVersionUID = 1L;

	static NetworkClient client;
	
	public CompanyClientAdapter(ConnectionType type) throws Exception {
		if (type == ConnectionType.TCP) {
			client = new TcpClient(HOSTNAME, PORT);
		} else {
			client = new UdpClient(HOSTNAME, PORT);
		}
	}
	
	public void close() throws IOException {
		client.close();
	}
	
	@Override
	public Iterator<Employee> iterator() {
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		return client.send("addEmployee", employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		return client.send("removeEmployee", id);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return client.send("getAllEmployees", null);
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		return client.send("getEmployeesByMonthBirth", month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return client.send("getEmployeesBySalary", String.format("%d, %d", salaryFrom, salaryTo));
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return client.send("getEmployeesByDepartment", department);
	}

	@Override
	public Employee getEmployee(long id) {
		return client.send("getEmployee", id);
	}

	@Override
	public void save(String pathName) {
		client.send("save", pathName);
		
	}

	@Override
	public void restore(String pathName) {
		client.send("restore", pathName);		
	}

}
