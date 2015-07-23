/*
 * Created on 26.03.2006
 *
 */
package at.rich.recordScheduler.ftp;

import java.io.IOException;

import at.rich.recordScheduler.scheduler.FtpConfig;

import cz.dhl.ftp.Ftp;
import cz.dhl.ftp.FtpFile;
import cz.dhl.io.CoFile;
import cz.dhl.io.CoLoad;
import cz.dhl.io.LocalFile;
import cz.dhl.ui.CoConsole;

/**
 * @author richard vogl
 *
 */
public class FtpWriter {
	static String SERVER = "radio.oeh.jku.at";
	static String USR = "radio";
	static String PSW = "krampusradio";
	static String PATH = "sendungen/";
	static CoConsole CONSOLE = null;
	static int PORT = Ftp.PORT;
	
	private static Ftp ftp;
	
	static{
		ftp=new Ftp();
		ftp.getContext().setConsole(CONSOLE);
	}

	/**
	 * 
	 */
	private FtpWriter() {
	}
	
	public static void setConfig(FtpConfig config){
		SERVER = config.server;
		USR = config.user;
		PSW = config.passw;
		PATH = config.path;
		PORT = config.port;
		setConsole(config.console);
	}
	
	public static FtpConfig getConfig(){
		return new FtpConfig(SERVER, USR, PSW, PATH, PORT, CONSOLE);
	}
	
	public static void writeFile(String name, String localpath){
		try {
			ftp.connect(SERVER, PORT);
			ftp.login(USR, PSW);
			
			CoFile target = new FtpFile(FtpWriter.PATH+name,ftp); 
			CoFile source = new LocalFile(localpath+name);
			CoLoad.copy(target, source);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			ftp.disconnect();
		}
	}
	
	/**
	 * @return Returns the port.
	 */
	public static int getPORT() {
		return PORT;
	}
	/**
	 * @param port The pORT to set.
	 */
	public static void setPORT(int port) {
		PORT = port;
	}
	/**
	 * @return Returns the pSW.
	 */
	public static String getPSW() {
		return PSW;
	}
	/**
	 * @param psw The pSW to set.
	 */
	public static void setPSW(String psw) {
		PSW = psw;
	}
	/**
	 * @return Returns the sITE.
	 */
	public static String getSITE() {
		return SERVER;
	}
	/**
	 * @param site The sITE to set.
	 */
	public static void setSITE(String site) {
		SERVER = site;
	}
	/**
	 * @return Returns the uSR.
	 */
	public static String getUSR() {
		return USR;
	}
	/**
	 * @param usr The uSR to set.
	 */
	public static void setUSR(String usr) {
		USR = usr;
	}
	public static void main(String[] args){
		FtpWriter.writeFile("out.mp3","");
	}

	/**
	 * @return
	 */
	public static Ftp getFtp() {
		return ftp;
	}

	/**
	 * @param console2
	 */
	public static void setConsole(CoConsole console) {
		CONSOLE = console;	
		ftp.getContext().setConsole(CONSOLE);
	}
	
	public static CoConsole getConsole(){
		return CONSOLE;		
	}
	
}
