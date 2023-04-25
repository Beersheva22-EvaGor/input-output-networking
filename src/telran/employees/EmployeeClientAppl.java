package telran.employees;
import java.util.ArrayList;
import java.util.Arrays;

import telran.employees.apllication.controller.CompanyControllerItems;
import telran.employees.net.CompanyNetProxy;
import telran.net.NetworkClient;
import telran.net.TcpClient;
import telran.view.*;
public class EmployeeClientAppl {

	private static final String FILE_PATH = "employees.data";
	private static InputOutput io = new StandardInputOutput();
	
	public void setIO(InputOutput io) {
		this.io = io;
	}
	public static void main(String[] args) throws Exception {
		
		NetworkClient client = new TcpClient("localhost", 4000);
		ICompany company = new CompanyNetProxy(client);
		company.restore(FILE_PATH);
		Item[] companyItems = CompanyControllerItems.getCompanyItems
				(company, new String[] {"QA", "Development", "Audit",
						"Management", "Accounting"});
		ArrayList<Item> items= new ArrayList<>(Arrays.asList(companyItems));
		items.add(Item.of("Exit & save", io1 -> company.save(FILE_PATH), true));
		Menu menu = new Menu("Company Application", items);
		menu.perform(io);

	}

}