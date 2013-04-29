package input;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class PreferenceContent extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String FilePath;
	public static int Max_Node ;
	public static int Max_Links ;
	public static int Wait_times ;
	public static boolean PA;
	private static JFrame jf;
	private PreferenceOptions po;

	public PreferenceContent(PreferenceOptions preferenceOptions) {
		this.po = preferenceOptions;


	}
		

	public static void main(String args[]) {
		//PreferenceInterface pi = new PreferenceInterface();
		//pi.showGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
	
	public void showGUI(){
		

		
	}
	
	private void preferenceContent(){
		
}

class Savefile extends JFrame {

	public Savefile() {
		JFileChooser fc = new JFileChooser();
		this.setSize(300, 200);
		this.setLocation(300, 200);
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		fc.setDialogTitle("Save File");
		fc.setApproveButtonText("OK");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(fc)) {
			PreferenceInterface.FilePath = fc.getSelectedFile().getPath();
		}
	}
}
}