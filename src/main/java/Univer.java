import com.vdurmont.emoji.EmojiParser;

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
        String semestr = getSemestr(IIN);
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
                    " and [univer_academ_calendar_pos].[acpos_semester] = '"+semestr+"' " +
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

    public static String getAttendanceforweek(String IIN , String[][] Record) throws SQLException {
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
            String rk1 = Attendencerk.get(7);
            String rk2 = Attendencerk.get(8);

            int rowCount = Record.length ;
            int colCount = Record[0].length;
            if (rs1 != null) {
                boolean bool=true;
                while (rs1.next()) {
                    if (Integer.parseInt(rs1.getString("r4")) == 55 && bool) {
                        bool = false;
                        // Сравнивает рк у последнего предмета
                        if(Integer.parseInt(rk1)<= Integer.parseInt(Record[rowCount-1][1])){
                            for (int i=0 ; i< rowCount; i++){

                                    countName = countName + Record[i][0] + " РК1:" + Record[i][1] + " РК2:" + Record[i][2] + " Экз:"+ Record[i][3] + " Итог:"+ Record[i][4] + "\n";

                            }
                            countName = countName + "\n\n Журнал посещений";
                        }else{
                        for(String SumAttendecerk : Attendencerk){
                            countName = SumAttendecerk + "\n";
                        }
                        countName = countName + "\n\n" +"Ваш текущий контроль РК1 \n";
                    }}else
                    if (Integer.parseInt(rs1.getString("r4")) == 56 && bool)
                        {
                        bool = false;
                            if(Integer.parseInt(rk2)<= Integer.parseInt(Record[rowCount-1][2])){
                                for (int i=0 ; i< rowCount; i++){

                                        countName = countName + Record[i][0] + " РК1:" + Record[i][1] + " РК2:" + Record[i][2] + " Экз:"+ Record[i][3] + " Итог:"+ Record[i][4] + "\n";

                                }
                                countName = countName + "\n\n Журнал посещений";
                            }else{
                            for(String SumAttendecerk : Attendencerk){
                                countName = SumAttendecerk + "\n";
                            }}
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

    public static ArrayList<String> getSumAttendance(String IIN) throws SQLException  {

        ResultSet rs1 = null;
        int sumrk1 = -1, sumrk2 = -1, i = 1;
        int subject_name = 0;
        ArrayList<String> SumAttendance = new ArrayList<String>(30);
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
                SumAttendance.add(Integer.toString(sumrk1));
                SumAttendance.add(Integer.toString(sumrk2));
                SumAttendance.add(7,Integer.toString(sumrk1));
                SumAttendance.add(8,Integer.toString(sumrk2));
                System.out.println();
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

    public static String[][] getProgressforAttendence(String IIN) throws SQLException{
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL =  " ;with tableforresult as " +
                    " (SELECT [univer_progress].[subject_name_ru]" +
                    "  ,max(case" +
                    " when ([controll_type_id] >= 2 and [controll_type_id]<= 35) or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "  else [univer_progress].[progress_result_rk1] end ) RK1" +
                    "  ,max(case" +
                    "  when ([controll_type_id] >= 2 and [controll_type_id]<= 35) or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "  when [controll_type_id] = 48 or [controll_type_id] = 49 then [progress_result_rk1]" +
                    "  else [univer_progress].[progress_result_rk2] end ) RK2" +
                    "  ,[univer_progress].[progress_result]" +
                    "  ,[univer_progress].[progress_result_rk2]" +
                    "  ,[univer_progress].[progress_result_rk1]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    "  WHERE [univer_students].[students_identify_code] LIKE '"+IIN+"' and [univer_progress].[status] = 1 and [univer_progress].[n_seme] = '"+semestr+"'" +
                    "  GROUP BY [univer_progress].[subject_name_ru]" +
                    "  ,[progress_result_rk1]" +
                    "  ,[univer_progress].[progress_result_rk2]" +
                    "  ,[univer_progress].[progress_result]" +
                    " )" +
                    " SELECT tableforresult.[subject_name_ru] as subject" +
                    "           ,tableforresult.RK1 as rk1" +
                    "   ,tableforresult.RK2 as rk2" +
                    "  ,tableforresult.[progress_result] as result" +
                    "  ,ROUND(((0.6*((tableforresult.RK1 + tableforresult.RK2)/2))+ ((tableforresult.[progress_result])*0.4)),0) as resultekz" +
                    "  FROM tableforresult" +
                    " ORDER BY tableforresult.[subject_name_ru]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }
            return result;
        }
    }





    public static String getStartDate(String IIN) throws IOException, ClassNotFoundException, SQLException {
        String SQL = "";
        String semestr = "";
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
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
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 and [univer_academ_calendar_pos].[acpos_semester] = '"+semestr+"'" +
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

    public  static String getSemestr(String IIN){
        countName = "";
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT top 1" +
                    "   [univer_progress].[n_seme] as n_seme" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1" +
                    " ORDER BY [n_seme] desc";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("n_seme");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }



    public static String[][] getTranskript(String IIN){
        String SQL = "";
        String SQL1 = "";

        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT CONCAT ([univer_students].[students_sname],' ',[univer_students].[students_name],' ',[univer_students].[students_father_name]) as fio" +
                    "   ,[univer_faculty].[faculty_name_ru]" +
                    "   ,[univer_speciality].[speciality_name_ru]" +
                    "   ,[univer_students].[students_curce_number]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1";

            SQL1 = " ;with tableforresult as " +
                    " (SELECT [univer_progress].[subject_name_ru]" +
                    "      ,[univer_progress].[progress_credit]" +
                    "      ,max(case" +
                    "       when [controll_type_id] >= 2 and [controll_type_id]<= 35 or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "       else [univer_progress].[progress_result_rk1] end ) RK1" +
                    "       ,max(case\n" +
                    "       when [controll_type_id] >= 2 and [controll_type_id]<= 35 or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "       when [controll_type_id] = 48 or [controll_type_id] = 49 then [progress_result_rk1]" +
                    "       else [univer_progress].[progress_result_rk2] end ) RK2" +
                    "      ,[univer_mark_type].[mark_type_symbol]" +
                    "      ,[univer_mark_type].[mark_type_gpa]" +
                    "      ,[univer_progress].[n_seme]" +
                    "      ,[univer_progress].[academ_year]" +
                    "      ,[univer_progress].[progress_result]" +
                    "      ,[univer_progress].[progress_result_rk2]" +
                    "      ,[univer_progress].[progress_result_rk1]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    "  WHERE [univer_students].[students_identify_code] LIKE '"+IIN+"' and [univer_progress].[status] = 1" +
                    "  GROUP BY [univer_progress].[subject_name_ru]" +
                    "      ,[univer_progress].[progress_credit]" +
                    "      ,[univer_mark_type].[mark_type_symbol]" +
                    "      ,[univer_mark_type].[mark_type_gpa]" +
                    "      ,[univer_progress].[n_seme]" +
                    "      ,[univer_progress].[academ_year]" +
                    "      ,[progress_result_rk1]" +
                    "      ,[univer_progress].[progress_result_rk2]" +
                    "      ,[univer_progress].[progress_result])" +
                    "      SELECT tableforresult.[subject_name_ru]" +
                    "      ,tableforresult.[progress_credit]" +
                    "      ,ROUND(((0.6*((tableforresult.RK1 + tableforresult.RK2)/2))+ ((tableforresult.[progress_result])*0.4)),0) as resultekz" +
                    "      ,tableforresult.[mark_type_symbol]" +
                    "      ,tableforresult.[mark_type_gpa]" +
                    "      ,tableforresult.[n_seme]" +
                    "      ,tableforresult.[academ_year]" +
                    "      FROM tableforresult" +
                    "      ORDER BY tableforresult.[academ_year]";
            ResultSet rs = stmt.executeQuery(SQL1);
            int rowCount = getRowCount(rs);
            int colCount = rs.getMetaData().getColumnCount();
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


    public static String[][] getExamSchedule(String IIN){
        Calendar c = new GregorianCalendar();
        String nowDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        c.add(Calendar.MONTH, -1);
        String takeMonth = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        c.add(Calendar.MONTH, 1);
        String addMonth = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        String SQL = "";
        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT  [subject_name_ru]" +
                    " ,CONCAT([personal_sname] ,' '" +
                    "       ,[personal_name],' '" +
                    "       ,[personal_father_name]) as FIO" +
                    " ,[building_name_ru]" +
                    " ,[univer_educ_plan_pos].[exam_form_id]" +
                    " ,[exam_time]" +
                    ", [audience_number_ru]" +
                    " FROM [atu_univer].[dbo].[univer_exam_schedule]" +
                    " JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].[group_id] = [univer_exam_schedule].[group_id]" +
                    " JOIN [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_exam_schedule].[group_id]" +
                    " JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " JOIN [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " JOIN [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] = [univer_group_student].[student_id]" +
                    " JOIN [atu_univer].[dbo].[univer_teacher] ON [univer_teacher].teacher_id = [univer_exam_schedule].examiner_teacher_id" +
                    " JOIN [atu_univer].[dbo].[univer_personal] ON [univer_personal].personal_id = [univer_teacher].personal_id " +
                    " JOIN [atu_univer].[dbo].[univer_audience] ON [univer_audience].audience_id = [univer_exam_schedule].audience_id" +
                    " JOIN [atu_univer].[dbo].[univer_building] ON [univer_building].building_id = [univer_audience].building_id" +
                    " Where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 and " +
                    " [univer_exam_schedule].[exam_time] > '"+takeMonth+"'" +
                    " ORDER BY [exam_time]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount + 1][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            result[0][0] = "Дисциплина";
            result[0][1] = "ФИО Преподавателя";
            result[0][2] = "Аудитория";
            result[0][3] = "Тип";
            int j = 1;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    if (rs2.getString(i + 1).equals("1") ) {
                        result[j][i] = "Устный";
                    }else if (rs2.getString(i + 1).equals("0")){
                        result[j][i] = "Письменный";
                    }
                    else {
                        if(i == 2)
                            result[j][i] = rs2.getString(i + 1) + " | Ауд " + rs2.getString(i + 4);
                        else
                        result[j][i] = rs2.getString(i + 1);

                    }
                }
                j++;


            }
            return  result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result1;

    }

    public static String[][] getAcademcal(String IIN) throws IOException, ClassNotFoundException, SQLException {
        String SQL = "";
        String dataStart = getStartDate(IIN);
        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT [univer_control].[control_name_ru]" +
                    "       ,[acpos_semester]" +
                    "       ,Convert(varchar(10),CONVERT(date,[acpos_date_start],106),103) as qwer" +
                    "       ,Convert(varchar(10),CONVERT(date,[acpos_date_end],106),103) as qwer1" +
                    "  FROM [atu_univer].[dbo].[univer_academ_calendar_pos]" +
                    "  JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].educ_plan_id = [univer_academ_calendar_pos].educ_plan_id" +
                    "  JOIN [atu_univer].[dbo].[univer_group] ON [univer_group].educ_plan_pos_id = [univer_educ_plan_pos].educ_plan_pos_id" +
                    "  JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].group_id = [univer_group].group_id" +
                    "  JOIN [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] = [univer_group_student].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_control] ON [univer_control].control_id = [univer_academ_calendar_pos].control_id" +
                    "  where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1  " +
                    " and [acpos_date_start] >= '"+dataStart+"' and [univer_academ_calendar_pos].acpos_module = 0" +
                    "  GROUP BY " +
                    "       [acpos_semester]" +
                    "      ,[univer_control].[control_name_ru]" +
                    "      ,[acpos_date_start]" +
                    "      ,[acpos_date_end]" +
                    "ORDER BY [acpos_semester], [acpos_date_start]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount + 1][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            result[0][0] = "Контроль";
            result[0][1] = "Семестр";
            result[0][2] = "Дата начала";
            result[0][3] = "Дата конца";
            int j = 1;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                            result[j][i] = rs2.getString(i + 1);
                    }
                j++;
                }

            return  result;
        }
    }

    public static String getAdvicer(String IIN){
        countName = "Ваш эдвайзер \n";
        String SQL = "";
        int i = 1;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT CONCAT([personal_sname] , ' '" +
                    "      ,[personal_name], ' '" +
                    "      ,[personal_father_name]) as fio" +
                    "      ,[personal_email]" +
                    "      ,[personal_mobile_phone]" +
                    "      ,[personal_home_phone]" +
                    "  FROM [atu_univer].[dbo].[univer_advicer]" +
                    "  JOIN [atu_univer].[dbo].[univer_advicer_student_link] ON [univer_advicer_student_link].advicer_id = [univer_advicer].advicer_id" +
                    "  JOIN [atu_univer].[dbo].[univer_personal] ON [univer_personal].personal_id = [univer_advicer].personal_id" +
                    "  JOIN [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] = [univer_advicer_student_link].[student_id]" +
                    "   where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                    countName = countName +" "+ rs.getString("fio") + "\n"+EmojiParser.parseToUnicode(":e-mail: ") + rs.getString("personal_email")
                            + EmojiParser.parseToUnicode("\n:telephone_receiver: ") + rs.getString("personal_mobile_phone")
                    +  EmojiParser.parseToUnicode("\n:phone: ")+ rs.getString("personal_home_phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  countName;
    }

    public static String[][] getSubject(String IIN) throws IOException, ClassNotFoundException, SQLException {
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = "SELECT DISTINCT [subject_name_ru]" +
                    "   ,[univer_educ_plan_pos].subject_id" +
                    "  FROM [atu_univer].[dbo].[univer_group]" +
                    "  JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].group_id = [univer_group].group_id" +
                    "  JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].educ_plan_pos_id = [univer_group].educ_plan_pos_id" +
                    "  JOIN [atu_univer].[dbo].[univer_subject] ON [univer_subject].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  JOIN univer_students ON univer_students.students_id = [univer_group_student].student_id" +
                    "  JOIN [atu_univer].[dbo].[univer_teacher_file] ON [univer_teacher_file].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [educ_plan_pos_semestr] = '"+semestr+"' ORDER BY [subject_name_ru]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }

            return result;

        }
    }
    public static String[][] getTeachers(String IIN, String SubjectId) throws IOException, ClassNotFoundException, SQLException {
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = "SELECT DISTINCT" +
                    "       CONCAT([personal_sname] , ' '" +
                    "      ,[personal_name], ' '" +
                    "      ,[personal_father_name]) as fio" +
                    "      , [univer_group].teacher_id" +
                    "      ,[univer_educ_plan_pos].subject_id" +
                    "  FROM [atu_univer].[dbo].[univer_group]" +
                    "  JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].group_id = [univer_group].group_id" +
                    "  JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].educ_plan_pos_id = [univer_group].educ_plan_pos_id" +
                    "  JOIN [atu_univer].[dbo].[univer_subject] ON [univer_subject].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  JOIN univer_students ON univer_students.students_id = [univer_group_student].student_id" +
                    "  JOIN [atu_univer].[dbo].[univer_teacher_file] ON [univer_teacher_file].teacher_id = [univer_group].teacher_id" +
                    "  JOIN [univer_teacher] ON [univer_teacher].teacher_id = [univer_teacher_file].teacher_id" +
                    "  JOIN [univer_personal] ON [univer_personal].personal_id = [univer_teacher].personal_id" +
                    "  where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [educ_plan_pos_semestr] = '"+semestr+"' and  [univer_teacher_file].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  and [univer_educ_plan_pos].subject_id = '"+SubjectId+"' ORDER BY [univer_group].teacher_id";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }

            return result;

        }
    }
    public static String[][] getFiles(String TeacherId, String SubjectId) throws IOException, ClassNotFoundException, SQLException {
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT [teacher_file_title]" +
                    "      ,[teacher_file_name]" +
                    "       ,[teacher_id]"+
                    "  FROM [atu_univer].[dbo].[univer_teacher_file] where [teacher_id] = '"+TeacherId+"' and [subject_id] = '"+SubjectId+"' ";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }

            return result;

        }
    }

    public static Double getGPA(String IIN){
        int creditSum = 0;
        double result = 0 , summa = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = " SELECT " +
                    "      ,[univer_progress].[progress_credit]" +
                    "      ,[univer_mark_type].[mark_type_gpa]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1" +
                    " ORDER BY  [univer_progress].[academ_year]";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    int credit = Integer.parseInt(rs.getString("progress_credit"));
                    creditSum = creditSum + credit;
                    summa = summa + Double.parseDouble(rs.getString("mark_type_gpa"))*credit;
                }
                result = summa / creditSum;
            } else {
                result = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

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
