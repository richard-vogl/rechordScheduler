/*
 * Created on 30.03.2006
 *
 */
package at.rich.recordScheduler.scheduler.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import at.rich.recordScheduler.scheduler.SchFileFilter;
import at.rich.recordScheduler.scheduler.SchedulerConfig;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richard vogl
 *
 */
public class SchedulerConfigDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1903952195625043401L;

	private static String TITLE = "Scheduler Config";
	
	private SchedulerConfig curConfig;	
	
	private JButton ok, cancel;
	
	private JSlider s_time;
	private JTextField t_time;
	private JCheckBox autostart;
	private JTextField filename;
	private JButton fileChooserButton;

	protected static final int MIN_T = 1;

	protected static final int MAX_T = 120;

	private static final int BIG_STEP_T = 60;

	private static final int SMALL_STEP_T = 1;
	
	public SchedulerConfigDialog(Frame frame, 
				Component locComp, SchedulerConfig initConfig){
		super(frame, TITLE, true);	
		curConfig = initConfig;
		Container cp = this.getContentPane();
		JPanel buttons = new JPanel(new GridLayout(1,2, 10, 10));
		JPanel items = new JPanel(new GridLayout(2,1,10,10));
		buttons.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		items.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		cp.setLayout(new BorderLayout());
		
		
		ok = new JButton("Ok");
		ok.addActionListener(new OkButtonListener());
		cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelButtonListener());
	
		s_time = new JSlider();		
		s_time.setMinimum(MIN_T);
		s_time.setMaximum(MAX_T);
		s_time.setMajorTickSpacing(BIG_STEP_T);
		s_time.setMinorTickSpacing(SMALL_STEP_T);
		s_time.setValue(curConfig.sleeptime/1000);
		s_time.addChangeListener(new SliderRefresh());
		t_time = new JTextField(String.valueOf(curConfig.sleeptime/1000));
		t_time.addActionListener(new TextFieldRefresh(MIN_T, MAX_T));
		t_time.setPreferredSize(new Dimension(30,20));
		autostart = new JCheckBox();
		autostart.setSelected(curConfig.autostart);
		
		filename=new JTextField(curConfig.scheduleFile);
		filename.setPreferredSize(new Dimension(100,20));
		fileChooserButton = new JButton("...");
		fileChooserButton.setPreferredSize(new Dimension(30,20));
		fileChooserButton.addActionListener(new FileChooserButtonActionListener(this));
		buttons.add(cancel);
		buttons.add(ok);
		JPanel sleeptimeP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sleeptimeP.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Sleep Time"));
		sleeptimeP.add(s_time);
		sleeptimeP.add(t_time);
		sleeptimeP.add(new JLabel("sec"));
		items.add(sleeptimeP);

		JPanel startP = new JPanel(new GridLayout(2,1,10,10)),
		       autostartP = new JPanel(new FlowLayout(FlowLayout.LEFT)),
			   filenameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		startP.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Startup Otions"));
		autostartP.add(new JLabel("Autostart at startup:"));
		autostartP.add(autostart);
		filenameP.add(new JLabel("Schedule-filename:"));
		filenameP.add(filename);
		filenameP.add(fileChooserButton);
		startP.add(filenameP);
		startP.add(autostartP);
		items.add(startP);

		cp.add(buttons, BorderLayout.SOUTH);
		cp.add(items, BorderLayout.CENTER);
		this.pack();
		
	}
	
	private final class FileChooserButtonActionListener implements ActionListener {
		private SchedulerConfigDialog target;
		public FileChooserButtonActionListener(SchedulerConfigDialog target){
			this.target=target;
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new SchFileFilter());
			int returnVal = jfc.showOpenDialog(target);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	filename.setText(jfc.getSelectedFile().getAbsolutePath());
		    }
			
		}
	}

	private final class SliderRefresh implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			t_time.setText(String.valueOf(s_time.getValue()));
		}
	}

	private final class TextFieldRefresh implements ActionListener {
		private final int MIN_T;

		private final int MAX_T;

		private TextFieldRefresh(int MIN_T, int MAX_T) {
			super();
			this.MIN_T = MIN_T;
			this.MAX_T = MAX_T;
		}

		public void actionPerformed(ActionEvent e) {
			int v = Integer.parseInt(t_time.getText());
			if (v<MIN_T){
				v=MIN_T;
				t_time.setText(String.valueOf(v));
			}
			if (v>MAX_T){
				v=MAX_T;
				t_time.setText(String.valueOf(v));
			}
			
			s_time.setValue(v);
			
		}
	}

	private final class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			setVisible(false);
		}
	}

	private final class OkButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			curConfig.sleeptime = s_time.getValue()*1000;
			curConfig.autostart = autostart.isSelected();
			curConfig.scheduleFile = filename.getText();
			setVisible(false);
		}
	}
}