package input;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class UserInputWriter {
	
	public CombinationalInputs userInputCombos = new CombinationalInputs(new ArrayList<ComboInput>());
	public ArrayList<UserInput> userInput;
	private int currentRow = 0;
	
	private String path;
	private int formIndex;
	
	public UserInputWriter(String path, int formIndex){
		this.path = path;
		this.formIndex = formIndex;		
	}
	
	public void writeToExcel()
	{
		// create a new file
		FileOutputStream out;
		try {
			out = new FileOutputStream("workbook.xls");
		
		// create a new workbook
		Workbook wb = new HSSFWorkbook();
		// create a new sheet
		Sheet s = wb.createSheet();
		// declare a row object reference
		Row r = null;
		// declare a cell object reference
		Cell c = null;
		// create 3 cell styles
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();
		CellStyle cs3 = wb.createCellStyle();
		DataFormat df = wb.createDataFormat();
		// create 2 fonts objects
		Font f = wb.createFont();
		Font f2 = wb.createFont();
	
		// in case of plain ascii
		wb.setSheetName(0, "Test");
		// create a sheet with 30 rows (0-29)
		int rownum;
		for (rownum = (short) 0; rownum < 30; rownum++)
		{
		    // create a row
		    r = s.createRow(rownum);
	
		    //r.setRowNum(( short ) rownum);
		    // create 10 cells (0-9) (the += 2 becomes apparent later
		    for (short cellnum = (short) 0; cellnum < 10; cellnum += 2)
		    {

	
	
		        // make this column a bit wider
		        s.setColumnWidth((short) (cellnum + 1), (short) ((50 * 8) / ((double) 1 / 20)));
		    }
		}
	
		//create 50 cells
		for (short cellnum = (short) 0; cellnum < 50; cellnum++)
		{
		    //create a blank type cell (no value)
		    c = r.createCell(cellnum);
		    // set it to the thick black border style
		    c.setCellStyle(cs3);
		}
	
		//end draw thick black border
	
	
		// demonstrate adding/naming and deleting a sheet
		// create a sheet, set its title then delete it
		s = wb.createSheet();
		wb.setSheetName(1, "DeletedSheet");
		//end deleted sheet
	
		// write the workbook to the output stream
		// close our file (don't blow out our file handles
		try {
			wb.write(out);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			System.out.println("Problems!");
		}
	}	
	
	public static void main(String[] args) {
		   UserInputWriter writer = new UserInputWriter("path", 0);
		   
		   writer.writeToExcel();
		
	   }
}