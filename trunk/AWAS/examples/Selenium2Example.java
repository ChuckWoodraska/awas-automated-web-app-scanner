package examples;

	import input.MyFirefoxDriver;

import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

	public class Selenium2Example  {
	    public static void main(String[] args) {
	        // Create a new instance of the Firefox driver
	        // Notice that the remainder of the code relies on the interface, 
	        // not the implementation.
	        WebDriver driver = new MyFirefoxDriver();
	        driver.get("http://www.google.com");

	        System.out.println("#anchors: "+driver.findElements(By.tagName("a")).size());
	        System.out.println("#forms: "+driver.findElements(By.tagName("form")).size());

	        for (WebElement element: driver.findElements(By.tagName("form"))){
	        	System.out.println("text: "+element.getText());
	        	System.out.println("id: "+element.getAttribute("id"));
	        	System.out.println("class: "+element.getAttribute("class"));
System.out.println(element);

//		        element.sendKeys("Cheese!");
//		        element.submit();
	        }
/*	        
	        for (WebElement element:       driver.findElements(By.tagName("a"))){
	        	System.out.println("text: "+element.getText());
	        	System.out.println("id: "+element.getAttribute("id"));
	        	System.out.println("class: "+element.getAttribute("class"));
	        	System.out.println("href: "+element.getAttribute("href"));
	        }	        
*/
	        	
	        // Find the text input element by its name
	        WebElement element = driver.findElement(By.name("q"));

	        
	        // Enter something to search for
	        element.sendKeys("Cheese!");

	        // Now submit the form. WebDriver will find the form for us from the element
	        element.submit();

	        // Check the title of the page
	        System.out.println("Page title is: " + driver.getTitle());
	        
	        //Close the browser
//	        driver.quit();
	    }
	}
