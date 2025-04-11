package news_recommend.news.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class    ConnectionUtil {
    public static final String URL = "jdbc:postgresql://dbclass.wisoft.io:10008/geonsang_banking";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        }catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
