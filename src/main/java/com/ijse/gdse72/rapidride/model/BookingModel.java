package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.BookingDto;
import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingModel {
    public ArrayList<BookingDto> getBookings() throws SQLException {
        String sql = "SELECT * FROM Booking";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<BookingDto> bookingDtos = new ArrayList<>();

        while(rst.next()){
            BookingDto bookingDto =new BookingDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getDate(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getInt(7),
                    rst.getString(8)
            );
            bookingDtos.add(bookingDto);
        }
        return bookingDtos;
    }

    public ArrayList<String> getAllCustomerId() throws SQLException {
        String sql = "SELECT customer_id FROM Booking";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<String> customerIds = new ArrayList<>();

        while (rst.next()){
            customerIds.add(rst.getString(1));
        }
        return customerIds;
    }

    public String getNextBookingId() throws SQLException {

        String sql = "SELECT booking_id FROM Booking ORDER BY booking_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if (rst.next()){

            String bookingId = rst.getString(1);
            String subbookingId = bookingId.substring(1);
            int lastIdIndex = Integer.parseInt(subbookingId);
            int nextIndex = lastIdIndex + 1;
            String newId = String.format("B%03d", nextIndex);
            return newId;
        }
        return "B001";
    }

    public BookingDto searchBookingId(String bookingId) throws SQLException {
        String sql = "SELECT * FROM Booking WHERE booking_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, bookingId);
        if (rst.next()) {
            return new BookingDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getDate(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getInt(7),
                    rst.getString(8)
            );
        }
        return null;
    }
    public CustomerDto searchCustomerId(String customerId) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE customer_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, customerId);
        if (rst.next()) {
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
}
