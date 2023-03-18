package telran.io.util;

public interface Handler {

	public void publish(LoggerRecord record);
	public default void close() {};
}
