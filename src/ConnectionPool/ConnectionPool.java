package ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
	/**
	 * Returns an available connection from the connection pool.
	 * 
	 * @return an available connection
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public Connection getConnection() throws SQLException, InterruptedException;

	/**
	 * Returns the url that was configured in the connection pool.
	 * 
	 * @return url
	 */
	public String getUrl();

	/**
	 * Returns the user that was configured in the connection pool.
	 * 
	 * @return user
	 */
	public String getUser();

	/**
	 * Returns the password that was configured in the connection pool.
	 * 
	 * @return password
	 */
	public String getPassword();

	/**
	 * Returns the maximum number of connections allowed by the connection pool.
	 * 
	 * @return maximum number of connections
	 */
	public int getSize();
}
