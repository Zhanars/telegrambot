import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class api {

    public static String[] getVacancies(String special_id) throws IOException {

            URL url = new URL("http://telegram.atu.kz/api/search.php?s="+special_id);
            Scanner in = new Scanner((InputStream) url.getContent());
            String content="";
            while (in.hasNext()){
                content += in.nextLine();
            }
            JSONObject object = new JSONObject(content);
            JSONArray getArray = object.getJSONArray("records");
        String[] result = new String[getArray.length()+1];
            result[0] = "По вашей специальности найдено " + getArray.length() + " вакансия(и) \n";
            for (int i = 1; i < getArray.length()+1; i++) {
                JSONObject obj = getArray.getJSONObject(i-1);
                result[i] = "--------------------------- \n";
                result[i] += i + ") Дата: " + (String) obj.get("created_date") + ",\n ";
                result[i] += "Должность: " + (String) obj.get("name") + ", \n ";
                result[i] += "Компания: " + (String) obj.get("company") + ", \n";
                result[i] += "Сайт: " + (String) obj.get("link") + "\n";
            }
            return result;
    }
    public static String[][] getDocs(){
        String[][] result = new String[1][1];
        URL url = null;
        try {
            url = new URL("http://telegram.atu.kz/api/readDoc.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner((InputStream) url.getContent());
        String content="";
        while (in.hasNext()){
            content += in.nextLine();
        }
        JSONObject object = new JSONObject(content);
        JSONArray getArray = object.getJSONArray("records");
        return result;
    }
}
