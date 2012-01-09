package input;

import java.io.Serializable;
import java.util.ArrayList;

public class ComboInput implements Serializable{

	private ArrayList<String> inputNames = new ArrayList<String>();
	private int numOfInputs = 0;
	
	public ComboInput(ArrayList<String> inputNames){
		this.inputNames = inputNames;
	}
	
	public ArrayList<String> getInputName(){
		return inputNames;
	}
	
	public void setInputName(ArrayList<String> inputNames){
		this.inputNames = inputNames;
	}
	
	public void setNumInputs(int num){
		this.numOfInputs = num;
	}
	
	public int getNumInputs(){
		return numOfInputs;
	}
	
	public int getSize(){
		return inputNames.size();
	}
}
