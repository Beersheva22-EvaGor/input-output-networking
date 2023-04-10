package telran.mygit;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.attribute.BasicFileAttributes;

public class GitRepository implements IGitRepository {
	private static final long serialVersionUID = 1L;
	private static final String DIRECTORY = ".";
	private static final String PATH_CONFIG = ".mygit.config";
	/**
	 * fileName -> fileName, size, date creation, date modifying
	 */
	private HashMap<FileAttributes, StatusGit> state = new HashMap<>();
	private List<String> ignoredFileNameExp = new ArrayList<>();
	private static IStructureGit<HashMap<FileAttributes, StatusGit>, CommitMessage> struct = new StructureGit<>();

	/* values to be possible to get from configuration file */
	private static String msg_noFilesCommit = "No files to commit";
	private static String msg_commited = "Commit's successfully done";
	private static String msg_branchAdded = "Branch %s was added";
	private static String msg_branchAlreadyExists = "Fault. Branch with the name %s already exists";
	private static String msg_branchRenamed = "Branch %s is renamed with the new name %s";
	private static String msg_renameDefaultBranch = "Prohibited to rename defalt branch";
	private static String msg_branchDeleted = "Branch %s is successfully deleted";
	private static String msg_HEADswitched = "HEAD switched to ";
	private static String msg_noSuchBruchExists = "No branch / commit with the name %s exists";
	private static String msg_saved = "All changes saved";
	private static String msg_filterAdded = "Filter was added" ;
	private static String msg_incorrectFilter = "Incorrect filter";
	private static String msg_noCommitedYet = "No commited yet";
	
	private static void readParameters() {
		try (FileReader reader = new FileReader(PATH_CONFIG)){
			Properties p = new Properties();
        	p.load(reader);
        	
        	msg_noFilesCommit = p.getProperty(msg_noFilesCommit);
        	msg_commited = p.getProperty(msg_commited);
        	msg_branchAdded =p.getProperty(msg_branchAdded);
        	msg_branchAlreadyExists = p.getProperty(msg_branchAlreadyExists);
        	msg_branchRenamed = p.getProperty(msg_branchRenamed);      	
        	msg_renameDefaultBranch = p.getProperty(msg_renameDefaultBranch);
        	msg_branchDeleted = p.getProperty(msg_branchDeleted);
        	msg_HEADswitched =p.getProperty(msg_HEADswitched);
        	msg_noSuchBruchExists = p.getProperty(msg_noSuchBruchExists);
        	msg_saved = p.getProperty(msg_saved);
        	msg_filterAdded = p.getProperty(msg_filterAdded);
        	msg_incorrectFilter = p.getProperty(msg_incorrectFilter);
        	msg_noCommitedYet = p.getProperty(msg_noCommitedYet);
        	
		} catch(Exception e) {
			// nothing to do because of default values
		}
	}
	
	/**
	 * put just files with statuses MODIFIED, UNTRACKED, COMMITED, IGNORED in state hash map
	 * 
	 * @throws IOException
	 */
	private void getSnapshot() throws IOException {
		List<Path> files = getPrefilteredFilesList();
		for (Path f : files) {
			BasicFileAttributes attr = Files.readAttributes(f, BasicFileAttributes.class);
			FileAttributes newFileAttr = new FileAttributes(f.getFileName().toString(), attr.size(), 
					attr.creationTime().toString(), attr.lastModifiedTime().toString());
			StatusGit status = getStatus(newFileAttr);
			if (status != StatusGit.IGNORED) {
				state.put(newFileAttr, status);
			}
		}
	}

	private void reloadStates() throws Exception {
		state = new HashMap<>();
		getSnapshot();
	}

	private StatusGit getStatus(FileAttributes newFile) {
		StatusGit res = null;
		HashMap<FileAttributes, StatusGit> statePrev = struct.getData(struct.getHeadNode());
		Set<FileAttributes> prevFiles = new HashSet<>();
		if (statePrev != null) {
			prevFiles = statePrev.keySet();
		}
		if (prevFiles.stream().filter(s -> s.equals(newFile)).findFirst().isEmpty()) {
			res = StatusGit.UNTRACKED;			
		}
		for (String regex : ignoredFileNameExp) {
			if (newFile.fileName().toString().matches(regex)) {
				res = StatusGit.IGNORED;
				break;
			}
		}
		if (res == null) {
			// if file was modified: modified name or dateModification but dateCreation is the same
			if (prevFiles.stream().filter(f -> f.dateCreation().equals(newFile.dateCreation()))
					.filter(f -> f.fileName().equals(newFile.fileName())
							|| f.dateModification().equals(newFile.dateModification()))
					.count() == 0) {
				res = StatusGit.MODIFIED;
			} else
				res = StatusGit.COMMITED;
		}

		return res;
	}

