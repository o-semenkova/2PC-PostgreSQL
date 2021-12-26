package com.tm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

public class Coordinator {
  private DataSource bookingDataSource;

  public Coordinator(DataSource bookingDataSource) {
    this.bookingDataSource = bookingDataSource;
  }

  public void bookTravel() throws SQLException {

    ExecutorService executor = Executors.newFixedThreadPool(2);
    boolean rollback = false;
    Statement s1;
    Connection bookingCon = bookingDataSource.getConnection();
    s1 = bookingCon.createStatement();
    String q00 = "BEGIN";
    String q01 = "PREPARE TRANSACTION 'foobar';";
    String q02 = "COMMIT PREPARED 'foobar';";
    String q03 = "ROLLBACK PREPARED 'foobar';";
    s1.executeUpdate(q00);
    s1.executeUpdate(q01);
    try {
      executor.execute(() -> {
        String q1 = "insert into fly.fly_booking(booking_id, client_name, fly_number, departure, destination, booking_date) values ('123AT', "
                    + "'Adam', '23-KLM', 'Borispol-KBP', 'Schiphol', '2013-06-01');";
        String q11 = "update account.client_account set amount = amount - 20 where client_name = 'Adam';";
        try {
          s1.executeUpdate(q11);
          try {
            s1.executeUpdate(q1);
          } catch (SQLException e) {
            e.printStackTrace();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      executor.execute(() -> {

        String q2 = "insert into hotel.hotel_booking(booking_id, hotel_name, arrival, departure)values('67HO', 'Bristol', '2013-06-01', "
                    + "'2013-06-07');";
        String q22 = "update account.client_account set amount = amount - 10 where client_name = 'Adam';";
        try {
          s1.executeUpdate(q22);
          try {
            s1.executeUpdate(q2);
          } catch (SQLException e) {
            e.printStackTrace();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

    } catch (Exception e) {
      System.out.println(e.getMessage());
      rollback = true;
      s1.executeUpdate(q03);
    } finally {
      if (!rollback) {
        s1.executeUpdate(q02);
      } else {
        s1.executeUpdate(q03);
      }
    }
    s1.close();
    bookingCon.close();
    executor.shutdown();
    try {
      executor.awaitTermination(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
