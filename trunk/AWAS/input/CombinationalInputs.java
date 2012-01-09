package input;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CombinationalInputs implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<ComboInput> combinations;
	
	public CombinationalInputs(ArrayList<ComboInput> combinations){
		this.combinations = combinations;
	}
	
	public void addComboInput(ComboInput input){
		combinations.add(input);
	}
	
	public ArrayList<ComboInput> getDataInputs(){
		return combinations;
	}
	
	public int getSize(){
		return combinations.size();
	}

}
