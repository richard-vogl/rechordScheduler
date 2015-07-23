/*
 * Created on 31.03.2006
 *
 */
package at.rich.recordScheduler.scheduler;

import java.io.Serializable;

import cz.dhl.ftp.Ftp;
import cz.dhl.ui.CoConsole;

/**
 * @author richard vogl
 *
 */
public class FtpConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -455128037372544913L;
	public String server;
	public String user;
	public String passw;
	public String path;
	public int port;
	transient public CoConsole console=null;
	
	public FtpConfig(String server,
			String user, String passw, String path, int port, CoConsole console){
		this.server = server;
		this.user = user;
		this.passw = passw;
		this.port = port;
		this.path = path;
	}
	
	public FtpConfig(String server,
			String user, String passw, String path){
		this(server,user,passw,path,Ftp.PORT, null);
	}
}
