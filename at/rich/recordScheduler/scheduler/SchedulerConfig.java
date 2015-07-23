/*
 * Created on 01.04.2006
 *
 */
package at.rich.recordScheduler.scheduler;

import java.io.Serializable;

/**
 * @author richard vogl
 *
 */
public class SchedulerConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392895764474671710L;
	public int sleeptime;
	public boolean autostart;
	public String scheduleFile;
	
	public SchedulerConfig(int sleeptime, boolean autostart, String scheduleFile){
		this.sleeptime=sleeptime;
		this.autostart=autostart;
		this.scheduleFile=scheduleFile;
	}

}
