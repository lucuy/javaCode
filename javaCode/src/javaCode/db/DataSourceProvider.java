package javaCode.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javaCode.GeneratorProperties;

import javax.sql.DataSource;

/**
 * 数据源
 * */
public class DataSourceProvider {
	private static Connection connection;
	private static DataSource dataSource;
	
	public synchronized static Connection getConnection() {
		try {
			if(connection == null || connection.isClosed()) {
				connection = getDataSource().getConnection();
			}
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getJdbcUrl(){
		return GeneratorProperties.getProperty("jdbc.url");
	}
	
	private static String getUsername(){
		return GeneratorProperties.getProperty("jdbc.username");
	}
	
	private static String getPassword(){
		return GeneratorProperties.getProperty("jdbc.password");
	}
	
	private static String getDriver(){
		return GeneratorProperties.getProperty("jdbc.driver");
	}
	
	private static DataSource getDataSource() {
		if(dataSource == null) {
			dataSource = new DriverManagerDataSource(getJdbcUrl(),getUsername(),getPassword(),getDriver());
		}
		
		return dataSource;
	}
	
	/**
	 * 自定义数据源类，用于重用Connection
	 * */
	private static class DriverManagerDataSource implements DataSource {
		private String url;
		private String username;
		private String password;
		private String driverClass;
		
		private static void loadJdbcDriver(String driver) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("not found jdbc driver class:["+driver+"]",e);
			}
		}
		
		public DriverManagerDataSource(String url, String username,String password, String driverClass) {
			this.url = url;
			this.username = username;
			this.password = password;
			this.driverClass = driverClass;
			loadJdbcDriver(this.driverClass);
		}

		@Override
		public Connection getConnection() throws SQLException {
			return DriverManager.getConnection(url, username, password);
		}
		
		@Override
		public Connection getConnection(String username, String password) throws SQLException {
			return DriverManager.getConnection(url, username, password);
		}
		
		@Override
		public PrintWriter getLogWriter() throws SQLException {
			throw new UnsupportedOperationException("getLogWriter");
		}

		@Override
		public void setLogWriter(PrintWriter out) throws SQLException {
			throw new UnsupportedOperationException("getLogWriter");
		}

		@Override
		public void setLoginTimeout(int seconds) throws SQLException {
			throw new UnsupportedOperationException("getLogWriter");
		}

		@Override
		public int getLoginTimeout() throws SQLException {
			throw new UnsupportedOperationException("getLogWriter");
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return null;
		}
		
		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return false;
		}

		@SuppressWarnings("unused")
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			return null;
		}
	}
}
