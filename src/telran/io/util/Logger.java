package telran.io.util;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

public class Logger {
	private Level level = Level.INFO;
	private ArrayList<Handler> handlers = new ArrayList<>();
	private String name;

	public Logger(Handler handler, String name) {
		handlers.add(handler);
		this.name = name;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void error(String message) {
		levels(Level.ERROR, message);
	}

	public void warn(String message) {
		levels(Level.WARN, message);
	}

	public void info(String message) {
		levels(Level.INFO, message);
	}

	public void debug(String message) {
		levels(Level.DEBUG, message);
	}

	public void trace(String message) {
		levels(Level.TRACE, message);
	}

	public void addHandler(Handler handler) {
		handlers.add(handler);
	}
	
	private void levels(Level level, String message) {
		if (this.level.ordinal() <= level.ordinal()) {
			LoggerRecord rec = new LoggerRecord(Instant.now(), ZoneId.systemDefault().getId(), level, name, message);
			for (Handler handler : handlers) {
				handler.publish(rec);
			}
		}
	}

}
