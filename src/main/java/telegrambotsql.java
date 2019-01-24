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
    public static String getStaticForCourse(String IIN){
        String result = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select * from [telegrambot].[dbo].[statistics] where [IIN] = '" + IIN + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                result = rs.getString("gpa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }
    public static void manageStatistics() {
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(Configuration.getUniverHost(), Configuration.getUniverUsername(), password);
             Statement stmt = conn.createStatement();
             Connection conn1 = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt1 = conn1.createStatement();) {
            String createTable = "IF OBJECT_ID ('telegrambot.dbo.statistics', 'U') IS NOT NULL DROP TABLE [telegrambot].[dbo].[statistics]; " +
                    "CREATE TABLE [telegrambot].[dbo].[statistics] (" +
                    "Id int PRIMARY KEY NOT NULL," +
                    "IIN varchar(50)," +
                    "fio varchar(100)," +
                    "faculty_name_ru varchar(100)," +
                    "faculty_id INT," +
                    "speciality_name_ru varchar(100)," +
                    "speciality_id INT," +
                    "curce INT," +
                    "gpa REAL);";
            stmt1.executeUpdate(createTable);
            SQL = "SELECT CONCAT (st.[students_sname],' ',st.[students_name],' ',st.[students_father_name]) as fio " +
                    ",st.students_identify_code " +
                    ",fc.faculty_full_name_ru " +
                    ",fc.faculty_id " +
                    ",sp.speciality_name_ru " +
                    ",sp.speciality_id " +
                    ",st.students_curce_number" +
                    ",[dbo].[getGPAForStudent](st.students_id,0,0) as gpa " +
                    "from [atu_univer].[dbo].[univer_students] st " +
                    "JOIN [atu_univer].[dbo].[univer_faculty] fc ON fc.[faculty_id] = st.faculty_id " +
                    "join [atu_univer].[dbo].[univer_speciality] sp ON sp.[speciality_id] = st.[speciality_id] " +
                    "join univer_group_student gs on gs.student_id = st.students_id " +
                    "where st.student_edu_status_id = 1 " +
                    "group by " +
                    "st.[students_sname]" +
                    ",st.[students_name]" +
                    ",st.[students_father_name]" +
                    ",st.students_identify_code" +
                    ",fc.faculty_full_name_ru" +
                    ",sp.speciality_name_ru" +
                    ",st.students_curce_number" +
                    ",st.students_id" +
                    ",fc.faculty_id" +
                    ",sp.speciality_id";
            ResultSet rs = stmt.executeQuery(SQL);
            int i = 1;
            while (rs.next()) {
                String insertSQL = "INSERT INTO [statistics] VALUES  (" +
                        i + ", '" +
                        rs.getString("students_identify_code") + "', '"+
                        rs.getString("fio") + "', '"+
                        rs.getString("faculty_full_name_ru") + "', '"+
                        rs.getString("faculty_id") + "', '"+
                        rs.getString("speciality_name_ru") + "', '"+
                        rs.getString("speciality_id") + "', "+
                        rs.getString("students_curce_number") + ", "+
                        rs.getString("gpa") + ")";
                stmt1.executeUpdate(insertSQL);
                i++;
            }
            System.out.println(i);
            stmt1.close();
            stmt.close();
            conn.close();
            conn1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
