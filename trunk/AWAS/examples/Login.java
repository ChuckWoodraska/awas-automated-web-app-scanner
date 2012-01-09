package examples;

import input.MyFirefoxDriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import combinatorial.AllPairs;


public class Login  {
	// POSSIBLE URLs
	//static String URL = "https://audience.nba.com/services/msib/flow/login?url=http%3A%2F%2Fwww.nba.com%2F";
	static String URL = "http://www.youtube.com/create_account?next=%2F";
	//static String URL = "https://www.google.com/accounts/ServiceLogin?uilel=3&service=youtube&passive=true&continue=http%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26nomobiletemp%3D1%26hl%3Den_US%26next%3D%252Findex&hl=en_US&ltmpl=sso";
	//static String URL = "https://login.yahoo.com/config/login?.src=fpctx&.intl=us&.done=http%3A%2F%2Fwww.yahoo.com%2F&rl=1";

	// TEST INPUTS
	static String[] username = {"test", "cselenium", "seleniumc"};
	static String[] password = {"test", "easy1234!"};
	static int[] checkbox = {0, 1};
	public static int[] selectList;
	public static int[] selectInputs;
	public static String[] selectName;
	static ArrayList<int[]> selectTester;
	static String title;
	
	
	// SIGNOUT INPUTS
	static String[] signout = {"SIGNOUT", "Sign Out", "Signout", "Log Out", "Logout"}; 
	
    public static void main(String[] args) {
    	// WEBDRIVERS
        WebDriver driver = new MyFirefoxDriver();
        WebDriver driver2 = new MyFirefoxDriver();
        
        // GET WEBSITE AND ALL ITS FORMS
        driver.get(URL);
        List<WebElement> allForms = driver.findElements(By.tagName("form"));
        WebElement element;
        // RUN THROUGH ALL FORMS
        for (WebElement form : allForms) {
        	
             
             // MAKE LIST OF ALL INPUTS
             List<WebElement> allOptions = form.findElements(By.tagName("input")); 
             //List<WebElement> allOpt = driver.findElements(By.tagName("input"));
             List<WebElement> allButtons = form.findElements(By.tagName("button"));
             List<WebElement> allSelects = form.findElements(By.tagName("select"));// drop down list
             allOptions.addAll(allButtons);
             allOptions.addAll(allSelects);
             
             // TAKE CARE OF DROPDOWNLIST SETUP
             selectList = new int[allSelects.size()];
             selectName = new String[allSelects.size()];
             int count = 0;
             for(WebElement select : allSelects)
     		{
            	
            	Select s1 = new Select(select);
            	
            	List<WebElement> listops = s1.getOptions();
            	
            	selectList[count] = listops.size();
            	selectName[count] = select.getAttribute("name");
     				// PRINT OUT SIZE OF SELECT LIST
     				//System.out.println(String.format("Size is: %d, Name is: %s", selectList[count], selectName[count]));
     				count++;
     		}
             System.out.println(String.format("Start allpairs"));
            selectTester = new ArrayList<int []>(allpairs());
            System.out.println(String.format("End allpairs"));
            
            
            // PRINT OUT FORM AND INPUTS FOR THAT FORM
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("NEW FORM");
    		System.out.println(String.format("Form is: %s  Action is: %s", form.getAttribute("name"),  form.getAttribute("action")));
    		for(WebElement option : allOptions)
    		{
    			// IF TYPE IS HIDDEN WE DONT PRINT IT
    			if(option.getAttribute("type").contentEquals("hidden"))
    			{
    				
    			}
    			else
    			{
    				// PRINT INPUT NAME IF IT HAS ONE AND PRINT INPUT TYPE
    				System.out.println(String.format("Input is: %s  Type is: %s", option.getAttribute("name"),  option.getAttribute("type")));
    			}
    		}
    		
    		// RUN THROUGH COMBINATIONS OF INPUTS
             for(int i = 0; i < username.length; i++)
        	{
        		for(int j = 0; j < password.length; j++)
        		{
        			// CHECK IF SELECTTESTER IS EMPTY TO SKIP DOING DROPDOWNLISTS
        			if(selectTester.isEmpty())
        			{
        				for(int k = 0; k < checkbox.length; k++)
	        			{
	        				
	        				
		        			// TEST ON 2nd DRIVER
		        			driver2.get(URL);
		        			for (WebElement option : allOptions) {
		        				
		        				// CHECK IF INPUT HAS A NAME
				            	if(option.getAttribute("name").contentEquals(""))
				            	{
				            		// CLICK A BUTTON WITH TAG <BUTTON>
				            		element = driver2.findElement(By.tagName("button"));
				            		if(element.getTagName().contentEquals("button"))
					            	{
				            			// CLICK ELEMENT
				            			elementclick(driver2, element);						            								            								            	
					            		
					            		// SIGNOUT
						            	signout(driver2);

					                    driver2.get(URL);					                    
					            	}
				            	}
				            	
				            	else
				            	{
				            		// GRAB ELEMENT FROM DRIVER TO USE IN DRIVER2
					            	element = driver2.findElement(By.name(option.getAttribute("name")));
					            	if(element.getAttribute("type").contentEquals("text") || element.getAttribute("type").contentEquals("textarea"))
					            	{
					            		element.sendKeys(username[i]);
					            		
					            	}
					            	if(element.getAttribute("type").contentEquals("password"))
					            	{
					            		element.sendKeys(password[j]);
					            		
					            	}
					            	if(element.getAttribute("type").contentEquals("checkbox") || element.getAttribute("type").contentEquals("radio"))
					            	{
					            		if(element.isSelected() && (checkbox[k] == 0))
					            			element.click();
					            		else if((element.isSelected() == false) && (checkbox[k] == 1))
					            			element.click();
					            		
					            	}
					            	if(element.getAttribute("type").contentEquals("submit") || element.getAttribute("type").contentEquals("image"))
					            	{
					            		// CLICK ELEMENT
				            			elementclick(driver2, element);						            								            								            	
					            		
					            		// SIGNOUT
						            	signout(driver2);

					                    driver2.get(URL);		
					            	}
				            	
				            	}
		        			}
		            	}
        			}
        			// DROPDOWNLIST IS PRESENT
        			else
        			{
        			for(int[] pair: selectTester)
        			{        				
		        			for(int k = 0; k < checkbox.length; k++)
		        			{		        						        				
			        			// TEST ON 2nd DRIVER
			        			driver2.get(URL);
			        			for (WebElement option : allOptions) {
			        				
			        				// CHECK IF INPUT HAS A NAME
					            	if(option.getAttribute("name").contentEquals(""))
					            	{
					            		// CLICK A BUTTON WITH TAG <BUTTON>
					            		element = driver2.findElement(By.tagName("button"));
					            		if(element.getTagName().contentEquals("button"))
						            	{
					            			
					            			selectlistinput(driver2, pair);
					            			
					            			// CLICK ELEMENT
					            			elementclick(driver2, element);						            								            								            	
						            		
						            		// SIGNOUT
							            	signout(driver2);

						                    driver2.get(URL);		
						                    
						            	}
					            	}
					            	else
					            	{
					            		// GRAB ELEMENT FROM DRIVER TO USE IN DRIVER2
						            	element = driver2.findElement(By.name(option.getAttribute("name")));
						            	if(element.getAttribute("type").contentEquals("text") || element.getAttribute("type").contentEquals("textarea"))
						            	{
						            		element.sendKeys(username[i]);
						            		
						            	}
						            	if(element.getAttribute("type").contentEquals("password"))
						            	{
						            		element.sendKeys(password[j]);
						            		
						            	}
						            	if(element.getAttribute("type").contentEquals("checkbox")|| element.getAttribute("type").contentEquals("radio"))
						            	{
						            		if(element.isSelected() && (checkbox[k] == 0))
						            			element.click();
						            		else if((element.isSelected() == false) && (checkbox[k] == 1))
						            			element.click();
						            		
						            	}
						            	if(element.getAttribute("type").contentEquals("submit") || element.getAttribute("type").contentEquals("image"))
						            	{
						            		
						           
						            		selectlistinput(driver2, pair);
						            		// CLICK ELEMENT
					            			elementclick(driver2, element);						            								            								            	
						            		
						            		// SIGNOUT
							            	signout(driver2);

						                    driver2.get(URL);		
						            	}
					            	
					            	}
			        			}
			            	}
			            	
	            		}
	        			
	        			// DELETE HISTORY
	        			//driver2.manage().deleteAllCookies();
	        			
	        			
        			}
        			

            	}
            }
            }
            

        //driver.quit();
    }
    
