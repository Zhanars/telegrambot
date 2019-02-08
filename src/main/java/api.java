import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class api {

    public static String getVacancies(String special_id){
        String result = "Тут будет вакансии";
        try {
            URL url = new URL("https://telegram.atu.kz/api/search.php?s="+special_id);
            Scanner in = new Scanner((InputStream) url.getContent());
            String content="";
            while (in.hasNext()){
                content += in.nextLine();
            }

            JSONObject object = new JSONObject(content);
            System.out.println(object);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
