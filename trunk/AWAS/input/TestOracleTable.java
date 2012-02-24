package input;

import input.ComboListTable.AddListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import testtree.TestNode;

/*
 * ComboBoxDemo.java uses these additional files:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 */
public class TestOracleTable extends JPanel implements ActionListener {

	private static final String saveString = "Save";
    private static final String dataString = "";
    JTextField dataField;
    JComboBox testOracleTypeList;
    public TestNode node;

    public TestOracleTable(TestNode node) {
        super(new BorderLayout());
        this.node = node;
        String[] testTypeStrings = { "Title", "Partial Text" };

        //Create the combo box, select the item at index 4.
        //Indices start at 0, so 4 specifies the pig.
        testOracleTypeList = new JComboBox(testTypeStrings);
        try{

    	} catch (Exception e) {
    		testOracleTypeList.setSelectedIndex(0);
    	}    
    	testOracleTypeList.addActionListener(this);

        
        dataField = new JTextField(dataString);
        try{
        	//dataField.setText(node.getTestOracle().getTestData().get(0));
        } catch (Exception e) {
    		dataField.setText("");
    	}   
        
        JButton saveButton = new JButton(saveString);
        SaveListener saveListener = new SaveListener(saveButton);
        saveButton.addActionListener(saveListener);
        saveButton.setActionCommand(saveString);
        saveButton.setEnabled(true);
        
      //Lay out the demo.
        add(testOracleTypeList, BorderLayout.NORTH);
        add(dataField, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
       // add(picture, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }
    

    /** Listens to the combo box. */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        cb.getSelectedIndex();
        
        //updateLabel(petName);
    }
    
    class SaveListener implements ActionListener{
        private boolean alreadyEnabled = false;
        private JButton button;
 
        public SaveListener(JButton button) {
            this.button = button;
        }
 
        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
        	TestOracle testOracle = node.getTestOracle();
        	System.out.println(dataField.getText());
        	//testOracle.setTestData(dataField.getText());
        	//testOracle.setTestTypeData(testOracleTypeList.getSelectedIndex());

        }

    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ComboBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TestOracle testOracle = new TestOracle();
        TestNode n = new TestNode("test");
        //Create and set up the content pane.
        JComponent newContentPane = new TestOracleTable(n);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}