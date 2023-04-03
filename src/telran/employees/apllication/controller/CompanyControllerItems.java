package telran.employees.apllication.controller;

import telran.employees.ICompany;
import telran.view.*;

public class CompanyControllerItems {
	
	public static Item userItemMenu(InputOutput io, ICompany company) {
		return UserMenu.userItemMenu(io, company);
	}

	public static Item adminItemMenu(InputOutput io, ICompany company) {
		return AdminMenu.adminItemMenu(io, company);
	}

	public static Item mainMenu(InputOutput io, ICompany company) {
		return new Menu("Operations with company's stuff", 
				userItemMenu(io, company),
				adminItemMenu(io, company),
				Item.exit());
	}
}
