package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.BookingDetailsDto;
import com.ijse.gdse72.rapidride.dto.DriverDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class BookingDetailsModel {
    public ArrayList<BookingDetailsDto> getBookingDetails() throws SQLException {
        String sql = "SELECT * FROM Booking_Details";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<BookingDetailsDto> bookingDetailsDtos = new ArrayList<>();

        while (rst.next()){
            BookingDetailsDto bookingDetailsDto = new BookingDetailsDto(
                    rst.getDate(1),
                    rst.getTime(2),
                    rst.getString(3),
                    rst.getString(4)
            );
            bookingDetailsDtos.add(bookingDetailsDto);
        }
        return bookingDetailsDtos;
    }

    public BookingDetailsDto searchBookingId(String bookingId) throws SQLException {
        String sql = "SELECT * FROM Booking_Details WHERE booking_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, bookingId);

        if (rst.next()) {
            return new BookingDetailsDto(
                    rst.getDate(1),
                    rst.getTime(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        }
        return null;
    }
    public BookingDetailsDto searchBookingDate(Date date) throws SQLException {
        String sql = "SELECT * FROM Booking_Details WHERE booking_date = ? ";

        ResultSet rst = CrudUtil.execute(sql, date);

        if (rst.next()) {
            return new BookingDetailsDto(
                    rst.getDate(1),
                    rst.getTime(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        }
        return null;
    }
}
