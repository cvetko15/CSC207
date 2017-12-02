package a2;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TreeNode implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1137056245318029932L;
	private String name; 
	private FileType type;
	protected String filePath;
	protected TreeNode parent; 
	protected Map<String, TreeNode> children;
	protected File file;	
	protected ArrayList<String[]> nameLog;
	
	public TreeNode(File file, String name, FileType type, TreeNode parent, ArrayList<String[]> nameLogs, HashMap<String, TreeNode> children) {
		// For deserialized images
		this.name = name;
		this.type = type;
		this.parent = parent;
		this.children = children;
		this.file = file;
		this.nameLog = nameLogs;
		this.filePath = file.getPath();
		
	}
	
	public TreeNode(File file, String name, FileType type, TreeNode parent, HashMap<String,TreeNode> children) {
		// For freshly created TreeNodes
		this.name = name;
		this.type = type;
		this.parent = parent;
		this.file = file;
		this.children = children;
		this.nameLog = new ArrayList<String[]> ();
		this.filePath = file.getPath();
	}

	/**
	 * Get the name of this TreeNode
	 * @return the name of this TreeNode
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this TreeNode
	 * @param name
	 * 			 the name of this TreeNode
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the parent of this TreeNode
	 * @param the parent of this TreeNode
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * Set the parent of this TreeNode
	 * @return the parent of this TreeNode
	 */
	public void setParent(Directory parent) {
		this.parent = parent;
	}

	/**
	 * Get the children of this TreeNode, Path to image
	 * @return the parent of this TreeNode
	 */
	public Collection<TreeNode> getChildren() {
		return this.children.values();
	}
	
	/**
	 * Add the given child to TreeNode
	 * @param path
	 * @param childNode
	 */
	public void addChild(String path, TreeNode childNode) {
		this.children.put(path, childNode);
	}
	
	/**
	 * Checks whether this TreeNode is an image
	 * @return Returns true if TreeNode is an Image else, returns false 
	 */
	public boolean isImage() {
		return this.type == FileType.IMAGE;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * Get the name log of this image
	 * @return this TreeNode's image
	 */
	public ArrayList<String[]> getnameLog() {
		return nameLog;
	}
	
		
	/**
	 * Add the time stamp to this TreeNode and all its parents 
	 * @param name
	 * @param changeDescription
	 */
	public void putTimestamp(String name, String changeDescription) {
		String[] thisChange;
		
		if (this.parent != null){
			thisChange = new String[] {LocalDateTime.now().toString(), name, changeDescription, this.parent.file.getPath()};
			this.nameLog.add(thisChange);
			this.parent.putTimestamp(name, changeDescription);
		} else {
			thisChange = new String[] {LocalDateTime.now().toString(), name, changeDescription, "" };
		}
		this.nameLog.add(thisChange);	
		
	}
	
	/**
	 * Get String History of the TreeNode
	 * @return the history of the TreeNode as a String
	 */
	public StringBuffer getHistory(){
		Integer index = 0;
		StringBuffer contents = new StringBuffer();
		for (String[] nestedList: this.nameLog){
			contents.append(index.toString() +": "+ nestedList[0] + ", " + nestedList[1] + ", " + nestedList[2]);
			contents.append("\n");
			index = index +1;
		} 
		return contents;
	}
		}

	
	


