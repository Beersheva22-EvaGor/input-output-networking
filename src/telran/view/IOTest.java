package telran.view;

import java.time.LocalDate;
import java.util.HashSet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class IOTest {
	StandartInputOutput io = new StandartInputOutput();

	@Test
//	@Disabled
	void testReadStringPredicate() {
		io.writeLine(io.readStringPredicate("Enter email", "Must be in a format example@domain.com", x -> x.matches("(.+)@(.+)\\.(.+)")));
	}
	
	@Test
//	@Disabled
	void testReadStringOptions() {
		HashSet<String> set  = new HashSet<>();
		set.add("TRUE");
		set.add("FALSE");
		io.writeLine(io.readStringOptions("Enter boolean in uppercase", "Must be 'TRUE' or 'FALSE'", set));
	}
	
	@Test
//	@Disabled
	void testReadInt() {
		int i = io.readInt("Input integer", "Cannot be parsed as integer");
		io.writeLine(i);
		int l1 = -50;
		int l2 = 50;
		i = io.readInt("Input integer in range "+ l1+ " .. "+ l2, "Cannot be parsed as integer or not in the scope",  l1, l2);
		io.writeLine(i);
	}

	@Test
//	@Disabled
	void testReadLong() {
		long l1 = Long.MIN_VALUE;
		long l2 = 0;
		long i = io.readLong("Input long in range "+ l1+ " .. "+ l2, "Cannot be parsed as long or not in the scope", l1, l2);
		io.writeLine(i);
	}
	
	@Test
//	@Disabled
	void testReadDate() {
		LocalDate date = io.readDateISO("Input date", "Cannot be parsed as date in ISO format (yyyy-mm-dd)");
		io.writeLine(date);
		LocalDate d1 = LocalDate.of(1999, 1, 1);
		LocalDate d2 = LocalDate.now();
		LocalDate date1 = io.readDate("Input date in range " +d1+" .. "+ d2 +" in format d-MM-yyyy", "Cannot be parsed as date or not in scope", "d-MM-yyyy", d1, d2 );
		io.writeLine(date1);
	}
	
	@Test
//	@Disabled
	void testReadNumber() {
		int n1 = -123;
		int n2 = 123;
		double i = io.readNumber("Input number in range " + n1 +" .. " + n2, "Cannot be parsed as double or not in the scope", n1, n2);
		io.writeLine(i);
	}
}
