/*
 * Created on 31.03.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package at.rich.recordScheduler.scheduler;

import java.io.Serializable;

/**
 * @author mani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RecorderConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9202048894580178059L;
	public static final String[] QUALITY_STR = {"lowest", "low", "middle", "high", "highest"};
	public static final int Q_LOWEST = 0;
	public static final int Q_LOW = 1;
	public static final int Q_MIDDLE = 2;
	public static final int Q_HIGH = 3;
	public static final int Q_HIGHEST = 4;
	private static final int STD_QUALITY = Q_MIDDLE;

	public String recpath;
	public boolean vbr;
	public int quality;
	public int bitrate; //One of: 32 40 48 56 64 80 96 112 128 160 192 224 256 320 (MPEG1)
	                    //Or: 8 16 24 32 40 48 56 64 80 96 112 128 144 160 (MPEG2 and MPEG2.5)
	
	public RecorderConfig(boolean vbr, int quality, int bitrate){
		this.vbr = vbr;
		this.quality = (quality>=0 && quality<QUALITY_STR.length)?quality: STD_QUALITY;
		this.bitrate = bitrate;
		this.recpath = "";
	}
	
	public RecorderConfig(boolean vbr, String quality, int bitrate){
		this(vbr, getQualityFromString(quality), bitrate);
	}

	/**
	 * @param quality2
	 * @return
	 */
	public static int getQualityFromString(String quality) {
		int l = QUALITY_STR.length, ri = -1;
		for (int i =0; i<l; i++){
			if (QUALITY_STR[i].equalsIgnoreCase(quality)){
				ri = i;
			}
		}
		return ri;
	}
	
	public static String getQualityString(int i){
		if (i>=0 && i<QUALITY_STR.length){
			return QUALITY_STR[i];
		}
		return null;
	}

}
