/*
 * Created on 30.03.2006
 *
 */
package at.rich.recordScheduler.scheduler.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import at.rich.recordScheduler.scheduler.RecorderConfig;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richard vogl
 *
 */
public class RecorderConfigDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8903276727710796039L;

	private static String TITLE = "Recorder Config";
	
	private RecorderConfig curConfig;	
	
	private JButton ok, cancel;
	
	private JCheckBox vbr;

	private JComboBox quality;

	private JTextField bitrate;
	
	private JTextField recpath;
	
	public RecorderConfigDialog(Frame frame, 
				Component locComp, RecorderConfig recorderConfig){
		super(frame, TITLE, true);	
		curConfig = recorderConfig;
		Container cp = this.getContentPane();
		JPanel buttons = new JPanel(new GridLayout(1,2, 10, 10));
		JPanel items = new JPanel(new GridLayout(4,2,10,10));
		buttons.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		items.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		cp.setLayout(new BorderLayout());
		
		
		ok = new JButton("Ok");
		ok.addActionListener(new OkButtonListener());
		cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelButtonListener());
	
		vbr = new JCheckBox();
		vbr.setSelected(curConfig.vbr);
		quality = new JComboBox(RecorderConfig.QUALITY_STR);
		quality.setSelectedIndex(curConfig.quality);
		bitrate = new JTextField(String.valueOf(curConfig.bitrate));
		recpath = new JTextField(curConfig.recpath);
		
		buttons.add(cancel);
		buttons.add(ok);
		
		items.add(new JLabel("VBR: "));
		items.add(vbr);

		items.add(new JLabel("Quality: "));
		items.add(quality);

		items.add(new JLabel("Bitrate: "));
		items.add(bitrate);
		
		items.add(new JLabel("Refording Path: "));
		items.add(recpath);
		
		cp.add(buttons, BorderLayout.SOUTH);
		cp.add(items, BorderLayout.CENTER);
		this.pack();
		
	}
	
	private final class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			setVisible(false);
		}
	}

	private final class OkButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			curConfig.vbr = vbr.isSelected();
			curConfig.quality = quality.getSelectedIndex();
			curConfig.bitrate = Integer.parseInt(bitrate.getText());
			curConfig.recpath = recpath.getText();
			setVisible(false);
		}
	}
}
