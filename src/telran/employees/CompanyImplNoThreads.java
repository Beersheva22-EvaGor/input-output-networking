package telran.employees;

import java.util.*;


import java.io.*;

public class CompanyImplNoThreads implements ICompany {

	private static final long serialVersionUID = 1L;
	private HashMap<Long, Employee> employees = new HashMap<>();
	private HashMap<Integer, Set<Employee>> employeesMonth = new HashMap<>();
	private HashMap<String, Set<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<Integer, Set<Employee>> employeesSalary = new TreeMap<>();

	@Override
	public Iterator<Employee> iterator() {

		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		if (employees.putIfAbsent(empl.id, empl) == null) {
			res = true;
			addIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
			addIndexMap(employeesDepartment, empl.getDepartment(), empl);
			addIndexMap(employeesSalary, empl.getSalary(), empl);
		}

		return res;
	}

	private <T> void addIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		map.computeIfAbsent(key, k -> new HashSet<>()).add(empl);

	}

	@Override
	public Employee removeEmployee(long id) {
		Employee empl = employees.remove(id);
		if (empl != null) {
			removeIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
			removeIndexMap(employeesDepartment, empl.getDepartment(), empl);
			removeIndexMap(employeesSalary, empl.getSalary(), empl);
		}
		return empl;
	}

	private <T> void removeIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		Set<Employee> set = map.get(key);
		set.remove(empl);
		if (set.isEmpty()) {
			map.remove(key);
		}

	}

	@Override
	public List<Employee> getAllEmployees() {

		return new ArrayList<>(employees.values());
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {

		return new ArrayList<>(employeesMonth.getOrDefault(month, Collections.emptySet()));
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {

		return employeesSalary.subMap(salaryFrom, true, salaryTo, true).values().stream().flatMap(Set::stream).toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {

		return new ArrayList<>(employeesDepartment.getOrDefault(department, Collections.emptySet()));
	}

	@Override
	public Employee getEmployee(long id) {

		return employees.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))) {
			output.writeObject(getAllEmployees());
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); // some error
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			List<Employee> allEmployees = (List<Employee>) input.readObject();
			allEmployees.forEach(this::addEmployee);
		} catch (FileNotFoundException e) {
			// empty object but no error
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); // some error
		}

	}

	@Override
	public void updateSalary(long emplId, int newSalary) {
		Employee empl = getEmployee(emplId);
		if (empl != null) {
			removeIndexMap(employeesSalary, empl.salary, empl);
			empl.salary = newSalary;
			addIndexMap(employeesSalary, newSalary, empl);
		}
	}

	@Override
	public void updateDepartment(long emplId, String department) {
			Employee empl = employees.get(emplId);
			if (empl != null) {
				removeIndexMap(employeesDepartment, empl.department, empl);
				empl.department = department;
				addIndexMap(employeesDepartment, department, empl);
			}

	}

}