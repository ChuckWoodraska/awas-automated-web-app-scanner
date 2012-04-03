/* 	All Rights Reserved
	Author Dianxiang Xu
*/
package testtree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.CancellationException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

// Save/load test tree into/from an excel file
//		First row: test generation information	
//			version for dealing with compatibility of different versions 
//			model file name used to generate the test tree 
//			coverage used to generate the test tree
//		For each node in the tree
// 			One row for the firing: node id, transition index, substitution, #children, negative, marking
//			One row for each test input <expression, isParameter>
//

public class TestTreeFile {

	private static int rowIndex = 0; 
	
	static public void saveTestDataToExcelFile(JTree tree, File outputFile) throws Exception {
		// open file stream
		rowIndex = 0;
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Test data");
		writeHeader(tree, sheet);
		writeAllNodes(tree, sheet);
		FileOutputStream out = new FileOutputStream(outputFile);
	    wb.write(out);
		out.close();
 	}
	
	static private void writeHeader(JTree tree, Sheet sheet){
        Row headerRow = sheet.createRow(rowIndex++);
        Cell versionCell = headerRow.createCell(0);


        Cell midCell = headerRow.createCell(1);

        
        Cell coverageCell = headerRow.createCell(2);

 
/*        
        Cell languageCell = headerRow.createCell(3);
        languageCell.setCellValue(tree.getSystemOptions().getLanguageIndex());

        Cell testFrameworkCell = headerRow.createCell(4);
        testFrameworkCell.setCellValue(tree.getSystemOptions().getTestFrameworkIndex());
*/
	}
	
	static private void writeAllNodes(JTree tree, Sheet sheet) throws CancellationException{
		//MID mid = tree.getMID();
		
		TestNode root = (TestNode) tree.getModel().getRoot();
		writeRootFiring(root, sheet);		
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		for(int i = 0; i < root.getChildCount(); ++i)
		{
			queue.addLast(root.getChildAt(i));
		}
		while (!queue.isEmpty()) {
			//tree.checkForCancellation();
			TestNode node = (TestNode) queue.poll();
			writeNodeFiring(node, sheet);
			writeUserInput(node, sheet);
			for(int i = 0; i < node.getChildCount(); ++i)
			{
				queue.addLast(node.getChildAt(i));
			}
		}
	}

	// Separate from writeFiring to make it more efficient
	static private void writeRootFiring(TestNode node, Sheet sheet){
		// one row in excel file
        Row currentRow = sheet.createRow(rowIndex++);
        
        Cell nodeNumberCell = currentRow.createCell(0);
        nodeNumberCell.setCellValue(node.getID());

        Cell transitionIndexCell = currentRow.createCell(1);
       	transitionIndexCell.setCellValue("");

        Cell subsitutionCell = currentRow.createCell(2);
		subsitutionCell.setCellValue("");

		Cell numberOfChildrenCell = currentRow.createCell(3);
		int numberOfChildren = node.getChildCount();// node.hasChildren()? node.children().size(): 0; 
		numberOfChildrenCell.setCellValue(numberOfChildren);

		Cell negativeCell = currentRow.createCell(4);
		//negativeCell.setCellValue(node.isNegative());

		Cell markingCell = currentRow.createCell(5);
		markingCell.setCellValue("");

	}
	
	static private void writeNodeFiring(TestNode node, Sheet sheet){
		// one row in excel file
        Row currentRow = sheet.createRow(rowIndex++);
        
        Cell nodeNumberCell = currentRow.createCell(0);
        nodeNumberCell.setCellValue(node.getID());

        Cell titleCell = currentRow.createCell(1);
        titleCell.setCellValue(node.getTitle());

        Cell urlCell = currentRow.createCell(2);
		String urlString = node.getURL();
		urlCell.setCellValue(urlString);

		Cell numberOfChildrenCell = currentRow.createCell(3);
		int numberOfChildren = node.getChildCount();
		numberOfChildrenCell.setCellValue(numberOfChildren);

		Cell isFormCell = currentRow.createCell(4);
		isFormCell.setCellValue(node.isForm.toString());

		Cell markingCell = currentRow.createCell(5);
		//Marking marking = node.getMarking();
		//markingCell.setCellValue(marking.toString());
	}
	
	static private void writeUserInput(TestNode node, Sheet sheet){
//		ParaTableModel paraTable = node.getParaTable();
//		if (paraTable!=null) {
//			for (ParaRecord record: paraTable.getDataVector())
//				if (!record.getExpression().trim().equals("")) {
//					Row currentRow = sheet.createRow(rowIndex++);
//					Cell expressionCell = currentRow.createCell(0);
//					expressionCell.setCellValue(record.getExpression());
//					Cell isParameterCell = currentRow.createCell(1);
//					isParameterCell.setCellValue(record.isParameter());
//				}
//		}
	}

