package telran.io.util;

import java.time.Instant;

public class LoggerRecord {

	private static Instant timestamp;
	private static String zoneID;
	private static Level level;
	private static String loggerName;
	private static String message;
		
	public LoggerRecord(Instant timestamp, String zoneID, Level level, String loggerName, String message) {
		super();
		LoggerRecord.timestamp = timestamp;
		LoggerRecord.zoneID = zoneID;
		LoggerRecord.level = level;
		LoggerRecord.loggerName = loggerName;
		LoggerRecord.message = message;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
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
