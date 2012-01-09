package input;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInput implements Serializable{
	
	private String inputID;
	private InputDataType.FormInputType inputType;
	public ArrayList<String> userInputData;
	
	public UserInput(String inputID, InputDataType.FormInputType inputType, ArrayList<String> userInputData){
		this.userInputData = userInputData;
		this.inputID = inputID;
		this.inputType = inputType;
	}

	public String getInputID(){
		return inputID;
	}
	
	public InputDataType.FormInputType getInputType(){
		return inputType;
	}
	
	public void addUserInputData(String input){
		userInputData.add(input);
	}
	
	public ArrayList<String> getUserInputData(){
		return userInputData;
	}
	
	public String getUserInputString(int index)
	{
		return userInputData.get(index);
	}
	
	public void setUserInputString(int index, String element)
	{
		userInputData.set(index, element);
	}
	
	public void setUserInputData(ArrayList<String> data)
	{
		userInputData = data;
	}
	
	public void clearUserInputData()
	{
		userInputData.clear();					  
	}
	
	public int getSize(){
		return userInputData.size();
	}

}
