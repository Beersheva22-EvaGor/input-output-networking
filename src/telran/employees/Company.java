package telran.employees;


import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

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
			map.put(obj, set);
		}
		set.add(employee);
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
		if (set.size() == 0) {
			map.remove(obj);
		}
	}

	/* O[N] */
	@Override
	public List<Employee> getAllEmployees() {
		return new ArrayList<Employee>(mapMain.values()); // shallow copy!!! (it suits for the current version of class)
	}

	/* O[1] */
	@Override
	public List<Employee> getEmployeesByMonth(int month) {
		List<Employee> res = months.get(month);
		return res == null ? new ArrayList<Employee>() : res;
	}

	/* O[N] */
	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		List<Employee> res =  salaries.subMap(salaryFrom, salaryTo).values().stream().flatMap(List::stream).collect(Collectors.toList());
		return res;
	}

	/* O[1] */
	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		List<Employee> res = departments.get(department);
		return res == null ? new ArrayList<Employee>() : res;
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
			if (!(Modifier.isStatic(f.getModifiers()) || Modifier.isTransient(f.getModifiers())))
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
