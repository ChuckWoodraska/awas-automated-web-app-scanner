package input;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.border.*;

import java.awt.*;
 
public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
//	private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
/*		
	public MultiLineCellRenderer() {
		super();
		setOpaque(true);
 		setEditorKit(new MyEditorKit());
		SimpleAttributeSet attrs=new SimpleAttributeSet();
//		StyleConstants.setAlignment(attrs,StyleConstants.ALIGN_CENTER);
		StyledDocument doc=(StyledDocument)getDocument();
		doc.setParagraphAttributes(0,doc.getLength()-1,attrs, false);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column) {
		
		JTable.DropLocation dropLocation = table.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column) {
            isSelected = true;
        }
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		}else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
//System.out.println(row+" : "+column);
//System.out.println("background: "+getBackground());
//System.out.println("foreround: "+getForeground());
		}
		if (hasFocus) {
//			System.out.println(row+"  "+column+" has focus");
			setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
//			if (table.isCellEditable(row, column)) {
//				setForeground(UIManager.getColor("Table.focusCellForeground") );
//				setBackground(UIManager.getColor("Table.focusCellBackground") );
//			}
		    if (!isSelected && table.isCellEditable(row, column)) {
				setForeground(UIManager.getColor("Table.focusCellForeground") );
				setBackground(UIManager.getColor("Table.focusCellBackground") );
		    }

		} else {
			setBorder(NO_FOCUS_BORDER);
		}
//		System.out.println(row+" : "+column);
//		System.out.println((value == null) ? "" : value.toString());
		
		setText((value == null) ? "" : value.toString());
		setFont(table.getFont());
		setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
		if (table.getRowHeight(row) < getPreferredSize().height) {
			table.setRowHeight(row, getPreferredSize().height);
		}
		return this;
	}
*/
	public MultiLineCellRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);
	}
 
  public Component getTableCellRendererComponent(JTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setFont(table.getFont());
    if (hasFocus) {
      setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
      if (table.isCellEditable(row, column)) {
        setForeground( UIManager.getColor("Table.focusCellForeground") );
        setBackground( UIManager.getColor("Table.focusCellBackground") );
      }
    } else {
//    	setBorder(NO_FOCUS_BORDER); 
//    	setBorder(new EmptyBorder(3, 3, 3, 3));
    }
	setBorder(new EmptyBorder(3, 3, 3, 3));
    if (column==0)
       setText((value == null) ? "" : "  "+value.toString());
	else
	   setText((value == null) ? "" : value.toString());
//	setCaretPosition(0);

	setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
	if (table.getRowHeight(row) < getPreferredSize().height) {
		table.setRowHeight(row, getPreferredSize().height);
	} 
	return this;
  }
    

/*	public void invalidate() {}

	public void validate() {}

	public void revalidate() {}

	public void repaint(long tm, int x, int y, int width, int height) {}

	public void repaint(Rectangle r) { }

	public void repaint() {}
*/
}

class MyEditorKit extends StyledEditorKit{
	private static final long serialVersionUID = 1L;

	public ViewFactory getViewFactory(){
	    return new StyledViewFactory();
    }

    static class StyledViewFactory implements ViewFactory{
    	public View create(Element elem){
		    String kind = elem.getName();

		    if (kind != null){
			    if (kind.equals(AbstractDocument.ContentElementName)){
				    return new LabelView(elem);
			    }
			    else if (kind.equals(AbstractDocument.ParagraphElementName)){
				    return new ParagraphView(elem);
			    }
			    else if (kind.equals(AbstractDocument.SectionElementName)){
				    return new CenteredBoxView(elem, View.Y_AXIS);
			    }
			    else if (kind.equals(StyleConstants.ComponentElementName)){
				    return new ComponentView(elem);
			    }
			    else if (kind.equals(StyleConstants.IconElementName)){
				    return new IconView(elem);
			    }
	    	}
		    // default to text display
		    return new LabelView(elem);
	    }
    } // class StyledViewFactory
 } // class MyEditorKit

class CenteredBoxView extends BoxView {
	
	public CenteredBoxView(Element elem, int axis){
	    super(elem,axis);
    }

	protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
	    super.layoutMajorAxis(targetSpan,axis,offsets,spans);
		int textBlockHeight = 0;
	    int offset = 0;
	    for (int i = 0; i < spans.length; i++){
		    textBlockHeight += spans[ i ];
	    }
	    offset = (targetSpan - textBlockHeight) / 2;
	    for (int i = 0; i < offsets.length; i++){
		    offsets[ i ] += offset;
	    }
    }
}
