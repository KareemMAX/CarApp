import java.sql.*;

/**
 * Database Singleton class
 *
 * Provides a single connection to the current database for other components to use.
 */
public class Database {
    private static Database instance = null;

    /**
     * Gets the singleton instance of the class
     * @return The active instance of the class
     */
    public static Database getInstance() {
        if (instance == null)
            instance = new Database();

        return instance;
    }

    private Connection connection;

    private Database() {
        String connectionUrl = "jdbc:sqlserver://localhost;databaseName=car_app;user=car;password=1234567890";

        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a query to the database that makes updates, such as {@code CREATE}, {@code UPDATE}, {@code INSERT},
     * {@code DELETE}. The function will return {@code true} if updated and {@code false} if any error has happened
     * during execution, also a stacktrace is printed if any error occurs.
     * @param sqlQuery The SQL query to be executed.
     * @return {@code true} if the execution happened successfully, {@code false} otherwise.
     */
    public boolean update(String sqlQuery) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
            return true;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    /**
     *  Makes a query to the database that makes queries, such as {@code SELECT}. The function will return a
     *  {@link ResultSet} if the query is successful and {@code null} if any error has happened during execution,
     *  also a stacktrace is printed if any error occurs.
     *  <p>
     *  Please note that {@link ResultSet} need to be closed after usage.
     *
     * @param sqlQuery The SQL query to be queried.
     * @return a {@link ResultSet} if the query executed successfully, {@code null} otherwise.
     */
    public ResultSet query(String sqlQuery) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return null;
    }
}
