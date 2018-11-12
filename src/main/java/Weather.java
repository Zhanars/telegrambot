import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //745779362:AAEFky83gaEOP4aB8RjUHq4_BcW7hCYSB68
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=9e90503591cf1838eca776cf2d0a7a6d");
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject object1 = getArray.getJSONObject(i);
            model.setIcon((String) object1.get("icon"));
            model.setMain((String) object1.get("main"));
        }

        if (model.getMain().equals("Clouds")){
            model.setMain("Облачно");
        }

        if (model.getName().equals("Almaty")){
            model.setName("Алматы");
        }

        return "Город: " + model.getName() + "\n" +
                "Температура: " + model.getTemp() + " C \n" +
                "Влажность: " + model.getHumidity() + "% \n" +
                "Погода: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";

    }
}
