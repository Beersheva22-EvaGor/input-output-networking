package telran.employees.apllication.controller;
import telran.employees.*;
import telran.view.*;

import java.time.LocalDate;
import java.util.*;
import static telran.view.Item.*;
public class CompanyControllerItems {
private static final int MIN_SALARY = 5000;
private static final int MAX_SALARY = 50000;
static private ICompany company;
static private HashSet<String> departments;
private CompanyControllerItems() {
	
}
static public Item[] getCompanyItems(ICompany company,
		String[] departments) {
	CompanyControllerItems.company = company;
	CompanyControllerItems.departments = new HashSet<>(Arrays.asList(departments));
	return getItems();
}
private static Item[] getItems() {
	
	return new Item[] {
		getAdminMenu(), getUserMenu() 	
	};
}
private static Item getUserMenu() {
	
	return new Menu("User actions", of("Display Employee Data",CompanyControllerItems::getEmployee),
			of("Display data of all employees",CompanyControllerItems::getAllEmployees),
			of("Display Employees by salary", CompanyControllerItems::getEmployeesBySalary),
			of("Display Employees by Month", CompanyControllerItems::getEmployeesByMonth),
			of("Display Employess from Department", CompanyControllerItems::getEmployeesByDepartment),
			exit());
}
private static Item getAdminMenu() {
	
	return new Menu("Admin actions",
			of("Add employee", CompanyControllerItems::addEmployee),
			of("Remove Employee", CompanyControllerItems::removeEmployee),
			exit());
}
private static Long getId(InputOutput io, boolean exist) {
	long id = io.readLong("Enter Employee ID", "Wrong ID", 1, Long.MAX_VALUE);
	Employee empl = company.getEmployee(id);
	return (empl == null && !exist) || (empl != null && exist) ? id : null;
}
private static String getDepartment(InputOutput io) {
	String department = io.readStringOptions("Enter Department " + departments, "Wrong Department", departments);
	return department;
}
private static void addEmployee(InputOutput io) {
	Long id = getId(io, false);
	if (id == null) {
		io.writeLine("Employee already exists");
	} else {
		String name = io.readString("Enter Employee's name");
		LocalDate birthDate = io.readDateISO("Enter birthdate", "Wrong birthdate");
		String department = getDepartment(io);
		int salary = getSalary(io);
		Employee empl = new Employee(id, name , birthDate , department , salary );
		io.writeLine(company.addEmployee(empl) ? "Employee added" : "Employee already exists");
	}
}
private static int getSalary(InputOutput io) {
	return io.readInt("Enter salary", "Wrong Salary", MIN_SALARY, MAX_SALARY);
}
private static void removeEmployee(InputOutput io) {
	Long id = getId(io, true);
	if (id == null) {
		io.writeLine("Employee doesn't exist");
	} else {
		Employee empl = company.removeEmployee(id);
		io.writeLine(empl != null ? "removed" : "not found");
	}
}
private static void getAllEmployees(InputOutput io) {
	displayResult(company.getAllEmployees(), io);
}
private static void getEmployee(InputOutput io) {
	Long id = getId(io, true);
	io.writeLine(id != null ? company.getEmployee(id) : "Employee doesn't exist");
	
}
private static void getEmployeesBySalary(InputOutput io) {
	int salaryMin = io.readInt("Enter Salary from", "Wrong salary", MIN_SALARY, MAX_SALARY);
	int salaryMax = io.readInt("Enter  salary to", "Wrong salary", salaryMin, MAX_SALARY);
	displayResult(company.getEmployeesBySalary(salaryMin, salaryMax), io);
}
private static void displayResult(List<Employee> employees, InputOutput io) {
	employees.forEach(io::writeLine);
	
}
private static void getEmployeesByMonth(InputOutput io) {
	int month = io.readInt("Enter month number", "Wrong month number", 1, 12);
	
	displayResult(company.getEmployeesByMonthBirth(month), io);
}
private static void getEmployeesByDepartment(InputOutput io) {
	String department = getDepartment(io);
	
	displayResult(company.getEmployeesByDepartment(department),io);
}

}