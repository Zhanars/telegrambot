import com.google.common.html.HtmlEscapers;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RusGuard {


    public static String getCount(String message) throws IOException, ClassNotFoundException, SQLException {
            String countName = "";
        String userName = "joker";
        String password = "Desant3205363";
        String connectUrl = "jdbc:sqlserver://185.97.115.131\\RUSGUARD:49181;database=RusGuardDB";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("gvjhgjkghkj");
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        System.out.println(c.getTime());
        System.out.println(date2);
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        System.out.println(date1);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT" +
                    "[Log].[DateTime] as dt, [Log].[Message] as mess  FROM [RusGuardDB].[dbo].[Employee]" +
                    "JOIN [RusGuardDB].[dbo].[Log] ON [Log].[EmployeeID] = [Employee].[_id]  WHERE ([Log].[DateTime] >= '"+date1+"') and [Employee].[PassportNumber] LIKE '%"+message+"%'";
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(rs);
            int columns = rs.getMetaData().getColumnCount();
            String HTMLString = "``` ";
            HTMLString = HTMLString+"| Дата и время|Вход/Выход|\n" +
                    " | ----------- | -------- |\n";
            if (rs!=null) {
                while (rs.next()) {
                    String mess = rs.getString("mess");
                    while (mess.length()<9){
                        mess = mess.concat(" ");
                    }
                    String dt = rs.getString("dt");
                    dt = dt.substring(5,16);
                    HTMLString = HTMLString + " | " + dt;
                    HTMLString = HTMLString + " | " + mess;
                    HTMLString = HTMLString + "|\n";
                }
            }else {
                countName = "123456";
            }
            HTMLString = HTMLString +"```";
            System.out.println(rs);
            System.out.println(countName);
            System.out.println(HTMLString);
            return HTMLString;
        }
    }
}
