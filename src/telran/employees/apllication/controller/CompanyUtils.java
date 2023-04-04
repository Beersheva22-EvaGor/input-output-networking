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

	public static String getCheckedDepartment(InputOutput io, ICompany company, List<String> departments) {
		String nextLine = "\r\n";
		String[] departmentsString = { "" };
		int[] i = { 0 };
		HashMap<Integer, String> depsMap = new HashMap<>();
		departments.forEach(d -> {
			i[0]++;
			departmentsString[0] += i[0] + " - " + d + nextLine;
		});
		departmentsString[0] = departmentsString[0].substring(0, departmentsString[0].length() - nextLine.length() - 1);
		return depsMap.get(io.readInt("Input number of department: \r\n" + departmentsString[0], "Wrong department", 1,
				departments.size()));
	}

}
