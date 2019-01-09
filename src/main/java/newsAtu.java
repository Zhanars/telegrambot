import firstmenu.Configuration;

import java.sql.*;

public class newsAtu {
    private static String result = "";
    private static String userName = Configuration.getMySQLUsername();
    private static String password = Configuration.getPass();
    private static String connectUrl = Configuration.getMySQLHost();
    public static void inserChatId(Long ChatId){

        try  ( Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {

            String SelectSql = "SELECT * FROM ChatIDs  WHERE ChatId = '" + ChatId + "'";
            ResultSet rs = stmt.executeQuery(SelectSql);
            if (!rs.next()) {
                String InsertSql = "INSERT INTO ChatIDs (ChatId) VALUES ('" + ChatId + "') ";
                stmt.executeUpdate(InsertSql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
