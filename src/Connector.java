import java.sql.*;
import java.util.*;

public class Connector {
    static final String DB_URL = "jdbc:mysql://localhost:3306/herogame";
    static final String USER = "root";
    static final String PASS = "";
    static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnector() {
        Connection connection = null;
        try {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean insert(String name, int score, java.sql.Timestamp datetime) {
        String query = "INSERT INTO games (name, score, date) VALUES (?, ?, ?)";
        try (Connection connection = getConnector();
                PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, score);
            ps.setTimestamp(3, datetime);

            int rowInsert = ps.executeUpdate();
            return rowInsert > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Leaderboard> scoreRecords() {
        String query = "SELECT * FROM games ORDER BY score DESC LIMIT 10";
        ArrayList<Leaderboard> leaderboards = new ArrayList<>();

        try (Connection connection = getConnector();
                Statement stmt = connection.createStatement();) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                leaderboards.add(new Leaderboard(rs.getString("name"), rs.getTimestamp("date"), rs.getInt("score")));
            }

        } catch (

        SQLException e) {
            e.printStackTrace();
        }
        return leaderboards;
    }
}
