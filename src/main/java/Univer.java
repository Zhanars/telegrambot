import java.io.IOException;
import java.sql.*;

public class Univer {
    private static String countName = "";
    private static String userName = "sa";
    private static String password = "Desant3205363";
    private static String connectUrl = "jdbc:sqlserver://185.97.115.134\\MSSQLSERVER;database=atu_univer";
    public static String IIN(String message) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("gvjhgjkghkj");
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            countName = "";
            String SQL = "SELECT [students_sname], [students_name] FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+message+"%'";
            ResultSet rs = stmt.executeQuery(SQL);
                while (rs.next()) {
                    countName = countName + rs.getString("students_sname");
                }
                return countName;
        }


    }
    public static  Boolean checkIIN(String IIN){
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+IIN+"%'";
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
    public static String getname(String message) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("gvjhgjkghkj");
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT [students_name] FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+message+"%'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("students_name");
            }
            return countName;
        }


    }
    public static String getlastname(String message) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("gvjhgjkghkj");
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT [students_sname] FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%"+message+"%'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("students_sname");
            }
            return countName;
        }


    }

}
