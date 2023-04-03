package telran.employees.apllication.controller;

import telran.employees.Employee;
import telran.employees.ICompany;
import telran.view.InputOutput;

import java.util.*;

public class CompanyUtils {

	public static String employeeToString(Employee e) {
		return String.format("Employee: id = %d, name = %s, date of birth = %s, department = %s, salary = %d",
				e.getId(), e.getName(), e.getBirthDate().toString(), e.getDepartment(), e.getSalary());
	}
	public static String getCheckedDepartment(InputOutput io, ICompany company) {
		String nextLine = "\r\n";
		String[] departmentsString = { "" };
		HashMap<Integer, String> depsMap = new HashMap<>();
		Arrays.stream(Departments.values()).forEach(d -> {
			int key = d.getKey();
			String value = d.getValue();
			depsMap.put(key, value);
			departmentsString[0] += key + " - " + value + nextLine;
		});
		departmentsString[0] = departmentsString[0].substring(0,departmentsString[0].length() - nextLine.length() - 1) ;
		return  depsMap.get(io.readInt("Input number of department: \r\n" + departmentsString[0],
				"Wrong department", 1, Departments.values().length));
	}

}
