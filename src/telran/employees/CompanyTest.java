package telran.employees;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.*;

class CompanyTest {

	String path = "company.data";
	Company company = new Company();
	String department = "dep1";
	Employee ivan = new Employee(0, "Ivan", LocalDate.of(2000, 1, 1), department, 10_000);
	Employee john = new Employee(1, "John", LocalDate.of(1990, 2, 2), "dep2", 10_000);
	Employee sara = new Employee(2, "Sara", LocalDate.of(2000, 3, 4), "dep3", 15_000);
	Employee moshe = new Employee(3, "Moshe", LocalDate.of(1975, 1, 1), department, 20_000);

	@BeforeEach
	void setUp() {
		company.addEmployee(ivan);
		company.addEmployee(john);
		company.addEmployee(sara);
		company.addEmployee(moshe);
	}
	
	@Test
	void getDepartmentTest() {		
		List<Employee> expected = new ArrayList<>();
		expected.add(ivan);
		expected.add(moshe);
		assertEquals(expected, company.getEmployeesByDepartment(department));
	}
	
	@Test
	void getSalaryTest() {		
		List<Employee> expected = new ArrayList<>();
		expected.add(ivan);
		expected.add(john);
		expected.add(sara);
		assertEquals(expected, company.getEmployeesBySalary(0, 16_000));
	}
	
	@Test
	void getMonthTest() {		
		List<Employee> expected = new ArrayList<>();
		expected.add(ivan);
		expected.add(moshe);
		assertEquals(expected, company.getEmployeesByMonth(1));
		assertEquals(new ArrayList<Employee>(), company.getEmployeesByMonth(0));
	}

	@Test
	void removeTest() {
		company.removeEmployee(3);
		List<Employee> expected = new ArrayList<>();
		expected.add(ivan);
		expected.add(john);
		expected.add(sara);
		assertEquals(expected, company.getAllEmployees());
	}
	
	@Test
	void saveTest() {
		company.save(path);
	}

	@Test
	void restoreTest() {
		Company companyRestored = new Company();
		companyRestored.restore(path);
		companyRestored.getAllEmployees().
			forEach(e-> System.out.printf("id = %d, Name = %s, department = %s, salary = %d, birth = %s\r\n", e.getId(), e.getName(), e.getDepartment(), e.getSalary(), e.getBirthDate().toString()));
	}
}
