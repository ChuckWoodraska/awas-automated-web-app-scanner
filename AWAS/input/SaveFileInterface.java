package input;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class SaveFileInterface extends JFrame {

	public SaveFileInterface() {
		JFileChooser jChooser2 = new JFileChooser();
		jChooser2.setCurrentDirectory(new File(".//"));// Default Path to Save
		jChooser2.setDialogType(JFileChooser.SAVE_DIALOG);

		int index = jChooser2.showDialog(null, "Save File");
		if (index == JFileChooser.APPROVE_OPTION) {

			File f = jChooser2.getSelectedFile();
			String fileName =  ".xls";        //Save Name here
			String writePath = jChooser2.getCurrentDirectory()      //Save Path here
					.getAbsolutePath() + fileName;
			try {
				/**************************************************
				 System.out.println();                            Call the function here
				 *************************************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
			int option = JOptionPane.showConfirmDialog(null, "Successfully Saved", "Message",
					JOptionPane.CLOSED_OPTION);
			System.exit(0);
		}
	}
	public static void main(String args[])
	{
		new SaveFileInterface();
	}
}