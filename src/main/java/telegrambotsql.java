import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class telegrambotsql {
    private static String countName = "";
    private static String userName = "joker";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:sqlserver://185.97.115.127\\RUSGUARD;database=telegrambot";
    public static String botsql(String message, Long chatid,int gettextlength) throws IOException, ClassNotFoundException, SQLException {
        String date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        if (gettextlength == 12) {
            if(Univer.checkIIN(message)) {
                if(checkChatId(chatid)){
                    try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                        String SQL = "DELETE from [dbo].[bots] WHERE [chatid]='"+chatid+"' and IIN != '"+message+"'";
                        stmt.executeUpdate(SQL);
                        countName = "ИИН удален";
                        System.out.println(countName);
                    }
                }
                if(checkIINandChatid(message, chatid)) {
                    try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                        String SQL = "INSERT INTO [dbo].[bots] ([IIN], [chatid], [Createdate], [firstName],[lastName] ) VALUES ('" + message + "',  '" + chatid + "' , '" + date + "', '"+Univer.getname(message)+"', '"+Univer.getlastname(message)+"'  ) ";
                        stmt.executeUpdate(SQL);
                        countName = "ИИН зарегестрирован";
                        System.out.println(countName);
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
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(rs);
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
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(rs);
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
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(rs);
            while (rs.next()) {
                countName = rs.getString("IIN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }

}
