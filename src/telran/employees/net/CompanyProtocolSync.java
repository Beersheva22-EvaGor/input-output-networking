package telran.employees.net;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import telran.employees.Company;
import telran.employees.Employee;
import telran.employees.PairId;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocolSync implements Protocol {
	Company company;
	@Override
	public synchronized Response getResponse(Request request) {
		Response response = null;
		try {
			Method method = getClass().getDeclaredMethod(request.type,
					Serializable.class);
			response = getResponseData((Serializable)method.invoke(this, request.data));
		} catch (NoSuchMethodException  e1) {
			response = new Response(ResponseCode.WRONG_REQUEST, request.type + " Request type not found");
		} catch (Exception e) {
			e.printStackTrace();
			response = new Response(ResponseCode.WRONG_DATA, e.toString());
		} 
		return response;
	}
	private Response getResponseData(Serializable data) {
		
		return new Response(ResponseCode.OK, data);
	}
	Serializable addEmployee(Serializable data) {
		Employee empl = (Employee)data;
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
		int month = (int)data;
		return (Serializable) company.getEmployeesByMonthBirth(month);
	}
	Serializable getEmployeesBySalary(Serializable data) {
		int[] salaries = (int[]) data;
		return (Serializable) company.getEmployeesBySalary(salaries[0], salaries[1]);
	}
	Serializable getEmployeesByDepartment(Serializable data) {
		String department = (String)data;
		return (Serializable) company.getEmployeesByDepartment(department);
	}
	Serializable getEmployee(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id);
	}
	Serializable save(Serializable data) {
		String filePath = (String)data;
		company.save(filePath);
		return "";
	}
	Serializable retsore(Serializable data) {
		String filePath = (String)data;
		company.restore(filePath);
		return "";
	}
	Serializable updateSalary(Serializable data) {
		@SuppressWarnings("unchecked")
		PairId<Integer> idSalary = (PairId<Integer>) data;
		company.updateSalary(idSalary.id(), idSalary.value());
		return "";
	}
	Serializable updateDepartment(Serializable data) {
		@SuppressWarnings("unchecked")
		PairId<String> idDepartment = (PairId<String>) data;
		company.updateDepartment(idDepartment.id(), idDepartment.value());
		return "";
	}
	public CompanyProtocolSync(Company company) {
		this.company = company;
	}
	
	

}
