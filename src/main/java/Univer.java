import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Univer {
    private static String countName = "";
    private static String userName = "sa";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:sqlserver://185.97.115.134\\MSSQLSERVER;database=atu_univer";

    public static String IIN(String message) throws IOException, ClassNotFoundException, SQLException {


        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            countName = "";
            String SQL = "SELECT [students_sname], [students_name] FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + message + "%' and [student_edu_status_id] = 1";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = countName + rs.getString("students_sname");
            }
            return countName;
        }
    }

    public static Boolean checkIIN(String IIN) {
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1 ";
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


        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            if (checkIINPersonalorStudent(IIN) == 1) {
                SQL = "SELECT [students_name] as name,[students_sname] as sname FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1  ";
            } else {
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


    public static int checkIINPersonalorStudent(String IIN) {
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT personal_identification_number FROM [atu_univer].[dbo].[univer_personal] WHERE [personal_identification_number] LIKE '%" + IIN + "%'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                result = 2;
            }
            String SQL1 = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1 ";
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
            SQL = " ;with cte_tbl as" +
                    " (SELECT  [univer_subject].[subject_name_ru],[univer_subject].[subject_id],[univer_educ_type].[educ_type_name_ru] ," +
                    " [univer_attendance].[att_date], [univer_attendance].[ball], [univer_sheet_result].[date_keep],[univer_sheet_result].[result]," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id=49 then [univer_academ_calendar_pos].acpos_date_start else 0 end) r1," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id=55 then [univer_academ_calendar_pos].acpos_date_end else 0 end) r2," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id=56 then [univer_academ_calendar_pos].acpos_date_end else 0 end) r3," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id = 57  then acpos_date_start else 0 end) r4," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id = 57  then acpos_date_end else 0 end) r5," +
                    " max(case" +
                    " when [univer_sheet_result].[result] is null then 0 else [univer_sheet_result].[result] end) r6" +
                    " FROM [atu_univer].[dbo].[univer_attendance]" +
                    " JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_attendance].[student_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " JOIN [atu_univer].[dbo].[univer_sheet_result] ON [univer_sheet_result].[student_id] = [univer_students].[students_id]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 " +
                    " and [univer_academ_calendar_pos].[acpos_semester] = [univer_educ_plan_pos].[educ_plan_pos_semestr]" +
                    " and  [univer_academ_calendar_pos].[acpos_module] = [univer_educ_plan_pos].[acpos_module] " +
                    " and [univer_attendance].[ball]>= 0 and [univer_attendance].[att_date] >= '" + date3 + "'" +
                    " and [univer_sheet_result].[subject_id] =  [univer_subject].[subject_id]" +
                    " and [univer_academ_calendar_pos].[acpos_semester] = [univer_sheet_result].[n_seme]" +
                    " GROUP BY [univer_subject].[subject_name_ru],[univer_subject].[subject_id],[univer_educ_type].[educ_type_name_ru] " +
                    " ,[univer_attendance].[att_date],[univer_attendance].[ball], [univer_sheet_result].[date_keep],[univer_sheet_result].[result]" +
                    ")" +
                    " select  cte_tbl.[subject_name_ru] as subname ,cte_tbl.[educ_type_name_ru] ," +
                    " Convert(varchar(10),CONVERT(date,cte_tbl.[att_date],106),103) as qwer, cte_tbl.[ball] as ball ," +
                    " max(case" +
                    " when cte_tbl.r1 <= cte_tbl.[att_date] and cte_tbl.r2 >= cte_tbl.[att_date] then '55' " +
                    " when cte_tbl.r2 < cte_tbl.[att_date] and cte_tbl.r3 >= cte_tbl.att_date then '56'" +
                    " end) r4" +
                    ", cte_tbl.[subject_id] as subject_name," +
                    "max(case" +
                    " when cte_tbl.r4 < cte_tbl.[date_keep] and cte_tbl.r5 >= cte_tbl.[date_keep] then cte_tbl.r6" +
                    " else 0" +
                    " end) as r8" +
                    " from cte_tbl" +
                    " GROUP BY cte_tbl.[subject_name_ru],cte_tbl.[educ_type_name_ru] , " +
                    " cte_tbl.[att_date], cte_tbl.[ball], cte_tbl.[subject_id]  " +
                    " ORder BY cte_tbl.[subject_name_ru]";
            ResultSet rs = stmt.executeQuery(SQL);
        }
        return SQL;
    }

    public static String getAttendanceforweek(String IIN) throws SQLException {
        countName = "";
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        ResultSet rs1 = null;
        ArrayList<String> Attendencerk = new ArrayList<String>();
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            Attendencerk.addAll(getSumAttendance(IIN));
            rs1 = stmt.executeQuery(getAttendance(IIN, date1));
            int columns1 = 0;
            columns1 = rs1.getMetaData().getColumnCount();
            if (rs1 != null) {
                boolean bool=true;
                while (rs1.next()) {
                    if (Integer.parseInt(rs1.getString("r4")) == 55 && bool) {
                        bool = false;
                        for(String SumAttendecerk : Attendencerk){
                            countName = SumAttendecerk + "\n";
                        }
                        countName = countName + "\n\n" +"Ваш текущий контроль РК1 \n";
                    }else
                    if (Integer.parseInt(rs1.getString("r4")) == 56 && bool)
                        {
                        bool = false;
                            for(String SumAttendecerk : Attendencerk){
                                countName = SumAttendecerk + "\n";
                            }
                            countName = countName + "\n\n" +"Ваш текущий контроль РК2 \n";
                    }
                    for (int i = 1; i <= columns1 - 3; i++) {
                        countName = countName + rs1.getString(i) + "  ";
                    }
                    countName = countName + "\n";
                }
            } else {
                countName = "123456";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return countName;
    }

    public static String getStartDate(String IIN) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("gvjhgjkghkj");
        String SQL = "";
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        System.out.println(c.getTime());
        System.out.println(date2);
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT TOP 1 Convert(varchar(10),CONVERT(date,[univer_academ_calendar_pos].[acpos_date_start],106),103) as start" +
                    " FROM [atu_univer].[dbo].[univer_attendance]" +
                    " JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_attendance].[student_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 and [univer_academ_calendar_pos].[acpos_semester] = [univer_educ_plan_pos].[educ_plan_pos_semestr]" +
                    " and  [univer_academ_calendar_pos].[acpos_module] = [univer_educ_plan_pos].[acpos_module] and [univer_attendance].[ball]>= 0 and [univer_academ_calendar_pos].control_id = 49" +
                    " and [univer_attendance].[att_date] > '" + date1 + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    countName = rs.getString("start");
                }
            }
        }
        return countName;
    }

    public static ArrayList<String> getSumAttendance(String IIN) throws SQLException  {

        ResultSet rs1 = null;
        int sumrk1 = -1, sumrk2 = -1, i = 1;
        int subject_name = 0;
        ArrayList<String> SumAttendance = new ArrayList<String>();
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            rs1 = stmt.executeQuery(getAttendance(IIN, getStartDate(IIN)));
            countName = "";
            String ekz = "";

            if (rs1 != null) {
                while (rs1.next()) {
                    if (subject_name != Integer.parseInt(rs1.getString("subject_name"))) {
                        if(sumrk1!=-1) {
                            countName = countName + " РК1: " + Integer.toString(sumrk1) + " РК2: " + Integer.toString(sumrk2) + " Экз: " + rs1.getString("r8") +"\n"+ rs1.getString("subname");
                            SumAttendance.add(countName);
                        } else {

                            countName = rs1.getString("subname");
                        }
                            subject_name = Integer.parseInt(rs1.getString("subject_name"));
                            i = i +1;
                            sumrk1 = 0;
                            sumrk2 = 0;
                    }
                    if (subject_name == Integer.parseInt(rs1.getString("subject_name"))){
                        String ball = rs1.getString("ball");
                        int len = ball.length();
                        ball = ball.substring(0,len-2);
                        if (Integer.parseInt(rs1.getString("r4")) == 55) {
                            sumrk1 = sumrk1 + Integer.parseInt(ball);
                        } else if (Integer.parseInt(rs1.getString("r4"))== 56) {
                            sumrk2 = sumrk2 + Integer.parseInt(ball);
                        }
                    }
                    ekz = rs1.getString("r8");
                }
                countName = countName + " РК1: " + Integer.toString(sumrk1) + " РК2: " + Integer.toString(sumrk2) + " Экз: " + ekz;
                SumAttendance.add(countName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return  SumAttendance;
    }

    public static String[][] getTranskript(String IIN){
        String SQL = "";
        String SQL1 = "";

        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT CONCAT ([univer_students].[students_sname],' ',[univer_students].[students_name],' ',[univer_students].[students_father_name]) as fio" +
                    "   ,[univer_faculty].[faculty_name_ru]" +
                    "  ,[univer_speciality].[speciality_name_ru]" +
                    "   ,[univer_students].[students_curce_number]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1";

            SQL1 = " SELECT [univer_progress].[subject_name_ru]" +
                    "      ,[univer_progress].[progress_credit]" +
                    " ,ROUND(((0.6*(([univer_progress].[progress_result_rk1] + [univer_progress].[progress_result_rk2])/2))+ (([univer_progress].[progress_result])*0.4)),0) as resultekz" +
                    "      ,[univer_mark_type].[mark_type_symbol]" +
                    " ,[univer_mark_type].[mark_type_gpa]" +
                    "      ,[univer_progress].[n_seme]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1";
            ResultSet rs = stmt.executeQuery(SQL1);
            int rowCount = getRowCount(rs);
            int colCount = rs.getMetaData().getColumnCount();
            System.out.println(rowCount);
            System.out.println(colCount);
            rs.close();
            String[][] result = new String[rowCount + 2][colCount];
            ResultSet rs1 = stmt.executeQuery(SQL);
            while (rs1.next()) {
                result[0][0] = rs1.getString("fio");
                result[0][1] = rs1.getString("faculty_name_ru");
                result[0][2] = rs1.getString("speciality_name_ru");
                result[0][3] = rs1.getString("students_curce_number");
            }
            result[1][0] = "Дисциплина";
            result[1][1] = "Кол-во кредитов";
            result[1][2] = "Оценка";
            result[1][3] = "Оценка по буквенной системе";
            result[1][4] = "Цифровой эквивалент";
            result[1][5] = "Семестр";
            rs1.close();
            ResultSet rs2 = stmt.executeQuery(SQL1);
            int i = 2;
            while (rs2.next()) {
                for (int j = 0; j < colCount; j++) {
                    result[i][j] = rs2.getString(j+1);
                }
                i++;
            }
            return  result;

        } catch (SQLException e) {
            e.printStackTrace();
            return  result1;
        }
    }

    public static ArrayList<String> getGPAforTranskript(String IIN){
        String SQL = "";

        ResultSet rs1 = null;
        int kr = -1, i = 1, sumkr = 0;
        double gpa = 0 , zifr = 0, sumzifkr = 0, sum = 0;
        int academ_year = 0;
        ArrayList<String> GPA = new ArrayList<String>();
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String countName = "";

            SQL = " SELECT [univer_progress].[subject_name_ru]" +
                    "      ,[univer_progress].[progress_credit]" +
                    " ,[univer_mark_type].[mark_type_gpa]" +
                    "      ,[univer_progress].[academ_year]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    if (academ_year != Integer.parseInt(rs.getString("academ_year"))) {
                        gpa = sum / sumkr;
                        if(sumkr!=0) {
                            countName = countName + Double.toString(gpa) +"\n"+ " GPA за " + rs1.getString("academ_year") + " год : ";
                            GPA.add(countName);
                        } else {

                            countName = "GPA за " + rs1.getString("academ_year") + " год : ";
                        }
                        academ_year = Integer.parseInt(rs1.getString("academ_year"));
                        i = i +1;
                        sumzifkr = 0;
                        sum = 0;
                        sumkr = 0;
                        gpa = 0;
                    }
                    if (academ_year == Integer.parseInt(rs1.getString("academ_year"))){
                        kr = Integer.parseInt(rs1.getString("progress_credit"));
                        zifr = Double.parseDouble(rs1.getString("mark_type_gpa"));
                        sumzifkr = kr * zifr;
                        sum = sum + sumzifkr;
                        sumkr = sumkr+kr;

                    }
                }
                gpa = sum / sumkr;
                countName = countName + Double.toString(gpa);
                GPA.add(countName);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return GPA;
        }
        return GPA;

    }









    private static int getRowCount(ResultSet resultSet) {
        int count = 0;
        if (resultSet == null) {
            return 0;
        }
        try {
            while (resultSet.next()){
                count++;
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {

        }
        return count;
    }
}
