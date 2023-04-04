package telran.employees.apllication.controller;

import java.util.*;

import telran.employees.*;
import telran.view.*;

public class CompanyAppl {

	private static InputOutput IO;	
	
	public CompanyAppl(InputOutput IO) {
		this.IO = IO;
	}
	
	private static ICompany company;
	private static final String FILE = "company.data";
	
	public static void main(String[] args) {
		company = new Company();
		restore();
		List<String> departments = Arrays.stream(new String[]{"QA", "Development", "Audit","Management", "Accounting"}).toList();
		CompanyControllerItems.mainMenu(IO, company, departments).perform(IO);
		IO.writeLine("Thank you and goodbuy!");
		save();
	}

	private static void restore() {
		try {
		company.restore(FILE);
		} catch (Exception e) {
		}
	}
	
	private static void save() {
		try {
		company.save(FILE);
		} catch (Exception e) {
		}
	}
}
