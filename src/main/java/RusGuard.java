import com.google.common.html.HtmlEscapers;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static String[][] getReportForMonth(String IIN) throws SQLException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.MONTH, -1);
        ArrayList<String> firstCol = new ArrayList<>();
        ArrayList<String> secondCol = new ArrayList<>();
        ArrayList<String> FirthCol = new ArrayList<>();
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = " ;with skudtbl as" +
                    "                     (SELECT CONVERT(date,[Log].[DateTime],106) as dateday, (datepart(weekday, [Log].[DateTime]) + @@datefirst - 2) % 7 + 1 as weekofday," +
                    "                     min (case" +
                    "                    when [Log].[LogMessageSubType] = 66 then CONVERT(CHAR(12), [Log].[DateTime],114) else '00:00:00' end) r1," +
                    "                    max (case" +
                    "                    when [Log].[LogMessageSubType]= 67 then CONVERT(CHAR(12), [Log].[DateTime],114) else '00:00:00' end) r2," +
                    "                    max (case " +
                    "                    when  [Log].[LogMessageSubType]= 69 then CONVERT(CHAR(12), [Log].[DateTime],114) else '00:00:00' end) r3," +
                    "                   [Log].[DateTime]" +
                    "                   ,[Log].[Message]" +
                    "                 ,[Log].[LogMessageSubType]" +
                    "                FROM [RusGuardDB].[dbo].[Log]" +
                    "                JOIN [RusGuardDB].[dbo].[Employee] ON [Employee].[_id] = [Log].[EmployeeID]" +
                    "                Where [Employee].[PassportNumber] LIKE '%" + IIN + "%' and [Log].[DateTime] > '"+date1+"' and ([Log].[LogMessageSubType] = 66" +
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
                    "                    else skudtbl.r2 end) inside," +
                    "                    max (case" +
                    "                    when skudtbl.[LogMessageSubType]= 67 then skudtbl.r2 " +
                    "                    when skudtbl.[LogMessageSubType] = 69 then skudtbl.r3 + skudtbl.[Message] " +
                    "                    else skudtbl.r1 end) outside" +
                    "                from skudtbl" +
                    "                GROUP BY skudtbl.dateday";
            ResultSet rs1 = stmt.executeQuery(SQL);
            if (rs1 != null){
                int rowCount = getRowCount(rs1);
                System.out.println(rowCount);
                int colCount = rs1.getMetaData().getColumnCount();
                System.out.println(colCount);
                rs1.beforeFirst();
                while (rs1.next()) {
                    firstCol.add(rs1.getString("weekofday"));
                    secondCol.add(rs1.getString("inside"));
                    FirthCol.add(rs1.getString("outside"));
                }
            }
            int colCount = firstCol.size();
            String[][] result = new String[colCount][3];
            for (int i=0; i<colCount; i++){
                result[i][0] = firstCol.get(i);
                result[i][1] = secondCol.get(i);
                result[i][2] = FirthCol.get(i);
            }
            return result;
        }

    }
    private static int getRowCount(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }
        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        }
        return 0;
    }

}