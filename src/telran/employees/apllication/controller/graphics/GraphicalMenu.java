package telran.employees.apllication.controller.graphics;

import telran.employees.EmployeeClientAppl;
import telran.employees.apllication.controller.*;
import telran.employees.net.application.CompanyTcpApplication;
import telran.view.InputOutput;

public class GraphicalMenu {
	
	private static InputOutput IO;

	public static void main(String[] args) throws Exception {
		
		IO = new Frame();

			EmployeeClientAppl appl = new EmployeeClientAppl();
			appl.setIO(IO);
			appl.main(null);		
	}
	
}