	/**
	 * Get list of files in assumption that no walking down the 1st level, no files
	 * starts with ".", no directories
	 */
	private List<Path> getPrefilteredFilesList() throws IOException {
		List<Path> res = new ArrayList<>();
		Path path = Path.of(DIRECTORY);
		try (Stream<Path> walk = Files.walk(path, 1)) {
			res = walk.filter(f -> !f.getFileName().toString().startsWith(".") && !Files.isDirectory(f))
					.collect(Collectors.toList());
		}
		return res;
	}

	public static IGitRepository init() {
		if (new File(GIT_FILE).exists()) {
			struct.restore(GIT_FILE);
		}
		IGitRepository rep = new GitRepository();
		readParameters();
		return rep;
	}

	@Override
	public String commit(String commitMessage) {
		try {
			reloadStates();
		} catch (Exception e) {
			return e.getMessage();
		}
		HashMap<FileAttributes, StatusGit> toCommit = getFilesToCommit();
		if (toCommit.size() == 0 ) {
			return msg_noFilesCommit;
		}
		CommitMessage message = new CommitMessage(commitMessage);
		if (struct.branches().size() == 0) {
			struct.addFirstNode(state, message);
		} else {
			struct.addNode(state, message);
		}

		return msg_commited;
	}

	private HashMap<FileAttributes, StatusGit> getFilesToCommit() {
		HashMap<FileAttributes, StatusGit> toCommit = new HashMap<>();		
		state.entrySet().stream().forEach(e ->  {
			if (e.getValue().name().equals(StatusGit.UNTRACKED.name()) || e.getValue().name().equals(StatusGit.MODIFIED.name())) {
				toCommit.put(e.getKey(), e.getValue());
			}
		}) ;
		return toCommit;
	}

	@Override
	public List<FileState> info() {
		try {
			reloadStates();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		return state.size() == 0 ? null
				: state.entrySet().stream().map(e -> new FileState(e.getKey().fileName().toString(), e.getKey().size(),
						e.getKey().dateCreation(), e.getKey().dateModification(), e.getValue()))
						.collect(Collectors.toList());
	}

	@Override
	public String createBranch(String branchName) {
		return struct.createNewBranch(branchName) ? String.format(msg_branchAdded, branchName)
				: String.format(msg_branchAlreadyExists, branchName);
	}

	@Override
	public String renameBranch(String branchName, String newName) {
		return struct.renameBranch(branchName, newName)
				? String.format(msg_branchRenamed, branchName, newName)
				: msg_renameDefaultBranch;
	}

	@Override
	public String deleteBranch(String branchName) {
		return deleteBranchChoice(branchName, false);
	}

	@Override
	public String deleteBranchForced(String branchName) {
		return deleteBranchChoice(branchName, true);
	}

	private String deleteBranchChoice(String branchName, boolean forced) {
		try {
			struct.deleteBranch(branchName, forced);
		} catch (Throwable e) {
			return e.getMessage();
		}
		return String.format(msg_branchDeleted, branchName);
	}

	@Override
	public List<CommitMessage> log() {
		return struct.log();
	}

	@Override
	public List<String> branches() {
		return struct.branches();
	}

	@Override
	public List<String> commitContent(String commitName) {
		var content = struct.getData(commitName);
		return content == null? null: content.keySet().stream().map(attr -> attr.fileName()).toList();
	}

	@Override
	public String switchTo(String name) {
		boolean res = struct.switchBranch(name);
		if (!res) {
			res = struct.gotoNode(name);
		}
		return res ? msg_HEADswitched + name : String.format(msg_noSuchBruchExists, name);
	}

	@Override
	public String getHead() {
		return struct.getHeadNode();
	}

	@Override
	public String save() {
		try {
			struct.save(GIT_FILE);
			return msg_saved;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@Override
	public String addIgnoredFileNameExp(String regex) {
		String symbols = "\\/\\\"';\\-~!&>#@\s";
		boolean res = regex.matches("^[^" + symbols + "_" + "][^" + symbols + "]*");
		if (res) {
			ignoredFileNameExp.add(regex);
		}
		return res ? msg_filterAdded : msg_incorrectFilter;
	}

	@Override
	public String viewAllCommits() {
		return struct.branches().size() == 0 ? msg_noCommitedYet : struct.viewAllNodes();
	}

}
