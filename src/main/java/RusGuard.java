import com.google.common.html.HtmlEscapers;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RusGuard {
    private static String userName = "joker";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:sqlserver://185.97.115.131\\RUSGUARD:49181;database=RusGuardDB";


    public static String getCount(String message) throws IOException, ClassNotFoundException, SQLException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT" +
                    "[Log].[DateTime] as dt, [Log].[Message] as mess  FROM [RusGuardDB].[dbo].[Employee]" +
                    "JOIN [RusGuardDB].[dbo].[Log] ON [Log].[EmployeeID] = [Employee].[_id]  WHERE ([Log].[DateTime] >= '" + date1 + "') and [Employee].[PassportNumber] LIKE '%" + message + "%'";
            ResultSet rs = stmt.executeQuery(SQL);
            String HTMLString = "``` ";
            HTMLString = HTMLString + "| Дата и время|Вход/Выход|\n" +
                    " | ----------- | -------- |\n";
            if (rs != null) {
                while (rs.next()) {
                    String mess = rs.getString("mess");
                    while (mess.length() < 9) {
                        mess = mess.concat(" ");
                    }
                    String dt = rs.getString("dt");
                    dt = dt.substring(5, 16);
                    HTMLString = HTMLString + " | " + dt;
                    HTMLString = HTMLString + " | " + mess;
                    HTMLString = HTMLString + "|\n";
                }
            } else {
                HTMLString = "123456";
            }
            HTMLString = HTMLString + "```";
            return HTMLString;
        }
    }

    public static String[][] getReportForMonth(String IIN, int month) throws SQLException {
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, month);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        if (month<0) {
            c.add(Calendar.MONTH, 1);
            date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        }
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = " ;with skudtbl as" +
                    "                     (SELECT CONVERT(date,[Log].[DateTime],106) as dateday, (datepart(weekday, [Log].[DateTime]) + @@datefirst - 2) % 7 + 1 as weekofday," +
                    "                     min (case" +
                    "                    when [Log].[LogMessageSubType] = 66 then CONVERT(CHAR(12), [Log].[DateTime],114) else '23:23:23' end) r1," +
                    "                    max (case" +
                    "                    when [Log].[LogMessageSubType]= 67 then CONVERT(CHAR(12), [Log].[DateTime],114) else '00:00:00' end) r2," +
                    "                    max (case " +
                    "                    when  [Log].[LogMessageSubType]= 69 then CONVERT(CHAR(12), [Log].[DateTime],114) else '00:00:00' end) r3," +
                    "                   [Log].[DateTime]" +
                    "                   ,[Log].[Message]" +
                    "                 ,[Log].[LogMessageSubType]" +
                    "                FROM [RusGuardDB].[dbo].[Log]" +
                    "                JOIN [RusGuardDB].[dbo].[Employee] ON [Employee].[_id] = [Log].[EmployeeID]" +
                    "                Where [Employee].[PassportNumber] LIKE '%" + IIN + "%' and ([Log].[DateTime] >= '" + date1 + "' and [Log].[DateTime] < '" + date2 + "') and ([Log].[LogMessageSubType] = 66" +
                    "                or [Log].[LogMessageSubType]=67 or [Log].[LogMessageSubType]=69 )" +
                    "                GROUP BY [Log].[DateTime],[Log].[LogMessageSubType], [Log].[Message] " +
                    " )" +
                    "                Select DISTINCT skudtbl.dateday as calendardate," +
                    " max(case " +
                    "  when skudtbl.weekofday = 1 then 'Пн' " +
                    "    when skudtbl.weekofday = 2 then 'Вт' " +
                    "      when skudtbl.weekofday = 3 then 'Ср' " +
                    "         when skudtbl.weekofday = 4 then 'Чт' " +
                    "           when skudtbl.weekofday = 5 then 'Пт' " +
                    "             when skudtbl.weekofday = 6 then 'Сб' " +
                    "                when skudtbl.weekofday = 7 then 'Вс' " +
                    "                    else '' end) weekofday , " +
                    "                        min (case" +
                    "                    when skudtbl.[LogMessageSubType] = 66 then skudtbl.r1 " +
                    "                    when skudtbl.[LogMessageSubType] = 69 then skudtbl.r3 + skudtbl.[Message]" +
                    "                    else skudtbl.r1 end) inside," +
                    "                    max (case" +
                    "                    when skudtbl.[LogMessageSubType]= 67  then skudtbl.r2 " +
                    "                    when skudtbl.[LogMessageSubType] = 69 then skudtbl.r3 + skudtbl.[Message] " +
                    "                    else skudtbl.r2 end) outside" +
                    "                from skudtbl" +
                    "                GROUP BY skudtbl.dateday";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount + 1][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            result[0][0] = "Дата";
            result[0][1] = "День недели";
            result[0][2] = "Время входа";
            result[0][3] = "Время выхода";
            int j = 1;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    if (rs2.getString(i + 1).equals("23:23:23") || rs2.getString(i + 1).equals("00:00:00")) {
                        result[j][i] = "--:--:--";
                    } else {
                        result[j][i] = rs2.getString(i + 1);
                    }
                }
                j++;
            }
            return result;
        }

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