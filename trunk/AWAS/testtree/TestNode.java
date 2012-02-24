package testtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import input.CombinationalInputs;
import input.ComboInput;
import input.FormGroup;
import input.FormRecord;
import input.TestOracle;
import input.UserInput;

import javax.swing.tree.DefaultMutableTreeNode;

public class TestNode extends DefaultMutableTreeNode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean displayTitle = true;
	
	private String url;
	private String id;	
	private FormGroup formRecords;
	public Boolean noTable = true;
	public Boolean isForm = false;
	public ArrayList<FormRecord> recordedInputs = new ArrayList<FormRecord>();
	
	public CombinationalInputs userInputCombos = new CombinationalInputs(new ArrayList<ComboInput>());
	private TestOracle testOracleData = new TestOracle();
	private ArrayList<UserInput> userInput;
	private ArrayList<UserInput> userInputInvalid;
	
	private Vector<Vector<Object>> validData = new Vector<Vector<Object>>();
	private Vector<Vector<Object>> invalidData = new Vector<Vector<Object>>();
	private Vector<Vector<Object>>  testOracleTable = new Vector<Vector<Object>>();
	public String[] columnNames;
	public String[] columnNamesTestOracleTable = {"#","Command", "Target", "Value"};
	public ArrayList<String> inputCombos = new ArrayList<String>();
	
	public TestNode(String title){
		super(title);
	}

	public TestNode(String title, String url){
		this(title);
		this.url = url;
	}
	
	public TestNode(String title, String url, String id){
		this(title);
		this.url = url;
		this.id = id;
	}
	
	public TestNode(String title, String url, String id, Boolean isForm){
		this(title);
		this.url = url;
		this.id = id;
		this.isForm = isForm;
	}
	
	public TestNode(String title, String url, String id, ArrayList<FormRecord> inputs, Boolean isForm){
		this(title);
		this.url = url;
		this.id = id;
		this.isForm = isForm;
		this.recordedInputs = inputs;
		
	}
	
	public String getURL(){		
		return url;
	}
	
	public String getID(){
		return id;
	}
	
	public String getTitle(){
		return super.toString();
	}
	
	public void setFormGroup( FormGroup  formRecords){
		this.formRecords = formRecords;
	}
	
	public FormGroup getFormGroups(){
		return formRecords;
	}
	
	public Boolean isForm()
	{
		return isForm;
	}
	
	public String getFullNodeInfo(){
		String inputs = "";
		for(int i = 0; i < recordedInputs.size(); ++i)
		{
			for(int j = 0; j < recordedInputs.get(i).getDataInputs().size(); ++j)
			inputs = inputs + " " + recordedInputs.get(i).getDataInputs().get(j).getInputValue();
		}
		for(int i = 0; i < recordedInputs.size(); ++i)
		{
			for(int j = 0; j < recordedInputs.get(i).getDataCombos().size(); ++j)
			inputs = inputs + " " + recordedInputs.get(i).getDataCombos().get(j);
		}
		return super.toString() +" "+url+" "+inputs;
	}
	
	public String toString(){
		String titleStr = displayTitle? " "+super.toString():"";
		String inputs = "";
		for(int i = 0; i < recordedInputs.size(); ++i)
		{
			for(int j = 0; j < recordedInputs.get(i).getDataInputs().size(); ++j)
			inputs = inputs + " " + recordedInputs.get(i).getDataInputs().get(j).getInputValue();
		}
		for(int i = 0; i < recordedInputs.size(); ++i)
		{
			for(int j = 0; j < recordedInputs.get(i).getDataCombos().size(); ++j)
			inputs = inputs + " " + recordedInputs.get(i).getDataCombos().get(j);
		}
		//System.out.println(titleStr.length());
		String tempStr = id!=null? id+"":"";
		tempStr = titleStr.length() > 1? tempStr + titleStr+ " " + inputs:tempStr+" "+"["+url+"]"+" "+inputs;
		return tempStr;
		//return (id!=null? id+titleStr + " " + inputs: titleStr+" "+inputs+"["+url+"]");
	}
	
	
	  public ArrayList<FormRecord> getRecordedInputs()
	  {
		  return recordedInputs;
	  }
	
	  public CombinationalInputs getCombinationalInput()
	  {
		  return userInputCombos;
	  }
	 
	  public ArrayList<UserInput> getUserInput()
	  {
		  return userInput;
	  }
	  
	  public  void setUserInput(ArrayList<UserInput> inputData)
	  {
		  this.userInput = inputData;
	  }
	 
	  public ArrayList<UserInput> getUserInputInvalid()
	  {
		  return userInputInvalid;
	  }
	  
	  public  void setUserInputInvalid(ArrayList<UserInput> inputData)
	  {
		  this.userInputInvalid = inputData;
	  }
	  
	  public TestOracle getTestOracle()
	  {
		  return testOracleData;
	  }
	  
	  public  void setTestOracle(TestOracle testOracleData)
	  {
		  this.testOracleData = testOracleData;
	  }
	  
	  public  void setId(int id)
	  {
		  this.id = id+"";
	  }
	  
	  public Vector<Vector<Object>> getValidData()
	  {
		  return validData;
	  }
	  
	  public Vector<Vector<Object>> getInvalidData()
	  {
		  return invalidData;
	  }
	  
	  public Vector<Vector<Object>> getTestOracleTable()
	  {
		  return testOracleTable;
	  }
	  
	  public void convertInputToVectors()
	  {
		  columnNames = new String[userInput.size()+1];
		  int count = 0;
	      	for(int i = 0; i < columnNames.length; ++i)
	      	{
	      		if(i == 0)
	      		{
	      			columnNames[0] = "#";
	      			count--;
	      		}
	      		else
	      		{
	      			columnNames[i] = userInput.get(count).getInputID();
	      		}
	      		//System.out.println(columnNames[i]+" ");
	      		count++;
	      	}	    
	      	
	      	int max = 10*(userInput.size()/10)+10;
	      	for(int row = 0; row < max; ++row)
	      	{
	      		Vector<Object> temp = new Vector<Object>();
	      			      		
	      		for(int col = 0; col < userInput.size(); ++col)
	      		{			
      					try{
      						temp.add(userInput.get(col).getUserInputData().get(row));  
      						//System.out.println(userInput.get(col).getUserInputData().get(row));
      					}
      					catch(IndexOutOfBoundsException e){
      						temp.add("");
      					}	      						      			
	      			      				
	      		}
	      		temp.insertElementAt(row+1, 0);
	      		//System.out.println(temp);
	      		validData.add(temp);
	      		
	      	}
	      	
	      	for(int row = 0; row < max; ++row)
	        {
	      		Vector<Object> invalidTemp = new Vector<Object>(); 
	      		for(int col = 0; col < userInputInvalid.size(); ++col)
	      		{			
      					try{
      						invalidTemp.add(userInputInvalid.get(col).getUserInputData().get(row));  
      					}
      					catch(IndexOutOfBoundsException e){
      						invalidTemp.add("");
      					}	      						      			
	      			      				
	      		}
	      		invalidTemp.insertElementAt(row+1, 0);
	      		invalidData.add(invalidTemp);
	        }
	      	
	      	max = 10*(testOracleData.getTestTypeData().size()/10)+10;
	      	for(int row = 0; row < max; ++row)
	      	{
	      		Vector<Object> temp = new Vector<Object>();
	      			
	      		for(int col = 0; col < 3; ++col)
	      		{			
      					try{
      						testOracleData.getTestTypeData().get(col);
      						if(col == 0)
      						{
      							temp.add(testOracleData.getTestTypeData().get(col));
      						}
      						else
      						{
      							for(int index = 0; index < testOracleData.getTestData().get(col).length; ++index)
          						{
          							temp.add(testOracleData.getTestData().get(col)[index]);
          						}
      						}
      						
      						//System.out.println(userInput.get(col).getUserInputData().get(row));
      					}
      					catch(IndexOutOfBoundsException e){
      						temp.add("");
      					}	      						      			
	      			      				
	      		}
	      		temp.insertElementAt(row+1, 0);
	      		//System.out.println(temp);
	      		testOracleTable.add(temp);
	      		
	      	}
	      	
	      	userInputCombos.getDataInputs().clear();
	      	ComboInput comboInput = new ComboInput(inputCombos);
	      	userInputCombos.getDataInputs().add(comboInput);
	      	
	      	
	  }
	  
	  @SuppressWarnings("null")
	public void convertVectorsToInput(TestNode node)
	  {

		  for(UserInput input : node.getUserInput())
		  {
				  input.getUserInputData().clear();		
		  }
		  
		  for(UserInput invalidInput : node.getUserInputInvalid())
		  {
				  invalidInput.getUserInputData().clear();			 
		  }
		  
		  testOracleData.getTestData().clear();
		  testOracleData.getTestTypeData().clear();
		  
				//System.out.println(userInput.get(0).getInputID());
		 
		  
		  for(Vector<Object> temp : node.getValidData())
		  {
				  for(int i = 1; i < temp.size(); ++i)
				  {
					  if(!temp.get(i).equals(""))
					  {
						  userInput.get(i-1).addUserInputData(temp.get(i).toString());
						  //System.out.println(temp.get(i).toString());				
					  }
					  
				  }			  
		  }		  
		  
		  for(Vector<Object> temp : node.getInvalidData())
		  {			  
				  for(int i = 1; i < temp.size(); ++i)
				  {
					 
				
					  if(!temp.get(i).equals(""))
					  {
						  userInputInvalid.get(i-1).addUserInputData(temp.get(i).toString());
						  //System.out.println(temp.get(i).toString());
					  }
						  
				  }			 
		  }		
		  
		  for(Vector<Object> temp : node.getTestOracleTable())
		  {			  
			  		String[] tempStr = {"",""};
				  for(int i = 1; i < 4; ++i)
				  {
					 
				
					  if(!temp.get(i).equals("") && i==1)
					  {
						  testOracleData.addTestOracleTypeData(temp.get(i).toString());
						  //System.out.println(temp.get(i).toString());
					  }
					  else if(!temp.get(i).equals("") && i==2)
					  {
						  tempStr[0] = temp.get(i).toString();						  
					  }
					  else if(!temp.get(1).equals(""))
					  {
						  tempStr[1] = temp.get(i).toString();
						  testOracleData.addTestOracleData(tempStr);						  
					  }
						  
				  }			 
		  }	
	  }	  
 
}
