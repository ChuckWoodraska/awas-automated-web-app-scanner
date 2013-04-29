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

public class PreferenceInterface extends JPanel implements ActionListener {

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
	public PreferenceOptions po;

	public PreferenceInterface(final PreferenceOptions po) {
		this.po = po;
		System.out.println(po.getMaxNode());
		
		this.setLayout(new GridBagLayout());
		JLabel MaxNode = new JLabel("Max Number of Test Nodes");
		JLabel MaxLink = new JLabel("Max Number of Test Links");
		JLabel RadioButton = new JLabel("Choose the model");
		JLabel WaitTime = new JLabel("Wait Time(ms)");
		JLabel SaveFile = new JLabel("Default Save File");
		final JTextField NodeText = new JFormattedTextField(new DecimalFormat());
		final JTextField LinkText = new JFormattedTextField(new DecimalFormat());
		final JTextField TimeText = new JTextField();
		final JTextField Path = new JTextField();
		final JRadioButton pt = new JRadioButton("Pairwise Testing(default)");
		pt.setActionCommand("ToSaveFile");
		JRadioButton ac = new JRadioButton("All Combinations");
		ButtonGroup bg = new ButtonGroup();
		JButton browse = new JButton("browse");
		JButton buttonOK = new JButton("OK");
		buttonOK.setActionCommand("Enable");
		
		NodeText.setText(Integer.toString(po.getMaxNode()));
		LinkText.setText(Integer.toString(po.getMaxLinks()));
		TimeText.setText(Integer.toString(po.getWaitTime()));
		Path.setText(po.getSaveFilePath());
		pt.setSelected(po.getPairWise());
		
		bg.add(pt);
		bg.add(ac);
		this.add(MaxNode, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						5, 0, 0), 0, 0));
		this.add(NodeText, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 5, 0, 5), 0, 0));
		this.add(MaxLink, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 0, 5), 0, 0));
		this.add(LinkText, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 0, 5), 0, 0));
		this.add(WaitTime, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 0, 5), 0, 0));
		this.add(TimeText, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 0, 5), 0, 0));
		this.add(SaveFile, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 0, 5), 0, 0));
		this.add(Path, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 0, 5), 0, 0));
		this.add(browse, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 0, 2), 0, 0));
		this.add(RadioButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 0, 2), 0, 0));
		this.add(pt, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 0, 2), 0, 0));
		this.add(ac, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 0, 2), 0, 0));
		this.add(buttonOK, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(2, 2, 0, 2), 0, 0));
		pt.setSelected(true);
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Savefile sf = new Savefile();
				sf.setResizable(false);
				Path.setText(FilePath);
			}
		});
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (NodeText.getText().equals("")) {
					}
					else{						
						Max_Node = Integer.parseInt(NodeText.getText());
						po.setMaxNode(Max_Node);
					}
					if (LinkText.getText().equals("")) {
					}
					else{
						Max_Links = Integer.parseInt(LinkText.getText());
						po.setMaxLinks(Max_Links);
					}
					if(TimeText.getText().equals("")){
					}
					else{
						Wait_times = Integer.parseInt(TimeText.getText());
						po.setWaitTime(Wait_times);
					}
					if (pt.isSelected()) {
						PA = true;
						po.setPairWise(PA);
					} else {
						PA = false;
						po.setPairWise(PA);
					}
					FilePath = Path.getText().toString();
					po.setSaveFilePath(FilePath);

				} catch(Exception exception){
					
				}
				finally{
					System.out.println(po.getMaxNode());
					po.savePreferenceOptionsToFile();
					jf.dispose();
					
				}
			}
		});
		
	}

	public static void main(String args[]) {
		//PreferenceInterface pi = new PreferenceInterface();
		//pi.showGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
	
	public void showGUI(){
        jf = new JFrame("Save");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setContentPane(new PreferenceInterface(po));
        jf.setSize(650, 320);
        jf.setVisible(true);
		jf.setResizable(false);
		
		
	}
	
	
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