package telran.employees;


import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.*;

public class Company implements ICompany {
	private static final long serialVersionUID = 1L;
	
	private transient final Logger LOG = Logger.getLogger(Company.class.getName());


	private HashMap<Long, Employee> mapMain = new HashMap<>(); // HashMap: key = id, value = Employee
	private HashMap<Integer, List<Employee>> months = new HashMap<>(); // HashMap of months of birth 
	private HashMap<String, List<Employee>> departments = new HashMap<>(); // HashMap of departments 
	private TreeMap<Integer, List<Employee>> salaries = new TreeMap<>(); // TreeMap: comparing salaries

	private class CompanyIterator implements Iterator<Employee> {
		Iterator<Long> iterKeys = mapMain.keySet().iterator();

		@Override
		public boolean hasNext() {
			return iterKeys.hasNext();
		}

		@SuppressWarnings("unlikely-arg-type")
		@Override
		public Employee next() {
			if (!hasNext()) {
				throw new IllegalStateException();
			}
			return mapMain.get(iterKeys);
		}
	}

	@Override
	public Iterator<Employee> iterator() {
		return new CompanyIterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		boolean res = mapMain.put(employee.getId(), employee) == null;
		if (res) {
			// to HashMap of months
			addToMap(months, employee.getBirthDate().getMonthValue(), employee);
			// to HashMap of departments
			addToMap(departments, employee.getDepartment(), employee);
			// to TreeMap of salaries
			addToMap(salaries, employee.getSalary(), employee);			
		}
		return res;
	}

	private <T> void addToMap(Map<T, List<Employee>> map, T obj, Employee employee) {
		List<Employee> set = map.get(obj);
		if (set == null) {
			set = new ArrayList<>();
		}
		set.add(employee);
		map.put(obj, set);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee employee = mapMain.remove(id);
		if (employee != null) {
			// from HashMap of months
			removeFromMap(months, employee.getBirthDate().getMonthValue(), employee);
			// from HashMap of departments
			removeFromMap(departments, employee.getDepartment(), employee);
			// from TreeMap of salaries
			removeFromMap(salaries, employee.getSalary(), employee);
		}
		return employee;
	}

	private <T> void removeFromMap(Map<T, List<Employee>> map, T obj, Employee employee) {
		List<Employee> set = map.get(obj);
		set.remove(employee);
		map.put(obj, set);
	}

	/* O[N] */
	@Override
	public List<Employee> getAllEmployees() {
		return new ArrayList<Employee>(mapMain.values()); // shallow copy!!! (it suits for the current version of class)
	}

	/* O[1] */
	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		return months.get(month);
	}

	/* O[N] */
	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		ArrayList<Employee> res = new ArrayList<>();
		salaries.subMap(salaryFrom, salaryTo).values().forEach(l -> res.addAll(l));
		return res;
	}

	/* O[1] */
	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return departments.get(department);
	}

	/* O[1] */
	@Override
	public Employee getEmployee(long id) {
		return mapMain.get(id);
	}

	@Override
	public void save(String pathName) {
		try {
			writeObject(pathName);
		} catch (Exception e) {
			LOG.severe(String.format("Serialization to the file %s failed\r\n %s", pathName, e.fillInStackTrace()));
		}
	}

	private void writeObject(String pathName) throws Exception {
		try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathName)))) {
			output.writeObject(this);
		} 		
	}
	
	private void readObject(String pathName) throws Exception {
		Company company = null;
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			company = (Company) input.readObject();
		} 
		
		Field[] fields = Company.class.getDeclaredFields();
		for(Field f : fields){
			if (f.getName() != "LOG" && f.getName() != "serialVersionUID")
			f.set(this, f.get(company));
		}
		
	}
	@Override
	public void restore(String pathName) {
		try {
			readObject(pathName);
		} catch (Exception e) {
			LOG.severe(String.format("Deserialization from the file failed%s \r\n %s", pathName,  e.fillInStackTrace()));
		}
	}


}
