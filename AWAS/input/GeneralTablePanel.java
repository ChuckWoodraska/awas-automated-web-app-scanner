/* 	All Rights Reserved
	Author Dianxiang Xu
*/
package input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import testtree.TestNode;



public class GeneralTablePanel extends JPanel implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 1L;

	private static final String insertRowBefore = "Insert Row Before";
	private static final String insertRowAfter = "Insert Row After";
	private static final String deleteRow = "Delete Row";
	private static final String adjustRowHeight = "Adjust Row Heights";
	private static final String saveTable = "Save Table";
	private static final String exitTable = "Exit Table";
	
//  non-evaluated guard conditions associated with net transitions are used only for creating dirty tests with branch coverage
//  they can be edited through Excel (next to the "effect" column). 
	private static final String[] functionNetColumnNamesWithGuard = {"No", "Transition", "Precondition", "Postcondition", "When", "Effect", "Guard"};
	private static final String[] functionNetColumnNames = {"No", "Transition", "Precondition", "Postcondition", "When", "Effect"};
	private static final String[] contractColumnNames = {"No", "Module", "Precondition", "Postcondition", "When", "Effect"};
	private static final String[] stateMachineColumnNames = {"No", "Event", "Start State", "End State", "Precondition", "Postcondition"};
	private static final String[] threatNetColumnNames = {"No", "Transition", "Precondition", "Postcondition", "When", "Effect"};
	private static final String[] threatTreeColumnNames = {"No", "Event", "Child Events", "Relation"};
//	private static final String[] combinatorialColumnNames = {"No", "Event", "Input Domains", "Postcondition"};
	private static final String[] combinatorialColumnNames = {"No", "Event", "Input Domains"};

	private static final String[] objectColumnNames = {"No", "Model-Level Object", "Implementation-Level Object"};

	private static final String[] methodColumnNamesForHTML = {"No", "Model-Level Event", "Command", "Target", "Value"};
	private static final String[] accessorColumnNamesForHTML = {"No", "Model-Level State", "Accesor Command", "Target", "Value"};
	private static final String[] mutatorColumnNamesForHTML = {"No", "Model-Level State", "Mutator Command", "Target", "Value"};

	private static final String[] methodColumnNames = {"No", "Model-Level Event", "Implementation Code"};
	private static final String[] accessorColumnNames = {"No", "Model-Level State", "Implementation Accessor"};
	private static final String[] mutatorColumnNames = {"No", "Model-Level State", "Implementation Mutator"};
	
	private static final int totalColumnCountForOperators = methodColumnNamesForHTML.length;

	public static enum MIDTableType {FUNCTIONNET, STATEMACHINE, CONTRACT, THREATNET, THREATTREE, COMBINATORIAL, OBJECT, METHOD, ACCESSOR, MUTATOR};

	//private GeneralEditor editor; 
	private GeneralTableModel tableModel;
	private JTable table;
	
	private TableCellEditor tableCellEditor;
	
	public GeneralTablePanel(Vector<Vector<Object>> data, String[] columnNames, int totalColumnCount){

		//System.out.println("numOfInputs:" + totalColumnCount);
		tableModel = new GeneralTableModel(data, columnNames, totalColumnCount, true);
		//tableModel.addTableModelListener(editor);
		table = new JTable(tableModel);
		table.getTableHeader().setFont(new Font("Helvetica Bold", Font.PLAIN ,12));
		table.getTableHeader().setForeground(new Color(0,0,0));

		// align headers of all columns
		TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer();
		JLabel headerLabel = (JLabel)rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER); 

		// align the header of first column
/*		DefaultTableCellHeaderRenderer headerRenderer = new DefaultTableCellHeaderRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setHeaderRenderer(headerRenderer);
*/
		table.setFont(new Font("Helvetica Bold", Font.PLAIN ,12));
		table.setRowHeight(20);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		
//		if (tableType==MIDTableType.FUNCTIONNET || tableType==MIDTableType.CONTRACT ||tableType==MIDTableType.STATEMACHINE)
			table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
//		else {
//			table.setDefaultRenderer(String.class, new MyDefaultCellRenderer());
//		}
	    table.setFillsViewportHeight(true);
/*	    table.addKeyListener(new java.awt.event.KeyAdapter() {
	        public void keyPressed(java.awt.event.KeyEvent evt) {
	        	if (evt.getKeyCode() == evt.VK_DOWN)
	        		evt.consume();
	        }
	   });
*/		
	    tableCellEditor= new TableCellEditor(new Font("Helvetica Bold", Font.PLAIN ,12));
	    table.setDefaultEditor(Object.class, tableCellEditor);
	    
	    
	   // if (table.isEditing())
	    	setupPopupMenu();
		ListSelectionModel listMod =  table.getSelectionModel();
		listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMod.addListSelectionListener(this);

		// align first column center  
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
	    setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);

		setPreferredColumnWidths();
	}
	
