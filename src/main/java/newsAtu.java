import java.sql.*;

public class newsAtu {
    private static String result = "";
    private static String userName = "atukz_ruslan";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:mysql://srv-pleskdb16.ps.kz:3306/atukz1_netfox";
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
