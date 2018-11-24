import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MSSQL {


    public static String getCount(String message) throws IOException, ClassNotFoundException, SQLException {
            String countName = "";
        String userName = "joker";
        String password = "Desant3205363";
        String connectUrl = "jdbc:sqlserver://185.97.115.131\\RUSGUARD;database=RusGuardDB";
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
            String SQL = "SELECT [Employee].[SecondName], [Employee].[FirstName], [Employee].[LastName], [Log].[DateTime], [Log].[Message]  FROM [RusGuardDB].[dbo].[Employee]" +
                    "JOIN [RusGuardDB].[dbo].[Log] ON [Log].[EmployeeID] = [Employee].[_id]  WHERE ([Log].[DateTime] >= '"+date1+"') and [Employee].[PassportNumber] LIKE '%"+message+"%'";
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(rs);
            int columns = rs.getMetaData().getColumnCount();
            String HTMLString = "<pre>&lt;table&gt;";
            if (rs!=null) {
                while (rs.next()) {
                    for (int i = 1; i<= columns; i++){
                        HTMLString = HTMLString + "&lt;tr&gt;&lt;td&gt;"+rs.getString(i)+"&lt;/tr&gt;&lt;/td&gt;";
                    //countName = countName + rs.getString(i)+ "\n";
                    }
                    //countName = countName + "\n";
                }
            }else {
                countName = "123456";
            }
            HTMLString = "&lt;/table&gt;</pre>";
            System.out.println(rs);
            System.out.println(countName);
            return countName;
        }


    }
}
