package telran.io.util;

import java.io.PrintStream;

public class SimpleStreamHandler implements Handler {
	private PrintStream stream;	
	public SimpleStreamHandler(PrintStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	public void publish(LoggerRecord record) {
		stream.append(record.toString());
	}

}
