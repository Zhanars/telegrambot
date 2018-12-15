import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import java.io.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

public class umkd {
    public static void getUMKD(Long ChatId, String TeacherId, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        String url = "smb://185.97.115.134/umkd/" + TeacherId + "/"+fileName;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "Администратор", "Univer2018#");
        SmbFile dir = null;
        try {
            dir = new SmbFile(url, auth);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
                String DocName = fileName;
                File child = new File(DocName);
                dir.connect();
                in = new BufferedInputStream(new SmbFileInputStream(dir));
                out = new BufferedOutputStream(new FileOutputStream(child));
                byte[] buffer = new byte[4096];
                int len = 0; //Read length
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bot.sendFile(ChatId, DocName);
        } catch (SmbException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
