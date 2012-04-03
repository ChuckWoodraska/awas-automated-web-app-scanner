package input;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import testtree.TestNode;
 
public class ComboListTable extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private JList list2;
    private DefaultListModel listModel;
    private DefaultListModel listModel2;
 
    private static final String addString = "Add";
    private static final String deleteString = "Delete";
    private JButton deleteButton;
    private ArrayList<String> inputNames;
    public TestNode node;

 
    public ComboListTable(ArrayList<String> s, TestNode node) {
        super(new BorderLayout());
        this.node = node;
        listModel = new DefaultListModel();
        for(String temp : s)
        {
        	listModel.addElement(temp);
        }

        listModel2 = new DefaultListModel();
        for(String temp : node.inputCombos)
        {
        	listModel2.addElement(temp);
        }
        
        JButton addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(true);
        
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(20);
        JScrollPane listScrollPane = new JScrollPane(list);
 
        list2 = new JList(listModel2);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setSelectedIndex(0);
        list2.addListSelectionListener(this);
        list2.setVisibleRowCount(20);
        JScrollPane listScrollPane2 = new JScrollPane(list2);
        
        
 
        deleteButton = new JButton(deleteString);
        deleteButton.setActionCommand(deleteString);
        deleteButton.addActionListener(new DeleteListener());
 
//        inputName = new JTextField(10);
//        inputName.addActionListener(addListener);
//        inputName.getDocument().addDocumentListener(addListener);
//        String name = listModel.getElementAt(
//                              list.getSelectedIndex()).toString();
 
        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(addButton);       
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(deleteButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
 
        add(listScrollPane, BorderLayout.WEST);
        add(buttonPane, BorderLayout.CENTER);
        add(listScrollPane2, BorderLayout.EAST);
    }
 
    class DeleteListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list2.getSelectedIndex();
            listModel2.remove(index);
            node.inputCombos.remove(index);
 
            int size = listModel2.getSize();
 
            if (size == 0) { //Nobody's left, disable firing.
                deleteButton.setEnabled(false);
 
            } else { //Select an index.
                if (index == listModel2.getSize()) {
                    //removed item in last position
                    index--;
                }
 
                list2.setSelectedIndex(index);
                list2.ensureIndexIsVisible(index);
            }
        }
    }
 
    //This listener is shared by the text field and the hire button.
    class AddListener implements ActionListener, ListSelectionListener {
        private boolean alreadyEnabled = false;
        private JButton button;
 
        public AddListener(JButton button) {
            this.button = button;
        }
 
        //Required by ActionListener.
        @Override
		public void actionPerformed(ActionEvent e) {
//            String name = inputName.getText();
// 
//            //User didn't type in a unique name...
//            if (name.equals("") || alreadyInList(name)) {
//                Toolkit.getDefaultToolkit().beep();
//                inputName.requestFocusInWindow();
//                inputName.selectAll();
//                return;
//            }
 
            int index = list2.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }
 
            listModel2.insertElementAt(list.getSelectedValue(), index);
            node.inputCombos.add(list.getSelectedValue().toString());
            
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());
 
//            //Reset the text field.
//            inputName.requestFocusInWindow();
//            inputName.setText("");
// 
            //Select the new item and make it visible.
            list2.setSelectedIndex(index);
            list2.ensureIndexIsVisible(index);
        }
 
        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel2.contains(name);
        }
 
        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }
 
        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }
 
        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }
 
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }
 
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    }
 
    //This method is required by ListSelectionListener.
    @Override
	public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
 
            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                deleteButton.setEnabled(false);
 
            } else {
            //Selection, enable the fire button.
                deleteButton.setEnabled(true);
            }
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<String> s = new ArrayList<String>();
        s.add("cool");
        ArrayList<String> s2 = new ArrayList<String>();
        TestNode n = new TestNode("test");
        //Create and set up the content pane.
        JComponent newContentPane = new ComboListTable(s, n);
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
            @Override
			public void run() {
                createAndShowGUI();
            }
        });
    }
}