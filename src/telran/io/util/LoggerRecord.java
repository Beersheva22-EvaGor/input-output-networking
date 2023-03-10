package telran.io.util;

import java.time.Instant;

public class LoggerRecord {

	private final Instant timestamp;
	private final String zoneID;
	private final Level level;
	private final String loggerName;
	private final String message;
	private static int counter = 0;
		
	public LoggerRecord(Instant timestamp, String zoneID, Level level, String loggerName, String message) {
		counter ++;
		this.timestamp = timestamp;
		this.zoneID = zoneID;
		this.level = level;
		this.loggerName = loggerName;
		this.message = message;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (counter == 1) {
			builder.append("-- LOGGING --\r\n");
		}
		builder.append("<record>\n");
		builder.append(String.format("\t<timestamp> %s </timestamp>\r\n", timestamp.toString()));
		builder.append(String.format("\t<zoneID> %s </zoneID>\r\n", zoneID));
		builder.append(String.format("\t<level> %s </level>\r\n", level));
		builder.append(String.format("\t<loggerName> %s </loggerName>\r\n", loggerName));
		builder.append(String.format("\t<message> %s </message>\r\n", message));
		builder.append("</record>\n");
		
		return builder.toString();
	}
	
}
