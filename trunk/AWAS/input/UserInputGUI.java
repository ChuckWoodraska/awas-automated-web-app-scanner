package input;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import testtree.TestNode;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UserInputGUI extends JPanel {
	
	
	public TestNode node;
	
	public UserInputGUI(TestNode currentNode){
		this.node = currentNode;
	}
	
    public void showUserInput()
    {

    	GeneralTablePanel validDataPanel = new GeneralTablePanel(node.getValidData(), node.columnNames, node.getUserInput().size()+1);
    	GeneralTablePanel invalidDataPanel = new GeneralTablePanel(node.getInvalidData(), node.columnNames, node.getUserInputInvalid().size()+1);
    	GeneralTablePanel validTestOracleTable = new GeneralTablePanel(node.getValidTestOracleTable(), node.columnNamesTestOracleTable, 4);
    	GeneralTablePanel invalidTestOracleTable = new GeneralTablePanel(node.getInvalidTestOracleTable(), node.columnNamesTestOracleTable, 4);
    	
    	ArrayList<String> s = new ArrayList<String>();
    	node.userInputCombos.getDataInputs();
    	for(int i = 1; i < node.columnNames.length; ++i)
    	{
    		s.add(node.columnNames[i]);
    	}
    	
        //Create and set up the content pane.
        JComponent comboListTable = new ComboListTable(s, node);
        comboListTable.setOpaque(true); //content panes must be opaque
        
        JPanel validPanel = new JPanel();
        JPanel invalidPanel = new JPanel();
        
		JFrame frame = new JFrame("Table");
		   frame.addWindowListener( new WindowAdapter() {
			      @Override
				public void windowClosing( WindowEvent e ) {
			        //System.exit(0);
			      }
			    });
	    
	   JScrollPane validDataPane = new JScrollPane(validDataPanel);
	   JScrollPane invalidDataPane = new JScrollPane(invalidDataPanel);
	   JScrollPane comboListPane = new JScrollPane(comboListTable);
	   JScrollPane validTestOraclePane = new JScrollPane(validTestOracleTable);
	   JScrollPane invalidTestOraclePane = new JScrollPane(invalidTestOracleTable);
	   
	   // Valid Data
	   validPanel.add(validDataPane);
	   validPanel.add(validTestOraclePane);
	   // Invalid Data
	   invalidPanel.add(invalidDataPane);
	   invalidPanel.add(invalidTestOraclePane);
	   
	   
	   // JComponent panel2 = makeTextPanel("INVALID");
	   JTabbedPane tabbedPane = new JTabbedPane();
	   tabbedPane.addTab("Valid", validPanel);
	   tabbedPane.addTab("Invalid", invalidPanel);
	   tabbedPane.addTab("Combinations", comboListPane);
	   //tabbedPane.addTab("Test Oracle", validTestOraclePane);
	   
	   //scrollpane.setPreferredSize(MainFrame.EditPanelSize);
	   frame.getContentPane().add(tabbedPane);
	   frame.pack();
	   frame.setVisible(true);
	   frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
    	
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(SwingConstants.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    


}
