package telran.mygit.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.*;

import telran.mygit.IMessage;
import telran.mygit.StructureGit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class StructureGitTest {

	static class Message implements IMessage {
		String mes;
		String key;
		static int i=0;

		Message(String mes) {
			this.mes = mes;
			key = Integer.toString(++i);
		}

		
		@Override
		public String getUniqueKey() {
			return key;
		}

		@Override
		public String toString() {
			return mes.toString();
		}
	}

	private static final String DEFAULT_BRANCH = "master";
	static StructureGit<List<String>, Message> struct;
	static List<String> files = new ArrayList<>();
	static Message message1;
	static Message message2;
	static Message message3;
	static Message message4;

	@BeforeAll
	static void setUp() throws Exception {	
		File dir = new File("D:\\Java\\MyGit");
		Arrays.stream(dir.listFiles()).forEach(f -> files.add(f.getName()));
		message1 = new Message("First commit");
		struct = new StructureGit<List<String>, Message>();
		struct.addFirstNode(files, message1);
	}

	@Test
	@Order(2)
	void iteratorTest() {
		Iterator<List<String>> it = struct.iterator();
		List<String> list = new ArrayList<>();
		while (it.hasNext()) {
			it.next().forEach(list::add);
		}
		files.add("4.txt");
		files.add("5.txt");
		files.add("6.txt");
		files.add("7.txt");
		files.add("8.txt");
		assertEquals(files, list);
	}

	@Test
	@Order(1)
	void addToSameBruchTest() {
		var ar = new String[] { "4.txt", "5.txt", "6.txt" };
		message2 = new Message("Second commit");
		struct.addNode(new ArrayList<String>(Arrays.asList(ar)), message2);
		ar = new String[] { "7.txt", "8.txt" };
		message3 = new Message("Third commit");
		struct.addNode(new ArrayList<String>(Arrays.asList(ar)), message3);
		var res = struct.getData(struct.getHeadNode()).toArray();
		assertArrayEquals(ar, res);
	}

	@Test
	@Order(3)
	void addBranch() {
		struct.gotoNode("2");
		assertEquals("2", struct.getHeadNode());
		
		String newBranch = "myBranch";
		var ar = new String[] { "7.txt", "8.txt" };
		message4 = new Message("Forths commit");
		struct.createNewBranch(newBranch);
		struct.addNode(new ArrayList<String>(Arrays.asList(ar)), message4);
		List<String> expected = new ArrayList<>();
		expected.add(DEFAULT_BRANCH);
		expected.add(newBranch + "*");
		sort(expected);
		var res = struct.branches();
		sort(res);
		assertEquals(expected, res);
	}

	private void sort(List<String> list) {
		list.sort(Comparator.naturalOrder());
	}

	@Test
	@Order(4)
	void switchBranchTest() {
		struct.switchBranch(DEFAULT_BRANCH);
		List<String> expected = new ArrayList<>();
		expected.add(DEFAULT_BRANCH + "*");
		expected.add("myBranch");
		sort(expected);
		var res = struct.branches();
		sort(res);
		assertEquals(expected, res);
	}

	@Test
	@Order(5)
	void renameBranchTest() {
		struct.renameBranch("myBranch", "new branch");
		List<String> expected = new ArrayList<>();
		expected.add(DEFAULT_BRANCH + "*");
		expected.add("new branch");
		sort(expected);
		var res = struct.branches();
		sort(res);
		assertEquals(expected, res);
	}

	@Test
	@Order(6)
	void logTest() {
		List<Message> logs = struct.log();
		List<Message> expected = new ArrayList<>();
		expected.add(message3);
		expected.add(message2);
		expected.add(message1);
		assertEquals(expected, logs);
	}

	@Test
	@Order(7)
	void toStringTest() {
		System.out.println(struct.toString());
	}
	
	String filename = "testStruct.txt";
	@Test
	@Order(8)
	void save() throws IOException {
		if (Files.exists(Path.of(filename))) {
			Files.delete(Path.of(filename));
			}
		struct.save(filename);
		struct.restore(filename);
		System.out.println(struct.viewAllNodes());

	}
}
