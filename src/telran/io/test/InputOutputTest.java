package telran.io.test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class InputOutputTest {
	private static final int SPACES_LEVEL = 2;
	String fileName = "myFile";
	String directoryName = "myDirectory1/myDirectory2";

	@BeforeEach
	void setUp() throws Exception {
		new File(fileName).delete();
		new File(directoryName).delete();
	}

	@Test
	@Disabled
	void testFile() throws IOException {
		File f1 = new File(fileName);
		assertTrue(f1.createNewFile());
		File dir1 = new File(directoryName);
		assertTrue(dir1.mkdirs());
		System.out.println(dir1.getAbsolutePath());

	}

	@Test
	void printDirectoryFileTest() throws IOException {
		System.out.println("*********************************Applying class File*********************");
		printDirectoryFile("..", 2);
	}

	void printDirectoryFile(String path, int maxLevel) throws IOException {
		// TODO based on class File
		// path -directory path
		// maxLevel - maximal level of printing, if maxLevel < 1, all levels should be
		// printed
		// output format
		// <directory name (no points, no full absolute path)
		// <node name> - dir | file
		// <node_name> .....
		// <node name> -
		// <node name> - dir | file
		// <node_name> .....
		// <node name> -
		File root = new File(path);
		if (root.isDirectory()) {
			if (maxLevel < 1) {
				maxLevel = Integer.MAX_VALUE;
			}
			System.out.println(root.getCanonicalFile().getName());
			printDirectory(root.listFiles(), maxLevel, 1);
		}
	}

	private void printDirectory(File[] listFiles, int maxLevel, int level) {
		if (level <= maxLevel) {
			Arrays.stream(listFiles).forEach(node -> {
				System.out.printf("%s%s - %s\n", " ".repeat(level * SPACES_LEVEL), node.getName(),
						node.isFile() ? "file" : "dir");
				if (node.isDirectory()) {
					printDirectory(node.listFiles(), maxLevel, level + 1);
				}
			});
		}

	}

	@Test
	@Disabled
	void testFiles() {
		Path path = Path.of(".");
		System.out.println(path.toAbsolutePath().getNameCount());

	}

	@Test
	void printDirectoryFilesTest() throws IOException {
		System.out.println("*********************************Applying Class Files*********************");
		printDirectoryFiles("..", 2);
	}

	void printDirectoryFiles(String path, int maxLevel) throws IOException {
		// TODO based on class Files
		// path -directory path
		// maxLevel - maximal level of printing, if maxLevel < 1, all levels should be
		// printed
		// output format
		// <directory name (no points, no full absolute path)
		// <node name> - dir | file
		// <node_name> .....
		// <node name> -
		// <node name> - dir | file
		// <node_name> .....
		// <node name>

		Path directory = Path.of(path);
		if (Files.isDirectory(directory)) {
			directory = directory.toAbsolutePath().normalize();
			int directoryLevel = directory.getNameCount();
			Files.walk(directory, maxLevel).forEach(node -> {
				System.out.printf("%s%s - %s\n",
						" ".repeat((node.getNameCount() - directoryLevel) * SPACES_LEVEL),
						node.getFileName(), Files.isDirectory(node) ? "dir" : "file");
			});
		}

	}

}