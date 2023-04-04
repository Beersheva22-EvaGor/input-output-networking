package telran.employees.apllication.controller;

import java.util.List;

import telran.employees.ICompany;
import telran.view.*;

public class CompanyControllerItems {
	
	public static Item userItemMenu(InputOutput io, ICompany company, List<String> departments) {
		return UserMenu.userItemMenu(io, company, departments);
	}

	public static Item adminItemMenu(InputOutput io, ICompany company, List<String> departments) {
		return AdminMenu.adminItemMenu(io, company, departments);
	}

	public static Item mainMenu(InputOutput io, ICompany company, List<String> departments) {
		return new Menu("Operations with company's stuff", 
				userItemMenu(io, company, departments),
				adminItemMenu(io, company, departments),
				Item.exit());
	}
}
