package telran.employees.apllication.controller;

import java.util.*;
import telran.employees.*;
import telran.view.*;

public class UserMenu {

	
	public static Item userItemMenu(InputOutput io, ICompany company, List<String> departments) {
		return new Menu("User actions",
				new Item[] { Item.of("Get all employees", x -> getAllEmployees(io, company)),
						Item.of("Get employees by month birth", x -> getEmployeesByMonthBirth(io, company)),
						Item.of("Get employees by salary", x -> getEmployeesBySalary(io, company)),
						Item.of("Get employees by department", x -> getEmployeesByDepartment(io, company, departments)),
						Item.of("Get employee by ID", x -> getEmployee(io, company)), Item.exit() });
	}

	private static void displayEmployeesFromList(InputOutput io, List<Employee> list) {
		if (list.isEmpty()) {
			io.writeLine("No employee was found");
		} else {
			list.forEach(e -> io.writeLine(CompanyUtils.employeeToString(e)));
		}
	}

	private static void getAllEmployees(InputOutput io, ICompany company) {
		displayEmployeesFromList(io, company.getAllEmployees());
	}

	private static void getEmployeesByMonthBirth(InputOutput io, ICompany company) {
		int month = io.readInt("Input month of birth", "Wrong month", 1, 12);
		displayEmployeesFromList(io, company.getEmployeesByMonthBirth(month));
	}

	private static void getEmployeesBySalary(InputOutput io, ICompany company) {
		int salaryFrom = io.readInt("Input lower border of salary", "Wrong salary", 0, Integer.MAX_VALUE);
		int salaryTo = io.readInt("Input higher border of salary", "Wrong salary", 0, Integer.MAX_VALUE);
		displayEmployeesFromList(io, company.getEmployeesBySalary(salaryFrom, salaryTo));
	}

	private static void getEmployeesByDepartment(InputOutput io, ICompany company, List<String> departments) {
		String department = CompanyUtils.getCheckedDepartment(io, company, departments);
		displayEmployeesFromList(io, company.getEmployeesByDepartment(department));
	}

	private static void getEmployee(InputOutput io, ICompany company) {
		HashSet<String> ids = new HashSet<>();

		company.getAllEmployees().forEach(e -> ids.add(String.valueOf(e.getId())));
		ids.add("exit");
		String res = io.readStringOptions("Input employee's ID or type 'exit'", "Wrong ID", ids);
		if (!res.toLowerCase().contains("exit")) {
			long id = Long.parseLong(res);
			io.writeLine(CompanyUtils.employeeToString(company.getEmployee(id)));
		} 
	}
}
