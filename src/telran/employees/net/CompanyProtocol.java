package telran.employees.net;

import java.io.Serializable;
import java.lang.reflect.Method;

import telran.employees.Company;
import telran.employees.Employee;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol {
	Company company;

	@Override
	public Response getResponse(Request request) {
		Method method;
		try {
			try {
				method = CompanyProtocol.class.getDeclaredMethod(request.type, Serializable.class );
			} catch (Exception e) {
				return new Response(ResponseCode.WRONG_REQUEST, request.type + " Request type not found");
			}
			return new Response(ResponseCode.OK, (Serializable) method.invoke(this, request.data));
		} catch (Throwable e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}

	Serializable addEmployee(Serializable data) {
		Employee empl = (Employee) data;
		return company.addEmployee(empl);
	}

	Serializable removeEmployee(Serializable data) {
		long id = (long) data;
		return company.removeEmployee(id);
	}

	Serializable getAllEmployees(Serializable data) {
		return (Serializable) company.getAllEmployees();
	}

	Serializable getEmployeesByMonthBirth(Serializable data) {
		int month = (int) data;
		return (Serializable) company.getEmployeesByMonthBirth(month);
	}

	Serializable getEmployeesBySalary(Serializable data) {
		int[] salaries = (int[]) data;
		return (Serializable) company.getEmployeesBySalary(salaries[0], salaries[1]);
	}

	Serializable getEmployeesByDepartment(Serializable data) {
		String department = (String) data;
		return (Serializable) company.getEmployeesByDepartment(department);
	}

	Serializable getEmployee(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id);
	}

	Serializable save(Serializable data) {
		String filePath = (String) data;
		company.save(filePath);
		return "";
	}

	Serializable retsore(Serializable data) {
		String filePath = (String) data;
		company.restore(filePath);
		return "";
	}

	public CompanyProtocol(Company company) {
		this.company = company;
	}

}