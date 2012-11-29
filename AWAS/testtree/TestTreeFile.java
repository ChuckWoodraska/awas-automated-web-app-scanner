/* 	All Rights Reserved
	Author Dianxiang Xu
*/
package testtree;

import input.CombinationalInputs;
import input.ComboInput;
import input.InputDataType;
import input.TestOracle;
import input.UserInput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        Cell idHeaderCell = headerRow.createCell(0);
        idHeaderCell.setCellValue("Node ID");


        Cell titleHeaderCell = headerRow.createCell(1);
        titleHeaderCell.setCellValue("Node Title");
        
        Cell urlHeaderCell = headerRow.createCell(2);
        urlHeaderCell.setCellValue("Node URL");
        
        Cell childrenHeaderCell = headerRow.createCell(3);
        childrenHeaderCell.setCellValue("# of Children");
        
        Cell isFormHeaderCell = headerRow.createCell(4);
        isFormHeaderCell.setCellValue("Is Form?");
        
        Cell isSessionStartHeaderCell = headerRow.createCell(5);
        isSessionStartHeaderCell.setCellValue("Is Session Start?");
 
        Cell hasTableHeaderCell = headerRow.createCell(6);
        hasTableHeaderCell.setCellValue("No Table?");
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
        nodeNumberCell.setCellValue("Root");

        Cell transitionIndexCell = currentRow.createCell(1);
       	transitionIndexCell.setCellValue(node.getTitle());

        Cell subsitutionCell = currentRow.createCell(2);
		subsitutionCell.setCellValue(node.getURL());

		Cell numberOfChildrenCell = currentRow.createCell(3);
		int numberOfChildren = node.getChildCount();
		numberOfChildrenCell.setCellValue(numberOfChildren);

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
		isFormCell.setCellValue(node.isForm().toString());

		Cell isSessionStartCell = currentRow.createCell(5);
		isSessionStartCell.setCellValue(node.isSessionStart.toString());
		

		Cell hasTableCell = currentRow.createCell(6);
		hasTableCell.setCellValue(node.hasTable().toString());
		
		Cell formNameCell = currentRow.createCell(7);
		formNameCell.setCellValue(node.getFormName().toString());
		
		int userInputInvalidStart = 0;
		try{
			Cell userInput;
			node.convertVectorsToInput(node);
			int count = 8; // first 7 cells are there already
			userInput = currentRow.createCell(count);
			userInput.setCellValue(node.getUserInput().size());
			++count;
			for(int i = 0; i < node.getUserInput().size(); ++i)				
			{
				userInput = currentRow.createCell(count);
				userInput.setCellValue(node.getUserInput().get(i).getUserInputData().size());
				++count;
				userInput = currentRow.createCell(count);
				userInput.setCellValue(node.getUserInput().get(i).getInputID());
				++count;
				userInput = currentRow.createCell(count);
				userInput.setCellValue(node.getUserInput().get(i).getInputType().toString());
				++count;
				for(int j = 0; j < node.getUserInput().get(i).getUserInputData().size(); ++j)
				{
					userInput = currentRow.createCell(count);
					userInput.setCellValue(node.getUserInput().get(i).getUserInputData().get(j));
					count++;
				}
			}
			
			userInputInvalidStart = count;
			Cell userInputInvalid;
			userInputInvalid = currentRow.createCell(userInputInvalidStart);
			userInputInvalid.setCellValue(node.getUserInputInvalid().size());
			++userInputInvalidStart;
			for(int i = 0; i < node.getUserInputInvalid().size(); ++i)
			{
				userInputInvalid = currentRow.createCell(userInputInvalidStart);
				userInputInvalid.setCellValue(node.getUserInputInvalid().get(i).getUserInputData().size());
				++userInputInvalidStart;
				userInput = currentRow.createCell(userInputInvalidStart);
				userInput.setCellValue(node.getUserInputInvalid().get(i).getInputID());
				++userInputInvalidStart;
				userInput = currentRow.createCell(userInputInvalidStart);
				userInput.setCellValue(node.getUserInputInvalid().get(i).getInputType().toString());
				++userInputInvalidStart;
				for(int j = 0; j < node.getUserInputInvalid().get(i).getUserInputData().size(); ++j)
				{
					userInputInvalid = currentRow.createCell(userInputInvalidStart);
					userInputInvalid.setCellValue(node.getUserInputInvalid().get(i).getUserInputData().get(j));
					userInputInvalidStart++;
				}
			}
			
		
		}
		catch(Exception e){
		}
		if(userInputInvalidStart != 0)
		{
			try{
				Cell validTestOracle;
				
				int count = userInputInvalidStart;
				System.out.println(count);
				validTestOracle = currentRow.createCell(count);
				validTestOracle.setCellValue(node.getValidTestOracle().getTestTypeData().size());
				++count;
				
				
				//System.out.println(node.getValidTestOracle().getTestData().get());
				
				
				for(int oracleCount = 0; oracleCount < node.getValidTestOracle().getTestTypeData().size(); ++oracleCount)
				{
					for(int i = 0; i < 2; ++i)
					{
						if(i == 0)
							{
								validTestOracle = currentRow.createCell(count);
								validTestOracle.setCellValue(node.getValidTestOracle().getTestTypeData().get(oracleCount));
								++count;
								//System.out.println(count);
							}
							else
							{
								validTestOracle = currentRow.createCell(count);
								validTestOracle.setCellValue(node.getValidTestOracle().getTestData().get(oracleCount).length);
								//System.out.println(node.getValidTestOracle().getTestData().get(i));
								++count;
								for(int index = 0; index < node.getValidTestOracle().getTestData().get(oracleCount).length; ++index)
								{
									
									validTestOracle = currentRow.createCell(count);
									validTestOracle.setCellValue(node.getValidTestOracle().getTestData().get(oracleCount)[index]);
									++count;
								}
							}
						
		
					}
				}
				
				
				Cell invalidTestOracle;
				
				invalidTestOracle = currentRow.createCell(count);
				//System.out.println(count);
				invalidTestOracle.setCellValue(node.getInvalidTestOracle().getTestTypeData().size());
				++count;
				//System.out.println(count);

				for(int oracleCount = 0; oracleCount < node.getInvalidTestOracle().getTestTypeData().size(); ++oracleCount)
				{
					for(int i = 0; i < 2; ++i)
					{
						if(i == 0)
							{
								invalidTestOracle = currentRow.createCell(count);
								invalidTestOracle.setCellValue(node.getInvalidTestOracle().getTestTypeData().get(oracleCount));
								++count;
								//System.out.println(count);
							}
							else
							{
								invalidTestOracle = currentRow.createCell(count);
								invalidTestOracle.setCellValue(node.getInvalidTestOracle().getTestData().get(oracleCount).length);
								++count;
								for(int index = 0; index < node.getInvalidTestOracle().getTestData().get(oracleCount).length; ++index)
								{
									
									invalidTestOracle = currentRow.createCell(count);
									invalidTestOracle.setCellValue(node.getInvalidTestOracle().getTestData().get(oracleCount)[index]);
									++count;
									//System.out.println(count);
								}
							}
						
		
					}
				}
				userInputInvalidStart = count;
			}
			catch(Exception e){
			
			}
			
			try{
				Cell combinationData;
				int count = userInputInvalidStart;
				combinationData = currentRow.createCell(count);
				//System.out.println(count);
				combinationData.setCellValue(node.getCombinationalInput().getDataInputs().size());
				++count;
				for(int combinationCount = 0; combinationCount < node.getCombinationalInput().getDataInputs().size(); ++combinationCount)
				{
					combinationData = currentRow.createCell(count);
					combinationData.setCellValue(node.getCombinationalInput().getDataInputs().get(combinationCount).getSize());
					++count;
					for(int combinationInputCount = 0; combinationInputCount < node.getCombinationalInput().getDataInputs().get(combinationCount).getSize(); ++combinationInputCount)
					{
						combinationData = currentRow.createCell(count);
						combinationData.setCellValue(node.getCombinationalInput().getDataInputs().get(combinationCount).getInputName().get(combinationInputCount).toString());
						++count;
					}
				}
			}
			catch(Exception e){
				
			}
		}
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
	public static TestNode loadTestDataFromExcelFile(File testDataFile) {
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
		
//			if (mid.checkAttackTransition()==null){
//				for (Transition transition: mid.getTransitions())
//					if (transition.isAttackTransition())
//						mid.addHiddenPlaceOrEvent(transition.getEvent());
//			}	
			//ProgressDialog progressDialog = new ProgressDialog(null, "Load Tree", "Loading Tree...");
			TestTreeLoader testImport = new TestTreeLoader(null, testDataFile, sheet);
			//Thread testImportThread = new Thread(testImport);

			//testImportThread.start();
			//progressDialog.setVisible(true);
			TestNode root = TestTreeFile.loadAllNodes(null, sheet);
			//System.out.println(root.getChildCount());

			return root;
			
		}
		catch (Exception e){
			//kernel.getFileManager().editNewFile(SystemOptions.DEFAULT_MODEL_TYPE);
			//kernel.print(LocaleBundle.bundleString("Invalid test data format"));
			System.out.println("Problem in loadTestDataFromExcelFile");
		}
		TestNode node = null;
		return node;
		
	}
	
	static private void updateSystemOptions(Row headerRow) throws Exception{
		Cell coverageCell = headerRow.getCell(2);	    
		//if (coverageCell==null){
			//throw new Exception(LocaleBundle.bundleString("Invalid test data format"));
		
	}
	
	static public TestNode loadAllNodes(ProgressDialog progressDialog, Sheet sheet) throws Exception {
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		
		TestNode root = readRootFiring(sheet);
		LinkedList<TestNode> queue = new LinkedList<TestNode>();
		queue.addLast(root);	
		while (!queue.isEmpty()) {
			//root.checkForCancellation();
			TestNode node = queue.poll();
			//progressDialog.setMessage("Loading "+rowIndex+" out of "+numberOfRows+"...");
//System.out.println("node: "+node);	
			int numberOfChildren = node.getNumberOfSuccessors();
//System.out.println("#Children: "+numberOfChildren);	
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
		return root;
		
	} 

	static TestNode readRootFiring(Sheet sheet) throws IOException {
		rowIndex = 1;
	    Row rootRow = sheet.getRow(rowIndex++);
	    if (rootRow==null) {
	    	System.out.println("root = null");	
	        //throw new IOException(LocaleBundle.bundleString("Incorrect test data format"));	 
	    }
		Cell numberOfChildrenCell = rootRow.getCell(3);
		TestNode root = new TestNode(rootRow.getCell(0).toString(), rootRow.getCell(1).toString(), rootRow.getCell(2).toString());
		root.setNumberrOfSuccessors(getNumberOfChildren(numberOfChildrenCell.toString()));
		System.out.println("root fired");
		return root;
	}

	static private int getNumberOfChildren(String numberString) throws IOException{
		int numberOfChildren = 0;
		try {
			//System.out.println("got children");	
			numberOfChildren = (int)Double.parseDouble(numberString);
			//System.out.println("Number of Children"+numberOfChildren);
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
	    Cell idNumberCell = rootRow.getCell(0);
//System.out.println("Node number "+nodeNumberCell.toString());
	    //if (!parent.getOutlineNumber().equals(""))
	    	//assert nodeNumberCell.toString().startsWith(parent.getOutlineNumber()): LocaleBundle.bundleString("Incorrect test data format");
        Cell titleCell = rootRow.getCell(1);
        Cell urlCell = rootRow.getCell(2);
		Cell numberOfChildrenCell = rootRow.getCell(3);
		Cell isFormCell = rootRow.getCell(4);
		Cell isStartSessionCell = rootRow.getCell(5);
		Cell hasTableCell = rootRow.getCell(6);
		Cell formName = rootRow.getCell(7);
		Cell numOfValidInputs = rootRow.getCell(8);
		

		
		//Transition transition = getTransition(transitionIndexCell.toString(), mid);
//System.out.println("Transition "+transition);

		//Substitution substitution = getSubstitution(substitutionCell.toString());
		//Marking marking = getMarking(markingCell.toString());
		//boolean negative = negativeCell.toString().equalsIgnoreCase("true");
		TestNode newNode = new TestNode(titleCell.toString(), urlCell.toString(), idNumberCell.toString(),false);
		parent.add(newNode);
		newNode.setParent(parent);
		newNode.setNumberrOfSuccessors(getNumberOfChildren(numberOfChildrenCell.toString()));
		newNode.setIsForm(Boolean.valueOf(isFormCell.toString()));
		newNode.setIsSessionStart(Boolean.valueOf(isStartSessionCell.toString()));
		newNode.setTable(Boolean.valueOf(hasTableCell.toString()));
		newNode.setFormName(formName.getStringCellValue());
		
		if(newNode.isForm() && !newNode.hasTable())
		{
			//System.out.println("Transition "+idNumberCell.toString());
			Cell userInputID;
			Cell userInputType;
			Cell userInputDataTemp;
			
			UserInput temp ;
			ArrayList<String> userInputData;
			ArrayList<UserInput> userInputs = new ArrayList<UserInput>();
			
			Cell numOfValidInputData;
			String[] columnNames = new String[(int)Double.parseDouble(numOfValidInputs.toString())+1];
			columnNames[0] = "#";
			int count = 9; //Check to make sure this is right
			for(int i = 0; i < (int)Double.parseDouble(numOfValidInputs.toString()); ++i)
			{
				
				numOfValidInputData = rootRow.getCell(count);
				++count;
				userInputID = rootRow.getCell(count);
				columnNames[i+1] = userInputID.toString();
				++count;
				userInputType = rootRow.getCell(count);
				++count;
				userInputData = new ArrayList<String>();
				for(int j = 0; j < (int)Double.parseDouble(numOfValidInputData.toString()); ++j)
				{
					
					userInputDataTemp = rootRow.getCell(count);
					userInputData.add(userInputDataTemp.toString());
					++count;
				}
				
				temp = new UserInput(userInputID.toString(), InputDataType.getFormInputType(userInputType.toString()), userInputData);
			
				userInputs.add(temp);
			}
				newNode.columnNames = columnNames;
				newNode.setUserInput(userInputs);
				//System.out.println(newNode.getUserInput().get(0).getUserInputData());
				userInputs = new ArrayList<UserInput>();
				++count;
			for(int i = 0; i < (int)Double.parseDouble(numOfValidInputs.toString()); ++i)
			{
				numOfValidInputData = rootRow.getCell(count);
				++count;
				userInputID = rootRow.getCell(count);
				++count;
				userInputType = rootRow.getCell(count);
				++count;
				userInputData = new ArrayList<String>();
				for(int j = 0; j < (int)Double.parseDouble(numOfValidInputData.toString()); ++j)
				{
					
					userInputDataTemp = rootRow.getCell(count);
					userInputData.add(userInputDataTemp.toString());
					++count;
				}
				
				temp = new UserInput(userInputID.toString(), InputDataType.getFormInputType(userInputType.toString()), userInputData);
			
				userInputs.add(temp);
			}
				newNode.setUserInputInvalid(userInputs);
				//System.out.println("read node: "+newNode);	
			TestOracle tempOracle = new TestOracle();
			String testOracleDataType = "";
			String[] testOracleData = {"",""};
			Cell testOracleTemp;
			Cell numOfValidTestOracleData = rootRow.getCell(count);
			++count;
			for(int oracleCount = 0; oracleCount < (int)Double.parseDouble(numOfValidTestOracleData.toString()); ++oracleCount)	
			{
				testOracleTemp = rootRow.getCell(count);
				testOracleDataType = testOracleTemp.toString();
				++count;
				++count;
				testOracleTemp = rootRow.getCell(count);
				//System.out.println(testOracleTemp.toString());
				testOracleData[0] = testOracleTemp.toString();
				//System.out.println(testOracleData[0]);
				++count;
				testOracleTemp = rootRow.getCell(count);
				//System.out.println(testOracleTemp.toString());
				testOracleData[1] = testOracleTemp.toString();
				//System.out.println(testOracleData[1]);
				++count;
				tempOracle.addTestOracleTypeData(testOracleDataType);
				tempOracle.addTestOracleData(testOracleData);
			}

			newNode.setValidTestOracle(tempOracle);

			
			tempOracle = new TestOracle();
			testOracleDataType = "";
			String[] invalidTestOracleData = {"",""};
			Cell invalidTestOracleTemp;
			Cell numOfInvalidTestOracleData = rootRow.getCell(count);
			++count;
			for(int oracleCount = 0; oracleCount < (int)Double.parseDouble(numOfInvalidTestOracleData.toString()); ++oracleCount)	
			{
				invalidTestOracleTemp = rootRow.getCell(count);
				testOracleDataType = invalidTestOracleTemp.toString();
				++count;
				++count;
				invalidTestOracleTemp = rootRow.getCell(count);
				invalidTestOracleData[0] = invalidTestOracleTemp.toString();
				++count;
				invalidTestOracleTemp = rootRow.getCell(count);
				invalidTestOracleData[1] = invalidTestOracleTemp.toString();
				++count;
				tempOracle.addTestOracleTypeData(testOracleDataType);
				tempOracle.addTestOracleData(invalidTestOracleData);
			}
			newNode.setInvalidTestOracle(tempOracle);
					
			ArrayList<String> namesOfInputs = new ArrayList<String>();
			ComboInput inputName;
			ArrayList<ComboInput> comboInputs = new ArrayList<ComboInput>();
			CombinationalInputs tempCombination;
			String tempName = "";
			String tempCount = "";
			Cell combinationalInput;
			Cell numOfCombinationalInputs = rootRow.getCell(count);
			++count;
			for(int combinationalCount = 0; combinationalCount < (int)Double.parseDouble(numOfCombinationalInputs.toString()); ++combinationalCount)	
			{
				combinationalInput = rootRow.getCell(count);
				tempCount = combinationalInput.toString();
				++count;
				for(int combinationalInputCount = 0; combinationalInputCount < (int)Double.parseDouble(tempCount); ++combinationalInputCount)
				{
					combinationalInput = rootRow.getCell(count);
					tempName = combinationalInput.toString();
					++count;
					namesOfInputs.add(tempName);
					
				}
				inputName = new ComboInput(namesOfInputs);
				comboInputs.add(inputName);

			}
			tempCombination = new CombinationalInputs(comboInputs);
			newNode.setInputCombos(namesOfInputs);
			newNode.convertInputToVectors(newNode);
		}
		
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
