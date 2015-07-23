package at.rich.recordScheduler.scheduler;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class RadioProgram{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GregorianCalendar c = new GregorianCalendar();

		int dayoweek = (c.get(Calendar.DAY_OF_WEEK)+5)%7;
		int offs = -dayoweek;
		c.add(GregorianCalendar.DAY_OF_WEEK, offs);

		System.out.println(c.get(GregorianCalendar.YEAR)+"-"+(c.get(GregorianCalendar.MONTH)+1)+"-"+c.get(GregorianCalendar.DATE));
		String date1 = dateString(c);
		c.add(GregorianCalendar.DAY_OF_WEEK, 6);
		String date2 = dateString(c);
		getProgram(date1, date2);
	}

	public static String getProgramEntry(String date, String time){
		URL url = null;
		URLConnection conn = null;
		StringBuffer sb = new StringBuffer(200);
		try {
			url = new URL("http://radio.oeh.jku.at/service/getProgramEntry.php?date="+date+"&time="+time);

			conn = url.openConnection();
			InputStream in = conn.getInputStream();
			byte[] buffer = new byte[200];
			int numRead = in.read(buffer);
			for (int i = 0; i<numRead; i++){
				sb.append((char)buffer[i]);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	public static String[][] getProgram(String date1, String date2){
		URL url = null;
		URLConnection conn = null;
		StringBuffer sb = new StringBuffer(10000);
		
		try {
			url = new URL("http://radio.oeh.jku.at/service/getProgram.php?date1="+date1+"&date2="+date2);

			conn = url.openConnection();
			InputStream in = conn.getInputStream();
			byte[] buffer = new byte[1000];
			int numRead;
			while ((numRead = in.read(buffer))>0){
				for (int i = 0; i<numRead; i++){
					sb.append((char)buffer[i]);
				}
			}	
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String[] lines = sb.toString().split("\\n");
		String[][] program = new String[48][8];
		for (int i = 0; i<lines.length; i++){
			program[i] = lines[i].split("\\t", 8);
		}
		return program;
	}
	
	public static String dateString(GregorianCalendar gc){
		int day = gc.get(GregorianCalendar.DATE);
		int month = gc.get(GregorianCalendar.MONTH);
		int year = gc.get(GregorianCalendar.YEAR);
		
		month++;

		String ms = (month > 9)?Integer.toString(month):"0"+Integer.toString(month);	
		String ds = (day > 9)?Integer.toString(day):"0"+Integer.toString(day);
		return year+"-"+ms+"-"+ds;		
	}

}
