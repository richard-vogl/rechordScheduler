package at.rich.recordScheduler.ftp.test;
import cz.dhl.io.*;
import cz.dhl.ftp.*;
import cz.dhl.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class JFtpSwingTest extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1375445526097125967L;
	/* ftp client */
    Ftp cl = new Ftp();
    /* dir swing component */
    private JCoDirComboBox dirChoice = new JCoDirComboBox();
    /* file swing component */
    private JCoFileTable fileList = new JCoFileTable();

    JFtpSwingTest() {
        /* Structure */
        JPanel toppanel = new JPanel(new BorderLayout());
        getContentPane().add("North", toppanel);
        toppanel.add("West", new JLabel("Look in: "));
        toppanel.add("Center", dirChoice);

        JScrollPane scroll = new JScrollPane(fileList);
        scroll.setPreferredSize(new Dimension(300, 300));
        getContentPane().add("Center", scroll);

        /* dir change listener */
        dirChoice.setDirActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update(dirChoice.getSelectedDir());
            }
        });

        /* file selection listener */
        fileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                CoFile dir = fileList.getSelectedFile();
                if (e.getClickCount() > 1 && dir != null)
                    update(dir);
            }
        });

        /* frame exit listener */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { /* disconnect from server 
            	    * this must be always run */
                cl.disconnect();
                /* close window frame */
                JFtpSwingTest.this.dispose();
                System.exit(0);
            }
        });
    }

    /* update directory contents 
     * thread safe implementation */
    void update(CoFile dir) {
        dirChoice.setEnabled(false);
        fileList.setEnabled(false);
        (new Thread(new Update(dir))).start();
    }

    class Update implements Runnable {
        CoFile dir;
        CoFile files[] = null;
        Update(CoFile dir) {
            this.dir = dir;
        }
        public void run() {
            if (dir != null) {
                if (dir.isDirectory() || dir.isLink())
                    files = dir.listCoFiles();
                if (files == null) {
                    dir = dirChoice.getDir();
                    files = fileList.getFiles();
                }
            }
            SwingUtilities.invokeLater(new UpdateUI(dir, files));
        }
    }

    class UpdateUI implements Runnable {
        CoFile dir;
        CoFile files[];
        UpdateUI(CoFile dir, CoFile files[]) {
            this.dir = dir;
            this.files = files;
        }
        public void run() {
            dirChoice.setDir(dir);
            fileList.setFiles(files);
        }
    }

    public static void main(String args[]) {
        final JFtpSwingTest frame = new JFtpSwingTest();
        frame.setTitle("jFTP");
        frame.pack();
        frame.setVisible(true);

        /* host = ftp.netscape.com;    path = pub;
         * user = anonymous (default); port = 21 (default); */
        FtpConnect cn = FtpConnect.newConnect("ftp://ftp.netscape.com/pub");
        /* Guest login ok, send your e-mail as password */
        cn.setPassWord("eternity@matrix.com");

        try { /* connect & login to host */
            frame.cl.connect(cn);

            /* get current directory */
            frame.update(new FtpFile(frame.cl.pwd(), frame.cl));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}