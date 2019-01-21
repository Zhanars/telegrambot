import firstmenu.Configuration;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class telegrambotsql {
    private static String countName = "";
    private static String userName = Configuration.getTelegramUsername();
    private static String password = Configuration.getPass();
    private static String connectUrl = Configuration.getTelegramBotHost();
    public static String registration(String message, Long chatid, int gettextlength) throws IOException, ClassNotFoundException, SQLException {
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
                        newsAtu.inserChatId(chatid);
                    }
                }else{
                    countName = "ИИН используется";


                }
            }else{
                countName = "Такой иин не существует";
            }

        } else {
            if (gettextlength ==4 && message.equals(getCode(chatid))){
                try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                    String SQL = "UPDATE [dbo].[bots] SET [temppassword] = NULL where [chatid] = '"+chatid+"' ";
                    stmt.executeUpdate(SQL);
                }
                Univer.resetPassword(getIIN(chatid));
                countName = "Пароль сброшен ";


            }else {
                countName = "Длина иин меньше 12";
            }

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
    public static void sendCodeEmail(String email, Long ChatId){
        int min = 1000;
        int max = 9999;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        String body = "Код подтверждения: "+String.valueOf(i);
        gmail.main(email, body);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "update [bots] set [temppassword] = '"+String.valueOf(i)+"' where chatid = '"+ChatId+"'";
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getCode(Long ChatId){
        String result = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select [temppassword] from [telegrambot].[dbo].[bots] where chatid = '"+ChatId+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                result = rs.getString("temppassword");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
