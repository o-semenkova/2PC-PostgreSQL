package com.tm;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class BookingDataSource implements DataSource {
  private static final Logger LOGGER = Logger.getLogger(BookingDataSource.class.getName());
  private Connection connection;

  @Override
  public Connection getConnection() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    if (connection == null) {
      String url = "jdbc:postgresql://localhost:8080/postgres";
      String user = "postgres";
      String password = "atomikos";
      try {
        connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Connection to postgres database cannot be provided.", ex);
      }
    }
    return connection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return null;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {

  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {

  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return 0;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }
}
