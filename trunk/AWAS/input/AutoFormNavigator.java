package input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverCommandProcessor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

import testtree.TestNode;

import combinatorial.AllCombinations;
import combinatorial.AllPairs;


public class AutoFormNavigator  {

	public static final String KEYWORD_FORM 	= "form";
	public static final String KEYWORD_NAME 	= "name";
	public static final String KEYWORD_TYPE 	= "type";
	public static final String KEYWORD_ID		= "id";
	public static final String KEYWORD_VALUE	= "value";
	public static final String KEYWORD_INPUT 	= "input";
	public static final String KEYWORD_SELECT 	= "select";
	
	public AutoFormNavigator(CombinationalInputs userInputCombos, ArrayList<UserInput> userInput, ArrayList<UserInput> invalidInput, TestOracle validTestOracle, TestOracle invalidTestOracle)
	{
		this.userInputCombos = userInputCombos;
		this.userInput = userInput;
		this.invalidInput = invalidInput;
		this.validTestOracle = validTestOracle;
		this.invalidTestOracle = invalidTestOracle;
	}
	
	
	// WAIT BEFORE SUBMIT
	private static int WAIT_TIME = 1000; // milliseconds (1 second = 1000);
	
	// SORT INDEX
	public int pointerToFirstButton = 0;
	
	// POSSIBLE URLs
	//static String URL = "http://webgoat:webgoat@localhost/WebGoat/attack";
	static String URL;
	
	// TEST INPUT COMBINATIONS
	//CombinationalInput userInputCombos = new CombinationalInput(0, new ArrayList<InputRecord>());
	private CombinationalInputs userInputCombos;
	private ArrayList<UserInput> userInput;
	private ArrayList<UserInput> invalidInput;
	
	static String[] upload = {"img.jpg"};
	
	// TEST COUNTER
	private int testCount = 0;
	
	// TEST NODE VARIABLES
	private FormRecord formrecord;
	private String formID;
	private int formIndex = 0;
	public ArrayList<TestNode> testNodes = new ArrayList<TestNode>();
	//public ArrayList<FormGroup> formGroups = new ArrayList<FormGroup>();
	public FormGroup formGroup;
	public TestOracle validTestOracle;
	public TestOracle invalidTestOracle;
	
	private Boolean testOracleValid = true;
	

	// BROWSER SETUP
	private FirefoxProfile firefoxProfile = new FirefoxProfile();	
	private WebDriver driver;     
    private WebDriver driver2;   
    
    private void getpastWebGoat(WebDriver currentDriver)
    {
    	
    	// WAIT
		synchronized (currentDriver)
		{
		    try {
				currentDriver.wait(WAIT_TIME);
			} catch (InterruptedException e) {
			}
		}
    	WebElement button = currentDriver.findElement(By.name("start"));
    	button.click();
    	// WAIT
		synchronized (currentDriver)
		{
		    try {
				currentDriver.wait(WAIT_TIME);
			} catch (InterruptedException e) {
			}
		}
    	currentDriver.findElement(By.linkText("Injection Flaws")).click();
    	currentDriver.findElement(By.linkText("Log Spoofing")).click();
    	
    	
    }
   
    private void setupFirefoxProfile()
    {
    	firefoxProfile.setPreference("network.http.phishy-userpass-length", 255); // So we can do username password in URL for Basic Authentication
    	driver = new FirefoxDriver(firefoxProfile);   
    	driver2 = new FirefoxDriver(firefoxProfile); 
    }
    
    public void navigatePage(String url) {
    	//setupFirefoxProfile();
    	driver = new MyFirefoxDriver();     
        driver2 = new MyFirefoxDriver();   
        driver.get(url);

        // COMMENT OUT IF NOT USING WEBGOAT
       // url = "http://localhost/WebGoat/attack";
       // getpastWebGoat(driver);
        
        
        List<WebElement> allForms = driver.findElements(By.tagName(KEYWORD_FORM));
        for (WebElement form : allForms){        
        	testCount = 0;        	        	
        	formID = form.getAttribute(KEYWORD_ID);
        	navigateForm(driver.getCurrentUrl(), form);
        	        	
        	++formIndex; 
        	System.out.println("Total Tests: "+ testCount);

        }
        
        driver.close();
        driver2.close();
        
    }       
    
	private void navigateForm(String url, WebElement form){
		System.out.println("Form: "+form.getAttribute(KEYWORD_ID));			
		List<Object> formInputs = collectAllFormInputElements(form);
		
		//COMMENT OUT IF NOT USING WEBGOAT
		//driver2.get(URL);
		//getpastWebGoat(driver2);
		//url = driver2.getCurrentUrl();
		
		
		// ONE INPUT
		if(inputCounter() == 1)
		{
	        //System.out.println("single");
			singleInput(url, formInputs);
		
		}
		// MANY INPUTS
		else
		{
	        //System.out.println("multiple");
			multipleInput(url, formInputs);
		}
		
	}
	