    // SIGNOUT FUNCTION
    public static void elementclick (WebDriver driver, WebElement element){
    	
    	try{
    		element.click();
    		}
    	catch ( StaleElementReferenceException e){
    			//driver.navigate().back();
    		}
    	Alert alert = driver.switchTo().alert();
    	try{
    		alert.accept();
    	}
    	catch (Exception e) {

        }			
  	 }
    
    // SIGNOUT FUNCTION
    public static void signout (WebDriver driver){
    	
    	for(int soindex = 0; soindex < signout.length; soindex++)
		{
			try {
				driver.findElement(By.linkText(signout[soindex])).click();
            } catch (Exception e) {

            }            
		}    	
  	 }

    // DROPDOWNLIST SELECTOR
    	public static void selectlistinput (WebDriver driver, int[] pair){
    	
	    	selectInputs = pair;
	    	for(int i =0; i < selectList.length; i++)
	    	{
	    		WebElement element = driver.findElement(By.name(selectName[i]));
	    		//element.findElement(By.name(selectName[i]));
	    		Select select = new Select(element);
	        	List<WebElement> listops = select.getOptions();
	        	WebElement element2 = listops.get(selectInputs[i]);
	        	element2.click();
	    		
	    	}

	    	
	  	 }
    	
    // GENERATE PAIRS	
    static ArrayList<int[]> allpairs(){
  		  int[] choices = selectList;
  		  boolean noShuffle = false;
  		  int maxGoes = 100;
  		  long seed = 42;
  		  
  		  return AllPairs.generatePairs(choices, seed, maxGoes, !noShuffle, null, false);
  	 }
    
   
}

