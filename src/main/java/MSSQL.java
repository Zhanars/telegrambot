import java.io.IOException;
import java.sql.*;

public class MSSQL {


    public static String getCount(String message) throws IOException, ClassNotFoundException, SQLException {
        String countName = "";
        String userName = "joker";
        String password = "Desant3205363";
        String connectUrl = "jdbc:sqlserver://185.97.115.127\\RUSGUARD;database=RusGuardDB";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT [SecondName] FROM [RusGuardDB].[dbo].[Employee] WHERE [FirstName] LIKE '%"+message+"%'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = countName + rs.getString("SecondName");
            }
            return countName;
        }


    }
}
