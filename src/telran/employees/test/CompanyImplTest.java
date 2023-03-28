package telran.employees.test;

import org.junit.jupiter.api.BeforeEach;

import telran.employees.Company;

public class CompanyImplTest extends CompanyTest {
@BeforeEach
@Override
void setUp() throws Exception {
	company = new Company();
	super.setUp();
}
}