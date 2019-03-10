import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class api {

    public static synchronized String[] getVacancies(String special_id) throws IOException {

        URL url = new URL("http://telegram.atu.kz/api/search.php?s=" + special_id);
        Scanner in = new Scanner((InputStream) url.getContent());
        String content = "";
        while (in.hasNext()) {
            content += in.nextLine();
        }
        JSONObject object = new JSONObject(content);
        JSONArray getArray = object.getJSONArray("records");
        String[] result = new String[getArray.length() + 1];
        result[0] = "По вашей специальности найдено " + getArray.length() + " вакансия(и) \n";
        for (int i = 1; i < getArray.length() + 1; i++) {
            JSONObject obj = getArray.getJSONObject(i - 1);
            result[i] = "--------------------------- \n";
            result[i] += i + ") Дата: " + (String) obj.get("created_date") + ",\n ";
            result[i] += "Должность: " + (String) obj.get("name") + ", \n ";
            result[i] += "Компания: " + (String) obj.get("company") + ", \n";
            result[i] += "Сайт: " + (String) obj.get("link") + "\n";
        }
        return result;
    }
    public static synchronized String[][] getDocs(){
        URL url = null;
        try {
            url = new URL("http://telegram.atu.kz/api/readDoc.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Scanner in = null;
        try {
            in = new Scanner((InputStream) url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content="";
        while (in.hasNext()){
            content += in.nextLine();
        }
        JSONObject object = new JSONObject(content);
        JSONArray getArray = object.getJSONArray("records");
        String[][] result = new String[getArray.length()][4];
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            result[i][0] = (String) obj.get("id");
            result[i][1] = (String) obj.get("name");
            result[i][2] = (String) obj.get("description");
            result[i][3] = (String) obj.get("link");
        }
        return result;
    }
    public static synchronized String forAbiturient(){
        String result = "Следующие файлы готовы к загрузке: \n";
        String[][] record = getDocs();
        for (int i = 0; i < record.length; i++){
            result += (i+1) + ")" + record[i][1];
            if (!record[i][2].equals("")){
                result += " - " + record[i][2];
            }
            result += "\n";
        }
        result += "Выберите что Вас интересует:";
        return result;
    }
    public static synchronized String[][] getHostel(){
        URL url = null;
        try {
            url = new URL("http://telegram.atu.kz/api/readHostel.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Scanner in = null;
        try {
            in = new Scanner((InputStream) url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content="";
        while (in.hasNext()){
            content += in.nextLine();
        }
        JSONObject object = new JSONObject(content);
        JSONArray getArray = object.getJSONArray("records");
        String[][] result = new String[6][4];
        result[0][0] = "Номер и адрес общежитии";
        result[0][1] = "Занятые места";
        result[0][2] = "Свободные места";
        result[0][3] = "Всего";
        for (int i = 1; i < 6; i++) {
            JSONObject obj = getArray.getJSONObject(i-1);
            result[i][0] = (String) obj.get("HouseId") + ", по адресу " + (String) obj.get("Address");
            result[i][1] = (String) obj.get("countreg");
            result[i][2] = String.valueOf(Integer.parseInt((String) obj.get("summa")) - Integer.parseInt((String) obj.get("countreg")));
            result[i][3] = (String) obj.get("summa");
        }
        return result;
    }
}
