package ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolTest {

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost/db";
		String user = "user";
		String password = "password";
		ConnectionPool connectionPool;
		try {
			connectionPool = BasicConnectionPool.create(url, user, password);
			List<Thread> threads = new ArrayList<>();
			for (int i = 0; i < 20; i++) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try (Connection connection = connectionPool.getConnection();) {
							Statement statement = connection.createStatement();
							ResultSet result = statement.executeQuery("SELECT COUNT(1) AS COUNT FROM table_a");
							while (result.next()) {
								System.out.println("Connection count: " + result.getObject("COUNT"));
							}
						} catch (SQLException | InterruptedException e) {
							System.err.println("Statement ERROR");
							e.printStackTrace();
						}
					}
				});
				threads.add(thread);
				thread.start();
			}
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (SQLException | InterruptedException e1) {
			System.err.println("Connection pool ERROR");
			e1.printStackTrace();
		}
	}

}
