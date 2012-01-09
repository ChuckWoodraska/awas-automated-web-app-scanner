package input;

public class TestType {

	public static final String KEYWORD_PARTIALTEXT 	= "text";
	public static final String KEYWORD_TEXTAREA = "textarea";
	public static final String KEYWORD_PASSWORD = "password";
	public static final String KEYWORD_CHECKBOX = "checkbox";
	public static final String KEYWORD_RADIO 	= "radio";
	public static final String KEYWORD_SUBMIT	= "submit";
	public static final String KEYWORD_BUTTON 	= "button";
	public static final String KEYWORD_IMAGE	= "image";
	public static final String KEYWORD_SELECTONE = "select-one";
	public static final String KEYWORD_SELECTMULTIPLE = "select-multiple";
	public static final String KEYWORD_UPLOAD 	= "file";
	public static final String KEYWORD_RESET 	= "reset";
	public static final String KEYWORD_HIDDEN 	= "hidden";


	public enum FormInputType {TEXTINPUT, TEXTAREAINPUT, PASSWORDINPUT, DROPDOWNLISTINPUT, CHECKBOXINPUT, RADIOINPUT, BUTTONINPUT, SUBMITINPUT, IMAGEINPUT, SELECTONEINPUT, SELECTMULTIPLEINPUT, UPLOADINPUT, RESETINPUT, COMBINATIONINPUT};

	public static FormInputType getFormInputType(String keyword){
		if (keyword.contentEquals(KEYWORD_PARTIALTEXT))
			return FormInputType.TEXTINPUT;
		else
		if (keyword.contentEquals(KEYWORD_TEXTAREA))
			return FormInputType.TEXTAREAINPUT;
		else
		if (keyword.contentEquals(KEYWORD_PASSWORD))
			return FormInputType.PASSWORDINPUT;
		else
		if (keyword.contentEquals(KEYWORD_CHECKBOX))
			return FormInputType.CHECKBOXINPUT;
		else
		if (keyword.contentEquals(KEYWORD_RADIO))
			return FormInputType.RADIOINPUT;
		else
			if (keyword.contentEquals(KEYWORD_SUBMIT))
				return FormInputType.SUBMITINPUT;
		else
			if (keyword.contentEquals(KEYWORD_IMAGE))
				return FormInputType.IMAGEINPUT;
		else
			if (keyword.contentEquals(KEYWORD_SELECTONE))
				return FormInputType.SELECTONEINPUT;
		else
			if (keyword.contentEquals(KEYWORD_SELECTMULTIPLE))
				return FormInputType.SELECTMULTIPLEINPUT;
		else
			if (keyword.contentEquals(KEYWORD_UPLOAD))
				return FormInputType.UPLOADINPUT;
		else
			if (keyword.contentEquals(KEYWORD_RESET))
				return FormInputType.RESETINPUT;
		else
			if (keyword.contentEquals(KEYWORD_BUTTON))
				return FormInputType.BUTTONINPUT;
		else 
		{
			System.out.println("Keyword of Input Type: "+keyword);			
		    return FormInputType.TEXTINPUT;	
		}
	}
}
