package telran.employees;
import java.util.ArrayList;
import java.util.Arrays;

import telran.employees.apllication.controller.CompanyControllerItems;
import telran.view.*;
public class EmployeeDesktopAppl {

	private static final String FILE_PATH = "employees.data";

	public static void main(String[] args) {
		InputOutput io = new StandardInputOutput();
		ICompany company = new Company();
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
