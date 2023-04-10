package telran.mygit;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public interface IGitRepository extends Serializable{
	String GIT_FILE = ".mygit";
	String commit(String commitMessage);
	List<FileState> info();
	String createBranch(String branchName);
	String renameBranch(String branchName, String newName);
	/**
	 * Doesn't delete branch if there're subbranches, delete otherwise
	 * @param branchName
	 * @return
	 */
	String deleteBranch(String branchName);
	
	/**
	 * Delete branch with all subbranches if they exist
	 * @param branchName
	 * @return
	 */
	public String deleteBranchForced(String branchName);
	/**
	 * @return list of objects containing a pair of commit name and commit message from the HEAD to a first commit
	 */
	List<CommitMessage> log();
	List<String> branches(); //list of branch names
	/**
	 * @param commitName
	 * @return list of the Path objects of files included in a specified commit
	 */
	List<String> commitContent(String commitName);
	String switchTo(String name); //name is either a commit name or a branch name
	String getHead(); //return null if head refers commit with no branch
	String save(); //saving to .mygit serialization to file (Object Stream)
	String addIgnoredFileNameExp(String regex);
	String viewAllCommits();
}
