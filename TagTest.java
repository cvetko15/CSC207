package photo_renamer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** DISCLAIMER!!!!!!!!
 * The serialized files need to be cleared before the first test in order for these tests to pass
 *
 */
public class TagTest {
	
	private static PhotoSystem pr;
	private Directory root;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
		pr = PhotoSystem.getInstance();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String currentPath = new File("").getAbsolutePath();
		if (!currentPath.endsWith("/src")) {
			currentPath += "/src";
		}  
		this.root = pr.buildTree(new File( currentPath + "/testImage"));

	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test the master list of tags for the whole system when adding and deleting tags
	 */

	@Test
	public void testSystemTagList() {
		Image childNode = (Image) root.findChild("image.png", root);
		try {
			pr.addImageTag(childNode, "Marija", root);
		} catch (TagExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String expectedName = "image @Marija.png";
		assertEquals(expectedName, childNode.getName());
		
		ArrayList<String> expectedTagList = new ArrayList<String>();
		expectedTagList.add("Marija");
		assertEquals(expectedTagList, pr.getTagList());	
		
		pr.deleteImageTag(childNode, "Marija", root);
		
		String expectedName2 = "image.png";
		assertEquals(expectedName2, childNode.getName());
				
	}
	
	/**
	 * Test the directory list of tags
	 */
	@Test
	public void testDirectoryTagList(){
		Image childNode = (Image) root.findChild("folder.png", root);
		try {
			pr.addImageTag(childNode, "Marija", root);
		} catch (TagExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String expectedName = "folder @Marija.png";
		assertEquals(expectedName, childNode.getName());
		
		HashMap<String,ArrayList<Image>> expectedTagList = new HashMap<String,ArrayList<Image>>();
		ArrayList<Image> listofImages = new ArrayList<Image> ();
		listofImages.add(childNode);
		expectedTagList.put("Marija", listofImages);
		assertEquals(expectedTagList, root.getListofTags());	
		
		pr.deleteImageTag(childNode, "Marija", root);
		
		String expectedName2 = "folder.png";
		assertEquals(expectedName2, childNode.getName());
	}
	
	/**
	 * Test the list of tags when adding to many images
	 */
	@Test
	public void testtoMany(){
		Image childNode1 = (Image) root.findChild("folder.png", root);
		Image childNode2 = (Image) root.findChild("image.png", root);
		Image childNode3 = (Image) root.findChild("fullFolder.png", root);
		try {
			pr.addImageTag(childNode1, "Marija", root);
			pr.addImageTag(childNode2, "Marija", root);
			pr.addImageTag(childNode3, "Marija", root);
		} catch (TagExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String,ArrayList<Image>> expectedTagList = new HashMap<String,ArrayList<Image>>();
		ArrayList<Image> listofImages = new ArrayList<Image> ();
		listofImages.add(childNode1);
		listofImages.add(childNode2);
		listofImages.add(childNode3);
		expectedTagList.put("Marija", listofImages);
		assertEquals(expectedTagList, root.getListofTags());	
		
		ArrayList<String> expectedTagList2 = new ArrayList<String>();
		expectedTagList2.add("Marija");
		assertEquals(expectedTagList2, pr.getTagList());	
		
		pr.deleteFromAllImages( "Marija", root);
		
		assertTrue(root.getListofTags().isEmpty());
	}

}
