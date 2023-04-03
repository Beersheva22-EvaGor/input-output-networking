package telran.employees.net;

import java.io.*;
import java.util.*;
import telran.employees.*;
import telran.net.NetworkClient;

public class CompanyNetProxy implements ICompany, Closeable {

	NetworkClient client;
	private static final long serialVersionUID = 1L;
	public CompanyNetProxy(NetworkClient client) {
		this.client = client;
	}
	@Override
	public Iterator<Employee> iterator() {
		
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		
		return client.send("addEmployee", empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		
		return client.send("removeEmployee", id);
	}

	@Override
	public List<Employee> getAllEmployees() {
		
		return client.send("getAllEmployees", "");
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		
		return client.send("getEmployeesByMonthBirth", month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		
		return client.send("getEmployeesBySalary", new int[] {salaryFrom, salaryTo});
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

	

	@Override
	public void close() throws IOException {
		client.close();
		
	}

}