//	public static GeneralTablePanel createModelTablePanel(XMIDEditor editor, MIDTableType tableType, Vector<Vector<Object>> data){
//		if (tableType==MIDTableType.FUNCTIONNET && hasGuardForPetriNet(data))
//			return new GeneralTablePanel(editor, tableType, data, functionNetColumnNamesWithGuard, functionNetColumnNamesWithGuard.length);			
//		return new GeneralTablePanel(editor, tableType, data, getColumnNames(tableType), functionNetColumnNames.length);
//	}
//	
//	public static GeneralTablePanel createObjectTablePanel(XMIDEditor editor,  Vector<Vector<Object>> data){
//		return new GeneralTablePanel(editor, MIDTableType.OBJECT, data, objectColumnNames, objectColumnNames.length);
//	}
//
//	public static GeneralTablePanel createMethodTablePanel(XMIDEditor editor, TargetLanguage language, Vector<Vector<Object>> data){
//		return new GeneralTablePanel(editor, MIDTableType.METHOD, data, getColumnNames(language, MIDTableType.METHOD), totalColumnCountForOperators);
//	}
//
//	public static GeneralTablePanel createAccessorTablePanel(XMIDEditor editor, TargetLanguage language, Vector<Vector<Object>> data){
//		return new GeneralTablePanel(editor, MIDTableType.ACCESSOR, data, getColumnNames(language, MIDTableType.ACCESSOR), totalColumnCountForOperators);
//	}
//	
//	public static GeneralTablePanel createMutatorTablePanel(XMIDEditor editor, TargetLanguage language, Vector<Vector<Object>> data){
//		return new GeneralTablePanel(editor, MIDTableType.MUTATOR, data, getColumnNames(language, MIDTableType.MUTATOR), totalColumnCountForOperators);
//	}

	private static boolean hasGuardForPetriNet(Vector<Vector<Object>> data){
		for (int rowIndex=0; rowIndex<data.size(); rowIndex++){
			Vector<Object> row = data.get(rowIndex);
			if (row.size()>6){
				String guard = (String)row.get(6);
				if (guard!=null && !guard.trim().equals(""))
					return true;
			}
		}
		return false;
	}
	
	public JTable getTable(){
		return table;
	}
	
	public GeneralTableModel getTableModel(){
		return tableModel;
	}
	
	public void setMinRows(int rows){
		tableModel.setMinimumRows(rows);
	}
	
	public void setFont(Font font){
		super.setFont(font);
		if (table!=null)
			table.setFont(font);
		if (tableCellEditor!=null)
			tableCellEditor.updateFont(font);
	}
	
	public static String[] getColumnNames(MIDTableType tableType){
		switch (tableType){
			case FUNCTIONNET: return functionNetColumnNames;
			case CONTRACT: return contractColumnNames;
			case STATEMACHINE: return stateMachineColumnNames;
			case THREATNET: return threatNetColumnNames;
			case THREATTREE: return threatTreeColumnNames;
			case COMBINATORIAL: return combinatorialColumnNames;
			case OBJECT: return objectColumnNames;
			default: return functionNetColumnNames;
		}
	}
	
	private void setPreferredColumnWidths(){
		Dimension size = getSize();
		
		int totalWidth = size.width-10;
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int selectedRow = table.getSelectedRow();			
		     if (selectedRow >= 0) {
//		    	 setToPreferredRowHeight(selectedRow);
//		          System.out.println("Row"+selectedRow+tableModel.rowString(selectedRow));
		          if (selectedRow==tableModel.getRowCount()-1 && !tableModel.isEmptyRow(selectedRow))
		  				tableModel.addRow();
		     }
		}
		validate();
		updateUI();
	}

	private void setToPreferredRowHeight(int rowIndex) {
		  	int height = 0;
		  	for (int c=0; c<table.getColumnCount(); c++) {
		       TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
	    	   Component comp = table.prepareRenderer(renderer, rowIndex, c);
	    	   height = Math.max(height, comp.getPreferredSize().height);
		  	}
		  	if (height!=table.getRowHeight(rowIndex))
		  		table.setRowHeight(rowIndex, height);
	}

	private void setupPopupMenu() {
		final JPopupMenu popupMenu = new JPopupMenu();

		table.addMouseListener( new MouseAdapter() { 
			public void mousePressed( MouseEvent e ) { 
				checkForTriggerEvent(e); 
			} 
			public void mouseReleased( MouseEvent e ) { 
				checkForTriggerEvent(e); 
			}
			private void checkForTriggerEvent( MouseEvent e ) { 
				if ( e.isPopupTrigger()) { 
					popupMenu.removeAll();
					//if (table.isEditing()){
						createPopupMenuItem(popupMenu, insertRowBefore, insertRowBefore);
						createPopupMenuItem(popupMenu, insertRowAfter, insertRowAfter);
						createPopupMenuItem(popupMenu, saveTable, saveTable);
						JMenuItem deleteRowItem = createPopupMenuItem(popupMenu, deleteRow, deleteRow);
						deleteRowItem.setEnabled(table.getSelectedRow()>=0);
						createPopupMenuItem(popupMenu, adjustRowHeight, adjustRowHeight);
						createPopupMenuItem(popupMenu, exitTable, exitTable);
					//}
					popupMenu.show( e.getComponent(), e.getX(), e.getY() );
				}	
			} 
		}); 
	}
	
	private JMenuItem createPopupMenuItem(JPopupMenu popupMenu, String title, String command){
		JMenuItem menuItem = popupMenu.add(title);
		menuItem.setActionCommand(command);
		menuItem.addActionListener(this);
		return menuItem;
	}
	
    // implements ActionListener
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == insertRowBefore) {
			int selectedRow= table.getSelectedRow();
			if (selectedRow==-1)
				selectedRow=0;
			tableModel.insertRow(selectedRow);
		} else		
		if (cmd == insertRowAfter) {
			int selectedRow= table.getSelectedRow();
			if (selectedRow==-1)
				selectedRow=table.getRowCount()-1;
			tableModel.insertRow(selectedRow+1);			
		} else
		if (cmd == saveTable) {
			
			
		} else
		if (cmd == deleteRow){
			tableModel.removeRow(table.getSelectedRow());
		} else
		if (cmd == adjustRowHeight){
			for (int row=0; row<table.getRowCount(); row++)
				setToPreferredRowHeight(row);
		} else
		if (cmd == exitTable){
			
		}
	}
	
}
