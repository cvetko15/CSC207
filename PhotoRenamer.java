package a2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PhotoRenamer {

	protected ArrayList<String> tagList; 
	protected static File tagFile;
	private static File logFile;
	private HashMap<String, TreeNode> pathToNode;
	public PhotoRenamer() throws ClassNotFoundException, IOException{
		
		// Serialize/
		tagFile = new File("tagList.ser");
		logFile = new File("Log.ser");
		this.tagList = new ArrayList<String>();
		this.pathToNode = new HashMap<String, TreeNode>();
		
        if (tagFile.exists()) {
        	this.readFromTagsFile(tagFile.getPath());
        } else if (!tagFile.exists()){
            tagFile.createNewFile();
        }
        
        if (logFile.exists()) {
        	 this.readLogsFromFile(logFile.getPath());
        } else {
             logFile.createNewFile();
        }
	}
	/**
	 * Return an array list of tags
	 * @return List of tags
	 */
	public  ArrayList<String> getTagList() {
		return this.tagList;
	}

	/**
	 * Set the this.tagList
	 * @param this.tagList
	 * 				The list of tags
	 */
	public  void setTagList(ArrayList<String> tagList) {
		this.tagList = tagList;
	}

	public Directory buildTree (File file){
			String path = file.getPath();
			try {
				this.readLogsFromFile(logFile.getPath());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Directory rootNode = null;
			if (this.pathToNode.containsKey(path)) {
				rootNode = (Directory) this.pathToNode.get(path);
			}else {
				rootNode = new Directory(file, file.getName(), FileType.DIRECTORY, null);
				this.pathToNode.put(path, rootNode);
			}
			buildTreeHelper(file, rootNode);
			// Serialize this log now
			this.saveMapToFile(logFile.getPath());
			return rootNode;
	}
	
	/**
	 * Build the tree of the given file
	 * @param file
	 * 			The file of the node
	 * @param curr
	 * 			The node we're building off of
	 */
	public void buildTreeHelper(File file, TreeNode curr) {
		
		// use the file.listFiles() method to get all the files/directories inside file
		File [] list = file.listFiles();
		
		// iterate though the files/directories inside file
		// if it is an Image, add it to the tree; if it is a directory recurse though it
		for(File child: list){
			String path = child.getPath();
			if (child.isDirectory()){
				// Deserialize path to node hashmap
				try {
					this.readLogsFromFile(logFile.getPath());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Directory childNode = null;
				if (this.pathToNode.containsKey(path)) {
					childNode = (Directory) this.pathToNode.get(child.getPath());
				}else {
					childNode = new Directory(child, child.getName(), FileType.DIRECTORY , curr);
					this.pathToNode.put(path, childNode);
					curr.addChild(childNode.filePath, childNode);
				}
				
				buildTreeHelper(child, childNode);
				this.pathToNode.put(curr.file.getPath(), curr);
				// Serialize this log now
				this.saveMapToFile(logFile.getPath());
				
			}else{		
				String extension = fileIsImage(child.getName());
				if (extension != null){
					Image childNode = null;
					if (this.pathToNode.containsKey(path)) {
						childNode = (Image) this.pathToNode.get(child.getPath());
						
					}else {
						childNode = new Image(child, child.getName(), FileType.IMAGE , curr, extension);
						this.pathToNode.put(path, childNode);
						curr.addChild(childNode.filePath, childNode);
						
					}
					// Serialize this log now
					this.pathToNode.put(curr.file.getPath(), curr);
					this.saveMapToFile(logFile.getPath());
					
				}
				
					}
			}
		}
	
	/**
	 * Add the given Tag to the image and directory logs
	 * @param img
	 * 			Image to be tagged
	 * @param tag
	 * 			The tag to be added
	 * @param dir
	 * 			The directory to which this image belongs to
	 */
	
	public  void addImageTag(Image img, String tag, Directory dir) {
		try {
			// Replace the paths and add a new tag
			
			((Directory) img.parent).addImageTag(img, tag);
			if (!this.tagList.contains(tag) ) {
				this.tagList.add(tag);
				this.saveTagsToFile(tagFile.getPath());
			}
			
			String path = img.filePath;
						
			img.addTag(tag);
			String newPath = img.filePath;
			
			this.pathToNode.remove(path);
			this.pathToNode.put(newPath, img);
			this.pathToNode.put(img.parent.filePath, img.parent);
			
			this.saveMapToFile(logFile.getPath());
			
		} catch (TagExistsException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Delete the given Tag from all images in the directory dir, keep track using the image and directory logs
	 * 
	 * @param tag
	 * 			The tag to be deleted
	 * @param dir
	 * 			The directory to which this image belongs to
	 */
	public  void deleteFromAllImages(String tag, Directory dir) {
		ArrayList<Image> Alist = dir.getListofTags().get(tag);
		if (Alist != null) {
			Image[] images = Alist.toArray(new Image[0]);
			for (Image img: images) {
				
				((Directory) img.parent).deleteImageTag(img, tag);
				
				String path = img.filePath;
				
				img.deleteTag(tag);
				
				String newPath = img.filePath;
				
				this.pathToNode.remove(path);
				this.pathToNode.put(newPath, img);
				this.pathToNode.put(img.parent.filePath, img.parent);
				
				this.saveMapToFile(logFile.getPath());
				
			}
		}
	}
	/**
	 * Add the given Tag to the image and directory logs
	 * @param img
	 * 			Image to be edited
	 * @param tag
	 * 			The tag to be deleted
	 * @param dir
	 * 			The directory to which this image belongs to
	 */
	public  void deleteImageTag(Image img, String tag, Directory dir) {
		
		// Replace the paths and delete a new tag
		
		((Directory) img.parent).deleteImageTag(img,tag);
		
		String path = img.filePath;
		
		img.deleteTag(tag);
		
		String newPath = img.filePath;
		
		this.pathToNode.remove(path);
		this.pathToNode.put(newPath, img);
		this.pathToNode.put(img.parent.filePath, img.parent);
		
		this.saveMapToFile(logFile.getPath());
		
	}
	
	/**
	 * Search for the given image by Tag
	 * @param tag
	 * 			Tag to be searched by
	 * @param dir
	 * 			The directory to which this image belongs to
	 * @return The images that contain this tag
	 */
	public  ArrayList<Image> searchImagesbyTag(String tag, Directory dir) {
		return dir.searchImagesbyTag(tag);
	}
	
	/**
	 * Find the image with the most tags
	 * @param current
	 * @return The image with the most tags
	 */
	public  Image mostTags(TreeNode current) {
		TreeNode newCurr = null;
		if (current.isImage()) {
			newCurr = (Image) mostTagsHelper(current.getParent(), (Image)current);
		}
		else {
			for (TreeNode child: current.getChildren()) {
				newCurr = mostTags(child);
			}
		}
		return (Image) newCurr;
		
	
	}
	
	/**
	 * Helper function for finding the image with the most tags
	 * @param curr 
	 * 			Current node
	 * @param max 
	 * 			Maximum number of tags an image has
	 * @return The image with the most tags
	 */
	private static Image mostTagsHelper(TreeNode curr, Image max) {
		Image newMax = max;
		if (curr.isImage()) {
			if (((Image) curr).getTags().size() >= max.getTags().size()) {
				newMax = (Image) curr;
			}
		}
		else {
			Collection<TreeNode> children = curr.getChildren();
			for (TreeNode child: children) {
				max = mostTagsHelper(child, max);
			}
			
			newMax = max;	
		}
		return (Image) newMax;	
	}
	
	/**
	 * Revert all the changes up until a certain point
	 * @param dir
	 * 			Directory to revert changes from
	 * @param begin
	 * 			The time to which we will revert the changes
	 */
	
	public  void revertChanges(Directory dir, Integer begin){
		
		String[][] log = dir.getnameLog().toArray(new String[0] [0]);
		int end = log.length - 1;

		if ( begin <= end) {
			while (end >= begin) {
				
				String[] nestedList = log[end];
				String change = nestedList[2];
				String ext = PhotoRenamer.fileIsImage(nestedList[1]);
				String original = new String();
				if (!nestedList[1].contains(" @")){
					original = nestedList[1];
				}
				else{
					original = nestedList[1].split(" @")[0] + ext;
				}
				Image imgNode = (Image) dir.findChildReverted(original, nestedList[3]);
				// Added
				
				if (change.startsWith("A")) {
					String newTag = change.split(" ")[1];
					deleteImageTag(imgNode, newTag, dir);
					
				}
				
				if (change.startsWith("D")) {
					String newTag = change.split(" ")[1];
					addImageTag(imgNode, newTag, dir);
				}
				
				if (change.startsWith("N")) {
					String[] oldTonew = (change.split(" : ")[1]).split(" -> ");
					String old = oldTonew[0];
					this.chooseOldName(imgNode, old);

				}
				end = end -1;
			}
		}
	}

	/**
	 * Get the most common tag
	 * @param dir
	 * @return the most common tag
	 */
	public  HashMap<Integer, ArrayList<String>> mostCommonTag(Directory dir){
		return dir.mostCommonTag();
	} 
	
	/**
	 * Get the time log of this directory 
	 * @param dir
	 * @return the most common tag
	 */
	public  StringBuffer getHistory(TreeNode dir) {
		return dir.getHistory();
		}

	/**
	 * Allow the user to choose from old names
	 * @param img
	 * 			The image we are editing
	 * @param dir
	 * 			The directory the image belongs to
	 */
	public  void chooseOldName(Image img, String name) {
		try{
			String path = img.filePath;
			
			img.chooseFromOldName(name);
			
			String newPath = img.filePath;
			
			this.pathToNode.remove(path);
			this.pathToNode.put(newPath, img);
			this.pathToNode.put(img.parent.filePath, img.parent);
			
			this.saveMapToFile(logFile.getPath());
			
		}catch (NonExistentNameException e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Return the extension of the file/node it it's an image
	 * @param name
	 * 			The name of the image
	 * @return The image's extension or null if this file is not an image
	 */
	private static  String fileIsImage(String name) {
		String[] allowedExtensions = {".jpg", ".gif", ".svg", ".jpeg", ".png", ".tiff", ".bmp", ".JPG"};
		for (String suff: allowedExtensions) {
			if (name.endsWith(suff)) {
				return suff;
			}
		}
		return null;
	}
	
	/**
	 * Return all the nodes in this image Tree 
	 * @param fileNode
	 * 				The root of the tree
	 * @param contents
	 * 				The String Buffer we are concatenating to
	 * @param prefix
	 * 				The prefix to append to
	 */
	public  void buildDirectoryContents(TreeNode fileNode, StringBuffer contents, String prefix) {
			
			// add the initial prefix and root FileNode
			contents.append(prefix);
			contents.append(fileNode.getName());
			// increase the prefix using the static variable DirctoryExplorer.PREFIX
			prefix = prefix + "--";
			
			// iterate though the children of the root node
			// if it is a file, add it; if it is a directory, recurse though it
			for (TreeNode child: fileNode.getChildren()){
				if (child.isImage()){
					contents.append('\n');  // '\n' character adds new line
					contents.append(prefix);
					contents.append(child.getName());
				} else{
					contents.append('\n');
					buildDirectoryContents(child, contents, prefix);
				}
}
	}

	/**
	 * Add more than one tag to the given image in the chosen directory
	 * @param tags
	 * 			The list of tags
	 * @param img
	 * 			The image the tags are to be added to
	 * @param dir
	 * 			The directory we are adding tags to 
	 */
	public  void addManyTags(String[] tags, Image img, Directory dir) {
		for (String tag: tags) {
			addImageTag(img, tag, dir);
		}
	}
    

 

	/**
	 * Save the structure to the given file
	 * @param filePath
	 * 				The filePath of the file we are writing to
	 * @param toStore
	 * 				The generic object toStore
	 */
	public  void saveMapToFile(String filePath){
		try {
	        OutputStream file = new FileOutputStream(filePath);
	        OutputStream buffer = new BufferedOutputStream(file);
	        ObjectOutput output = new ObjectOutputStream(buffer);
	
	        // serialize the Map
	        output.writeObject(this.pathToNode);
	        output.close();
		 } catch (IOException ex) {
	          System.out.println("Can't Save");
	        } 
    }
	
	/**
	 * Save the structure to the given file
	 * @param filePath
	 * 				The filePath of the file we are writing to
	 * @param toStore
	 * 				The generic object toStore
	 */
	public  void saveTagsToFile(String filePath){
		try {
	        OutputStream file = new FileOutputStream(filePath);
	        OutputStream buffer = new BufferedOutputStream(file);
	        ObjectOutput output = new ObjectOutputStream(buffer);
	
	        // serialize the Map
	        output.writeObject(this.tagList);
	        output.close();
		 } catch (IOException ex) {
	          System.out.println("Can't Save");
	        } 
    }
	

	@SuppressWarnings({ "unchecked" })
	public void readLogsFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            
            this.pathToNode = (HashMap<String, TreeNode>) input.readObject();
            
            input.close();
        } catch (IOException ex) {
          
        }  
        
	}
	
	@SuppressWarnings({ "unchecked" })
	public void readFromTagsFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            
            this.tagList = (ArrayList<String>) input.readObject();
            
            input.close();
        } catch (IOException ex) {
          
        }  
        
	}
	public HashMap<String, TreeNode> getPathToNode() {
		return this.pathToNode;
	}
}

