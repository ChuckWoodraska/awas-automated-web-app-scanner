package input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public class TestOracle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> testTypeData = new ArrayList<String>();
	private ArrayList<String[]> testData = new ArrayList<String[]>();
	
	public TestOracle(){

	}
	
	public ArrayList<String> getTestTypeData()
	{
		return testTypeData;
	}
	
	public ArrayList<String[]> getTestData()
	{
		return testData;
	}
	
	public void setTestData(String[] data)
	{
		testData.add(data);
	}
	
	public void setTestTypeData(String data)
	{
		testTypeData.add(data);
	}
	
	public void addTestOracleData(String[] input){
		testData.add(input);
	}
	
	public void addTestOracleTypeData(String input){
		testTypeData.add(input);
	}
	
}