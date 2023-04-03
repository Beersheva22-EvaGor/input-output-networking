package telran.employees.apllication.controller;

import telran.employees.net.CompanyNetProxy;
import telran.net.TcpClient;
import telran.view.InputOutput;
import telran.view.StandardInputOutput;

public class CompanyClientAppl {
	private static InputOutput IO = new StandardInputOutput();
	static CompanyNetProxy companyNetProxy;
	
	public CompanyClientAppl(InputOutput IO) {
		this.IO = IO;
	}
	

	public static void main(String[] args) throws Exception {
		companyNetProxy = new CompanyNetProxy(new TcpClient("localhost", 4000));
		CompanyControllerItems.mainMenu(IO, companyNetProxy).perform(IO);
		IO.writeLine("Thank you and goodbuy!");
	}
}
