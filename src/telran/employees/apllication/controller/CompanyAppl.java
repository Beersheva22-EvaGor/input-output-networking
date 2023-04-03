package telran.employees.apllication.controller;

import telran.employees.Company;
import telran.employees.ICompany;
import telran.view.InputOutput;
import telran.view.StandardInputOutput;

public class CompanyAppl {

	private static InputOutput IO = new StandardInputOutput();
	
	public CompanyAppl(InputOutput IO) {
		this.IO = IO;
	}
	
	private static ICompany company;
	private static final String FILE = "company.data";
	
	public static void main(String[] args) {
		company = new Company();
		restore();
		CompanyControllerItems.mainMenu(IO, company).perform(IO);
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
