package input;

import java.util.ArrayList;

public class FormRecord {
	
	private int formIndex;
	private ArrayList<InputRecord> dataInputs;
	private ArrayList<Integer> dataCombinations;
	private String submitButtonID; 	// the ID of the normal/image submit buttons
	
	public FormRecord(int formIndex, ArrayList<InputRecord> dataInputs, ArrayList<Integer> dataCombinations, String submitButtonID){
		this.formIndex = formIndex;
		this.dataInputs = dataInputs;
		this.dataCombinations = dataCombinations;
		this.submitButtonID = submitButtonID;
	}

	
	public void addDataInput(InputRecord input){
		dataInputs.add(input);
	}
	
	public void addDataCombo(Integer combo){
		dataCombinations.add(combo);
	}
	
	public int getFormIndex(){
		return formIndex;
	}
	
	public ArrayList<InputRecord> getDataInputs(){
		return dataInputs;
	}
	
	public ArrayList<Integer> getDataCombos(){
		return dataCombinations;
	}
	
	public String getSubmitButtonID(){
		return submitButtonID;
	}
	
	public void setSubmitButtonID(String submitID){
		submitButtonID = submitID;
	}
}
