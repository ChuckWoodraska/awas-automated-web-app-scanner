package input;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserInputReader {
	
	private CombinationalInputs userInputCombos = new CombinationalInputs(new ArrayList<ComboInput>());
	private ArrayList<UserInput> userInput = new ArrayList<UserInput>();
	private ArrayList<UserInput> userInputInvalid = new ArrayList<UserInput>();
	private int currentRow = 0;
	
	private String path;
	private int formIndex;
	
	public UserInputReader(String path, int formIndex){
		this.path = path;
		this.formIndex = formIndex;	
	}
		
	public void readFromForm(String formName, String url)
	{
		WebDriver driver = new MyFirefoxDriver(); 
		driver.get(url);
		WebElement form = driver.findElement(By.id(formName));
		AutoFormNavigator afi = new AutoFormNavigator(userInputCombos, userInput, userInputInvalid);
		List<Object> inputs = afi.collectAllFormInputElements(form);
		for(int index=0; index<afi.pointerToFirstButton; index++){
			Object formInputElement = inputs.get(index);
			if(formInputElement instanceof WebElement)
			{
				String tempInputType = ((WebElement) formInputElement).getAttribute(AutoFormNavigator.KEYWORD_TYPE);
				if(tempInputType.equals(InputDataType.KEYWORD_TEXT) || tempInputType.equals(InputDataType.KEYWORD_TEXTAREA) || tempInputType.equals(InputDataType.KEYWORD_PASSWORD))
				{	
					String tempInputID = ((WebElement) formInputElement).getAttribute(AutoFormNavigator.KEYWORD_NAME);
					//ArrayList<String> userInputData = new ArrayList<String>();
					//ArrayList<String> userInputData2 = new ArrayList<String>();
					UserInput input = new UserInput(tempInputID, InputDataType.getFormInputType(tempInputType), new ArrayList<String>());	
					UserInput input2 = new UserInput(tempInputID, InputDataType.getFormInputType(tempInputType), new ArrayList<String>());	
					userInput.add(input);
					userInputInvalid.add(input2);
				}
			}
		
		}
		driver.close();
	}
	
  @SuppressWarnings("deprecation")
	public void readFromExcel(String formName, String url)
	{
		InputStream inputStream = null;

		try
		{
			inputStream = new FileInputStream(path);

	POIFSFileSystem fileSystem = null;

	try
	{
		fileSystem = new POIFSFileSystem(inputStream);

		HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
		HSSFSheet sheet = workBook.getSheetAt(0); // change from constant to variable at some point in time
		Iterator<Row> rows= sheet.rowIterator();
//		userInput = new ArrayList<UserInput>();
//		userInputInvalid = new ArrayList<UserInput>();
//		userInputCombos = new CombinationalInputs(new ArrayList<ComboInput>());
		Boolean formEnd = false;
		HSSFRow row = (HSSFRow)rows.next();
		while(row.getRowNum() != currentRow)
		{
			row = (HSSFRow)rows.next();
			
		}
		while (rows.hasNext())
		{
			
			if(formEnd)
				break;

			
			row = (HSSFRow)rows.next();
			
			// display row number in the console.
			// System.out.println ("Row No.: " + row.getRowNum());
		
			// once get a row its time to iterate through cells.
			Iterator<Cell> cells = row.cellIterator();
			int numOfCombinationInputs = 0;	
			while(cells.hasNext())
			{ 
				ArrayList<String> userInputData = new ArrayList<String>();
				ArrayList<String> userInputDataInvalid = new ArrayList<String>();
				
				HSSFCell cell = (HSSFCell)cells.next();
		
				// System.out.println ("Cell No.: " + cell.getCellNum());
				// System.out.println("String value: " + cell.getStringCellValue());
				if(cell.getStringCellValue().contains("//"))				
				{
					break;
				}
				else if(cell.getStringCellValue().contentEquals("FORM"))
				{		
					while(cells.hasNext())
					{
						cell = (HSSFCell)cells.next();
						if(cell.getCellNum() == 1)
						{						
							formIndex = (int) cell.getNumericCellValue();
						}
					}
					break;
				}
				else if(cell.getStringCellValue().contentEquals("FORMEND"))
				{					
					currentRow = row.getRowNum();
					formEnd = true;
					break;
				}
				else if(cell.getStringCellValue().contentEquals("COMBINATORIAL"))
				{			
					while(cells.hasNext())
					{
						cell = (HSSFCell)cells.next();
						if(cell.getCellNum() == 1)
						{						
							numOfCombinationInputs = (int) cell.getNumericCellValue();
						}

					}					
					
					int count = 0;
					while(count < numOfCombinationInputs)	
					{
						row = (HSSFRow)rows.next();
						cells = row.cellIterator();
						cell = (HSSFCell)cells.next();
						// System.out.println ("Row No.: " + row.getRowNum());
						// System.out.println ("Cell No.: " + cell.getCellNum());
						// System.out.println("String value: " + cell.getStringCellValue());
						
						ArrayList<String> comboNames = new ArrayList<String>();
						if(cell.getStringCellValue().contains("//"))				
						{}
						else if(cell.getStringCellValue().contentEquals("VALID"))
						{
							String tempInputID = "";
							String tempInputType = "";
							ArrayList<String> tempInputValues = new ArrayList<String>();
							ArrayList<String> tempInputValuesInvalid = new ArrayList<String>();
							while(cells.hasNext())
							{
								cell = (HSSFCell)cells.next();
								if(cell.getCellNum() == 1)
								{
									tempInputID = cell.getStringCellValue();
									comboNames.add(tempInputID);
								}
								else if(cell.getCellNum() == 2)
								{
									tempInputType = cell.getStringCellValue();
								}
								else if(cell.getCellNum() > 2)
								{
									if(cell.getStringCellValue().contentEquals(""))
									{}
									else if(cell.getStringCellValue().contentEquals("BLANK"))
									{
										tempInputValues.add("");
									}
									else
									{
										tempInputValues.add(cell.getStringCellValue());
									}									
								}									
							}
							UserInput input = new UserInput(tempInputID, InputDataType.getFormInputType(tempInputType), userInputData);
							userInput.add(input);
							ComboInput comboInput = new ComboInput(comboNames);						
							userInputCombos.addComboInput(comboInput);
							
							row = (HSSFRow)rows.next();
							cells = row.cellIterator();
						
							while(cells.hasNext())
							{
								cell = (HSSFCell)cells.next();
								if(cell.getCellNum() > 2)
								{
									if(cell.getStringCellValue().contentEquals(""))
									{}
									else if(cell.getStringCellValue().contentEquals("BLANK"))
									{
										tempInputValuesInvalid.add("");
									}
									else
									{
										tempInputValuesInvalid.add(cell.getStringCellValue());
									}	
								}
								
							}
							UserInput inputInvalid = new UserInput(tempInputID, InputDataType.getFormInputType(tempInputType), userInputDataInvalid);
							userInputInvalid.add(inputInvalid);
							++count;
						}	
					}
					
				}
				else if(cell.getStringCellValue().contains("VALID"))
				{		
					String tempInputID = "";
					String tempInputType = "";
					while(cells.hasNext())
					{
						cell = (HSSFCell)cells.next();
						if(cell.getCellNum() == 1)
						{
							tempInputID = cell.getStringCellValue();
						}
						else if(cell.getCellNum() == 2)
						{
							tempInputType = cell.getStringCellValue();
						}
						else if(cell.getCellNum() > 2)
						{
							if(cell.getStringCellValue().contentEquals(""))
							{}
							else if(cell.getStringCellValue().contentEquals("BLANK"))
							{
								userInputData.add("");
							}
							else
							{
								//System.out.println(cell.getStringCellValue());
								userInputData.add(cell.getStringCellValue());
							}							
						}
						//System.out.println(userInputData);
					}	
					UserInput input = new UserInput(tempInputID, InputDataType.getFormInputType(tempInputType), userInputData);
					userInput.add(input);
					
					row = (HSSFRow)rows.next();
					cells = row.cellIterator();
					
					while(cells.hasNext())
					{
						cell = (HSSFCell)cells.next();
						if(cell.getCellNum() > 2)
						{
							if(cell.getStringCellValue().contentEquals(""))
							{}
							else if(cell.getStringCellValue().contentEquals("BLANK"))
							{
								userInputDataInvalid.add("");
							}
							else
							{
								userInputDataInvalid.add(cell.getStringCellValue());
							}
							
						}

					}	
					
					UserInput inputInvalid = new UserInput(tempInputID, InputDataType.getFormInputType(tempInputType), userInputDataInvalid);
					userInputInvalid.add(inputInvalid);
					//System.out.println("array of string"+uit.get(count));
		        }
			}			
		}
	}
	catch (IOException e)
	{
			e.printStackTrace();
	}
	
		}
		catch (FileNotFoundException e)
		{
			System.out.println ("File not found in the specified path.");
			readFromForm(formName, url);
			
			userInputCombos = new CombinationalInputs(new ArrayList<ComboInput>());
		}

}  
  
  public CombinationalInputs getCombinationalInput()
  {
	  return userInputCombos;
  }
 
  public ArrayList<UserInput> getUserInput()
  {
	  return userInput;
  }
 
  public ArrayList<UserInput> getUserInputInvalid()
  {
	  return userInputInvalid;
  }
}