package telran.mygit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesGit {
	private static final String FILE_NAME = ".mygit.config";
		
	private static void setProperties(String filename) throws IOException {
		Properties p = new Properties();
		p.setProperty("error_addNode", "Go to last node of the branch or branch out from this node");
		p.setProperty("error_deleteDefaultBranch", "Impossible to delete default branch");
		p.setProperty("error_deleteBranch_HEADon", "Impossible to delete: current node is set on branch %s or subbranch");
		p.setProperty("error_delete_conjunction", "There's a conjunction on the branch %s ");
		p.setProperty("error_noCommit", "No commit to store");
		p.setProperty("error_noBrunch", "No such branch exists: ");
		
		p.setProperty("msg_noFilesCommit", "No files to commit");
		p.setProperty("msg_commited", "Commit's successfully done");
		p.setProperty("msg_branchAdded", "Branch %s was added");
		p.setProperty("msg_branchAlreadyExists", "Fault. Branch with the name %s already exists");
		p.setProperty("msg_branchRenamed", "Branch %s is renamed with the new name %s");
		p.setProperty("msg_renameDefaultBranch", "Prohibited to rename defalt branch");
		p.setProperty("msg_branchDeleted", "Branch %s is successfully deleted");
		p.setProperty("msg_HEADswitched", "HEAD switched to ");
		p.setProperty("msg_noSuchBruchExists", "No branch / commit with the name %s exists");
		p.setProperty("msg_saved", "All changes saved");
		p.setProperty("msg_filterAdded", "Filter was added");
		p.setProperty("msg_incorrectFilter", "Incorrect filter");
		p.setProperty("msg_noCommitedYet", "No commited yet");
		
		// store the properties to a file
		p.store(new FileWriter(filename), "Parameters for myGit repository");
	}

	public static void main(String[] args) throws IOException {
		File file = new File(FILE_NAME);
		file.createNewFile();
		
		setProperties(FILE_NAME);
	}
}
