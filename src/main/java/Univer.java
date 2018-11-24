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



    public static String getAttendance(String IIN, String date3) throws IOException, ClassNotFoundException, SQLException {
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
                SQL = ";with cte_tbl as" +
                        " (SELECT  [univer_subject].[subject_name_ru],[univer_educ_type].[educ_type_name_ru] ," +
                        " [univer_attendance].[att_date], [univer_attendance].[ball]," +
                        " max(case" +
                        " when [univer_academ_calendar_pos].control_id=49 then [univer_academ_calendar_pos].acpos_date_start else 0 end) r1," +
                        " max(case" +
                        " when [univer_academ_calendar_pos].control_id=55 then [univer_academ_calendar_pos].acpos_date_end else 0 end) r2," +
                        " max(case" +
                        " when [univer_academ_calendar_pos].control_id=56 then [univer_academ_calendar_pos].acpos_date_end else 0 end) r3" +
                        " FROM [atu_univer].[dbo].[univer_attendance]" +
                        " JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_attendance].[student_id]" +
                        " JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                        " JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                        " JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                        " JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                        " JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                        " WHERE [univer_students].[students_identify_code] LIKE '"+IIN+"' and [univer_students].[student_edu_status_id] = 1 " +
                        " and [univer_academ_calendar_pos].[acpos_semester] = [univer_educ_plan_pos].[educ_plan_pos_semestr]" +
                        " and  [univer_academ_calendar_pos].[acpos_module] = [univer_educ_plan_pos].[acpos_module] " +
                        " and [univer_attendance].[ball]>= 0 and [univer_attendance].[att_date] >= '"+date3+"'" +
                        " GROUP BY [univer_subject].[subject_name_ru],[univer_educ_type].[educ_type_name_ru] " +
                        " ,[univer_attendance].[att_date],[univer_attendance].[ball] )" +
                        " select  cte_tbl.[subject_name_ru],cte_tbl.[educ_type_name_ru] ," +
                        " Convert(varchar(10),CONVERT(date,cte_tbl.[att_date],106),103) as qwer, cte_tbl.[ball] ," +
                        " max(case" +
                        " when cte_tbl.r1 < cte_tbl.[att_date] and cte_tbl.r2 >cte_tbl.[att_date] then 'RK1' " +
                        " when cte_tbl.r2 < cte_tbl.[att_date] and cte_tbl.r3 > cte_tbl.att_date then 'RK2'" +
                        " end) r4" +
                        " from cte_tbl" +
                        " GROUP BY cte_tbl.[subject_name_ru],cte_tbl.[educ_type_name_ru] , " +
                        " cte_tbl.[att_date], cte_tbl.[ball] " +
                        " ORder BY cte_tbl.[subject_name_ru]";
                System.out.println(SQL);
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
        }
        return countName;
    }

    public static String getStartDate(String IIN) throws IOException, ClassNotFoundException, SQLException{
        System.out.println("gvjhgjkghkj");
        String SQL = "";
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        System.out.println(c.getTime());
        System.out.println(date2);
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT TOP 1 [univer_academ_calendar_pos].[acpos_date_start] as start" +
                    " FROM [atu_univer].[dbo].[univer_attendance]" +
                    " JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_attendance].[student_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '"+IIN+"' and [univer_students].[student_edu_status_id] = 1 and [univer_academ_calendar_pos].[acpos_semester] = [univer_educ_plan_pos].[educ_plan_pos_semestr]" +
                    " and  [univer_academ_calendar_pos].[acpos_module] = [univer_educ_plan_pos].[acpos_module] and [univer_attendance].[ball]>= 0 and [univer_academ_calendar_pos].control_id = 49" +
                    " and [univer_attendance].[att_date] > '"+date1+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    countName = rs.getString("start");
                }
            }
        }
        return countName;
    }





}
