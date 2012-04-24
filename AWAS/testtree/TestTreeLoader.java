/* 	All Rights Reserved
	Author Dianxiang Xu
*/
package testtree;

import java.io.File;
import java.util.concurrent.CancellationException;

import javax.swing.JTree;

import org.apache.poi.ss.usermodel.Sheet;

class TestTreeLoader implements Runnable {
	
	private ProgressDialog progressDialog;
	private JTree tree;
	private TestNode node;
	private File testDataFile;
	private File midFile;
	private Sheet sheet;
	
	TestTreeLoader(ProgressDialog progressDialog, File testDataFile, Sheet sheet) {
		this.progressDialog = progressDialog;
		//this.node = node;
		this.testDataFile = testDataFile;
		this.sheet = sheet;
	}
	
	public void run ()  {
		try {
			
			node = TestTreeFile.loadAllNodes(progressDialog, sheet);
			
			//progressDialog.dispose();
			System.out.println(node.getChildCount()+" Number of children getnode");
		}
		catch (CancellationException e){
			handleException();
		}
		catch (Exception e){
			handleException();
		}
	}
	
	private void handleException(){
		progressDialog.dispose();
	}
	
	public TestNode getNode()
	{
		return node;
	}
}
