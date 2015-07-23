/*
 * Created on 28.03.2006
 *

 */
package at.rich.recordScheduler.scheduler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import at.rich.recordScheduler.ftp.FtpWriter;
import at.rich.recordScheduler.recorder.LineAudioRecorder;


/**
 * @author richard vogl
 *
 */

public class SchedulerThread extends Thread {
	private TimeTableModel timeTableModel;
	
	private boolean running;
	
	private Calendar cal;
	
	private LineAudioRecorder recorder;
	private String curRecBroadcastName;
	
	private int sleeptime;
	
	private boolean autostart;

	private String scheduleFile;

	private RecorderConfig recorderConfig;
	
	private OutConsole console;

	private String curRecFileName;

	private String dateString;

	private boolean holdStart;
	private boolean holdStop;

	public SchedulerThread(TimeTableModel ttm, SchedulerConfig sc, RecorderConfig rc, OutConsole console){
		this.timeTableModel = ttm;	
		this.recorderConfig=rc;
		running = true;
		cal = new GregorianCalendar(Locale.getDefault());
		this.sleeptime=sc.sleeptime;
		this.autostart=sc.autostart;
		this.scheduleFile=sc.scheduleFile;
		this.console = console;
		this.holdStart = false;
		this.holdStop = false;
	}
	
	public void setHoldStart(boolean hold){
		holdStart = hold;		
	}
	
	public void setHoldStop(boolean hold){
		holdStop = hold;
	}
	
	public void setConfig(SchedulerConfig sc){
		this.sleeptime=sc.sleeptime;
		this.autostart=sc.autostart;
	}
	
	public SchedulerConfig getConfig(){
		return new SchedulerConfig(sleeptime, autostart, scheduleFile);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		int day,hour, min;
		int row, col;		
		boolean half;
		while (running){
			day = (cal.get(Calendar.DAY_OF_WEEK)+5)%7+1;  //put sunday as last day of week, not as first...
			hour = cal.get(Calendar.HOUR_OF_DAY);
			min = cal.get(Calendar.MINUTE);
			half = (min>=30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setCalendar(cal);
			dateString = sdf.format(new Date());
			row = hour*2;
			if (half) row++;
			col = day;
			//System.out.println("row ,col:"+row+","+col);
			if (row>=0 && row<48){ 
				//System.out.println("start");
				checkRecState(row, col);
			}
			try {				
				//console.print("--- Scheduler ---\n");
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				console.printNoBr("|");
			}
		}
		cleanUp();
	}
	
	/**
	 * 
	 */
	private void cleanUp() {
		if (this.recorder != null){
			if (this.recorder.isRecording()){
				recorder.stopRecording();
			}
		}		
	}

	/**
	 * @param row
	 * @param col
	 * @param retries
	 * @param curfilename
	 * @param dateString
	 * @return
	 */
	private void checkRecState(int row, int col) {
		timeTableModel.resetWeek();
		this.timeTableModel.setCurrent(row, col);
		String	curTimetableBroadcastName = (String)this.timeTableModel.getValueAt(row, col);		
		
		if (curTimetableBroadcastName==null) {
			curTimetableBroadcastName = "";
		}
		if (!curTimetableBroadcastName.equalsIgnoreCase(curRecBroadcastName)){
			// not rec the cur broadcast
			if (!holdStop){
				if (recorder!=null && recorder.isRecording()){
					// stop an ftp
					stopRecAndFtpFile();
				}
				if (curTimetableBroadcastName != null && 
					!curTimetableBroadcastName.equalsIgnoreCase("") &&
					!holdStart){
					// start recording
					startRec(curTimetableBroadcastName);
				}
			}
		}else{
			//everthing ok, rec the scheduled...
			console.printNoBr(".");
		}
	}

	/**
	 * @param curfilename
	 * @param sendungsName
	 */
	@SuppressWarnings("unused")
	private void recoverFailure(String curfilename, String sendungsName) {
		recorder.startRecording();
		if (!recorder.isRecording()){
			console.print("failure, restarting recorder for: "+sendungsName);
			recorder = new LineAudioRecorder(new File(curfilename), recorderConfig);
			recorder.startRecording();
		}
	}

	/**
	 * @param curfilename
	 * @param sendungsName
	 */
	private void startRec(String broadcastName) {
		console.print("start recording: "+broadcastName+"\n...");
		curRecBroadcastName = broadcastName;
		curRecFileName = curRecBroadcastName.replaceAll("[^A-Za-z0-9. ]", "")+"-"+dateString+".mp3";
		recorder = new LineAudioRecorder(new File(recorderConfig.recpath+curRecFileName), recorderConfig);
		recorder.startRecording();
	}

	/**
	 */
	private void stopRecAndFtpFile() {
		console.print("stoping recording for: "+curRecBroadcastName);
		recorder.stopRecording();
		
		console.print("putting on ftp-server: "+curRecFileName);
		FtpWriter.writeFile(curRecFileName, recorderConfig.recpath);
	}

	public void stopScheduling(){
		this.timeTableModel.setCurrent(-1, -1);
		running=false;
	}
}
