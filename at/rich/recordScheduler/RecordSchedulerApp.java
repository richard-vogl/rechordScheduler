/*
 * Created on 17.03.2006
 *
 */
package at.rich.recordScheduler;

import javax.swing.JFrame;

import at.rich.recordScheduler.scheduler.SchedulerGUI;

/**
 * @author Richard Vogl
 *
 * Main Class for The Record Scheduler Application
 */
public class RecordSchedulerApp {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		

	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame gui = new SchedulerGUI();
                gui.pack();
                gui.setVisible(true);
            }
        });
	}
	
}
