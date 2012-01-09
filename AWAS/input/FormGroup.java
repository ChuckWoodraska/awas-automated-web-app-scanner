package input;

import java.io.Serializable;
import java.util.ArrayList;

public class FormGroup  implements Serializable{
	
	private int formIndex;
	private String formID;
	private ArrayList<FormRecord> formInputs;

	
	public FormGroup(int formIndex, String formID, ArrayList<FormRecord> formInputs){
		this.formIndex = formIndex;
		this.formID = formID;
		this.formInputs = formInputs;
	}

	
	public void addFormInput(FormRecord input){
		formInputs.add(input);
	}
	
	public int getFormIndex(){
		return formIndex;
	}
	
	public String getFormID(){
		return formID;
	}
	
	public ArrayList<FormRecord> getFormInputs(){
		return formInputs;
	}

}