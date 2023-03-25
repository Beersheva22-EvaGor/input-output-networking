package telran.net.company;

import java.util.ArrayList;

import telran.employees.*;
import telran.net.*;

public class CompanyProtocol implements Protocol{
	static Company company = new Company();
			
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response getResponse(Request request) {
		try {
		return switch (request.type.toLowerCase()) {
		case "addemployee" -> new Response(ResponseCode.OK, company.addEmployee((Employee)request.data));
		case "removeemployee" -> new Response(ResponseCode.OK, company.removeEmployee((long) request.data));
		case "getallemployees" -> new Response(ResponseCode.OK, new ArrayList(company.getAllEmployees()));
		case "getemployeesbymonthbirth" -> new Response(ResponseCode.OK,  new ArrayList(company.getEmployeesByMonthBirth((int)request.data)));
		case "getemployeesbysalary" -> {
			String[] args = request.data.toString().split(",");
			yield new Response(ResponseCode.OK,  new ArrayList(company.getEmployeesBySalary(Integer.parseInt(args[0].trim()),Integer.parseInt(args[1].trim()))));}
		case "getemployeesbydepartment" -> new Response(ResponseCode.OK,  new ArrayList(company.getEmployeesByDepartment((String)request.data)));
		case "getemployee" -> new Response(ResponseCode.OK, company.getEmployee((long)request.data));
		case "save" -> {
			company.save((String)request.data);
			yield new Response(ResponseCode.OK, null);}
		case "restore" -> {
			company.restore((String)request.data);
			yield new Response(ResponseCode.OK, null);}
		default -> new Response(ResponseCode.WRONG_REQUEST, request.type + " wrong request");
		}; 
		} catch (Exception e){
			return  new Response(ResponseCode.WRONG_DATA, request.type + " wrong data");
		}
	}


}
