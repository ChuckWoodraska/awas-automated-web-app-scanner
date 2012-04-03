package input;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class TableCellEditor extends DefaultCellEditor implements ActionListener {
	private static final long serialVersionUID = 1L;
	
    final JTextArea textArea = new JTextArea();
	private Font font;
	
	public TableCellEditor(Font textFont) {
	    super(new JTextField());
	    this.font=textFont;
	    textArea.setWrapStyleWord(true);
	    textArea.setLineWrap(true);
		textArea.setMargin(new Insets(3,3,3,3));
		setPopupMenuForTextArea();
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    scrollPane.setBorder(null);
	    editorComponent = scrollPane;

	    delegate = new DefaultCellEditor.EditorDelegate() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void setValue(Object value) {
				textArea.setFont(font);
				textArea.setText((value != null) ? value.toString() : "");
			}
			
			@Override
			public Object getCellEditorValue() {
				return textArea.getText();
			}
	    };
	}

	public void updateFont(Font newFont){
		this.font=newFont;
	}
	
	private static final String editCellInNewWindow = "Edit Cell in New Window";

	private void setPopupMenuForTextArea() {

		final JPopupMenu popupMenu = new JPopupMenu();

		createPopupMenuItem(popupMenu, editCellInNewWindow, editCellInNewWindow);
		
		textArea.addMouseListener( new MouseAdapter() { 
			@Override
			public void mousePressed( MouseEvent e ) { 
				checkForTriggerEvent(e); 
			} 
			@Override
			public void mouseReleased( MouseEvent e ) { 
				checkForTriggerEvent(e); 
			}
			private void checkForTriggerEvent( MouseEvent e ) { 
				if ( e.isPopupTrigger()) { 
					popupMenu.show(e.getComponent(), e.getX(), e.getY() );					
				}	
			} 
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					new SeparateTableCellEditor(textArea);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == editCellInNewWindow) {
			new SeparateTableCellEditor(textArea);
		}
	}

}
