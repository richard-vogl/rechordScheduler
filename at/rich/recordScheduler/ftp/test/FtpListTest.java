package at.rich.recordScheduler.ftp.test;
import cz.dhl.io.*;
import cz.dhl.ftp.*;
import java.io.IOException;

public class FtpListTest {
    public static void main(String args[]) {
        /* host = ftp.netscape.com;    path = pub;
         * user = anonymous (default); port = 21 (default); */
        FtpConnect cn = FtpConnect.newConnect("ftp://ftp.netscape.com/pub");
        /* Guest login ok, send your e-mail as password */
        cn.setPassWord("eternity@matrix.com");
        Ftp cl = new Ftp();

        try { /* connect & login to host */
            cl.connect(cn);

            /* get current directory */
            CoFile dir = new FtpFile(cl.pwd(), cl);

            /* list & print current directory */
            CoFile fls[] = dir.listCoFiles();
            if (fls != null)
                for (int n = 0; n < fls.length; n++)
                    System.out.println(
                        fls[n].getName() + (fls[n].isDirectory() ? "/" : ""));
        } catch (IOException e) {
            System.out.println(e);
        } finally { /* disconnect from server 
        	  * this must be always run */
            cl.disconnect();
        }
    }
}