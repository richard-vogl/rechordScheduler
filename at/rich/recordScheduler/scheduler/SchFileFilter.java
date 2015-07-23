package at.rich.recordScheduler.scheduler;
import java.io.File;

import javax.swing.filechooser.FileFilter;
/*
 * Created on 20.03.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author mani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SchFileFilter extends FileFilter {

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File file) {
		String name = file.getName();
		String ext;
		int len = name.length();
		if (len>4){
			ext = name.substring(len-4);
		}else{
			ext="";
		}
		return file.isFile()&&(ext.equalsIgnoreCase(".sch"));
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "sch - schedule";
	}

}
