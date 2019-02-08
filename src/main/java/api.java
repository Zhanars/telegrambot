import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class api {

    public static String getVacancies(String special_id) throws IOException {
        String result = "По вашей специальности найдено ";
            URL url = new URL("http://telegram.atu.kz/api/search.php?s="+special_id);
            Scanner in = new Scanner((InputStream) url.getContent());
            String content="";
            while (in.hasNext()){
                content += in.nextLine();
            }
            System.out.println(content);
            JSONObject object = new JSONObject(content);
            JSONArray getArray = object.getJSONArray("records");
            result += getArray.length() + " вакансия(и) \n";
            for (int i = 0; i < getArray.length(); i++) {
                System.out.println(i);
                JSONObject obj = getArray.getJSONObject(i);
                result += "--------------------------- \n";
                result += (i + 1) + ") Дата: " + (String) obj.get("created_date") + ",\n ";
                result += "Должность: " + (String) obj.get("name") + ", \n ";
                result += "Компания: " + (String) obj.get("company") + ", \n";
                result += "Сайт: " + (String) obj.get("link") + "\n";
            }
        return result;
    }
}
