import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import java.io.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class umkd {
    public static void getUMKD(Long ChatId, String TeacherId, String SubjectId) {
        InputStream in = null;
        OutputStream out = null;
        System.out.println(TeacherId);
        String url = "smb://185.97.115.134/umkd/" + TeacherId + "/";
        try {
            String[][] Record = Univer.getFiles(TeacherId,SubjectId);
            int rowCount = Record.length;
            String[] files = new String[rowCount];

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "Администратор", "Univer2018#");
        SmbFile dir = null;
        try {
            dir = new SmbFile(url, auth);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            for (SmbFile f : dir.listFiles()) {
                String DocName = f.getName();
                System.out.println(f.getName());
                File child = new File(DocName);
                f.connect();
                in = new BufferedInputStream(new SmbFileInputStream(f));
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
            }
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
