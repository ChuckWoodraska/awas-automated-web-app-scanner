package input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public class TestOracle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Integer> testTypeData = new ArrayList<Integer>();
	private ArrayList<String> testData = new ArrayList<String>();
	
	public TestOracle(){

	}
	
	public ArrayList<Integer> getTestTypeData()
	{
		return testTypeData;
	}
	
	public ArrayList<String> getTestData()
	{
		return testData;
	}
	
	public void setTestData(String data)
	{
		testData.add(data);
	}
	
	public void setTestTypeData(Integer data)
	{
		testTypeData.add(data);
	}
}