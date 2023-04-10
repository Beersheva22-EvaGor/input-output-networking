package telran.mygit;

import java.io.Serializable;
import java.util.List;

public interface IStructureGit<T, M> extends Serializable{
	/**
	 * Switch to another existent branch
	 * @param branch
	 * @return true if switched
	 */
	boolean switchBranch(String branch);
	
	/**
	 * @return list of branch names. For current branch the asterisk follows the name
	 */
	List<String> branches();
	
	
	/** 
	 * @return data of the specified item id
	 */
	T getData(String id);
	
	/**
	 * @return id of the current (head) node
	 */
	String getHeadNode();
	boolean addFirstNode(T data, M message);
	boolean addNode(T data, M message);
	
	boolean gotoNode(String nodeId);
	
	/**
	 * @param branchName
	 * @param newName
	 * @return false if there's no branch with name "branchName", otherwise true. Throws RuntimeException if trying to rename default branch
	 */
	boolean renameBranch(String branchName, String newName);
	
	/**
	 * @return list of objects containing a pair of commit name  and commit message from the HEAD to a first commit
	 */
	List<M> log();
	void save(String pathName);
	void restore(String pathName);
	boolean createNewBranch (String branchName);
	
	/**
	 * Delete branch with the given name. Throw RuntimeError if the conjunction
	 * found and forced is false.
	 * 
	 * @param branch - name of the branch to delete
	 * @param forced - if true than delete the given branch and all the branches
	 *               branched out of this one, otherwise throws RuntimeException
	 * @return true if deleted
	 * @throws Exception
	 */
	boolean deleteBranch(String branch, boolean forced) throws Exception;

	String viewAllNodes();
}
