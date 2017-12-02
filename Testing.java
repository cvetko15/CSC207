package a2;

import java.io.File;
import java.io.IOException;


public class Testing {
	public static void main (String[] args) throws ClassNotFoundException, IOException {
		
		String m = "Marija";
		//String  l = "Lana";
		/*
		TreeNode root = new TreeNode("name", FileType.DIRECTORY, null);
		
		Image im1 = new Image("im1", tagList, root);
		//root.addChild("im1", im1);
		/*
		im1.addTag(m);
		im1.addTag(l);
		*/
		/*
		TreeNode root2 = new TreeNode("name2", FileType.DIRECTORY, root);
		root.addChild("name2", root2);
		Image im2 = new Image("im2", tagList2, root2);
		Image im3 = new Image("im3", tagList3, root2);
		
		root2.addChild("im2", im2);
		root2.addChild("im3", im3);
		*/
/*		
		im3.addTag(l);
		im3.addTag(m);
		im3.addTag(x);
		
		im2.addTag(l);*/
		
		PhotoRenamer pr = new PhotoRenamer();
		System.out.println(pr.getPathToNode());
		/*
		pr.addImageTag(im1, l);
		pr.addImageTag(im2, m);
		pr.addImageTag(im1, m);
		pr.addImageTag(im3, m);
		pr.addImageTag(im3, x);
		pr.addImageTag(im2, x);
		pr.addImageTag(im1, x);
		System.out.println(pr.mostCommonTag());
		System.out.println(root.getTags().toString());
		System.out.println(pr.getListofTags().toString());
		System.out.println(pr.mostTags(root).getName());
		pr.deleteFromAllImages(x);
		System.out.println(pr.getListofTags().toString());
		System.out.println(root.getTags().toString());
		//im1.deleteTag(m);
		//System.out.println(im1.imageLogs());
		//System.out.println(im2.imageLogs());
		//System.out.println(root2.imageLogs());
		//System.out.print(root.imageLogs());
		 * */
		//PhotoRenamer pr = new PhotoRenamer();
		File file = new File ("/Users/Lana/Desktop/test1");
		//Directory fileTree = new Directory(file, file.getName(), FileType.DIRECTORY, null);
		Directory fileTree = pr.buildTree(file);
		StringBuffer contents = new StringBuffer();
		pr.buildDirectoryContents(fileTree, contents, "--");
		System.out.println(contents.toString());
		
		//Directory childDir = (Directory) fileTree.findChild("sub", fileTree);
		//Directory grandDir = (Directory) fileTree.findChild("test3", childDir);
		Image image1 = (Image) fileTree.findChild("Hello-World.png", fileTree);
		//Image image2 = (Image) fileTree.findChild("newName.JPG",childDir);
		//System.out.println(pr.getHistory(fileTree).toString());
		//Image image3 = (Image) fileTree.findChild("Hello-World.png",grandDir);
		pr.addImageTag(image1, m, fileTree);
		pr.deleteImageTag(image1, m, fileTree);
		//pr.deleteImageTag(image1, l, fileTree);
		//pr.deleteImageTag(image1, l, fileTree);
		//pr.revertChanges(fileTree, 3);
		//pr.addImageTag(image1, l, fileTree);
		//pr.deleteImageTag(image1, x, fileTree);
		//System.out.println(image1.getName());
		//pr.addImageTag(image1, m,fileTree);
		//pr.addImageTag(image3, l,fileTree);
		
		//System.out.println(image1.getName());
		//System.out.println(fileTree.getListofTags());
		//pr.deleteImageTag(image3, l, fileTree);
		//System.out.println(fileTree.getListofTags());
		//System.out.println(childDir.getListofTags());
		//System.out.println(grandDir.getListofTags());
		//System.out.println(image1.getName());
		
		
		//pr.addImageTag(image2, l, childDir);
		//pr.addImageTag(image2, m, childDir);
//		
		//pr.revertChanges(fileTree, 3);
//		StringBuffer contents = new StringBuffer();
//		System.out.println(contents.toString());
//		pr.buildDirectoryContents(childDir, contents, "--");
//		System.out.println(contents.toString()); 
		
		//System.out.println(image2.getName());
		
		//pr.deleteImageTag(image2, l, fileTree);
		//pr.deleteImageTag(image1, l, fileTree);
		//System.out.println(pr.mostCommonTag(fileTree));
		//pr.deleteFromAllImages(l, fileTree);
		//pr.deleteFromAllImages(m, childDir);
		//pr.deleteImageTag(image1, m, fileTree);
		//System.out.println(fileTree.getListofTags());
		//System.out.println(childDir.getListofTags());
	
		//System.out.println(fileTree.getListofTags());
		//pr.revertChanges(fileTree, 4);

		//pr.deleteImageTag(image1, m, fileTree);
		//System.out.println(grandDir.getListofTags());
		//System.out.println(pr.allimageLogs(fileTree));
		//pr.deleteFromAllImages(m, fileTree);
		System.out.println(pr.getHistory(fileTree).toString());
		//pr.revertChanges(fileTree, 3);
		//System.out.println(pr.getHistory(fileTree).toString());
				
}
}
	

	
