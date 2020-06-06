import firstmenu.Configuration;
import org.telegram.telegrambots.meta.api.objects.Message;

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
    public static synchronized String registration(String message, Long chatid, int gettextlength) {
        String date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        if (gettextlength == 12) {
            if(Univer.checkIINPersonalorStudent(message) > 0) {
                if(checkChatId(chatid)){
                    try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                        String SQL = "DELETE from [dbo].[bots] WHERE [chatid]='"+chatid+"' and IIN != '"+message+"'";
                        stmt.executeUpdate(SQL);
                        countName = "ИИН удален";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(checkIINandChatid(message, chatid)) {
                    try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                        String SQL = "INSERT INTO [dbo].[bots] ([IIN], [chatid], [Createdate], [firstName],[lastName], [checkperson]) VALUES ('" + message + "',  '" + chatid + "' , '" + date + "', '"+Univer.getStOrPersonName(message)+"' , '"+Univer.checkIINPersonalorStudent(message)+"' ) ";
                        stmt.executeUpdate(SQL);
                        countName = "ИИН подтвержден, Для взаимодействия с ботом необходимо отправить номер телефона. Это может использоваться для интеграции с другими сервисами";
                        newsAtu.inserChatId(chatid);
                    } catch (SQLException e) {
                        e.printStackTrace();
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Univer.resetPassword(getIIN(chatid));
                countName = "Пароль сброшен ";


            }else {
                countName = "Длина иин меньше 12";
            }

        }
        return countName;

    }

    public static synchronized String ContactBot(Long chatid, String  lastName, String firstname, String Contact) {

                try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                    String SQL = "UPDATE [dbo].[bots] SET  [firtsNameTelegram] = '"+firstname+"'" +
                            "      ,[lastNameTelegram] = '"+lastName+"'" +
                            "      ,[ContactTelegram] = '"+Contact+"' where [chatid] = '"+chatid+"' ";
                    stmt.executeUpdate(SQL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        countName = "Вы успешно авторизовались. Для изменения ИИНа введите новый иин";

        return countName;

    }




    public static synchronized Boolean checkIINandChatid(String IIN, Long ChatId){
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select IIN from [dbo].[bots] where IIN ='" + IIN + "' and chatid = '" + ChatId + "'";
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



    public static synchronized Boolean checkChatId(Long ChatId){
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





    public static synchronized Boolean checkContact(Long ChatId){
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "select ContactTelegram from [dbo].[bots] where chatid = '"+ChatId+"' and ContactTelegram != 'NULL' ";
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

    public static synchronized String getIIN(Long ChatId){
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

    public static synchronized String getfromBotsName(Long ChatId){
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

    public static synchronized int getStatus(String ChatId)
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
    public static synchronized void sendCodeEmail(String email, Long ChatId){
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
    public static synchronized String getCode(Long ChatId){
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
    public static synchronized void manageStatistics() {
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
                    "edu_levels_id INT," +
                    "realgpa REAL," +
                    "rz REAL," +
                    "dateupdate DATETIME);";
            stmt1.executeUpdate(createTable);
            SQL = " SELECT CONCAT (stu.[students_sname],' ',stu.[students_name],' ',stu.[students_father_name]) as fio " +
                    "                   ,stu.students_identify_code " +
                    "                   ,fc.faculty_full_name_ru " +
                    "                   ,fc.faculty_id " +
                    "                   ,sp.speciality_name_ru " +
                    "                   ,sp.speciality_id " +
                    "                   ,stu.students_curce_number" +
                    "                   ,stu.edu_levels_id" +
                    "                   ,[dbo].[getGPAForStudent](stu.students_id,0,0) as gpa    " +
                    "                   ,(SELECT sum(isnull([dbo].[getProgressFinalRez]([controll_type_id],[progress_result_rk1]" +
                    "                   ,[progress_result_rk2],[progress_result]),0)*pr.progress_credit) / case when SUM(pr.progress_credit) > 0 then SUM(pr.progress_credit) else 1 end " +
                    "                    from [univer_progress] pr left join [univer_mark_type] mt on mt.mark_type_id=pr.mark_type_id, [univer_students] st with (nolock) " +
                    "                    where st.students_id=pr.student_id and pr.status=1 and student_id=stu.students_id )  as rz " +
                    "                   from [atu_univer].[dbo].[univer_students] stu " +
                    "                   JOIN [atu_univer].[dbo].[univer_faculty] fc ON fc.[faculty_id] = stu.faculty_id " +
                    "                   join [atu_univer].[dbo].[univer_speciality] sp ON sp.[speciality_id] = stu.[speciality_id] " +
                    "                   join univer_group_student gs on gs.student_id = stu.students_id " +
                    "                   where stu.student_edu_status_id = 1 " +
                    "                   group by " +
                    "                   stu.[students_sname]" +
                    "                   ,stu.[students_name]" +
                    "                   ,stu.[students_father_name]" +
                    "                   ,stu.students_identify_code" +
                    "                   ,fc.faculty_full_name_ru" +
                    "                   ,sp.speciality_name_ru" +
                    "                   ,stu.students_curce_number" +
                    "                   ,stu.students_id" +
                    "                   ,fc.faculty_id" +
                    "                   ,sp.speciality_id" +
                    "                   ,stu.edu_levels_id";
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
                        rs.getString("edu_levels_id") + ", "+
                        rs.getString("gpa") + ", "+
                        rs.getString("rz") + ")";
                stmt1.executeUpdate(insertSQL);
                i++;
                System.out.println(i);
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
    public static synchronized String getRatingforCourse(String IIN){
        String SQL = "", res = "" , gpa = "";
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT IIN,round(realgpa,2) as gpa FROM [dbo].[statistics] where curce = '" + getCourse(IIN) + "' and edu_levels_id = '"+getEduLevel(IIN)+"' ORDER BY rz desc";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result++;
                if (IIN.equals(rs.getString("IIN"))) {
                    res = "Вы на " + result + " месте, из ";
                    gpa = rs.getString("gpa");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        res = res + result + ". Ваш gpa = " + gpa;
        return res;
    }
    public static synchronized String getRatingforFacultet(String IIN){
        String SQL = "", res = "", gpa = "";
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT  IIN, round(realgpa,2) as gpa FROM [dbo].[statistics] where faculty_id = '" + getFacultetId(IIN) + "' AND curce = '" + getCourse(IIN) + "' and edu_levels_id = '"+getEduLevel(IIN)+"' ORDER BY rz desc";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result++;
                if (IIN.equals(rs.getString("IIN"))) {
                    res = "Вы на " + result + " месте, из ";
                    gpa = rs.getString("gpa");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        res = res + result + ". Ваш gpa = " + gpa;
        return res;
    }
    public static synchronized String getRatingforSpecial(String IIN){
        String SQL = "", res="", gpa = "";
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT IIN, round(realgpa,2) as gpa FROM [dbo].[statistics] where speciality_id = '" + getSpecialId(IIN) + "' AND curce = '" + getCourse(IIN) + "' and edu_levels_id = '"+getEduLevel(IIN)+"' ORDER BY rz desc";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result++;
                if (IIN.equals(rs.getString("IIN"))) {
                    res = "Вы на " + result + " месте, из ";
                    gpa = rs.getString("gpa");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        res = res + result + ". Ваш gpa = " + gpa;
        return res;
    }
    public static synchronized int getCourse(String IIN){
        String SQL = "";
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT TOP 1 * FROM [statistics] WHERE [IIN] LIKE '" + IIN +"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result = Integer.parseInt(rs.getString("curce"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static synchronized int getEduLevel(String IIN){
        String SQL = "";
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT TOP 1 * FROM [statistics] WHERE [IIN] LIKE '" + IIN +"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result = Integer.parseInt(rs.getString("edu_levels_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static synchronized int getFacultetId(String IIN){
        String SQL = "";
        int facultetid = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT TOP 1 * FROM [statistics] WHERE [IIN] LIKE '" + IIN +"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                facultetid = Integer.parseInt(rs.getString("faculty_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultetid;
    }
    public static synchronized int getSpecialId(String IIN){
        String SQL = "";
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT TOP 1 * FROM [statistics] WHERE [IIN] LIKE '" + IIN +"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result = Integer.parseInt(rs.getString("speciality_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static synchronized void insertLog(Message message){
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            String SQL = "INSERT INTO [dbo].[logs] ([chatid], [mess], [firstNameTelegram], [lastNameTelegram], [IIN]) VALUES ('" + message.getChatId() + "',  '" + message.getText() + "' , '" + message.getFrom().getFirstName() + "', '" + message.getFrom().getLastName() + "' , '" + getIIN(message.getChatId()) + "' ) ";
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
