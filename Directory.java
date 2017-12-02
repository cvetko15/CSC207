package a2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Directory extends TreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6204516908066748633L;
	private  HashMap<String,ArrayList<Image>> listofTags; 
	
	public Directory(File file, String name, FileType type, TreeNode parent, ArrayList<String[]> nameLogs, HashMap<String, TreeNode> children, HashMap<String, ArrayList<Image>> tagsToImages) {
		super(file, name, type, parent, nameLogs, children);
		this.listofTags = tagsToImages;
	}
	
	public Directory(File file, String name, FileType type, TreeNode curr) {
		super(file, name, type, curr, new HashMap<String, TreeNode>());
		this.listofTags = new HashMap <String,ArrayList<Image>> ();
	}
	
	public HashMap<String,ArrayList<Image>> getListofTags() {
		return listofTags;
	}
	
	/**
	 * Set a HashMap of tags to Images 
	 * 
	 * @param listofTags
	 * 					HashMap of tags to lists
	 */
	public void setListofTags(HashMap<String, ArrayList<Image>> listofTags) {
		this.listofTags = listofTags;
	}
	
	/**
	 * Find the current imageNode with the given name and parent path
	 * @param name
	 * 			Name of the image
	 * @param parent
	 * 			Parent of the image
	 * @return The Image Node or null
	 */
	public TreeNode findChild(String name, TreeNode parent) {
		return findChildNode(name, this, parent);
		
	}

	/**
	 * Helper function for FindChildNode
	 * @param name
	 * 			Name of the image
	 * @param curr
	 * 			Current node we are checking
	 * @param parent 
	 * 			The parent of the current node 
	 * @return The Image Node or null
	 */
	private TreeNode findChildNode(String name, TreeNode curr, TreeNode parent) {
		TreeNode result = null;
		
		if (curr != null) {
			String currName = curr.getName();
			
			// Check that the two nodes are the same file
			if (currName.equals(name)) {
				if ((curr.getParent() == null) && (parent == null)) {
					return curr;
				} else {
					String parentPath = parent.file.getPath();
					String currPath = curr.getParent().file.getPath();
					if (parentPath.equals(currPath))  {
						return curr;
					}
				}
					
			}else {
				if (!curr.isImage()) {
					Collection<TreeNode> currChildren = curr.getChildren();
					for (TreeNode child: currChildren){
						result = findChildNode(name, child, parent);
						if (result != null) {
							return result;
							}
							
						}
					}
				}
				
			}
			return result;
			
		}
	
	/**
	 * Find the current imageNode with the given name and parent path for the use of the reversions
	 * @param name
	 * 			Name of the image
	 * @param parentPath
	 * 			Parent path of the image
	 * @return The Image Node or null
	 */
	public TreeNode findChildReverted(String name, String parentPath) {
		return findChildNodeHelper(name, this, parentPath);
		
	}

	/**
	 * Reversion helper function
	 * @param name
	 * 			Name of the image
	 * @param curr
	 * 			The currentNode we are traversing
	 * @param parentPath
	 * 			Parent path of the image
	 * @return The Image Node or null
	 */
	private TreeNode findChildNodeHelper(String name, TreeNode curr, String parentPath) {
		TreeNode result = null;
		
		if (curr != null) {
			String currName = curr.getName();
			
			// Check that the two nodes are the same file
			if (curr.isImage()) {
				currName = ((Image) curr).getOriginalName();
			}
			if (currName.equals(name)) {
				if ((curr.getParent() == null) && (parentPath.equals(""))) {
					return curr;
				} else {
					String currPath = curr.getParent().file.getPath();
					if (parentPath.equals(currPath))  {
						return curr;
					}
				}
			}else {
				if (!curr.isImage()) {
					Collection<TreeNode> currChildren = curr.getChildren();
					for (TreeNode child: currChildren){
						result = findChildNodeHelper(name, child, child.getParent().file.getPath());
						if (result != null) {
							return result;
							}
							
						}
					}
				}
		}
		return result;
			
		}
	
	/**
	 * Add the image tag to the list of tags and to all of the parents 
	 * @param img
	 * @param tag
	 */
	public  void addImageTag(Image img, String tag) {
		Directory parent = (Directory) this.getParent();
		if ( parent != null) {
			parent.addImageTag(img, tag);
		}
			ArrayList<Image> images = new ArrayList<>();
			if (this.listofTags.containsKey(tag)) {
				images = this.listofTags.get(tag);
			}
			images.add(img);
			this.listofTags.put(tag,images);
	}
	
	/**
	 * Delete the image tag to the list of tags and to all of the parents 
	 * @param img
	 * @param tag
	 */
	public void deleteImageTag(Image img, String tag) {
		// Tags are shown on the screen
		Directory parent = (Directory) this.getParent();
		if ( parent != null) {
			parent.deleteImageTag(img,tag);
		}
		ArrayList<Image> images = new ArrayList<>();
		if (this.listofTags.containsKey(tag)) {
			images = this.listofTags.get(tag);
		
			images.remove(img);
		
			if (images.isEmpty()) {
				this.listofTags.remove(tag);
			}
			else {
				this.listofTags.put(tag,images);
			}
		}
	}
	
	/**
	 * Search the images by tag
	 * @param tag
	 */
	public  ArrayList<Image> searchImagesbyTag(String tag) {
		// Search image files based on tags
		return this.listofTags.get(tag);
		
	}
	
	/**
	 * Find the most common tag in this directory
	 * @return Number of tags to tag names
	 */
	public  HashMap<Integer, ArrayList<String>> mostCommonTag(){
		Set<String> tags = this.listofTags.keySet();
		int max = 0;
		HashMap <Integer, ArrayList<String>> countofTags = new HashMap <Integer, ArrayList<String>>();
		ArrayList<String> mostTags = new ArrayList<String>();
		for (String thisTag: tags) {
			int size = this.listofTags.get(thisTag).size();
			if (size>max){
				mostTags = new ArrayList<String>();
				mostTags.add(thisTag);
				max = size;
			}
			else if (size == max) {
				mostTags.add(thisTag);	
					
				}
				
				}
		countofTags.put(max, mostTags);
		return countofTags;
	} 
	
	
}
