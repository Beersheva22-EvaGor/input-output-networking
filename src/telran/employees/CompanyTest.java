package telran.employees;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.*;

import telran.net.company.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTest {
	private static final long ID1 = 123;
	private static final int MONTH1 = 1;
	private static final String DEPARTMENT1 = "department1";
	private static final int SALARY1 = 1000;
	private static final long ID2 = 124;
	private static final String DEPARTMENT2 = "department2";
	private static final int SALARY2 = 2000;
	private static final LocalDate BIRTH2 = LocalDate.of(2000, MONTH1, 1);
	private static final int MONTH2 = 2;
	private static final int SALARY3 = 3000;
	private static final long ID3 = 125;
	private static final int SALARY4 = 4000;
	private static final long ID4 = 126;
	private static final long ID10 = 100000;
	private static final String FILE_NAME = "test.data";
	Employee empl1 = new Employee(ID1, "name", LocalDate.of(2000, MONTH1, 1), DEPARTMENT1, SALARY1);
	Employee empl2 = new Employee(ID2, "name", LocalDate.of(2000, MONTH1, 1), DEPARTMENT2, SALARY2);
	Employee empl3 = new Employee(ID3, "name", LocalDate.of(2000, MONTH2, 1), DEPARTMENT1, SALARY3);
	Employee empl4 = new Employee(ID4, "name", LocalDate.of(2000, MONTH1, 1), DEPARTMENT2, SALARY4);
	Employee[] employees = { empl1, empl2, empl3, empl4 };
	static ICompany company;

	@BeforeAll
	static void instantiate() throws Exception {
		company = new CompanyClientAdapter(ConnectionType.TCP);
	}

	@BeforeEach
	void setUp() throws Exception {
		company.getAllEmployees().forEach(e -> company.removeEmployee(e.getId()));
		for (Employee empl : employees) {
			company.addEmployee(empl);
		}
	}

	@Test
	void addEmployeeTest() {
		Employee newEmployee = new Employee(ID10, DEPARTMENT2, BIRTH2, DEPARTMENT1, SALARY1);
		assertTrue(company.addEmployee(newEmployee));
		assertFalse(company.addEmployee(newEmployee));
	}

	@Test
	void employeesBySalaryTest() {
		assertTrue(company.getEmployeesBySalary(100000000, 100000100).isEmpty());
		Employee[] expected = { empl1, empl2, empl3 };
		Employee[] actual = company.getEmployeesBySalary(SALARY1, SALARY3).toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		company.removeEmployee(ID1);
		company.removeEmployee(ID2);
		company.removeEmployee(ID3);
		assertTrue(company.getEmployeesBySalary(SALARY1, SALARY3).isEmpty());
	}

	@Test
	void employeesByDepartmentTest() {
		assertTrue(company.getEmployeesByDepartment("gggggg").isEmpty());
		Employee[] expected = { empl2, empl4 };
		Employee[] actual = company.getEmployeesByDepartment(DEPARTMENT2).toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		company.removeEmployee(ID2);
		company.removeEmployee(ID4);
		assertTrue(company.getEmployeesByDepartment(DEPARTMENT2).isEmpty());
	}

	@Test
	void employeesByMonthTest() {
		assertTrue(company.getEmployeesByMonthBirth(20).isEmpty());
		Employee[] expected = { empl1, empl2, empl4 };
		Employee[] actual = company.getEmployeesByMonthBirth(MONTH1).toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
		company.removeEmployee(ID1);
		company.removeEmployee(ID2);
		company.removeEmployee(ID4);
		assertTrue(company.getEmployeesByMonthBirth(MONTH1).isEmpty());
	}

	@Test
	void removeEmployeeTest() {
		assertEquals(empl1, company.removeEmployee(ID1));
		assertNull(company.removeEmployee(ID1));
		runTest(new Employee[] { empl2, empl3, empl4 });
	}

	@Test
	private void runTest(Employee[] expected) {
		Employee[] actual = company.getAllEmployees().toArray(Employee[]::new);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
	}

	@Test
	@Order(1)
	void saveTest() {
		company.save(FILE_NAME);
	}

	@Test
	@Order(2)
	void restoreTest() {
		Company company2 = new Company();
		company2.restore(FILE_NAME);
		assertIterableEquals(company, company2);
	}

	@AfterAll
	static void close() throws IOException {
		if (company instanceof CompanyClientAdapter) {
			((CompanyClientAdapter) company).close();
		}
	}

}