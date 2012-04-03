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
	private File testDataFile;
	private File midFile;
	private Sheet sheet;
	
	TestTreeLoader(ProgressDialog progressDialog, JTree tree, File testDataFile, Sheet sheet) {
		this.progressDialog = progressDialog;
		this.tree = tree;
		this.testDataFile = testDataFile;
		this.sheet = sheet;
	}
	
	public void run ()  {
		try {
			//tree.setProgressDialog(progressDialog);
			TestTreeFile.loadAllNodes(progressDialog, tree, sheet);
			progressDialog.dispose();
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
}
