/*
 * Created on 30.03.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package at.rich.recordScheduler.scheduler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author mani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TimeTableCellRenderer extends JLabel implements TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8352997902325507040L;

	public TimeTableCellRenderer(){
		setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, 
			                                       boolean isSelected, boolean hasFocus, 
												   int row, int column){
		
		TimeTableModel ttm = (TimeTableModel) table.getModel();
		
		setText((String) value);
		
		if (isSelected) {
			setBackground(Color.getHSBColor(0.0f,0.0f,0.5f));
			setOpaque(true);
		}else if (ttm.isCurrent(row, column)){
			setBackground(Color.getHSBColor(0.0f,1f,0.7f));
			setOpaque(true);
		}else{
			setOpaque(false);
		}
		if (hasFocus){
			setBorder(BorderFactory.createEtchedBorder());//.createLineBorder(Color.getHSBColor(0.65f,0.32f,1f)));
		}else{
			setBorder(null);
		}
		return this;
		

	}

}
