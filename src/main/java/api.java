import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class api {

    public static String getVacancies(String special_id) throws IOException {
        String result = "";
            URL url = new URL("https://telegram.atu.kz/api/search.php?s="+special_id);
            Scanner in = new Scanner((InputStream) url.getContent());
            String content="";
            while (in.hasNext()){
                content += in.nextLine();
            }
            JSONObject object = new JSONObject(content);
            JSONArray getArray = object.getJSONArray("records");
            for (int i = 0; i < getArray.length(); i++) {
                JSONObject obj = getArray.getJSONObject(i);
                result += "Дата: " + (String) obj.get("created_date") + ", ";
                result += "вакансия: " + (String) obj.get("name") + ", ";
                result += "компания: " + (String) obj.get("company") + "\n";
            }
        return result;
    }
}
