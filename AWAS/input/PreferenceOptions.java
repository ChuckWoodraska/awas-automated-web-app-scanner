package input;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.Serializable;
import java.util.ArrayList;

public class PreferenceOptions implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String PreferenceOptionsFileName = "preferences.dat";
	
	public int maxNode = 1000;
	public int maxLinks = 1000;
	public int waitTime = 1000;
	public boolean pairWise = true;
	public String saveFilePath = ".//";
	
	public PreferenceOptions(){
	}

	/////////////////////////////////////////////////////////
	//Editing preferences
	/////////////////////////////////////////////////////////	


	public int getMaxNode(){
		return maxNode;
	}

	public void setMaxNode(int maxNode){
		this.maxNode = maxNode;
	}
	
	public int getMaxLinks(){
		return maxLinks;
	}

	public void setMaxLinks(int maxLinks){
		this.maxLinks = maxLinks;
	}
	
	public int getWaitTime(){
		return waitTime;
	}

	public void setWaitTime(int waitTime){
		this.waitTime = waitTime;
	}
	
	public boolean getPairWise(){
		return pairWise;
	}

	public void setPairWise(boolean pairWise){
		this.pairWise = pairWise;
	}

	public String getSaveFilePath(){
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath){
		this.saveFilePath = saveFilePath;
	}
	/////////////////////////////////////////////////////////
	//Persistence
	/////////////////////////////////////////////////////////	
	public void savePreferenceOptionsToFile() {
		try {
			ReadWriteObj.write(this, PreferenceOptionsFileName);
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	public PreferenceOptions readPreferenceOptionsFromFile(){
		PreferenceOptions preferenceOptions = null;
		try {
			preferenceOptions = (PreferenceOptions) (ReadWriteObj.read(PreferenceOptionsFileName));
		}
		catch (Exception e) {
//			e.printStackTrace();
			preferenceOptions = new PreferenceOptions();
		}
		
		return preferenceOptions;
	}
	
}