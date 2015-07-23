/*
 * Created on 17.03.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package at.rich.recordScheduler.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * @author Richard Vogl
 *
 * The Data Model for the Scheduler Timetable
 */
public class TimeTableModel implements TableModel {

	private static final int HOURS = 48;
	private static final int DAYS = 7;
	private static final String[] DAY = {" - Time -", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private static final long CACHETIME = 5*60*1000;
	
	private SchedulerGUI target;
	
	private List<TableModelListener> m_listeners;
	
	private String[][] cache; // moL, tuL, weL, thL, frL, saL, suL;
	private long cachestamp;
	
	private int m_weekOff;
	
	private int m_currow = -1;
	private int m_curcol;
	private String start_date;
	private String end_date;
	
	public TimeTableModel(SchedulerGUI target){
		m_listeners = new ArrayList<TableModelListener>();
		cache = new String[48][8];
		this.target = target;
		resetWeek();				
	}
	
	public void resetWeek(){
		m_weekOff = 0;
		refreshCache();
		fireTableAltered(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
	}
	
	public void incWeek(){
		m_weekOff++;
		refreshCache();
		fireTableAltered(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
		
	}
	public void decWeek(){
		m_weekOff--;
		refreshCache();
		fireTableAltered(new TableModelEvent(this, TableModelEvent.HEADER_ROW));		
	}
	
	public int getWeekOffset(){
		return m_weekOff;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return HOURS;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return DAYS+1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		if (col==1){
			return (DAY[col]).substring(0,3)+" "+start_date;
		}
		if (col==7){
			return (DAY[col]).substring(0,3)+" "+end_date;
		}
		return DAY[col];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void refreshCache(){
		GregorianCalendar c = new GregorianCalendar();
		c.add(Calendar.WEEK_OF_YEAR, m_weekOff);
		int dayoweek = (c.get(Calendar.DAY_OF_WEEK)+5)%7;
		int offs = -dayoweek;
		c.add(Calendar.DAY_OF_MONTH, offs);

		start_date = RadioProgram.dateString(c);
		c.add(GregorianCalendar.DAY_OF_WEEK, 6);
		end_date = RadioProgram.dateString(c);
		
		cache = RadioProgram.getProgram(start_date, end_date);
			
		this.cachestamp = System.currentTimeMillis();
		
		this.target.repaint();
		//fireTableAltered(new TableModelEvent(this, TableModelEvent.ALL_COLUMNS));

	}
	

	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */	
	public Object getValueAt(int row, int col) {
		long millis = System.currentTimeMillis()-cachestamp ;
		
		if (millis>CACHETIME){
			refreshCache();
		}
		int h = row/2;
		int m = (row%2)*30;
		
		String ms = (m<10)?"0"+m:""+m,
			   time = (h<10)?"0"+h+":"+ms:""+h+":"+ms;
		
		return (col==0)?time:this.cache[row][col];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object val, int row, int col) {
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
	 */
	public void addTableModelListener(TableModelListener listener) {
		m_listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
	 */
	public void removeTableModelListener(TableModelListener listener) {
		m_listeners.remove(listener);
	}
	
	private synchronized void fireTableAltered(TableModelEvent event){
		 Iterator iter = m_listeners.iterator();
		
		TableModelListener tml;
		while (iter.hasNext()){
			tml = (TableModelListener) iter.next();
			tml.tableChanged(event);
		}	
	}
	
	public void setCurrent(int row, int col){
		this.m_currow = row;
		this.m_curcol = col;
		this.target.repaint();
//		fireTableAltered(new TableModelEvent(this, TableModelEvent.ALL_COLUMNS));

	}
	public int getCurrentCol(){
		return this.m_currow;
	}
	public int getCurrentRow(){
		return this.m_curcol;
	}
	public boolean isCurrent(int row, int col) {

		return row==m_currow && col == m_curcol;
	}

}
