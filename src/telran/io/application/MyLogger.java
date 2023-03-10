package telran.io.application;

import java.io.IOException;
import java.util.logging.*;

public class MyLogger {
	static Handler handler1, handler2;
	static Logger LOG;
	public static void main(String[] args) throws SecurityException, IOException {
		handler1 = new FileHandler("D:\\Java\\BSH_TR\\JARs\\file1.txt");
		handler2 = new ConsoleHandler();
		LOG = Logger.getLogger("myLogger");
		LOG.addHandler(handler1);
		//LOG.addHandler(handler2);
		LOG.setLevel(Level.FINEST);
		handler1.setLevel(Level.WARNING);
		handler2.setLevel(Level.INFO);
		
		
		LOG.warning("WARNING1");
		LOG.info("INFO1");
		LOG.fine("FINE1");
		LOG.warning("WARNING2");
		LOG.info("INFO2");
		
	}

}
