package telran.employees;

import java.util.*;
import java.util.concurrent.locks.*;
import java.util.stream.IntStream;
import java.io.*;

public class Company implements ICompany {

	private static enum LockType {READ, WRITE}
	private static final long serialVersionUID = 1L;
	private static final int N_RESOURCES = 4;
	private static final int EMPLOYEES_INDEX = 0;
	private static final int EMPLOYEES_DEPARTMENT_INDEX = 1;
	private static final int EMPLOYEES_MONTH_INDEX = 2;
	private static final int EMPLOYEES_SALARY_INDEX = 3;
	private HashMap<Long, Employee> employees = new HashMap<>();
	private HashMap<Integer, Set<Employee>> employeesMonth = new HashMap<>();
	private HashMap<String, Set<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<Integer, Set<Employee>> employeesSalary = new TreeMap<>();
	
	private ReentrantReadWriteLock[] rwLocks =
			IntStream.range(0, N_RESOURCES).mapToObj(i -> new ReentrantReadWriteLock())
			.toArray(ReentrantReadWriteLock[]::new);
			
	
	private Lock[] readLocks =
			Arrays.stream(rwLocks).map(ReentrantReadWriteLock::readLock)
			.toArray(Lock[]::new);
	private Lock[] writeLocks =
			Arrays.stream(rwLocks).map(ReentrantReadWriteLock::writeLock)
			.toArray(Lock[]::new);
	private void lock(LockType type, int ... indexes) {
		Lock[] locks = type == LockType.READ ? readLocks : writeLocks;
		Arrays.stream(indexes).sorted().forEach(i -> locks[i].lock());
	}
	private void unlock(LockType type, int ... indexes) {
		Lock[] locks = type == LockType.READ ? readLocks : writeLocks;
		Arrays.stream(indexes).sorted().forEach(i -> locks[i].unlock());
	}
	@Override
	public Iterator<Employee> iterator() {
		
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		int[] indexes = IntStream.range(0, N_RESOURCES).toArray();
		lock(LockType.WRITE, indexes);
		try {
			boolean res = false;
			if (employees.putIfAbsent(empl.id, empl) == null) {
				res = true;
				addIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
				addIndexMap(employeesDepartment, empl.getDepartment(), empl);
				addIndexMap(employeesSalary, empl.getSalary(), empl);
			}
			return res;
		} finally {
			unlock(LockType.WRITE, indexes);
		}
	}

	private <T> void addIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		map.computeIfAbsent(key, k->new HashSet<>()).add(empl);
		
	}

	@Override
	public Employee removeEmployee(long id) {
		int[] indexes = IntStream.range(0, N_RESOURCES).toArray();
		lock(LockType.WRITE, indexes);
		try {
			Employee empl = employees.remove(id);
			if (empl != null) {
				removeIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
				removeIndexMap(employeesDepartment, empl.getDepartment(), empl);
				removeIndexMap(employeesSalary, empl.getSalary(), empl);
			}
			return empl;
		} finally {
			unlock(LockType.WRITE, indexes);
		}
	}

	private <T>void removeIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		Set<Employee> set = map.get(key);
		set.remove(empl);
		if (set.isEmpty()) {
			map.remove(key);
		}
		
	}

	@Override
	public List<Employee> getAllEmployees() {
		lock(LockType.READ, EMPLOYEES_INDEX);
		
		try {
			return new ArrayList<>(employees.values());
		} finally {
			unlock(LockType.READ, EMPLOYEES_INDEX);
		}
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		
		lock(LockType.READ, EMPLOYEES_MONTH_INDEX);
		
		try {
			return new ArrayList<>(employeesMonth.getOrDefault(month, Collections.emptySet()));
		}finally {
			unlock(LockType.READ, EMPLOYEES_MONTH_INDEX);
		}
		
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		lock(LockType.READ, EMPLOYEES_SALARY_INDEX);
		try {
			return employeesSalary.subMap(salaryFrom, true, salaryTo, true).values().stream().flatMap(Set::stream)
					.toList();
		} finally {
			unlock(LockType.READ, EMPLOYEES_SALARY_INDEX);
		}
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		lock(LockType.READ, EMPLOYEES_DEPARTMENT_INDEX);
		try {
			return new ArrayList<>(employeesDepartment.getOrDefault(department, Collections.emptySet()));
		} finally {
			unlock(LockType.READ, EMPLOYEES_DEPARTMENT_INDEX);
		}
	}

	@Override
	public Employee getEmployee(long id) {
		lock(LockType.READ, EMPLOYEES_INDEX);
		try {
			return employees.get(id);
		} finally {
			unlock(LockType.READ, EMPLOYEES_INDEX);
		}
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))){
			output.writeObject(getAllEmployees());
		} catch(Exception e) {
			throw new RuntimeException(e.toString()); //some error
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			List<Employee> allEmployees = (List<Employee>) input.readObject();
			allEmployees.forEach(this::addEmployee);
		}catch(FileNotFoundException e) {
			//empty object but no error
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); //some error
		}

	}

	@Override
	public void updateSalary(long emplId, int newSalary) {
		
			lock(LockType.READ, EMPLOYEES_INDEX);
			lock(LockType.WRITE, EMPLOYEES_SALARY_INDEX);
			try {
				Employee empl = employees.get(emplId);
				if (empl != null) {
				removeIndexMap(employeesSalary, empl.salary, empl);
				empl.salary = newSalary;
				addIndexMap(employeesSalary, newSalary, empl);
				}
			} finally {
				unlock(LockType.READ, EMPLOYEES_INDEX);
				unlock(LockType.WRITE, EMPLOYEES_SALARY_INDEX);
			} 
		
		
	}

	@Override
	public void updateDepartment(long emplId, String department) {
		lock(LockType.READ, EMPLOYEES_INDEX);
		lock(LockType.WRITE, EMPLOYEES_DEPARTMENT_INDEX);
		try {
			Employee empl = employees.get(emplId);
			if (empl != null) {
				removeIndexMap(employeesDepartment, empl.department, empl);
				empl.department = department;
				addIndexMap(employeesDepartment, department, empl);
			}
		} finally {
			unlock(LockType.READ, EMPLOYEES_INDEX);
			unlock(LockType.WRITE, EMPLOYEES_DEPARTMENT_INDEX);
		}
		
	}
	

}