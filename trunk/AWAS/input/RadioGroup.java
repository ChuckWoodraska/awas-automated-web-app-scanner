package input;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class RadioGroup {
	
	private ArrayList<WebElement> radioButtons;
	private String name;
	
	public RadioGroup()
	{
		radioButtons = new ArrayList<WebElement>();
		name = "";
	}
	
	public RadioGroup(String name)
	{
		radioButtons = new ArrayList<WebElement>();
		this.name = name;
	}
	
	public void addRadioButton(WebElement radioButton)
	{
		radioButtons.add(radioButton);
		
	}
	
	public RadioGroup relocate(WebDriver driver)
	{
		//ArrayList<WebElement> newRadioButtons = new ArrayList<WebElement>();
		RadioGroup newRadioGroup = new RadioGroup(name);
		for(WebElement radioButton : radioButtons)
		{
			WebElement newElement = driver.findElement(By.id(radioButton.getAttribute("id")));
			assert newElement != null;
			newRadioGroup.addRadioButton(newElement);
		}
		return newRadioGroup;
	}
	
	public int size()
	{
		return radioButtons.size();
	}
	
	public WebElement getRadioButtonAt(int index)
	{
		return radioButtons.get(index);
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<WebElement> getRadioButtons()
	{
		return radioButtons;
	}
	
	public void printRBList()
	{
		for(WebElement rb : radioButtons)
		{
			System.out.println(rb.getAttribute(AutoFormNavigator.KEYWORD_VALUE));
		}
	}
}