	public List<Object> collectAllFormInputElements(WebElement form){

		ArrayList<WebElement> formRadioButtons = new ArrayList<WebElement>();
		ArrayList<SelectmultipleGroup> formSelectmultipleInputs = new ArrayList<SelectmultipleGroup>();
        List<WebElement> allFormInputs = form.findElements(By.tagName(KEYWORD_INPUT));
        int comboInputsCount = 0;
        
        
        // GET RID OF HIDDEN ELEMENTS AND ELEMENTS WITHOUT A NAME
        for(int i = allFormInputs.size()-1; i >= 0; --i)
        {
        	WebElement input = allFormInputs.get(i);
        	if(input.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_RADIO))
        	{
        		formRadioButtons.add(0,input);
        		allFormInputs.remove(i);
        	}
        	else if(input.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_HIDDEN) || input.getAttribute(KEYWORD_NAME).contentEquals(""))
        	{
        		allFormInputs.remove(i);
        	}
        	try{
    		
	        	for(String temp: userInputCombos.getDataInputs().get(0).getInputName())
	        	{
	        		if(input.getAttribute(KEYWORD_NAME).contentEquals(temp))
	        		{        		
	        			allFormInputs.remove(i);
	        			comboInputsCount = 1;
	        		}
	        	}
        	}
    		catch(IndexOutOfBoundsException e){
    			// Nothing in userInputCombos so it errors
    		}

        }
        
        // ADD TEXTAREAS TO INPUT LIST [INPUTS, TEXTAREAS]
        List<WebElement> allTextareas = form.findElements(By.tagName(InputDataType.KEYWORD_TEXTAREA));
        allFormInputs.addAll(allTextareas);               
        
       // ADD SELECTS TO INPUT LIST [INPUTS, TEXTAREAS, SELECTS]
        List<WebElement> allSelects = form.findElements(By.tagName(KEYWORD_SELECT)); // drop down list and list view
        for(int i = allSelects.size()-1; i >= 0; --i)
        {
        	WebElement select = allSelects.get(i);
        	if(select.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_SELECTMULTIPLE))
        	{
        		formSelectmultipleInputs.add(generateSelectmultipleCombinations(select));
        		
        		allSelects.remove(i);
        	}
        }
        allFormInputs.addAll(allSelects);        
        
        // SORT INPUTS SO ALL BUTTON/SUBMITS ARE AT THE END [INPUTS, BUTTONS/SUBMITS]
        sortInputs(allFormInputs);  
        
        // ADD BUTTONS TO THE END OF THE SORTED LIST [INPUTS, BUTTONS/SUBMITS]
        List<WebElement> allButtons = form.findElements(By.tagName(InputDataType.KEYWORD_BUTTON));
        allFormInputs.addAll(allButtons);        
        
        // CREATE NEW LIST OF OBJECTS TO CONTAIN RADIO BUTTONS WITH OTHER INPUTS
        List<Object> formInputs = new ArrayList<Object>();
        
        // ADD IN ALL INPUTS SO LIST IS BROKEN UP INTO [RADIO BUTTON GROUPS, ALL OTHER INPUTS, BUTTONS/SUBMITS]
        ArrayList<RadioGroup> radioButtonGroups = groupRadioButtons(formRadioButtons);
        formInputs.addAll(radioButtonGroups);
        formInputs.addAll(formSelectmultipleInputs);
        if(comboInputsCount > 0)
        	formInputs.add(userInputCombos.getDataInputs().get(0));

        pointerToFirstButton = pointerToFirstButton + radioButtonGroups.size() + formSelectmultipleInputs.size() + comboInputsCount;

       
     
        formInputs.addAll(allFormInputs);
        
        return formInputs;
	}

	private void sortInputs(List<WebElement> formInputs)
	{
		int head = 0;
        // SORT INPUTS [INPUTS, SUBMIT]
//        System.out.println("Before swapping, ArrayList contains :");
//        for(WebElement input : formInputs)
//        {
//        	 System.out.println(input.getAttribute(KEYWORD_NAME)+" "+input.getAttribute(KEYWORD_TYPE));
//        }
        head = 0;
        if(formInputs.size() == 0){
        	pointerToFirstButton = 0;
        }
        else
        {
        	pointerToFirstButton = formInputs.size()-1;
        }
        
        for(WebElement input: formInputs)
        {
        	if(head == pointerToFirstButton || head > pointerToFirstButton)
        		break;
        	
        	
        	if(isButton(input))      
        	{
        		WebElement input2 = formInputs.get(pointerToFirstButton);
        		while(isButton(input2))        		
        		{
        			if(head == pointerToFirstButton)
        				break;
        			--pointerToFirstButton;
        			input2 = formInputs.get(pointerToFirstButton);
        		}
        		Collections.swap(formInputs,head,pointerToFirstButton);	
        	}
        	++head;
        }
       
        // SET UP FOR SECOND SORT TO SORT MULTIPLE CHOICE AND SINGLE CHOICE
        if(head != pointerToFirstButton)
        {
        	pointerToFirstButton = head-1;
        }
        

//        System.out.println("After swapping, ArrayList contains :");
//        for(WebElement input : formInputs)
//        {
//        	 System.out.println(input.getAttribute(KEYWORD_NAME)+" "+input.getAttribute(KEYWORD_TYPE));
//        }
        
	}
	
	private Boolean isButton(WebElement input)
	{
		return input.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_SUBMIT) || 
			input.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_IMAGE) ||
			input.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_BUTTON)||
			input.getAttribute(KEYWORD_TYPE).contentEquals(InputDataType.KEYWORD_RESET);
	}
	
	private ArrayList<RadioGroup> groupRadioButtons(ArrayList<WebElement> formRadioButtons) 
	{
		ArrayList<RadioGroup> radioGroupList = new ArrayList<RadioGroup>();

		while(!(formRadioButtons.isEmpty()))
		{
			String rbName = formRadioButtons.get(0).getAttribute(KEYWORD_NAME);
			RadioGroup group = new RadioGroup(rbName);
			for(WebElement rb: formRadioButtons)
			{
				if(rbName.contentEquals(rb.getAttribute(KEYWORD_NAME))){
					group.addRadioButton(rb);
					
				}
			}
			
			for(WebElement toBeRemoved: group.getRadioButtons())
			{
				formRadioButtons.remove(toBeRemoved);
			}			
			
			radioGroupList.add(group);
			
		}
		return radioGroupList;
	}
	
	private SelectmultipleGroup generateSelectmultipleCombinations(WebElement selectmultipleInput)
	{
			
			
			Select s = new Select(selectmultipleInput);					        	
			List<WebElement> listviewOptions = s.getOptions();	
			int[] sizes = new int[listviewOptions.size()];
			
    		for(int i = 0; i < listviewOptions.size(); ++i)
    		{
    			sizes[i] = 2;
    		}
    		AllCombinations allCombos = new AllCombinations();
    		SelectmultipleGroup selectmultipleInputCombos = new SelectmultipleGroup(selectmultipleInput.getAttribute(KEYWORD_NAME), allCombos.getCombinations(sizes));
    		//selectmultipleInputCombos.printSMCList();
			return selectmultipleInputCombos;

		
	}
	
	private int inputCounter()
	{
		int i = 0;
		for(i = 0; i < pointerToFirstButton; ++i)
		{
		}
		return i;
	}	   
	
	private void singleInput(String url, List<Object> formInputs)
	{
		
		// FILL IN FORM
		driver2.get(url);
		Object formInputElement = formInputs.get(0);
		int choices = getFormInputChoices(formInputs.get(0));
		for(int index = 0; index < choices; ++index)
		{
			formrecord = new FormRecord(formIndex, new ArrayList<InputRecord>(), new ArrayList<Integer>(), "");
			{
				for(int btnIndex = pointerToFirstButton; btnIndex<formInputs.size(); ++btnIndex )
				{
					driver2.get(url);
					WebElement element;
					if(formInputElement instanceof RadioGroup)
					{
						WebElement oldElement = ((RadioGroup)formInputElement).getRadioButtonAt(index);
						element = driver2.findElement(By.id(oldElement.getAttribute(KEYWORD_ID)));
						element.click();
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);	
						formrecord.addDataCombo(index);
					}
					else if(formInputElement instanceof SelectmultipleGroup)
					{
						element = driver2.findElement(By.name(((SelectmultipleGroup)formInputElement).getName()));
						Select s = new Select(element);				        	
			        	List<WebElement> listviewOptions = s.getOptions();
			        	int[] combos = ((SelectmultipleGroup) formInputElement).getSelectmultipleCombinations(index);
			        	for(int i = 0; i < listviewOptions.size(); ++i)
			        	{
			        		WebElement listviewItem = listviewOptions.get(i);
			        		if(combos[i] == 1)
			        		{
			        			listviewItem.click();
			        		}
			        	}
			        	
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(index);
					}
					else if(formInputElement instanceof WebElement)
					{
						element = driver2.findElement(By.name(((WebElement) formInputElement).getAttribute(KEYWORD_NAME)));
						executeFormInput(element, index, driver2);
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);	
						formrecord.addDataCombo(index);
					}
						
					
									
					// SUBMIT
					try {
						elementClick(driver2, driver2.findElement(By.id(((WebElement) formInputs.get(btnIndex)).getAttribute(KEYWORD_ID))));					
		    		}
		    		catch (Exception e) {
		    			try {
			    			elementClick(driver2, driver2.findElement(By.name(((WebElement) formInputs.get(btnIndex)).getAttribute(KEYWORD_NAME))));
			    		}
			    		catch (Exception e1) {
	
			    		}
	
		    		}
				}
			}
		}	
		try{

		
		if(invalidInput.get(0).getSize() > 0)
		{
			choices = getFormInputChoices(formInputs.get(0));
			for(int index = 0; index < choices; ++index)
			{
				formrecord = new FormRecord(formIndex, new ArrayList<InputRecord>(), new ArrayList<Integer>(), "");
				{
					for(int btnIndex = pointerToFirstButton; btnIndex<formInputs.size(); ++btnIndex )
					{
						driver2.get(url);
						WebElement element;
						if(formInputElement instanceof RadioGroup)
						{
							WebElement oldElement = ((RadioGroup)formInputElement).getRadioButtonAt(index);
							element = driver2.findElement(By.id(oldElement.getAttribute(KEYWORD_ID)));
							element.click();
							
							InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
							formrecord.addDataInput(inputrecord);
							formrecord.addDataCombo(index);
						}
						else if(formInputElement instanceof SelectmultipleGroup)
						{
							element = driver2.findElement(By.name(((SelectmultipleGroup)formInputElement).getName()));
							Select s = new Select(element);				        	
				        	List<WebElement> listviewOptions = s.getOptions();
				        	int[] combos = ((SelectmultipleGroup) formInputElement).getSelectmultipleCombinations(index);
				        	for(int i = 0; i < listviewOptions.size(); ++i)
				        	{
				        		WebElement listviewItem = listviewOptions.get(i);
				        		if(combos[i] == 1)
				        		{
				        			listviewItem.click();
				        		}
				        	}
				        	
							
							InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
							formrecord.addDataInput(inputrecord);	
							formrecord.addDataCombo(index);
						}
						else if(formInputElement instanceof WebElement)
						{
							element = driver2.findElement(By.name(((WebElement) formInputElement).getAttribute(KEYWORD_NAME)));
							executeInvalidFormInput(element, index, driver2);
							
							InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
							formrecord.addDataInput(inputrecord);	
							formrecord.addDataCombo(index);
						}
							
						
										
						// SUBMIT
						try {
							elementClick(driver2, driver2.findElement(By.id(((WebElement) formInputs.get(btnIndex)).getAttribute(KEYWORD_ID))));					
			    		}
			    		catch (Exception e) {
			    			try {
				    			elementClick(driver2, driver2.findElement(By.name(((WebElement) formInputs.get(btnIndex)).getAttribute(KEYWORD_NAME))));
				    		}
				    		catch (Exception e1) {
		
				    		}
		
			    		}
					}
				}
			}
		}

		}
		catch(IndexOutOfBoundsException e){
		}
		// COUNT TESTS
		testCount += choices * (formInputs.size()-pointerToFirstButton);
	}
	
	private ArrayList<int[]> buildInvalidFormInputCombinations(ArrayList<int[]> formInputCombinations, List<Object> formInputs)
	{		
		
		ArrayList<int[]> formInvalidInputCombinations = new ArrayList<int[]>();
		
		String[] formInputNames = new String[invalidInput.size()];
		int[] formInputIndexes = new int[invalidInput.size()];
		for(int index=0; index<invalidInput.size(); index++)
		{
			formInputNames[index] = invalidInput.get(index).getInputID();
		}
		//System.out.println(formInputNames);
		for(int[]combination : formInputCombinations)
		{
			for (int index=0; index<combination.length; index++){
				
				Object formInputElement = formInputs.get(index);
				for(int j=0; j<invalidInput.size(); j++)
				{
					try{
						WebElement element = driver.findElement(By.name(((WebElement) formInputElement).getAttribute(KEYWORD_NAME)));
					
						if(formInputNames[j].equals(element.getAttribute(KEYWORD_NAME)))
						{
							formInputIndexes[j] = index;					
						}
					}catch(ClassCastException e){
					
					}
				}
			}
		}
		//System.out.println(formInputIndexes);
		for(int i = 0; i < formInputIndexes.length; ++i)
		{
			int invalidcount = invalidInput.get(i).getUserInputData().size();
			//System.out.println(invalidcount);
			for(int[]combination : formInputCombinations)
			{

				int index = formInputIndexes[i];
				if(combination[index]==0)
				{
					
						for(int k = 0; k < invalidcount; ++k)
						{
							int tempc[] = new int[combination.length];
							//System.out.println(combination[index]);
							combination[index] = k * (-1) - 1;
							//System.out.println(combination[index]);
							for(int t = 0; t < combination.length; ++t)
							{
								tempc[t] = combination[t];
								//System.out.print(combination[t]+" ");	
							}
							System.out.println("");
							combination[index] = k;
							formInvalidInputCombinations.add(tempc);
						}
				}
			}
			
		}
		
		return formInvalidInputCombinations;
		

	}
	
	private void multipleInput(String url, List<Object> formInputs)
	{
		// GENERATE PAIRS
		ArrayList<int[]> formInputCombinations = generateFormInputCombinations(formInputs, pointerToFirstButton);
		//System.out.println(formInputCombinations);
		
		ArrayList<int[]> formInvalidInputCombinations = buildInvalidFormInputCombinations(formInputCombinations, formInputs);
		ArrayList<int[]> formCombinationsArrayList = new ArrayList<int[]>(formInputCombinations.size()+formInvalidInputCombinations.size());
		formCombinationsArrayList.addAll(formInputCombinations);
		formCombinationsArrayList.addAll(formInvalidInputCombinations);

		// FILL IN FORM
		for (int[] combination: formCombinationsArrayList){
			formrecord = new FormRecord(formIndex, new ArrayList<InputRecord>(), new ArrayList<Integer>(), "");
			for(int btnIndex = pointerToFirstButton; btnIndex<formInputs.size(); ++btnIndex)
			{
				driver2.get(url);
				for (int index=0; index<combination.length; index++){
																				
					Object formInputElement = formInputs.get(index);
					WebElement element;
					if(formInputElement instanceof RadioGroup)
					{
						WebElement oldElement = ((RadioGroup)formInputElement).getRadioButtonAt(combination[index]);
						element = driver2.findElement(By.id(oldElement.getAttribute(KEYWORD_ID)));
						element.click();
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(combination[index]);
					}
					else if(formInputElement instanceof SelectmultipleGroup)
					{
						element = driver2.findElement(By.name(((SelectmultipleGroup)formInputElement).getName()));
						Select s = new Select(element);				        	
			        	List<WebElement> listviewOptions = s.getOptions();
			        	int[] combos = ((SelectmultipleGroup) formInputElement).getSelectmultipleCombinations(combination[index]);
			        	for(int i = 0; i < listviewOptions.size(); ++i)
			        	{
			        		WebElement listviewItem = listviewOptions.get(i);
			        		
			        		if(combos[i] == 1)
			        		{
			        			listviewItem.click();
			        		}
			        	}
			        	
			        	//executeFormInput(element, combination[index], driver2);						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(combination[index]);
					}
					else if(formInputElement instanceof WebElement)
					{
						element = driver2.findElement(By.name(((WebElement) formInputElement).getAttribute(KEYWORD_NAME)));
						executeFormInput(element, combination[index], driver2);
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(combination[index]);
					}
					else if(formInputElement instanceof ComboInput)
					{
						for(int i = 0; i < userInputCombos.getDataInputs().get(0).getSize(); ++i)
						{
							element = driver2.findElement(By.name(((ComboInput) formInputElement).getInputName().get(i)));
							executeFormInput(element, combination[index], driver2);
							InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
							formrecord.addDataInput(inputrecord);
							formrecord.addDataCombo(combination[index]);
						}
						
					}
					
					
					
					
				}				
				
				//SUBMIT
				try {
					elementClick(driver2, driver2.findElement(By.id(((WebElement) formInputs.get(btnIndex)).getAttribute(KEYWORD_ID))));					
	    		}
	    		catch (Exception e) {
	    			try {
		    			elementClick(driver2, driver2.findElement(By.name(((WebElement) formInputs.get(btnIndex)).getAttribute(KEYWORD_NAME))));
		    		}
		    		catch (Exception e1) {

		    		}
		    		
		    		

	    		}   		

			}
		}
		

		
		// COUNT TESTS
		testCount += formCombinationsArrayList.size() * (formInputs.size()-pointerToFirstButton);
	}
    
	
	
	private InputDataType.FormInputType getFormInputType(Object formInputElement){
		
		
		
		if(formInputElement instanceof RadioGroup)
			return InputDataType.FormInputType.RADIOINPUT;
		else if (formInputElement instanceof SelectmultipleGroup)
		{			
			return InputDataType.FormInputType.SELECTMULTIPLEINPUT;
		}
		else if (formInputElement instanceof ComboInput)
		{
			return InputDataType.FormInputType.COMBINATIONINPUT;
		}
		else
		{
			assert formInputElement instanceof WebElement;
			return InputDataType.getFormInputType(((WebElement) formInputElement).getAttribute(KEYWORD_TYPE));
		}
	}
	
	private int getFormInputChoices(Object formInputElement){
		
		// for tag "input"
		
		InputDataType.FormInputType formInputType = getFormInputType(formInputElement);
		{
			switch (formInputType){
				case TEXTINPUT: 	
					for(int i = 0; i < userInput.size(); ++i)
					{
						if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
						{
							return userInput.get(i).getSize();
						}

					}
				case TEXTAREAINPUT: 
					for(int i = 0; i < userInput.size(); ++i)
					{
						if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
						{
							return userInput.get(i).getSize();
						}
	
					}
				case PASSWORDINPUT: 
					for(int i = 0; i < userInput.size(); ++i)
					{
						if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
						{
							return userInput.get(i).getSize();
						}
	
					}
				case SELECTONEINPUT: assert formInputElement instanceof Select;
									Select s1 = new Select((WebElement) formInputElement);					        	
						        	List<WebElement> dropdownOptions = s1.getOptions();					        						        
									return dropdownOptions.size();
				case SELECTMULTIPLEINPUT: return ((SelectmultipleGroup) formInputElement).size();								
				case CHECKBOXINPUT: return 2;
				case RADIOINPUT: return ((RadioGroup) formInputElement).size();
				case UPLOADINPUT: return upload.length;
				case COMBINATIONINPUT: 
					String inputname = userInputCombos.getDataInputs().get(0).getInputName().get(0);
					for(int i = 0; i < userInput.size(); ++i)
					{
						if(inputname == userInput.get(i).getInputID())
						{
							userInputCombos.getDataInputs().get(0).setNumInputs(userInput.get(i).getSize());
							return userInput.get(i).getSize();
						}
					}
					
					

				default: return 2;
			}
		}
	}
		
    private ArrayList<int[]> computeAllPairs(int[] formInputSizes){
		  long seed = 42;
		  int maxGoes = 100;
		  boolean noShuffle = true;
		  return AllPairs.generatePairs(formInputSizes, seed, maxGoes, noShuffle, null, false);
	 }

	private ArrayList<int[]> generateFormInputCombinations(List<Object> formInputs, int numberOfInputs){
	    assert formInputs.size() > numberOfInputs;
		int[] formInputSizes = new int[numberOfInputs];
	    String[] formInputNames = new String[numberOfInputs];
		for(int index=0; index<numberOfInputs; index++){
			Object formInputElement = formInputs.get(index);
			if(formInputElement instanceof WebElement)
			{
				formInputNames[index] = ((WebElement) formInputElement).getAttribute(KEYWORD_NAME);
			}
			else if(formInputElement instanceof ComboInput)
			{
				//formInputNames[index] = ((ComboInput) formInputElement).getInputName();
			}
			else if(formInputElement instanceof SelectmultipleGroup)
			{
				formInputNames[index] = ((SelectmultipleGroup) formInputElement).getName();
			}
			else
			{
				formInputNames[index] = ((RadioGroup) formInputElement).getName();
			}
			formInputSizes[index] = getFormInputChoices(formInputElement);
			System.out.println("Input name: "+formInputNames[index] +" #choices:"+ formInputSizes[index]);			
		}
		return computeAllPairs(formInputSizes);
	}
	
	private void executeFormInput(Object formInputElement, int choiceIndex, WebDriver driver){
		// for tag "input"
		InputDataType.FormInputType formInputType = getFormInputType(formInputElement);
		{
			switch (formInputType){
				case TEXTINPUT: 
								if(choiceIndex < 0)
								{
									testOracleValid = false;
									int tempChoiceIndex =  (choiceIndex + 1) * (-1);
									for(int i = 0; i < invalidInput.size(); ++i)
									{
										if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
										{
											assert invalidInput.get(i).getSize()>tempChoiceIndex;
											if(!invalidInput.get(i).getUserInputString(tempChoiceIndex).contentEquals("DEFAULT"))
											{
												//System.out.println("choice Index "+ choiceIndex);
												((WebElement) formInputElement).clear();
					        					((WebElement) formInputElement).sendKeys(invalidInput.get(i).getUserInputString(tempChoiceIndex));
											}
										}
				
									}
								}
								else
								{
									for(int i = 0; i < userInput.size(); ++i)
									{
										if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
										{
											assert userInput.get(i).getSize()>choiceIndex;
											if(!userInput.get(i).getUserInputString(choiceIndex).contentEquals("DEFAULT"))
											{
												//System.out.println("choice Index "+ choiceIndex);
												((WebElement) formInputElement).clear();
					        					((WebElement) formInputElement).sendKeys(userInput.get(i).getUserInputString(choiceIndex));
											}
										}
				
									}
								}
	        					break;
				case TEXTAREAINPUT: 
									if(choiceIndex < 0)
									{
										testOracleValid = false;
										int tempChoiceIndex =  (choiceIndex + 1) * (-1);
										for(int i = 0; i < invalidInput.size(); ++i)
										{
											if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
											{
												assert invalidInput.get(i).getSize()>tempChoiceIndex;
												if(!invalidInput.get(i).getUserInputString(tempChoiceIndex).contentEquals("DEFAULT"))
												{
													//System.out.println("choice Index "+ choiceIndex);
													((WebElement) formInputElement).clear();
						        					((WebElement) formInputElement).sendKeys(invalidInput.get(i).getUserInputString(tempChoiceIndex));
												}
											}
					
										}
									}
									else
									{
										for(int i = 0; i < userInput.size(); ++i)
										{
											if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
											{
												assert userInput.get(i).getSize()>choiceIndex;
												if(!userInput.get(i).getUserInputString(choiceIndex).contentEquals("DEFAULT"))
												{
													//System.out.println("choice Index "+ choiceIndex);
													((WebElement) formInputElement).clear();
						        					((WebElement) formInputElement).sendKeys(userInput.get(i).getUserInputString(choiceIndex));
												}
											}
					
										}
									}
								break;
				case PASSWORDINPUT: 
								if(choiceIndex < 0)
								{
									testOracleValid = false;
									int tempChoiceIndex =  (choiceIndex + 1) * (-1);
									for(int i = 0; i < invalidInput.size(); ++i)
									{
										if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
										{
											assert invalidInput.get(i).getSize()>tempChoiceIndex;
											if(!invalidInput.get(i).getUserInputString(tempChoiceIndex).contentEquals("DEFAULT"))
											{
												//System.out.println("choice Index "+ choiceIndex);
												((WebElement) formInputElement).clear();
					        					((WebElement) formInputElement).sendKeys(invalidInput.get(i).getUserInputString(tempChoiceIndex));
											}
										}
				
									}
								}
								else
								{
									for(int i = 0; i < userInput.size(); ++i)
									{
										if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(userInput.get(i).getInputID()))
										{
											assert userInput.get(i).getSize()>choiceIndex;
											if(!userInput.get(i).getUserInputString(choiceIndex).contentEquals("DEFAULT"))
											{
												//System.out.println("choice Index "+ choiceIndex);
												((WebElement) formInputElement).clear();
					        					((WebElement) formInputElement).sendKeys(userInput.get(i).getUserInputString(choiceIndex));
											}
										}
				
									}
								}
								
								break;
				case CHECKBOXINPUT: assert choiceIndex<2;
	        					if(((WebElement) formInputElement).isSelected() && choiceIndex==0 || !((WebElement) formInputElement).isSelected() && choiceIndex==1)
	        						((WebElement) formInputElement).click();
	        					break;
				case SELECTONEINPUT: 
								assert (formInputElement instanceof Select);
								Select s1 = new Select((WebElement) formInputElement);					        	
					        	List<WebElement> dropdownOptions = s1.getOptions();
					        	assert dropdownOptions.size()>choiceIndex;
					        	WebElement dropdownItem = dropdownOptions.get(choiceIndex);
					        	dropdownItem.click();
								break;	
				case SELECTMULTIPLEINPUT: 
								// HANDLED IN THE SINGLE/MULTIPLE INPUT FUNCTIONS RIGHT NOW
								Select s = new Select((WebElement) formInputElement);				        	
					        	List<WebElement> listviewOptions = s.getOptions();
					        	int[] combos = ((SelectmultipleGroup) formInputElement).getSelectmultipleCombinations(choiceIndex);
					        	for(int i = 0; i < listviewOptions.size(); ++i)
					        	{
					        		WebElement listviewItem = listviewOptions.get(i);
					        		if(combos[i] == 1)
					        		{
					        			listviewItem.click();
					        		}
					        	}
								break;	
				case RADIOINPUT: // HANDLED IN THE SINGLE/MULTIPLE INPUT FUNCTIONS					
								break;
				case UPLOADINPUT:									
								((WebElement) formInputElement).click();
								((WebElement) formInputElement).sendKeys("img.jpg");
								((WebElement) formInputElement).sendKeys("{ENTER}");
								break;
				default: ;
		}
		}
	}

	public void replayNavigate(String url, List<Object> formInputs, int[] combination, String buttonName)
	{    
        WebDriver driver2 = new MyFirefoxDriver();
		// FILL IN FORM
			formrecord = new FormRecord(formIndex, new ArrayList<InputRecord>(), new ArrayList<Integer>(), "");
			for(int btnIndex = pointerToFirstButton; btnIndex<formInputs.size(); ++btnIndex)
			{
				driver2.get(url);
				for (int index=0; index<combination.length; index++){
																				
					Object formInputElement = formInputs.get(index);
					WebElement element;
					if(formInputElement instanceof RadioGroup)
					{
						WebElement oldElement = ((RadioGroup)formInputElement).getRadioButtonAt(combination[index]);
						element = driver2.findElement(By.id(oldElement.getAttribute(KEYWORD_ID)));
						element.click();
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(combination[index]);
					}
					else if(formInputElement instanceof SelectmultipleGroup)
					{
						element = driver2.findElement(By.name(((SelectmultipleGroup)formInputElement).getName()));
						Select s = new Select(element);				        	
			        	List<WebElement> listviewOptions = s.getOptions();
			        	int[] combos = ((SelectmultipleGroup) formInputElement).getSelectmultipleCombinations(combination[index]);
			        	for(int i = 0; i < listviewOptions.size(); ++i)
			        	{
			        		WebElement listviewItem = listviewOptions.get(i);
			        		
			        		if(combos[i] == 1)
			        		{
			        			listviewItem.click();
			        		}
			        	}
			        	
			        	//executeFormInput(element, combination[index], driver2);						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(combination[index]);
					}
					else if(formInputElement instanceof WebElement)
					{
						element = driver2.findElement(By.name(((WebElement) formInputElement).getAttribute(KEYWORD_NAME)));
						executeFormInput(element, combination[index], driver2);
						
						InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
						formrecord.addDataInput(inputrecord);
						formrecord.addDataCombo(combination[index]);
					}
					else if(formInputElement instanceof ComboInput)
					{
						for(int i = 0; i < userInputCombos.getDataInputs().get(0).getSize(); ++i)
						{
							element = driver2.findElement(By.name(((ComboInput) formInputElement).getInputName().get(i)));
							executeFormInput(element, combination[index], driver2);
							InputRecord inputrecord = new InputRecord(InputDataType.getFormInputType(element.getAttribute(KEYWORD_TYPE)),element.getAttribute(KEYWORD_NAME), element.getAttribute(KEYWORD_VALUE));
							formrecord.addDataInput(inputrecord);
							formrecord.addDataCombo(combination[index]);
						}
						
					}
					
					
					
					
				}				
				
				//SUBMIT
				try {
					elementClick(driver2, driver2.findElement(By.id(buttonName)));					
	    		}
	    		catch (Exception e) {

	    		}   		

			}
		
		
		// COUNT TESTS
		testCount += combination.length * (formInputs.size()-pointerToFirstButton);
		driver2.close();
	}
	
	private void executeInvalidFormInput(Object formInputElement, int choiceIndex, WebDriver driver){
		// for tag "input"
		InputDataType.FormInputType formInputType = getFormInputType(formInputElement);
		{
			switch (formInputType){
				case TEXTINPUT: 
								for(int i = 0; i < invalidInput.size(); ++i)
								{
									if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(invalidInput.get(i).getInputID()))
									{
										assert invalidInput.get(i).getSize()>choiceIndex;
										if(!invalidInput.get(i).getUserInputString(choiceIndex).contentEquals("DEFAULT"))
										{
											//System.out.println("choice Index "+ choiceIndex);
											((WebElement) formInputElement).clear();
				        					((WebElement) formInputElement).sendKeys(invalidInput.get(i).getUserInputString(choiceIndex));
										}
									}
			
								}
								
	        					break;
				case TEXTAREAINPUT: 
								for(int i = 0; i < invalidInput.size(); ++i)
								{
									if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(invalidInput.get(i).getInputID()))
									{
										assert invalidInput.get(i).getSize()>choiceIndex;
										if(!invalidInput.get(i).getUserInputString(choiceIndex).contentEquals("DEFAULT"))
										{
											((WebElement) formInputElement).clear();
				        					((WebElement) formInputElement).sendKeys(invalidInput.get(i).getUserInputString(choiceIndex));
										}
									}
			
								}
								
								break;
				case PASSWORDINPUT: 
								for(int i = 0; i < invalidInput.size(); ++i)
								{
									if(((WebElement) formInputElement).getAttribute(KEYWORD_NAME).contentEquals(invalidInput.get(i).getInputID()))
									{
										assert invalidInput.get(i).getSize()>choiceIndex;
										if(!invalidInput.get(i).getUserInputString(choiceIndex).contentEquals("DEFAULT"))
										{
											//System.out.println("choice Index "+ choiceIndex);
											((WebElement) formInputElement).clear();
				        					((WebElement) formInputElement).sendKeys(invalidInput.get(i).getUserInputString(choiceIndex));
										}
									}
			
								}
								
								break;
				case CHECKBOXINPUT: assert choiceIndex<2;
	        					if(((WebElement) formInputElement).isSelected() && choiceIndex==0 || !((WebElement) formInputElement).isSelected() && choiceIndex==1)
	        						((WebElement) formInputElement).click();
	        					break;
				case SELECTONEINPUT: 
								assert (formInputElement instanceof Select);
								Select s1 = new Select((WebElement) formInputElement);					        	
					        	List<WebElement> dropdownOptions = s1.getOptions();
					        	assert dropdownOptions.size()>choiceIndex;
					        	WebElement dropdownItem = dropdownOptions.get(choiceIndex);
					        	dropdownItem.click();
								break;	
				case SELECTMULTIPLEINPUT: 
								// HANDLED IN THE SINGLE/MULTIPLE INPUT FUNCTIONS RIGHT NOW
								Select s = new Select((WebElement) formInputElement);				        	
					        	List<WebElement> listviewOptions = s.getOptions();
					        	int[] combos = ((SelectmultipleGroup) formInputElement).getSelectmultipleCombinations(choiceIndex);
					        	for(int i = 0; i < listviewOptions.size(); ++i)
					        	{
					        		WebElement listviewItem = listviewOptions.get(i);
					        		if(combos[i] == 1)
					        		{
					        			listviewItem.click();
					        		}
					        	}
								break;	
				case RADIOINPUT: // HANDLED IN THE SINGLE/MULTIPLE INPUT FUNCTIONS					
								break;
				case UPLOADINPUT:									
								((WebElement) formInputElement).click();
								((WebElement) formInputElement).sendKeys("img.jpg");
								((WebElement) formInputElement).sendKeys("{ENTER}");
								break;
				default: ;
		}
		}
	}
	
    private void elementClick(WebDriver currentDriver, WebElement element){
    	
		// WAIT
		synchronized (currentDriver)
		{
		    try {
				currentDriver.wait(WAIT_TIME);
			} catch (InterruptedException e) {
				System.out.println("Error at synchronizing in elementClick");
			}
		}

    	formrecord.setSubmitButtonID(element.getAttribute(KEYWORD_ID));
    	//System.out.println("Current title before click:" + currentDriver.getTitle());
    	String URL = currentDriver.getCurrentUrl();
    	// CLICK SUBMIT BUTTON
    	try{
    		//System.out.println("click");
    		element.click();    		
    	}
    	catch ( StaleElementReferenceException e){
    		System.out.println("Error at Submit in elementClick");
    	}
//    	Alert alert = currentDriver.switchTo().alert();
//    	if (alert!=null){
//    		try {
//    			System.out.println("alert");
//    			alert.accept();
//    		}
//    		catch (Exception e) {
//    			System.out.println("3");
//    		}
//    	}

    	currentDriver = testOracle(currentDriver);
    	testOracleValid = true;
    	//System.out.println("Below testOracle");
    	formGroup = new FormGroup(formIndex, formID, new ArrayList<FormRecord>());
    	formGroup.addFormInput(formrecord);
    	
    	TestNode node = new TestNode(formID , URL, formIndex+"", formGroup.getFormInputs(), false);
    	testNodes.add(node);
    	
    	System.out.println(currentDriver.getCurrentUrl());
    	signout(currentDriver);
  	 }
    
    private WebDriver testOracle(WebDriver currentDriver)
    {
    	String result = "";
    	WebDriverCommandProcessor webDriverCommandProcessor = new WebDriverCommandProcessor("", currentDriver);
    	int index = 0; 

    	if(testOracleValid)
    	{
        	while(index < validTestOracle.getTestTypeData().size())
        	{
        		result = webDriverCommandProcessor.doCommand(validTestOracle.getTestTypeData().get(index), validTestOracle.getTestData().get(index));
        		System.out.println(result);
        		++index;
        	}
    	}
    	else
    	{
        	while(index < invalidTestOracle.getTestTypeData().size())
        	{
        		result = webDriverCommandProcessor.doCommand(invalidTestOracle.getTestTypeData().get(index), invalidTestOracle.getTestData().get(index));
        		System.out.println(result);
        		++index;
        	}
    	}

    	return currentDriver;
    }

    
    public void signout(WebDriver driver){
    	String[] signout = {"SIGNOUT", "Sign Out", "Signout", "Log Out", "Logout" , "Sign out"}; 
    	for(int soindex = 0; soindex < signout.length; soindex++){
			try {
				driver.findElement(By.linkText(signout[soindex])).click();
            } catch (Exception e) {
            }            
		}    	
  	 }
    	    
    public static void main(String[] args) {
    	//new AutoFormNavigator().navigatePage(URL);
    }
}

