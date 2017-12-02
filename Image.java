package a2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Image extends TreeNode{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8929157819687300701L;
	private String originalName;	
	private String ext; 
	private ArrayList<String> tags;
	private File imageFile; 

	/**
	 * For Deserialized images
	 * @param file
	 * @param name
	 * @param tags
	 * @param nameLogs
	 * @param parent
	 * @param ext
	 */
	public Image(File file, String name, ArrayList<String> tags, ArrayList<String[]> nameLogs, TreeNode parent, String ext) {
		super(file, name,  FileType.IMAGE,parent, nameLogs, null);
		this.originalName = name;
		this.tags = tags;
		this.putTimestamp(this.originalName, "INITALIZED " +this.originalName);
		this.ext = ext;
		this.imageFile  = file;
		
		
	}
	/**
	 * For newly created 
	 * @param file
	 * @param name
	 * @param type
	 * @param parent
	 * @param ext
	 */
	public Image(File file, String name, FileType type, TreeNode parent, String ext) {
		super(file, name,type, parent, null);
		this.originalName = name;
		this.putTimestamp(this.originalName, "INITIALIZED "+this.originalName);
		this.ext = ext;
		this.imageFile  = file;
		this.tags = new ArrayList<String>();
	}
	
	/**
	 * Get the tags of this image
	 * @return the tags of this image
	 */
	public ArrayList<String> getTags() {
		return this.tags;
	}
	
	/**
	 * Get the extension of this image
	 * @return the extension of this image
	 */
	public String getExt() {
		return ext;
	}
	
	/**
	 * Set the extension of this image
	 * @param ext
	 *  	the extension of this image
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}
	
	/**
	 * Get the imageFile of this image
	 * @return the imagefile
	 */
	public File getImageFile() {
		return imageFile;
	}
	
	/**
	 * Set the imageFile of this image
	 * @param imageFile
	 */
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	
	/**
	 * Get the original name of this image
	 * @return the original of this image
	 */
	public String getOriginalName() {
		return this.originalName;
	}

	/**
	 * Set the name of this image with the made change. Also add this change to the time log
	 * @param name
	 * 			The new name
	 * @param changeDescription
	 * 						The change that has been made
	 */
	public void setName(String name, String changeDescription) {
		Path source = Paths.get(this.imageFile.getPath());
		try {
			Path newPath = source.resolveSibling(name);
			Files.move(source, newPath);
			
			this.imageFile = new File(newPath.toString());
			this.filePath = newPath.toString();
		} catch (IOException e) {
			System.out.println("Oops, we can't change " + super.getName() + " to " + name);
		}
		super.setName(name);
		this.putTimestamp(name, changeDescription);
		
	}

	/**
	 * Add tag to this image
	 * @param tag
	 * @throws TagExistsException
	 */
	public void addTag(String tag) throws TagExistsException{
		if (this.tags.contains(tag)) {
			throw new TagExistsException("This image already contains the tag: @" + tag + ", please select another tag.");
		}
		
		this.tags.add(tag);
		String currentName = super.getName();
		String[] nameExt = currentName.split(this.ext);
		String newName = nameExt[0] + " @" + tag + this.ext ;
		this.setName(newName, "ADD "+ tag);
	}
	
	/**
	 * Delete the tag of this image
	 * @param tagName
	 */
	public void deleteTag(String tagName) {
		if (this.tags.contains(tagName)) {
			this.tags.remove(tagName);
			String currentName = super.getName();
			String[] nameExt = currentName.split(this.ext);
			String extensionlessName = this.originalName.split(this.ext)[0];
			String newName = extensionlessName;
			String[] existingTags = (nameExt[0].split(" @"));
			for (String name: existingTags){
				if ((!name.equals(tagName) && (!name.equals(extensionlessName)))){
					newName += " @" + name;
				}
			
			}
			this.setName(newName + this.ext, "DELETE "+ tagName);
		}
	}	
	
	/**
	 * Change then name to the selected old name
	 * @param name
	 */
	public void chooseFromOldName(String name) throws NonExistentNameException{
		String[][] nestedList = this.nameLog.toArray(new String[0][0]);
		boolean found = false;
		for (String[] names: nestedList){
			if (names[1].equals(name)){
				String description = "NAME CHANGED FROM : " + this.getName() + " -> " + name;
				this.setName(name, description);
				found = true;
				break;
			}
		}

	  if (!found){
		throw new NonExistentNameException ("Name does not exist");
	}
	}
}

