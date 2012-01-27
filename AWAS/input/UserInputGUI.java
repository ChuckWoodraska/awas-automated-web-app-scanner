package input;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

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

    	GeneralTablePanel tablePanel = new GeneralTablePanel(node.getValidData(), node.columnNames, node.getUserInput().size()+1);
    	GeneralTablePanel tablePanel2 = new GeneralTablePanel(node.getInvalidData(), node.columnNames, node.getUserInputInvalid().size()+1);
    	
    	ArrayList<String> s = new ArrayList<String>();
    	node.userInputCombos.getDataInputs();
    	for(int i = 1; i < node.columnNames.length; ++i)
    	{
    		s.add(node.columnNames[i]);
    	}
    	
    	
    	
//    	for(int i = 0; i < node.userInputCombos.getDataInputs().size(); ++i)
//    	{
//    		for(int j = 0; j < node.userInputCombos.getDataInputs().get(i).getSize(); ++j)
//    		{
//        		s1.addAll(node.userInputCombos.getDataInputs().get(j).getInputName());
//    		}
//    	}
    	
        //Create and set up the content pane.
        JComponent newContentPane = new ComboListTable(s, node);
        newContentPane.setOpaque(true); //content panes must be opaque
        
        //Create and set up the content pane.
        JComponent testOraclePanel = new TestOracleTable(node);
        newContentPane.setOpaque(true); //content panes must be opaque

		JFrame frame = new JFrame("Table");
		   frame.addWindowListener( new WindowAdapter() {
			      public void windowClosing( WindowEvent e ) {
			        //System.exit(0);
			      }
			    });
	   
		   
		   
		   
	   JScrollPane scrollpane = new JScrollPane(tablePanel);
	   JScrollPane scrollpane2 = new JScrollPane(tablePanel2);
	   JScrollPane scrollpane3 = new JScrollPane(newContentPane);
	   JScrollPane scrollpane4 = new JScrollPane(testOraclePanel);
	   // JComponent panel2 = makeTextPanel("INVALID");
	   JTabbedPane tabbedPane = new JTabbedPane();
	   tabbedPane.addTab("Valid", scrollpane);
	   tabbedPane.addTab("Invalid", scrollpane2);
	   tabbedPane.addTab("Combinations", scrollpane3);
	   tabbedPane.addTab("Test Oracle", scrollpane4);
	   
	   //scrollpane.setPreferredSize(MainFrame.EditPanelSize);
	   frame.getContentPane().add(tabbedPane);
	   frame.pack();
	   frame.setVisible(true);
	   frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    	
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    


}
