package input;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class OpenFileInterface extends JFrame{
	public OpenFileInterface()
	{
		JFileChooser jChooser = new JFileChooser();
	    jChooser.setCurrentDirectory(new File(".//"));            //Default Path
	    jChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    int index = jChooser.showDialog(null, "Open Folder");
	    if (index == JFileChooser.APPROVE_OPTION) {

	    JTextField jt = new JTextField() ;               
	    jt.setText(jChooser.getSelectedFile().getAbsolutePath());
	    String readPath = jt.getText() + "\\";                    // The filenName to be open
	    try {
				/**************************************************
				 System.out.println("111");                            Open the file here
				 *************************************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	public static void main(String[] args){
		new OpenFileInterface();
		
	}
}