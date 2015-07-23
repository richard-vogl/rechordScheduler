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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import at.rich.recordScheduler.scheduler.FtpConfig;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richard vogl
 *
 */
public class FtpConfigDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1088726264487678438L;

	private static String TITLE = "Ftp Config";
	
	private FtpConfig curConfig;	
	
	private JButton ok, cancel;
	
	private JTextField server, user, pass, path, port;
	
	public FtpConfigDialog(Frame frame, 
				Component locComp, FtpConfig initConfig){
		super(frame, TITLE, true);	
		curConfig = initConfig;
		Container cp = this.getContentPane();
		JPanel buttons = new JPanel(new GridLayout(1,2, 10, 10));
		JPanel items = new JPanel(new GridLayout(5,2,10,10));
		buttons.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		items.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		cp.setLayout(new BorderLayout());
		
		
		ok = new JButton("Ok");
		ok.addActionListener(new OkButtonListener());
		cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelButtonListener());
	
		server = new JTextField(curConfig.server);
		user = new JTextField(curConfig.user);
		pass = new JTextField(curConfig.passw);
		path = new JTextField(curConfig.path);
		port = new JTextField(String.valueOf(curConfig.port));
		
		
		buttons.add(cancel);
		buttons.add(ok);
		
		items.add(new JLabel("Server: "));
		items.add(server);

		items.add(new JLabel("Username: "));
		items.add(user);

		items.add(new JLabel("Password: "));
		items.add(pass);
		
		items.add(new JLabel("Path: "));
		items.add(path);
		
		items.add(new JLabel("Port Nr.: "));
		items.add(port);

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
			curConfig.server = server.getText();
			curConfig.user = user.getText();
			curConfig.passw = pass.getText();
			curConfig.port = Integer.parseInt(port.getText());
			curConfig.path = path.getText();
			setVisible(false);
		}
	}
}
