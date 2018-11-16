import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MSSQL {


    public static String getCount(String message) throws IOException, ClassNotFoundException, SQLException {
            String countName = "";
        String userName = "joker";
        String password = "Desant3205363";
        String connectUrl = "jdbc:sqlserver://185.97.115.127\\RUSGUARD;database=RusGuardDB";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("gvjhgjkghkj");
        String date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT [Employee].[SecondName], [Employee].[FirstName], [Employee].[LastName], [Log].[DateTime], [Log].[Message]  FROM [RusGuardDB].[dbo].[Employee]" +
                    "JOIN [RusGuardDB].[dbo].[Log] ON [Log].[EmployeeID] = [Employee].[_id]  WHERE [Log].[DateTime] > '"+date+"' and [Employee].[PassportNumber] LIKE '%"+message+"%'";
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(rs);
            int columns = rs.getMetaData().getColumnCount();
            if (rs!=null) {
                while (rs.next()) {
                    for (int i = 1; i<= columns; i++){
                    countName = countName + rs.getString(i)+ "\n";
                    }
                    countName = countName + "\n";
                }
            }else {
                countName = "123456";
            }
            System.out.println(rs);
            System.out.println(countName);
            return countName;
        }


    }
}
