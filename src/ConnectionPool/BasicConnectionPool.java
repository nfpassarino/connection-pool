package ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool {
	private final static int INITIAL_SIZE = 10;
	private final static int MAX_SIZE = 10;
	private final static int TIME_OUT = 1000;
	private final static int MAX_RETRIES = 3;
	private String url;
	private String user;
	private String password;
	private List<ConnectionWrapper> connectionPool;

	private BasicConnectionPool(String url, String user, String password, List<ConnectionWrapper> initialPool)
			throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		this.connectionPool.addAll(initialPool);
	}

	public static BasicConnectionPool create(String url, String user, String password) throws SQLException {
		List<ConnectionWrapper> initialPool = new ArrayList<>();
		for (int i = 0; i < INITIAL_SIZE; i++) {
			initialPool.add(createConnection(url, user, password));
		}
		return new BasicConnectionPool(url, user, password, initialPool);
	}

	@Override
	public Connection getConnection() throws SQLException, InterruptedException {
		for (int i = 0; i < MAX_RETRIES; i++) {
			for (ConnectionWrapper connection : connectionPool) {
				if (connection.isAvailable()) {
					connection.setAvailable(false);
					return connection;
				}
			}
			if (connectionPool.size() < MAX_SIZE) {
				connectionPool.add(createConnection(url, user, password));
			} else {
				Thread.sleep(TIME_OUT);
			}
		}
		throw new SQLException("No connections are available");
	}

	private static ConnectionWrapper createConnection(String url, String user, String password) throws SQLException {
		return new ConnectionWrapper(DriverManager.getConnection(url, user, password));
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int getSize() {
		return connectionPool.size();
	}
}
