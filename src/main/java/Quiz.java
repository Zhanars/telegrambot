import firstmenu.Configuration;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.*;

public class Quiz {
    private static String countName = "";
    private static String userName = Configuration.getTelegramUsername();
    private static String password = Configuration.getPass();
    private static String connectUrl = Configuration.getTelegramBotHost();
    public static synchronized String saveQuiz(Message message, String answer){
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "INSERT INTO [dbo].[quizSample] ([ChatId], [question], [answer]) VALUES ('" + message.getChatId() + "',  '" + message.getText() + "' , '" + answer + "') ";
            stmt.executeUpdate(SQL);
            countName = "Спасибо за ответ!\nНам очень важен ваше мнение.";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }
    public static synchronized Boolean hasAnswerQuiz(Long ChatId, String text){
        boolean result = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT * from [dbo].[quizSample] WHERE ChatId ='"+ChatId+"' and question ='"+text+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
