/*
 * Created on 17.03.2006
 *
 */
package at.rich.recordScheduler.scheduler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import at.rich.recordScheduler.ftp.FtpWriter;
import at.rich.recordScheduler.scheduler.dialogs.FtpConfigDialog;
import at.rich.recordScheduler.scheduler.dialogs.RecorderConfigDialog;
import at.rich.recordScheduler.scheduler.dialogs.SchedulerConfigDialog;

/**
 * @author Richard Vogl 
 *
 * Scheduler GUI
 */
public class SchedulerGUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1013645866109023101L;

	private static final String TITLE_S = "Scheduler";
	
	private static final String CONFIG_FILENAME = "scheduler.config";
	
	private Container contentPane;
	
	private JMenuBar menuBar;
	
	private JTable timeTable;

	private JFileChooser chooser;

	private TimeTableModel timeTableModel;
	
	private JMenu m_file;

	private JMenuItem m_f_exit;

	private JMenuItem m_s_start;
	private JMenuItem m_s_stop;
	private JMenu m_help;
	private JMenu m_scheduler;

	private JMenu m_configure;
	private JMenuItem m_o_ftp;
	private JMenuItem m_o_recorder;
	private JMenuItem m_o_scheduler;
	
	private JToolBar toolbar;
	
	private SchedulerThread scheduler;
	
	private JButton onair;

	protected static final Color COLOR_CUR_WEEK = Color.getHSBColor(0.3f, 0.25f, 1f);

	protected static final Color COLOR_NEXT_WEEK = Color.getHSBColor(0.7f, 0.25f, 1f);
	protected static final Color COLOR_PREV_WEEK = Color.getHSBColor(0.0f, 0.25f, 1f);

	private FtpConfig ftpConfig;
	private RecorderConfig recorderConfig;
	private SchedulerConfig schedulerConfig;
	
	private FtpConfigDialog ftpConfigDialog;
	private RecorderConfigDialog recorderConfigDialog;
	private SchedulerConfigDialog schedulerConfigDialog;

	private JButton playButton;
	private JButton stopButton;
	
	private OutConsole console;

	private JPanel weekSelector;

	private JCheckBox manualStartCheck;

	private JCheckBox manualStopCheck;

	private JButton manualStartButton;

	private JButton manualStopButton;

	
	/**
	 * @throws java.awt.HeadlessException
	 */
	public SchedulerGUI() throws HeadlessException {
		this.addWindowListener(new MainWindowListener());
		loadConfig();
		initComponents();
		startUpOptions();
	}

	/**
	 * 
	 */
	private void startUpOptions() {
		if (schedulerConfig.autostart){
			doStartScheduler();
		}
		if (schedulerConfig.scheduleFile != null &&
			!schedulerConfig.scheduleFile.equalsIgnoreCase("")  &&
			schedulerConfig.scheduleFile.length()>0){
//			System.out.println("filename:"+schedulerConfig.scheduleFile+":aus");
//			File loadingf = new File(schedulerConfig.scheduleFile);
//			openSchedulerFile(loadingf);
		}
//		setChanged(false);
	}

	/**
	 * 
	 */
	private void initComponents() {
		setTitle(TITLE_S);
		chooser = new JFileChooser();
		SchFileFilter filter = new SchFileFilter();
	    chooser.setFileFilter(filter);
		
		contentPane = this.getContentPane();
		
		//////////////////
		// init menu
		//////////////////
		
		menuBar = new JMenuBar();
		
		m_file = new JMenu("File");
		m_file.setMnemonic(KeyEvent.VK_F);
		m_f_exit = new JMenuItem("Exit");
		m_f_exit.setMnemonic(KeyEvent.VK_E);
		m_f_exit.addActionListener(new FileExitActionListener());
		m_file.add(m_f_exit);
		
		m_scheduler = new JMenu("Scheduler");
		m_scheduler.setMnemonic(KeyEvent.VK_S);
		m_s_start = new JMenuItem("Start",
				new ImageIcon("res/play.gif"));
		m_s_start.setMnemonic(KeyEvent.VK_A);
		m_scheduler.add(m_s_start);
		m_s_start.addActionListener(new StartSchedulerActionListener());
		m_s_stop = new JMenuItem("Stop",
				new ImageIcon("res/stop.gif"));
		m_s_stop.setMnemonic(KeyEvent.VK_O);
		m_s_stop.addActionListener(new StopSchedulerActionListener());
		m_s_stop.setEnabled(false);
		m_scheduler.add(m_s_stop);
		
		m_configure = new JMenu("Configure");
		m_configure.setMnemonic(KeyEvent.VK_C);
		m_o_ftp = new JMenuItem("Ftp...");
		m_o_ftp.setMnemonic(KeyEvent.VK_F);
		m_o_ftp.addActionListener(new FtpConfigActionListener());
		m_configure.add(m_o_ftp);
		m_o_recorder = new JMenuItem("Recorder...");
		m_o_recorder.setMnemonic(KeyEvent.VK_R);
		m_o_recorder.addActionListener(new RecorderConfigActionListener());
		m_configure.add(m_o_recorder);
		m_o_scheduler = new JMenuItem("Scheduler..");
		m_o_scheduler.setMnemonic(KeyEvent.VK_S);
		m_o_scheduler.addActionListener(new SchedulerConfigActionListener());
		m_configure.add(m_o_scheduler);
		
		m_help = new JMenu("Help");
		m_help.setMnemonic(KeyEvent.VK_H);
		
		menuBar.add(m_file);
		menuBar.add(m_scheduler);
		menuBar.add(m_configure);
		menuBar.add(m_help);
		
		//////////////
		// init timetable
		//////////////
		
		timeTable = new JTable();
		timeTableModel = new TimeTableModel(this);
		timeTable.setModel(timeTableModel);
		timeTable.setDefaultRenderer(String.class, new TimeTableCellRenderer());
		timeTable.setPreferredScrollableViewportSize(new Dimension(600, 384));
		timeTable.setBackground(COLOR_CUR_WEEK);
		JScrollPane scrollPane = new JScrollPane(timeTable);
		
		weekSelector = new JPanel(new GridLayout(1,3));
		JButton prevWeek = new JButton("<<--");
		prevWeek.addActionListener(new WeekChangeListener(-1));
		JButton nextWeek = new JButton("-->>");
		nextWeek.addActionListener(new WeekChangeListener(1));
		JButton thisWeek = new JButton("Current Week");
		thisWeek.addActionListener(new WeekChangeListener(0));
		weekSelector.add(prevWeek);
		weekSelector.add(thisWeek);
		weekSelector.add(nextWeek);
		
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		JPanel mPane = new JPanel(new GridLayout(2,1));
		
		
		this.setJMenuBar(menuBar);	
		
		//////////////////
		// init toolbar
		//////////////////
		
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
/*		playButton = new JButton(new ImageIcon("res/play.gif"));
		playButton.addActionListener(new StartSchedulerActionListener());
		toolbar.add(playButton);
		
		stopButton = new JButton(new ImageIcon("res/stop.gif"));
		stopButton.addActionListener(new StopSchedulerActionListener());
		stopButton.setEnabled(false);
		toolbar.add(stopButton);
		
//		onair = new JButton(new ImageIcon("res/rec.gif"));
//		stopButton.addActionListener(new StopSchedulerActionListener());
//		onair.setEnabled(false);
//		toolbar.add(onair); */
		
		manualStartCheck = new JCheckBox();
		manualStartCheck.addActionListener(new EnableManualStartActionListener());
		manualStartButton = new JButton("Start");
		manualStartButton.addActionListener(new ManualStartActionListener());
		manualStopCheck = new JCheckBox();	
		manualStopCheck.addActionListener(new EnableManualStopActionListener());
		manualStopButton = new JButton("Stop");
		manualStopButton.addActionListener(new ManualStopActionListener());
		
		
		manualStartCheck.setEnabled(false);
		manualStartButton.setEnabled(false);
		manualStopCheck.setEnabled(false);
		manualStopButton.setEnabled(false);
		
		toolbar.add(new JLabel("Manual REC Controls:"));
		toolbar.add( manualStartCheck);
		toolbar.add(manualStartButton);
		toolbar.add( manualStopCheck);
		toolbar.add(manualStopButton);
	
		mPane.add(toolbar);
		mPane.add(weekSelector);
		contentPane.add(mPane, BorderLayout.NORTH);
		
		console = new OutConsole(5,20);
		ftpConfig.console=console;
		FtpWriter.setConsole(console);
		FtpWriter.setConfig(ftpConfig);
		contentPane.add(new JScrollPane(console,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.SOUTH);
		
		pack();
		
		ftpConfigDialog = new FtpConfigDialog(this,this,ftpConfig);
		recorderConfigDialog = new RecorderConfigDialog(this,this,recorderConfig);
		schedulerConfigDialog = new SchedulerConfigDialog(this,this, schedulerConfig);
		
		Dimension screenSize =
	          Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width/2-this.getWidth()/2,
				screenSize.height/2-this.getHeight()/2);
		ftpConfigDialog.setLocation(screenSize.width/2-ftpConfigDialog.getWidth()/2,
				screenSize.height/2-ftpConfigDialog.getHeight()/2);
		recorderConfigDialog.setLocation(screenSize.width/2-recorderConfigDialog.getWidth()/2,
				screenSize.height/2-recorderConfigDialog.getHeight()/2);
		schedulerConfigDialog.setLocation(screenSize.width/2-schedulerConfigDialog.getWidth()/2,
				screenSize.height/2-schedulerConfigDialog.getHeight()/2);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void loadConfig(){
		File configFile = new File(CONFIG_FILENAME);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(configFile));
			ftpConfig = (FtpConfig) ois.readObject();
			recorderConfig = (RecorderConfig) ois.readObject();
			schedulerConfig =(SchedulerConfig) ois.readObject();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Unable to open config file!", "Error", JOptionPane.OK_OPTION);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{

			if (ftpConfig ==null){
				ftpConfig = FtpWriter.getConfig();
			}
			if (recorderConfig==null){
				recorderConfig = new RecorderConfig(true, RecorderConfig.Q_HIGHEST, 192);
			}
			if (schedulerConfig==null){
				schedulerConfig = new SchedulerConfig(15000, false, "");
			}
			try {
				if (ois!=null)
				ois.close();
			} catch (IOException e) {}
		}
	}
	
	
	//TODO: implement config save as XML
	private void saveConfig(){
		File configFile = new File(CONFIG_FILENAME);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(configFile));
			oos.writeObject(ftpConfig);
			oos.writeObject(recorderConfig);
			oos.writeObject(schedulerConfig);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doFileExit() {

		saveConfig();
		System.exit(0);
	}
	
	private void doStartScheduler(){
		m_s_start.setEnabled(false);
	//	playButton.setEnabled(false);
		m_s_stop.setEnabled(true);
	//	stopButton.setEnabled(true);
		
		manualStartCheck.setEnabled(true);
		manualStopCheck.setEnabled(true);
		manualStartCheck.setSelected(false);
		manualStopCheck.setSelected(false);

	//	timeTable.setEnabled(false);
		scheduler = new SchedulerThread(timeTableModel, schedulerConfig, recorderConfig, console);
		scheduler.start();		
	}
	
	private void doStopScheduler(){
		m_s_stop.setEnabled(false);
	//	stopButton.setEnabled(false);
		m_s_start.setEnabled(true);
	//	playButton.setEnabled(true);
		scheduler.stopScheduling();
	//	timeTable.setEnabled(true);
		
		manualStartCheck.setEnabled(false);
		manualStartButton.setEnabled(false);
		manualStopCheck.setEnabled(false);
		manualStopButton.setEnabled(false);
		manualStartCheck.setSelected(false);
		manualStopCheck.setSelected(false);
		
	}
	
	private void doManualStop() {
		manualStopCheck.setEnabled(true);
		manualStopCheck.setSelected(false);
		manualStopButton.setEnabled(false);
		scheduler.setHoldStop(false);
		scheduler.interrupt();
	}
	

	private void doManualStart() {
		manualStartCheck.setEnabled(true);
		manualStartCheck.setSelected(false);
		manualStartButton.setEnabled(false);
		scheduler.setHoldStart(false);
		scheduler.interrupt();
	}
	private void doEnableManualStop() {
		manualStopCheck.setEnabled(false);
		manualStopButton.setEnabled(true);
		scheduler.setHoldStop(true);
	}
	

	private void doEnableManualStart() {
		manualStartCheck.setEnabled(false);
		manualStartButton.setEnabled(true);
		scheduler.setHoldStart(true);
		
	}
	
	private void doChangeWeek(int inc) {
		switch (inc){
		case -1:
			((TimeTableModel)timeTable.getModel()).decWeek();
			break;
		case 1:
			((TimeTableModel)timeTable.getModel()).incWeek();
			
			break;
		case 0: 
			((TimeTableModel)timeTable.getModel()).resetWeek();
			timeTable.setBackground(COLOR_CUR_WEEK);
			break;
			
		}
		if (((TimeTableModel)timeTable.getModel()).getWeekOffset()>0){

			timeTable.setBackground(COLOR_NEXT_WEEK);
		}else if (((TimeTableModel)timeTable.getModel()).getWeekOffset()==0){
			timeTable.setBackground(COLOR_CUR_WEEK);
		}else{
			timeTable.setBackground(COLOR_PREV_WEEK);
			
		}

	}
	
	private void doShowFtpConfig() {
		ftpConfigDialog.setVisible(true);	
		FtpWriter.setConfig(ftpConfig);
	}
	
	private void doShowRecorderConfig() {
		recorderConfigDialog.setVisible(true);		
	}
	
	private void doShowSchedulerConfig() {
		schedulerConfigDialog.setVisible(true);
		
	}
	
	private final class MainWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
			doFileExit();
		}
	}

	private final class WeekChangeListener implements ActionListener {
		private int inc;
		
		public WeekChangeListener(int inc){
			this.inc = inc;
		}

		public void actionPerformed(ActionEvent e) {
			doChangeWeek(this.inc);
		}
	}

	private final class SchedulerConfigActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			doShowSchedulerConfig();				
		}
	}

	private final class RecorderConfigActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			doShowRecorderConfig();				
		}
	}

	private final class FtpConfigActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			doShowFtpConfig();				
		}
	}

	private final class StopSchedulerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doStopScheduler();
		}
	}

	private final class StartSchedulerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doStartScheduler();
		}
	}
	
	private final class EnableManualStopActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doEnableManualStop();
		}
	}

	private final class EnableManualStartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doEnableManualStart();
		}

	}
	private final class ManualStopActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doManualStop();
		}
	}

	private final class ManualStartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			doManualStart();
		}

	}

	private final class FileExitActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			doFileExit();
		}
	}
}