	//////////////////////////////////////////////////////
	// read test data from excel file	
	static public File getMidFileOfTestData(File testDataFile) {
		File midFile = null;
		try {
			HSSFWorkbook workBook = new HSSFWorkbook (new FileInputStream(testDataFile));
			Sheet sheet = workBook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			if (headerRow==null) {
				//frame.print(LocaleBundle.bundleString("Invalid test data format"));
				return null;
			}
			Cell versionCell = headerRow.getCell(0);
			Cell midCell = headerRow.getCell(1);	        
			if (versionCell==null || midCell==null){
				//frame.print(LocaleBundle.bundleString("Invalid test data format"));
				return null;
			}
		    midFile = new File(midCell.toString());
		    if (!midFile.exists()){
		    	String fileName = midFile.getName();
		    	String dir = testDataFile.getParent();
		    	midFile = new File(dir+File.separator+fileName);
		    	if (!midFile.exists()){
		    		//frame.print(LocaleBundle.bundleString("Cannot find model file")
		    		//		+" "+midCell.toString() + " "+LocaleBundle.bundleString("or")+" "+fileName+".");
		    		return null;
		    	}	
		    }
		}
		catch (Exception e) {
	//		e.printStackTrace();
			//frame.print(LocaleBundle.bundleString("Fail to load test data"));			
		}
		return midFile;
	}

	//////////////////////////////////////////////////////
	// read test data from excel file	
	public static void loadTestDataFromExcelFile(File testDataFile) {
		try {
			HSSFWorkbook workBook = new HSSFWorkbook (new FileInputStream(testDataFile));
			Sheet sheet = workBook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			//if (headerRow==null)
				//throw new Exception(LocaleBundle.bundleString("Invalid test data format"));
			Cell versionCell = headerRow.getCell(0);
			Cell midCell = headerRow.getCell(1);	        
			//if (versionCell==null || midCell==null)
				//throw new Exception(LocaleBundle.bundleString("Invalid test data format"));
//			updateSystemOptions(headerRow); 
			TestNode root = new TestNode("Root");
			JTree tree = new JTree(root, false);
//			if (mid.checkAttackTransition()==null){
//				for (Transition transition: mid.getTransitions())
//					if (transition.isAttackTransition())
//						mid.addHiddenPlaceOrEvent(transition.getEvent());
//			}	
			ProgressDialog progressDialog = new ProgressDialog(null, null, null);
			TestTreeLoader testImport = new TestTreeLoader(progressDialog, tree, testDataFile, sheet);
			Thread testImportThread = new Thread(testImport);
			testImportThread.start();
			progressDialog.setVisible(true);
			
		}
		catch (Exception e){
			//kernel.getFileManager().editNewFile(SystemOptions.DEFAULT_MODEL_TYPE);
			//kernel.print(LocaleBundle.bundleString("Invalid test data format"));
			System.out.println("Problem in loadTestDataFromExcelFile");
		}
		
	}
	
	static private void updateSystemOptions(Row headerRow) throws Exception{
		Cell coverageCell = headerRow.getCell(2);	    
		//if (coverageCell==null){
			//throw new Exception(LocaleBundle.bundleString("Invalid test data format"));
		
	}
	
	static public void loadAllNodes(ProgressDialog progressDialog, JTree tree, Sheet sheet) throws Exception {
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		
		TestNode root = readRootFiring(sheet);
		LinkedList<TestNode> queue = new LinkedList<TestNode>();
		queue.addLast(root);	
		while (!queue.isEmpty()) {
			//tree.checkForCancellation();
			TestNode node = queue.poll();
			//progressDialog.setMessage(LocaleBundle.bundleString("Loading")+" "+rowIndex+" "+LocaleBundle.bundleString("out of")+" "+numberOfRows+"...");
System.out.println("node: "+node);	
			int numberOfChildren = node.getNumberOfSuccessors();
System.out.println("#Children: "+numberOfChildren);	
			if (numberOfChildren>0) {
				for (int i=0; i<numberOfChildren; ++i){
					//tree.checkForCancellation();
					TestNode child = readNodeFiring(node, sheet);
					//readUserInput(child, sheet);
					//node.add(child);
					//System.out.println("#Children: "+i);	
				}
				for(int i = 0; i < node.getChildCount(); ++i)
				{
					queue.addLast((TestNode) node.getChildAt(i));
				}
			}
		}		
		//root.resetChildrenOutlineNumbers(tree.getSystemOptions().getMaxIdDepth());
		
	} 

