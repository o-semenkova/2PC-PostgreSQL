package com.tm;

import java.sql.SQLException;
import javax.sql.DataSource;

public class BookingServiceApp {
  public static void main(String[]args) throws SQLException {
    DataSource bookingDs = new BookingDataSource();
    Coordinator coordinator = new Coordinator(bookingDs);
    coordinator.bookTravel();
  }
}
