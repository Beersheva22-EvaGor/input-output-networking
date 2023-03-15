package telran.io.objects;
public class RestorePersonsApplication {

	public static void main(String[] args) throws Exception {
		Persons persons = Persons.restore();
		
		persons.forEach(System.out::println);
	}

}