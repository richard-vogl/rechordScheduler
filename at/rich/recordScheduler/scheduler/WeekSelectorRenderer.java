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
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author mani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WeekSelectorRenderer extends JLabel implements ListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color c1, c2;
	
	public WeekSelectorRenderer(Color c1, Color c2){
		this.c1=c1;
		this.c2=c2;
		setOpaque(true);
		setFont(new Font("Arial", Font.PLAIN, 12));
	}
	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
		
		setText((String)value);
		if (index%2==0){
			setBackground(c1);
		}else{
			setBackground(c2);
		}
		if (isSelected&&index!=-1){
			setBorder(BorderFactory.createEtchedBorder(1));
		}else{
			setBorder(null);
		}

		return this;
	}

}
