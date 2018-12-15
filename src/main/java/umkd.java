import jcifs.smb.NtlmPasswordAuthentication;
import okhttp3.*;

import java.io.*;

public class umkd {
    public static void getUMKD(Long ChatId, String TeacherId, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        String url = "smb://185.97.115.134/umkd/" + TeacherId + "/" + fileName;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "Администратор", "Univer2018#");
        String HTTP = "https://api.telegram.org/bot745779362:AAEFky83gaEOP4aB8RjUHq4_BcW7hCYSB68/sendDocument?chat_id=" + ChatId;
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
