package telran.employees.apllication.controller;

import java.time.LocalDate;
import java.util.List;

import telran.employees.Employee;
import telran.employees.ICompany;
import telran.view.*;

public class AdminMenu {

	public static Item adminItemMenu(InputOutput io, ICompany company, List<String> departments) {
		return new Menu("Administrator actions", 
				new Item[] { 
						Item.of("Add employee", x -> addEmployee(io, company, departments)),
						Item.of("Remove employee", x -> removeEmployee(io, company)), 
//						Item.of("Update employee's salary", x -> updateSalary(io, company)), 
//						Item.of("Update employee's department", x -> updateDepartment(io, company)), 
						Item.exit() });
	}

//	private static Object updateSalary(InputOutput io, ICompany company) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	private static Object updateDepartment(InputOutput io, ICompany company) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	private static void addEmployee(InputOutput io, ICompany company, List<String> departments) {
		long id = io.readLong("Input employee's ID", "Wrong ID", 0, Long.MAX_VALUE);
		if (company.getEmployee(id) != null) {
			io.writeLine(String.format("Employee with ID %d already exists", id));
		} else {
			String name = io.readString("Input employee's name");
			LocalDate birthDate = io.readDate("Input date of birth of the employee", "Wrong date of birth", "yyyy-MM-dd", 
					LocalDate.now().minusYears(300), LocalDate.now().minusYears(13));
			int salary = io.readInt("Input employee's salary", "Wrong salary", 0, Integer.MAX_VALUE);
			String department = CompanyUtils.getCheckedDepartment(io, company, departments);
			
			Employee employee = new Employee(id, name, birthDate, department, salary);
			if (company.addEmployee(employee)) {
				io.writeLine(String.format("Employee with ID %d was added", id));
			} else {
				io.writeLine(String.format("Employee with ID %d was NOT added. Try again", id));
			}
		}
	}

	private static void removeEmployee(InputOutput io, ICompany company) {
		// can be realized as getEmployee.getEmployee (choosing of options), but here's
		// another way
		Long id = io.readLong("Input employee ID", "Wrong ID", 0, Long.MAX_VALUE);
		Employee employee = company.removeEmployee(id);
		io.writeLine(employee == null ? String.format("Employee with the ID %d not found", id)
				: CompanyUtils.employeeToString(employee) + " was removed");
	}
}
