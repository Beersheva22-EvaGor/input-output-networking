package telran.employees.apllication.controller.graphics;

import telran.employees.apllication.controller.*;
import telran.employees.net.application.CompanyTcpApplication;
import telran.view.InputOutput;

public class GraphicalMenu {
	
	private static InputOutput IO;
	private static boolean appTypeIsLocal = false;

	public static void main(String[] args) throws Exception {
		appTypeIsLocal = args[0].toLowerCase()=="local" ? true : false;
		
		IO = new Frame();
		
		if (appTypeIsLocal) {
			CompanyAppl appl = new CompanyAppl(IO);
			appl.main(null);
		} else {			
			CompanyClientAppl appl = new CompanyClientAppl(IO);
			appl.main(null);
		}
		
	}
	
}
