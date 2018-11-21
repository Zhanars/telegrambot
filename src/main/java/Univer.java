import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Univer {
    private static String countName = "";
    private static String userName = "sa";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:sqlserver://185.97.115.134\\MSSQLSERVER;database=atu_univer";
    public static String IIN(String message) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("gvjhgjkghkj");
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            countName = "";
            String SQL = "SELECT [students_sname], [students_name] FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+message+"%' and [student_edu_status_id] = 1";
            ResultSet rs = stmt.executeQuery(SQL);
                while (rs.next()) {
                    countName = countName + rs.getString("students_sname");
                }
                return countName;
        }


    }
    public static  Boolean checkIIN(String IIN){
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+IIN+"%' and [student_edu_status_id] = 1 ";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                bool = true;
            } else {
                bool = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bool;
    }
    public static String getStOrPersonName(String IIN) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("gvjhgjkghkj");
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            if(checkIINPersonalorStudent(IIN) == 1) {
                SQL = "SELECT [students_name] as name,[students_sname] as sname FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1  ";
            }else {
                SQL = "SELECT [personal_name] as name,[personal_sname] as sname FROM [atu_univer].[dbo].[univer_personal] WHERE [personal_identification_number] LIKE '%" + IIN + "%'";
            }
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("name");
                countName = countName + "','";
                countName = countName + rs.getString("sname");

            }
            return countName;
        }


    }


    public static  int checkIINPersonalorStudent(String IIN){
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT personal_identification_number FROM [atu_univer].[dbo].[univer_personal] WHERE [personal_identification_number] LIKE '%"+IIN+"%'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                result = 2;
            }
            String SQL1 = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+IIN+"%' and [student_edu_status_id] = 1 ";
            ResultSet rs1 = stmt.executeQuery(SQL1);
            if (rs1.next()) {
                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String getAttendance(String IIN) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("gvjhgjkghkj");
        String SQL = "";
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        System.out.println(c.getTime());
        System.out.println(date2);
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());

        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                SQL = "SELECT [univer_subject].[subject_name_ru],[univer_control].[control_short_name_ru],[univer_educ_type].[educ_type_name_ru] , Convert(varchar(10),CONVERT(date,[univer_attendance].[att_date],106),103) as name , [univer_attendance].[ball] FROM [atu_univer].[dbo].[univer_students]" +
                        "JOIN  [atu_univer].[dbo].[univer_attendance] ON [univer_attendance].[student_id] = [univer_students].[students_id]" +
                        "JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                        "JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                        "JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                        "JOIN  [atu_univer].[dbo].[univer_educ_plan] ON [univer_educ_plan].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                        "JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan].[educ_plan_id] " +
                        "JOIN  [atu_univer].[dbo].[univer_controll_type_control_link] ON [univer_controll_type_control_link].[controll_type_id] = [univer_educ_plan_pos].[controll_type_id]" +
                        "JOIN  [atu_univer].[dbo].[univer_control] ON [univer_control].[control_id] = [univer_controll_type_control_link].[control_id] " +
                        "JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                        "WHERE [univer_students].[students_identify_code] LIKE '%" + IIN + "%' and [univer_students].[student_edu_status_id] = 1 and [univer_attendance].[att_date] > '"+date1+"' and [univer_attendance].[ball] > '0' " +
                        " and [univer_academ_calendar_pos].[acpos_date_end] > [univer_attendance].[att_date] and [univer_academ_calendar_pos].[acpos_date_start] < [univer_attendance].[att_date] ";

            ResultSet rs1 = stmt.executeQuery(SQL);

            int columns1 = rs1.getMetaData().getColumnCount();
            if (rs1 != null) {
                while (rs1.next()) {
                    for (int i = 1; i<= columns1; i++){
                        countName = countName + rs1.getString(i)+ "  ";
                    }
                    countName = countName + "\n";
                }
            }else {
                countName = "123456";
            }

            return countName;
        }


    }


}
