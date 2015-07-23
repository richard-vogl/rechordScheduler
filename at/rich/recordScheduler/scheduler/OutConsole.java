/*
 * Created on 04.04.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package at.rich.recordScheduler.scheduler;

import java.awt.Font;

import javax.swing.JTextArea;

import cz.dhl.ui.CoConsole;
			
/**
 * @author mani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OutConsole extends JTextArea implements CoConsole {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6500114854820066156L;

	public OutConsole(int x, int y){
		super(x,y);
		this.setEditable(false);
		this.setFont(new Font("Courier", Font.PLAIN, 12));
		this.setAutoscrolls(true);
		//this.setPreferredSize(new Dimension(200,100));
		this.setText("=== Scheduler ===");
	}

	public void print(String s) {
		this.append("\n"+s);
	}

	/**
	 * @param string
	 */
	public void printNoBr(String s) {
		this.append(s);
		
	}

}
