package at.rich.recordScheduler.ftp.test;
import cz.dhl.ftp.*;
import java.io.*;

public class FtpStreamTest {
    public static void main(String args[]) {
        /* host = ftp.netscape.com;    path = pub;
         * user = anonymous (default); port = 21 (default); */
        FtpConnect cn = FtpConnect.newConnect("ftp://ftp.netscape.com/");
        /* Guest login ok, send your e-mail as password */
        cn.setPassWord("eternity@matrix.com");
        Ftp cl = new Ftp();
        FtpInputStream is = null;

        try {
            String line;

            /* connect & login to host */
            cl.connect(cn);

            /* source FtpFile remote file */
            FtpFile file = new FtpFile("/Welcome", cl);
            System.out.println("From: " + file.toString());

            /* open ftp input stream */
            is = new FtpInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            /* read ftp input stream */
            while ((line = br.readLine()) != null)
                System.out.println(line);

            /* close reader */
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        } finally { /* close ftp input stream 
                * this must be always run */
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
            /* disconnect from server 
             * this must be always run */
            cl.disconnect();
        }
    }
}