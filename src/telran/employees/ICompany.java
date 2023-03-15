package telran.employees;

import java.io.Serializable;
import java.util.List;

public interface ICompany extends Iterable<Employee>, Serializable{
	boolean addEmployee(Employee employee);	//if already exists - returns false
	Employee removeEmployee(long id);		// returns reference to removed Employee or null if there's no such Employee
	List<Employee> getAllEmployees();
	List<Employee> getEmployeesByMonth(int month);	// all employees who were born in a given month
	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);	//employees with salary in a given range
	List<Employee> getEmployeesByDepartment(String department);
	Employee getEmployee(long id);
	void save(String pathName);		// save all employee objects
	void restore(String pathName);	// restore
}
