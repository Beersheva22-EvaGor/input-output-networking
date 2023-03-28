package telran.view;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.*;
import java.time.format.DateTimeFormatter;
public interface InputOutput {
	String readString(String prompt);

	void writeString(Object obj);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}
	
	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {
		boolean running = true;
		R res = null;
		while (running) {
			try {
				String str = readString(prompt);
				res = mapper.apply(str);
				running = false;
			} catch (Exception e) {
				writeLine(errorPrompt + " - " + e.getMessage());
			}
		}
		return res;
	}
	
	private Function<String, String> mapper(String errorMessage, Predicate<String> predicate) {
		 return x -> {
				if (predicate.test(x)) {
					return x;
				}
				else {
					throw new RuntimeException(errorMessage);			
				}
			};
	}
	default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, mapper("String doesn't meet demands", predicate));
	}


	default String readStringOptions(String prompt, String errorPrompt, Set<String> options) {	
		return readObject(prompt, errorPrompt, mapper("String's not in the options", x -> options.contains(x)));
	}

	default int readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, x -> Integer.parseInt(x));
	}

	default int readInt(String prompt, String errorPrompt, int min, int max) {
		return readObject(prompt, errorPrompt, x -> compareValues(Integer.parseInt(x), min, max));
	}

	default long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(prompt, errorPrompt, x -> compareValues(Long.parseLong(x), min, max));
	}

	default LocalDate readDateISO(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, x -> LocalDate.parse(x));
	}

	default LocalDate readDate(String prompt, String errorPrompt, String format, LocalDate min, LocalDate max) {
		return readObject(prompt, errorPrompt, x -> compareValues(LocalDate.parse(x, DateTimeFormatter.ofPattern(format)), min, max));
	}

	default double readNumber(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, x -> compareValues(Double.parseDouble(x), min, max));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Comparable> T compareValues(T num, T min, T max) {
		if (num.compareTo(min)>=0 && num.compareTo(max)<=0) {
			return num;
		}
		else {
			throw new RuntimeException("Number is not in the given scope");			
		}
	}
}
