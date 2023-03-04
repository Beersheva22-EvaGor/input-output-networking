package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class InputOutputTest {
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
	}
	
	@Test
	@Disabled
	void testFiles() {
		Path path = Path.of(".");
		System.out.println(path.toAbsolutePath().getNameCount());
	}

	@Test
	void printDirectoryFileTest() {
		printDirectoryFile("D:\\Java\\Mine", 3);
	}
	
	void printDirectoryFile(String path, int maxLevel) {
		// maxLevel - maximal level of printing, if maxLevel < 1, all levels should be printed		
		File file = new File(path);
		System.out.println(printLevel("", new File[] {file}, maxLevel, 0 ));		
	}
	
	private String printLevel(String s, File[] files, int maxLevel, int currentLevel) {
		if ((maxLevel>0 && currentLevel <= maxLevel) || maxLevel < 1) {
			for (File f: files) {
				s +=format(f, currentLevel)+"\n";
				if (f.isDirectory()) {
					try { 	// try-catch is useful for crashed and some kind of system/unreadable folders
					s += printLevel("", f.listFiles(), maxLevel, currentLevel + 1);	
					} catch (Exception ex) {}	
				}
			}			
		}
		return s;
	}

	
	@Test
	void printDirectoryFilesTest() {
			printDirectoryFiles("D:\\Java\\Mine", 3);
	}
	
	void printDirectoryFiles(String _path, int maxLevel) {
		Path path = Path.of(_path);
		int initialCounter = path.toAbsolutePath().getNameCount();
		try {
			if (maxLevel<1) {
				maxLevel = Integer.MAX_VALUE;
			}		
			
	        try (Stream<Path> walk = Files.walk(path, maxLevel, new FileVisitOption[] {})) {
	        	walk.map(f ->format(f.toFile(),f.toAbsolutePath().getNameCount() - initialCounter))
	            		.forEach(System.out::println);;
	        }
		} catch (Exception ex) {}
		
	}

	private String format(File f, int level) {
		String s = f.getName();
		if (s.startsWith(".")) {
			s = s.replaceFirst(".", "");
		}
		if (f.isDirectory()) {
			s += " - dir";
		} else {
			s+= " - file";
		}
		return " ".repeat(3*level) + s;
	}
}
