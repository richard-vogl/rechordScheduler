package at.rich.recordScheduler.scheduler.timeTable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class TimeTableComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3687355223709552088L;
	private static final int NUMLINES = 25;
	private static final int NUMCOLS = 8;
	private int lineheight = 15;
	private int mincolwidth = 20;
	
	private static String[] HEADERS = {"- Time -","Monday","Thuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	private static String[] TIMES = {"06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00","00:00","01:00","02:00","03:00","04:00","05:00"};

	public TimeTableComponent(){	
		this.setMinimumSize(new Dimension(NUMCOLS*mincolwidth,lineheight*NUMLINES));
		this.setPreferredSize(new Dimension(NUMCOLS*mincolwidth*3,lineheight*NUMLINES));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(0.97f, 0.97f, 1f));
		g.fillRect(0, 0, this.getWidth(), lineheight*NUMLINES);
		printRaster(g);
	}
	
	private void printRaster(Graphics g) {
		int width = this.getWidth();
		int textwidth = 0;
		
		 //height / (float)NUMLINES;
		float colwidth = width / (float)NUMCOLS;
		
		g.setColor(Color.WHITE);
		
		for (int i = 0; i<=NUMLINES; i++){
			if (i>0 && i<25){
				textwidth = g.getFontMetrics().stringWidth(TIMES[i-1]);
				g.setColor(Color.BLACK);
				g.drawString(TIMES[i-1], (int) ((colwidth-textwidth)/2), (int) lineheight*(i+1)-2);
				
			}
			if (i<2){
				g.setColor(Color.BLACK);
			}else{
				g.setColor(Color.WHITE);
			}
			g.drawLine(0,(int)(i*lineheight),width, (int)(i*lineheight));

		}

		for (int i =0; i <= NUMCOLS; i++){
			if (i<HEADERS.length){
				textwidth = g.getFontMetrics().stringWidth(HEADERS[i]);
				g.setColor(Color.BLACK);
				if (textwidth>colwidth){
					g.drawString(HEADERS[i].substring(0,2)+"...", (int)((i*colwidth)+(colwidth-g.getFontMetrics().stringWidth(HEADERS[i].substring(0,2)+"..."))/2), (int) lineheight-2);
				}else{
					g.drawString(HEADERS[i], (int)((i*colwidth)+(colwidth-textwidth)/2), (int) lineheight-2);
				}
				g.setColor(Color.WHITE);
			}
			if (i<2){
				g.setColor(Color.BLACK);
			}else{
				g.setColor(Color.WHITE);
			}
			g.drawLine((int)(i*colwidth), 0, (int)(i*colwidth), lineheight*NUMLINES);
		}		
	}

	public static void main(String[] args){
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame gui = new JFrame();
                JScrollPane sp = new JScrollPane(new TimeTableComponent(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                gui.getContentPane().add(sp);
                gui.pack();
                gui.setVisible(true);
                gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
	}
}
