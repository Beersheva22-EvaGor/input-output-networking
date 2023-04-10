package telran.mygit;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StructureGit<T, M extends IMessage> extends AbstractCollection<T> implements IStructureGit<T, M> {
	private static final long serialVersionUID = 1L;
	private static final String PATH_CONFIG = ".mygit.config";

	@SuppressWarnings("hiding")
	private class Node<T, M extends IMessage> implements Serializable {
		private static final long serialVersionUID = 1L;
		List<String> children;
		String parent = null;
		T data;
		M message;

		Node(T data, M message, String parent) {
			this.data = data;
			this.message = message;
			this.parent = parent;
			children = new ArrayList<>();
		}
	}

	
	private final String getUniqueCode(Node<T, M> node) {
		return node.message.getUniqueKey();
	}

	private HashMap<String, LinkedHashMap<String, Node<T, M>>> branches = new HashMap<>();
	private static final String DEFAULT_BRANCH = "master";
	private int size = 0;
	/** <currentBranch,currentNodeId> */
	private Map.Entry<String, String> head;

	public StructureGit() {
		setHead(DEFAULT_BRANCH, null);
		readParameters();
	}

	/* values to be possible to get from configuration file */
	private String error_addNode = "Go to last node of the branch or branch out from this node"; 
	private String error_deleteDefaultBranch = "Impossible to delete default branch";
	private String error_deleteBranch_HEADon ="Impossible to delete: current node is set on branch %s or subbranch";
	private String error_delete_conjunction = "There's a conjunction on the branch %s ";
	private String error_noCommit = "No commit to store";
	private String error_noBrunch = "No such branch exists: ";
	
	private void readParameters() {
		try (FileReader reader = new FileReader(PATH_CONFIG)){
			Properties p = new Properties();
        	p.load(reader);
        	
        	error_addNode = p.getProperty(error_addNode);
        	error_deleteDefaultBranch = p.getProperty(error_deleteDefaultBranch);
        	error_deleteBranch_HEADon =p.getProperty(error_deleteBranch_HEADon);
        	error_delete_conjunction = p.getProperty(error_delete_conjunction);
        	error_noCommit = p.getProperty(error_noCommit);
        	error_noBrunch = p.getProperty(error_noBrunch);
        	
		} catch(Exception e) {
			// nothing to do because of default values
		}
	}

	@Override
	public boolean addFirstNode(T data, M message) {
		createNewBranch(DEFAULT_BRANCH);
		addNode(data, message);
		return true;
	}

	@Override
	public boolean createNewBranch(String branchName) {
		boolean res = !branches.containsKey(branchName);
		if (res) {
			LinkedHashMap<String, Node<T, M>> nodes = new LinkedHashMap<>();
			nodes.put(null, null);
			branches.put(branchName, nodes);
			// put null node to the tree
			LinkedHashMap<String, Node<T, M>> nodesOfOldBranch = branches.get(head.getKey());
			Node<T, M> nodeToBeBranched = nodesOfOldBranch.get(head.getValue());
			// add children
			addChildToNode(nodeToBeBranched, branchName);
			// switch branch
			setHead(branchName, head.getValue());
		}
		return res;

	}


	@Override
	public Iterator<T> iterator() {
		List<T> list = branches.values().stream().map(e -> e.values()).flatMap(c -> c.stream()).filter(n -> n != null)
				.map(n -> n.data).collect(Collectors.toList());
		return list.iterator();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean addNode(T data, M message) {
		String branch = head.getKey();
		LinkedHashMap<String, Node<T, M>> nodes = branches.get(branch);
		boolean res = nodes != null;
		if (res) {
			Node<T, M> lastNode = lastNodeOfBranch(branch);
			if (head.getValue() != null && lastNode != null && !head.getValue().equals(getUniqueCode(lastNode))) {
				throw new RuntimeException(error_addNode);
			}			
			String parentBranch = getBranchByNodeId(head.getValue());
			Node<T, M> newNode = new Node<T, M>(data, message, parentBranch);
			String newNodeId = getUniqueCode(newNode);
			setHead(branch, newNodeId);
			size++;
			
			if (lastNode == null) {
				nodes.remove(lastNode);
			}
			nodes.put(getUniqueCode(newNode), newNode);
			addChildToNode(lastNode, head.getKey());
		}
		return res;
	}

	/**
	 * @param nodeParent
	 * @param newNode
	 */
	private void addChildToNode(Node<T, M> nodeParent, String branch) {
		if (nodeParent != null) {

			nodeParent.children.add(branch);
		}
	}

	@Override
	public boolean switchBranch(String branch) {
		boolean res = branches.containsKey(branch);
		if (res) {
			setHead(branch, getUniqueCode(lastNodeOfBranch(branch)));
		}
		return res;
	}

	private Node<T, M> lastNodeOfBranch(String branch) {
		if (branches.size() == 0) {
			return null;
		}
		Iterator<Map.Entry<String, Node<T, M>>> iterator = branches.get(branch).entrySet().iterator();
		Map.Entry<String, Node<T, M>> lastElement = null;
		while (iterator.hasNext()) {
			lastElement = iterator.next();
		}
		return lastElement.getValue();
	}

	@Override
	public boolean deleteBranch(String branch, boolean forced) {
		if (branch == DEFAULT_BRANCH) {
			throw new RuntimeException(error_deleteDefaultBranch);
		}
		boolean res = branches.containsKey(branch);
		if (!res) {
			throw new RuntimeException(error_noBrunch + branch);
		}

		if (head.getKey().equals(branch)) {
			throw new RuntimeException(
					String.format(error_deleteBranch_HEADon, branch));
		}

		List<Node<T, M>> nodes =  branches.get(branch).values().stream().collect(Collectors.toList());
		for (Node<T, M> node : nodes) {
			if (isJunction(node) && !forced) {
				if (forced) {
					List<String> children = node.children;
					for (String child : children) {
						String childBranch = branches.entrySet().stream()
								.filter(br -> br.getValue().keySet().contains(child)).findFirst().get().getKey();
						if (childBranch != branch) {
							deleteBranch(childBranch, true);
						}

					}
				} else {
					throw new RuntimeException(String.format(error_delete_conjunction, branch));
				}
			}
		}
		
		String parent = nodes.iterator().next().parent;
		for (Node<T,M> node :  branches.get(parent).values()) {
			List<String> children = node.children;
			for (int i = 0; i< children.size(); i++) {
				if (children.get(i).equals(branch)) {
					children.remove(i);
					break;
				}
			}
		}		
		
		branches.remove(branch);
		size -= nodes.size();
		return res;
	}

	private String getBranchByNode(Node<T, M> node) {
		return branches.entrySet().stream().filter(br -> br.getValue().values().contains(node)).findFirst().get()
				.getKey();
	}

	private String getBranchByNodeId(String nodeId) {
		return branches.entrySet().stream().filter(br -> br.getValue().keySet().contains(nodeId)).findFirst().get()
				.getKey();
	}

	private boolean isJunction(Node<T, M> node) {
		return node.children.size() > 1;
	}

	@Override
	public List<String> branches() {
		ArrayList<String> res = new ArrayList<>();
		branches.keySet().forEach(b -> {
			if (b.equals(head.getKey())) {
				b += "*";
			}
			res.add(b);
		});
		return res;
	}

	@Override
	public T getData(String id) {
		Node<T, M> res = getIds(id);
		return res == null ? null : res.data;
	}

	@Override
	public String getHeadNode() {
		return head.getValue();
	}

	@Override
	public boolean gotoNode(String nodeId) {
		Node<T, M> node = getIds(nodeId);
		boolean res = node != null;
		if (res) {
			setHead(getBranchByNode(node), nodeId);
		}
		return res;
	}

	private Node<T, M> getIds(String nodeId) {
		try {
			return branches.values().stream().flatMap(c -> c.entrySet().stream()).filter(e -> e.getKey().equals(nodeId))
					.findFirst().get().getValue();
		} catch (Exception e) {
			return null;
		}

	}

	private void setHead(String branch, String nodeId) {
		head = new AbstractMap.SimpleEntry<String, String>(branch, nodeId);
	}

	@Override
	public boolean renameBranch(String branchName, String newName) {
		boolean res = !branchName.equals(DEFAULT_BRANCH);
		if (res) {
			LinkedHashMap<String, Node<T, M>> nodes = branches.get(branchName);
			// change node that linked to this branch from children
			String parentBranch = nodes.entrySet().iterator().next().getValue().parent;
			LinkedHashMap<String, Node<T, M>> nodesParent = branches.get(parentBranch);
			nodesParent.values().forEach(n -> {
				if (n.children.contains(branchName)) {
					n.children.remove(branchName);
					n.children.add(newName);
				}
			});
			branches.remove(branchName);
			branches.put(newName, nodes);
			if (head.getKey().equals(branchName)) {
				setHead(newName, head.getValue());
			}
		}
		return res;
	}

	@Override
	public List<M> log() {
		List<M> res = new ArrayList<M>();
		boolean fl = false;
		List<Node<T, M>> allNodesOfBranch = new ArrayList<>();
		branches.get(head.getKey()).values().stream().forEach(allNodesOfBranch::add);
		for (int i = allNodesOfBranch.size() - 1; i > -1; i--) {
			Node<T, M> node = allNodesOfBranch.get(i);
			if (!fl && getUniqueCode(node).equals(head.getValue())) {
				fl = true;
			}
			if (fl) {
				res.add(node.message);
			}
		}
		return res;
	}

	@Override
	public String viewAllNodes() {
		String s = displayBranch(DEFAULT_BRANCH, 0);
		return s;
	}

	private String displayBranch(String branch, int level) {
		String space = " ".repeat(level * 3);
		String res = space + String.format("%s%s:\r\n", branch, branch.equals(head.getKey()) ? "*" : "");
		LinkedHashMap<String, Node<T, M>> nodes = branches.get(branch);
		for (var nodeEntry : nodes.entrySet()) {
			if (nodeEntry.getValue() != null) {
				String key = nodeEntry.getKey();
				res += String.format("%s-%s%s\r\n", space, nodeEntry.getValue().message, 
						head.getValue().equals(key) ? "*" : "");
				Node<T, M> node = nodeEntry.getValue();
				if (isJunction(node)) {
					for (String childBranch : node.children) {
						if (!childBranch.equals(branch)) {
							res += displayBranch(childBranch, ++level);
						}
					}
				}
			}
		}
		return res;
	}

	private class DataToStore<T, M extends IMessage> implements Serializable{
		private static final long serialVersionUID = 1L;
		HashMap<String, LinkedHashMap<String, Node<T, M>>> branches = new HashMap<>();
		Map.Entry<String, String> head;
		int size;
	}
	
	@Override
	public void save(String pathName) {
		if (size == 0) {
			throw new RuntimeException(error_noCommit);
		}
		DataToStore<T, M> data = new DataToStore<>();
		data.branches = storeBranches(branches);
		data.size = size;
		data.head = head;

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))) {
			output.writeObject(data);
			
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); // some error
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			DataToStore<T,M> data = (DataToStore<T,M>) input.readObject();
			branches = storeBranches(data.branches);
			setHead(data.head.getKey(), data.head.getValue());
			this.size = data.size;
		} catch (FileNotFoundException e) {
			// empty object but no error
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); // some error
		}

	}

	private HashMap<String, LinkedHashMap<String, Node<T, M>>> storeBranches(
			HashMap<String, LinkedHashMap<String, Node<T, M>>> branchesSource) {
		HashMap<String, LinkedHashMap<String, Node<T, M>>> branchesTo = new HashMap<>();
		for (var e : branchesSource.entrySet()) {
			 LinkedHashMap<String, Node<T, M>> map = new LinkedHashMap<>();
			 for (var v : e.getValue().entrySet()) {
				 var n = v.getValue();
				 Node<T, M> node = new Node<>(n.data, n.message, n.parent);
				 node.children = n.children;
				 map.put(v.getKey(), node);
			 }
			 branchesTo.put(e.getKey(), map);
		}
		return branchesTo;
	}
}
