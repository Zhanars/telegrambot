import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class telegrambotsql {
    private static String countName = "";
    private static String userName = "joker";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:sqlserver://185.97.115.131\\RUSGUARD:49181;database=telegrambot";
    public static String registration(String message, Long chatid,int gettextlength) throws IOException, ClassNotFoundException, SQLException {
        String date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        if (gettextlength == 12) {
            if(Univer.checkIINPersonalorStudent(message) > 0) {
                if(checkChatId(chatid)){
                    try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                        String SQL = "DELETE from [dbo].[bots] WHERE [chatid]='"+chatid+"' and IIN != '"+message+"'";
                        stmt.executeUpdate(SQL);
                        countName = "ИИН удален";
                    }
                }
                if(checkIINandChatid(message, chatid)) {
                    try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                        String SQL = "INSERT INTO [dbo].[bots] ([IIN], [chatid], [Createdate], [firstName],[lastName], [checkperson]) VALUES ('" + message + "',  '" + chatid + "' , '" + date + "', '"+Univer.getStOrPersonName(message)+"' , '"+Univer.checkIINPersonalorStudent(message)+"' ) ";
                        stmt.executeUpdate(SQL);
                        countName = "ИИН подтвержден, теперь вы можете работать с ботом. Для изменения ИИНа введите в новый иин";
                    }
                }else{
                    countName = "ИИН используется";


                }
            }else{
                countName = "Такой иин не существует";
            }

        }else {
            countName = "Длина иин меньше 12";

        }
        return countName;

    }
    public static Boolean checkIINandChatid(String IIN, Long ChatId){
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select IIN from [dbo].[bots] where IIN ='"+IIN+"' and chatid = '"+ChatId+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  bool;
    }
    public static Boolean checkChatId(Long ChatId){
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select chatid from [dbo].[bots] where chatid = '"+ChatId+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                bool = true;
            } else {
                bool = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  bool;
    }

    public static String getIIN(Long ChatId){
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select IIN from [dbo].[bots] where chatid = '"+ChatId+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("IIN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }

    public static String getfromBotsName(Long ChatId){
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select firstName, lastName from [dbo].[bots] where chatid = '"+ChatId+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("firstName") + " " + rs.getString("lastName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }

    public static int getStatus(String ChatId)
    {
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select checkperson from [dbo].[bots] where chatid = '"+ChatId+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                 result = rs.getInt("checkperson");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String saveQuiz(Message message, String answer){
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "INSERT INTO [dbo].[quizSample] ([ChatId], [question], [answer]) VALUES ('" + message.getChatId() + "',  '" + message.getText() + "' , '" + answer + "') ";
            stmt.executeUpdate(SQL);
            countName = "Спасибо за ответ!\nНам очень важен ваше мнение.";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }
    public static Boolean hasAnswerQuiz(Long ChatId, String text){
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
