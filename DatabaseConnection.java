import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The {@code DatabaseConnection} class provides a utility method to establish a connection to the
 * MySQL database used by the PoisePMS system.
 *
 * <p>This class centralizes the logic for creating a database connection, allowing other classes
 * within the system to reuse it by calling {@link #getConnection()}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * try (Connection conn = DatabaseConnection.getConnection()) {
 *     // Your database operations go here
 * } catch (SQLException e) {
 *     e.printStackTrace();
 * }
 * }
 * </pre>
 *
 * @author
 * @version 1.0
 * @since 2024
 */
public class DatabaseConnection {

    /**
     * The URL for connecting to the MySQL database. It specifies the database location and the database name.
     * Change or update this to suit your own database connection.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/PoisePMS";

    /**
     * The username for the MySQL database connection.
     * Change or update to suit your own user details.
     */
    private static final String USER = "otheruser";

    /**
     * The password for the MySQL database connection.
     * Change or update to suit your own database password.
     */
    private static final String PASSWORD = "swordfish";

    /**
     * Establishes and returns a connection to the MySQL database using the defined URL, username, and password.
     *
     * <p>This method will create a new connection every time it is called. Ensure that the connection is properly
     * closed after use to avoid potential resource leaks.</p>
     *
     * @return A {@link Connection} object representing the established connection to the database.
     * @throws SQLException If a database access error occurs or the URL, username, or password is incorrect.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}