package telran.io.util;

public interface Handler {

	public void publish(LoggerRecord record);

	default void close() {};
}
