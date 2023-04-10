package telran.mygit.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import telran.mygit.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GitRepositoryTest {
	private static final String path = "D:\\Java\\MyGit";
	private static final String GIT_FILE = ".mygit";
	private static final String DEFAULT_BRANCH = "master";
	static IGitRepository rep = GitRepository.init();
	private static String myBranch = "myBranch";
	@BeforeAll
	static void setUp() throws Exception {
		 for(Path file : Files.list(Path.of(path)).toList()) {
	          Files.delete(file);
	        }
		createNewFile("1.txt");
		createNewFile("2.txt");
		Field dir = GitRepository.class.getDeclaredField("DIRECTORY");
		dir.setAccessible(true);
		dir.set(null, path);
		
		if (Files.exists(Path.of(GIT_FILE))) {
		Files.delete(Path.of(GIT_FILE));
		}
	}

	List<String> expected = new ArrayList<String>(Arrays.asList(new String[] {"1.txt", "2.txt", "3.txt"}));
	
	@Test
//	@Disabled
	@Order(1)
	void commitsTest() throws IOException  {
		rep.commit("first");
		createNewFile("3.txt");
		assertEquals("Commit's successfully done", rep.commit("second"));		
		assertEquals(expected, getCommitContent(rep.getHead()));
		assertEquals("No files to commit", rep.commit("third"));
	}


	private List<String> getCommitContent(String commitName) {
		List<String> lastCommit = rep.commitContent(commitName).stream().collect(Collectors.toList());
		lastCommit.sort(Comparator.naturalOrder());
		return lastCommit;
	}


	private static void createNewFile(String fileName) throws IOException {
		Files.createFile(Path.of(path + "\\" + fileName));
	}

	@Test
//	@Disabled
	@Order(2)
	void switchTest() throws IOException {
		createNewFile("4.txt");
		assertEquals("Commit's successfully done", rep.commit("third"));
		String commitName = rep.log().stream().filter(r -> r.getMessage().equals("second")).findFirst().get().getUniqueKey();
		rep.switchTo(commitName);
		assertEquals(expected, getCommitContent(rep.getHead()));
	}
	
	@Test
//	@Disabled
	@Order(3)
	void branchout() throws IOException {
		rep.createBranch(myBranch);
		rep.switchTo(rep.getHead());
		assertNull(rep.commitContent(rep.getHead()));
		createNewFile("5.txt");
		assertEquals("Commit's successfully done", rep.commit("forth"));
		createNewFile("6.txt");
		assertEquals("Commit's successfully done", rep.commit("fifth"));
	}
	
	@Test
	@Order(4)
//	@Disabled
	void deleteBranchTest() {
		rep.switchTo(DEFAULT_BRANCH);
		assertEquals(2, rep.branches().size());
		assertEquals("Branch "+myBranch+" is successfully deleted", rep.deleteBranch(myBranch));
		assertEquals(1, rep.branches().size());
	}
	
	@Test
//	@Disabled
	@Order(5)
	void deleteBranchForcedTest() throws IOException {
		String commitName = rep.log().stream().filter(r -> r.getMessage().equals("second")).findFirst().get().getUniqueKey();
		rep.switchTo(commitName);
		rep.createBranch(myBranch);
		rep.switchTo(rep.getHead());
		assertNull(rep.commitContent(rep.getHead()));
		createNewFile("7.txt");
		assertEquals("Commit's successfully done", rep.commit("_forth"));
		createNewFile("8.txt");
		assertEquals("Commit's successfully done", rep.commit("_fifth"));

		commitName = rep.log().stream().filter(r -> r.getMessage().equals("_forth")).findFirst().get().getUniqueKey();
		rep.switchTo(commitName);
		rep.createBranch("one more branch");
		createNewFile("9.txt");
		rep.commit("_sixth");
		
		rep.switchTo(DEFAULT_BRANCH);
		String str = rep.deleteBranch(myBranch);
		assertTrue(str.startsWith("There's a conjunction on the branch"));
		assertTrue(rep.deleteBranchForced(myBranch).contains("is successfully deleted"));
	}
	
	@Test
	void addPatternIgnored() {
		assertEquals("Incorrect filter", rep.addIgnoredFileNameExp("@1.txt"));
		assertEquals("Filter was added", rep.addIgnoredFileNameExp("^1.*"));
		rep.commit("after ignored filter");
		assertFalse(rep.commitContent(rep.getHead()).contains("1.txt"));
	}
	
	@Test
	@AfterAll
	static void saverestore() {	
		String structBefore = rep.viewAllCommits();
		rep.save();
		rep = GitRepository.init();
		String structAfter = rep.viewAllCommits();
		assertEquals(structBefore, structAfter);

	}
	

}
