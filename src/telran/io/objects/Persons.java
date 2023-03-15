package telran.io.objects;

import java.io.*;
import java.util.*;

public class Persons implements Serializable, Iterable<Person>{
	static String filePath = "persons.data";
	private static final long serialVersionUID = 1L;
	List<Person> persons = new ArrayList<>();
	void addPerson(Person person) {
		persons.add(person);
	}
	@Override
	public Iterator<Person> iterator() {
		
		return persons.iterator();
	}
	public void save() {
		try {
			writeObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public Persons restore() {
		Persons res = null;
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
			res = (Persons) input.readObject();
		} catch (Exception e) {
			res = new Persons();
		}
		return res;
	}
	
	private void writeObject() throws Exception {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath))) {
			output.writeObject(this);
		} 
		
	}
	
	

}