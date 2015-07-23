package at.rich.recordScheduler.ftp.test;
import cz.dhl.io.*;
import cz.dhl.ftp.*;
import java.io.IOException;

public class FtpLoadTest {
    public static void main(String args[]) {
        /* host = ftp.netscape.com;    path = pub;
         * user = anonymous (default); port = 21 (default); */
        FtpConnect cn = FtpConnect.newConnect("ftp://ftp.netscape.com/");
        /* Guest login ok, send your e-mail as password */
        cn.setPassWord("eternity@matrix.com");
        Ftp cl = new Ftp();

        try { /* connect & login to host */
            cl.connect(cn);

            /* source FtpFile remote file */
            CoFile file = new FtpFile("/Welcome", cl);
            System.out.println("From: " + file.toString());

            /* destination LocalFile home dir */
            CoFile to =
                new LocalFile(System.getProperty("user.dir"), "Welcome");
            System.out.println("To:   " + to.toString());

            /* download /Welcome file to home dir*/
            System.out.println("Load: " + CoLoad.copy(to, file));
        } catch (IOException e) {
            System.out.println(e);
        } finally { /* disconnect from server 
               	  * this must be always run */
            cl.disconnect();
        }
    }
}