	static TestNode readRootFiring(Sheet sheet) throws IOException {
		rowIndex = 1;
	    Row rootRow = sheet.getRow(rowIndex++);
	    if (rootRow==null) {
	    	System.out.println("root = null");	
	        //throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));	 
	    }
		Cell numberOfChildrenCell = rootRow.getCell(3);
		TestNode root = new TestNode("root");
		root.setNumberrOfSuccessors(getNumberOfChildren(numberOfChildrenCell.toString()));
		return root;
	}

	static private int getNumberOfChildren(String numberString) throws IOException{
		int numberOfChildren = 0;
		try {
			System.out.println("got children");	
			numberOfChildren = (int)Double.parseDouble(numberString);
			System.out.println("Number of Children"+numberOfChildren);
		}
		catch (NumberFormatException e){
			//throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));
		}
	   // if (numberOfChildren<0)	
			//throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));
 
//	   System.out.println("#children "+numberOfChildren);	    
	    return numberOfChildren;
	}
	
	static TestNode readNodeFiring(TestNode parent, Sheet sheet) throws IOException {
		
	    Row rootRow = sheet.getRow(rowIndex++);
	   //if (rootRow==null)
	       // throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));	        
	    Cell nodeNumberCell = rootRow.getCell(0);
System.out.println("Node number "+nodeNumberCell.toString());
	    //if (!parent.getOutlineNumber().equals(""))
	    	//assert nodeNumberCell.toString().startsWith(parent.getOutlineNumber()): LocaleBundle.bundleString("Incorrect test data format");
        Cell transitionIndexCell = rootRow.getCell(1);
        Cell substitutionCell = rootRow.getCell(2);
		Cell numberOfChildrenCell = rootRow.getCell(3);
		Cell negativeCell = rootRow.getCell(4);
		Cell markingCell = rootRow.getCell(5);
		
		//Transition transition = getTransition(transitionIndexCell.toString(), mid);
//System.out.println("Transition "+transition);

		//Substitution substitution = getSubstitution(substitutionCell.toString());
		//Marking marking = getMarking(markingCell.toString());
		//boolean negative = negativeCell.toString().equalsIgnoreCase("true");
		TestNode newNode = new TestNode(transitionIndexCell.toString(), nodeNumberCell.toString(), substitutionCell.toString(),false);
		newNode.setParent(parent);
		newNode.setNumberrOfSuccessors(getNumberOfChildren(numberOfChildrenCell.toString()));
System.out.println("read node: "+newNode);	
		
		return newNode;
	}

//	static private Transition getTransition(String transitionIndexString, MID mid) throws IOException {
//		int transitionIndex = -1;
//		try {
//			transitionIndex = (int)Double.parseDouble(transitionIndexString);
//		}
//		catch (NumberFormatException e){
//			throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));
//		}
//		if (transitionIndex==-1)	
//			return new Transition(MID.ConstructorEvent);
//		else 
//			return mid.getTransitionAtIndex(transitionIndex);
//	}
//	
//	static private Substitution getSubstitution(String substitutionString) throws IOException{
//		Substitution substitution = null; 
//		try {
//			substitution = MIDParser.parseSubstitutionString(substitutionString);
////			System.out.println("Substitution:" + substitution.printAllBindings());
//		}
//		catch (ParseException e){
//			throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));			
//		}
//		return substitution;
//	}
//	
//	static private Marking getMarking(String markingString) throws IOException {
//		Marking marking = new Marking(); 
//		if (markingString!=null && !markingString.equals(""))
//			try {
//				marking = MIDParser.parseMarkingString(markingString);
//			}
//			catch (ParseException e){
////				System.out.println("Marking: " + markingString);
//				throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));						
//			}	
//		return marking;
//	}

	static void readUserInput(TestNode node, Sheet sheet) {
		Row row = sheet.getRow(rowIndex);
//System.out.println("Columns: "+row.getPhysicalNumberOfCells());					
//		Vector<ParaRecord> dataVector = new Vector<ParaRecord>();
//		while (row!=null && row.getPhysicalNumberOfCells()==2) {
//			Cell expressionCell = row.getCell(0);
//			Cell isParameterCell = row.getCell(1);
////System.out.println("Exp: " + expressionCell.toString());			
//			if (expressionCell!=null && !expressionCell.toString().equals("")){
//				boolean isParameter = isParameterCell!=null && isParameterCell.toString().equalsIgnoreCase("true")? true: false;
//				dataVector.add(new ParaRecord(expressionCell.toString(), isParameter));
//				rowIndex++;
//				row = sheet.getRow(rowIndex);
//			}
//		}
//		if (dataVector.size()>0)
//			node.setParaTable(new ParaTableModel(dataVector));
	}
	
	
}
