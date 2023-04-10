package telran.mygit.view;

import telran.employees.apllication.controller.graphics.Frame;
import telran.mygit.*;
import telran.view.*;

public class MyGitApplication {

	static IGitRepository rep;
	private static final InputOutput IO = new Frame("myGIT");
	
	public static void main(String[] args) {
		rep = GitRepository.init();
		Menu menu = MenuGit.getMenuItemsGit(rep);
		menu.perform(IO);
		
	}

}
