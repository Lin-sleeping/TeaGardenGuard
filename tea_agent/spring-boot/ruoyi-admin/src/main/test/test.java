import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionTest {

    // 修改为你的数据库配置
    private static final String URL = "jdbc:mysql://localhost:3306/wumeismart?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "021026";

    public static void main(String[] args) {
        testConnection();
    }

    public static void testConnection() {
        System.out.println("Testing JDBC connection to MySQL...");

        try {
            // 1. 加载驱动 (JDBC 4.0+ 可以自动加载，这行可省略)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 获取连接
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // 3. 测试连接
            if (connection != null) {
                System.out.println("Connection successful!");
                System.out.println("Database product: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Database version: " + connection.getMetaData().getDatabaseProductVersion());

                // 4. 关闭连接
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
    }
}