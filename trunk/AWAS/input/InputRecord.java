package input;

public class InputRecord {

	private InputDataType.FormInputType inputType;
	private String inputName;
	private String inputValue;		// input value (for user provided text) OR input index (for GUI objects)
	
	public InputRecord(InputDataType.FormInputType inputType, String inputName, String inputValue){
		this.inputType = inputType;
		this.inputName = inputName;
		this.inputValue = inputValue;
	}
	
	public InputRecord(InputDataType.FormInputType inputType, String inputName, int inputValueIndex){
		this(inputType, inputName, inputValueIndex+"");
	}

	public InputDataType.FormInputType getInputType(){
		return inputType;
	}
	
	public String getInputName(){
		return inputName;
	}
	
	public String getInputValue(){
		return inputValue;
	}
	
	public int getInputValueIndex(){
		try {
			return Integer.parseInt(inputValue);
		}
		catch (Exception e){
			return 0;
		}
	}
}
