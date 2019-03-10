import firstmenu.Configuration;
import jcifs.smb.NtlmPasswordAuthentication;
import okhttp3.*;

import java.io.*;

public class umkd {
    public static synchronized void getUMKD(Long ChatId, String TeacherId, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        String host = Configuration.getUMKDurl();
        String username = Configuration.getUMKDUsername();
        String pass = Configuration.getUMKDPass();
        String url = host + TeacherId + "/" + fileName;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, pass);
        String HTTP = "https://api.telegram.org/bot"+Configuration.getBottoken()+"/sendDocument?chat_id=" + ChatId;
        String fileSource = url.substring(4);
        OkHttpClient client = new OkHttpClient();
        File sourceFile = new File(fileSource);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("document", sourceFile.getName(), RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), sourceFile))
                .build();

        Request request = new Request.Builder()
                .url(HTTP)
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
