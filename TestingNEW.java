package a2;

import java.io.File;
import java.io.IOException;

public class TestingNEW {

	public static void main (String[] args) throws ClassNotFoundException, IOException, NonExistentNameException {
		
		// Initialize System 
		PhotoRenamer pr = new PhotoRenamer();
		
		// Initialize tags
		String m = "Marija";
		String  l = "Lana";
		
		File file = new File ("/Users/Lana/Desktop/test1");
		
		// #2: Build Tree and print contents (in PhotoRenamer)
		Directory fileTree = pr.buildTree(file);
		StringBuffer contents = new StringBuffer();
	
		pr.buildDirectoryContents(fileTree, contents, "--");
		System.out.println(contents.toString());
		
		System.out.println("\n");
		
		// Initialize children of fileTree for testing purposes 
		Directory childDir = (Directory) fileTree.findChild("test2", fileTree);
		Image image1 = (Image) fileTree.findChild("Hello-World.png", fileTree);
		Image image2 = (Image) fileTree.findChild("newName.jpg",childDir);
		
		// #3 and #4: Adding one tag and adding a set of tags, and rename image (in Image)
		String [] listofTags = {l,m};
				
		pr.addManyTags(listofTags, image1, fileTree);
		System.out.println(image1.getName());
		pr.addImageTag(image2, m, fileTree);
		System.out.println(image2.getName());
		System.out.println("\n");
		
		// #5: Keeping track of tags
		System.out.println(pr.getTagList().toString());
		System.out.println(fileTree.getListofTags().toString());
		System.out.println(image1.getTags().toString());
		System.out.println("\n");
		
		// #6: Keeping track of history and revering, choose from old name
		System.out.println(pr.getHistory(fileTree).toString());
		System.out.println("\n");		
		pr.chooseOldName(image2, image2.getOriginalName());
		System.out.println(image2.getName());
		System.out.println("\n");
		pr.revertChanges(fileTree, 6);
		System.out.println(pr.getHistory(fileTree).toString());
		System.out.println("\n");
		
		// Re-add tag "Marija" for testing purposes to show exception 
		pr.addImageTag(image2, m, fileTree);
		System.out.println("\n");
		
		// EXTRA FEATURES! 
		System.out.println(pr.mostTags(fileTree)); // return image1
		System.out.println(pr.mostCommonTag(fileTree)); // return 2: Marija
		System.out.println(pr.searchImagesbyTag(m, fileTree));  // return image1 and image2
		pr.deleteImageTag(image1, m, fileTree);
		pr.deleteImageTag(image1, l, fileTree);
		pr.deleteFromAllImages(m, fileTree);
		pr.deleteFromAllImages(l, fileTree);
		System.out.println("\n");
		
		// List of all changes
		System.out.println(pr.getHistory(fileTree).toString());
 	} 
}
