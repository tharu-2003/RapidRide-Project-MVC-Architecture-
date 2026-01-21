package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.BookingDto;
import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.PaymentDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {
    public ArrayList<PaymentDto> getPayments() throws SQLException {
        String sql = "SELECT * FROM Payment";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<PaymentDto> paymentDtos = new ArrayList<>();

        while (rst.next()){
            PaymentDto paymentDto = new PaymentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getTime(6),
                    rst.getString(7)
            );
            paymentDtos.add(paymentDto);
        }
        return paymentDtos;
    }

    public PaymentDto searchBookingId(String bookingId) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE booking_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, bookingId);
        if (rst.next()) {
            return new PaymentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getTime(6),
                    rst.getString(7)
            );
        }
        return null;
    }
    public PaymentDto searchPaymentId(String paymentId) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE payment_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, paymentId);
        if (rst.next()) {
            return new PaymentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getTime(6),
                    rst.getString(7)
            );
        }
        return null;
    }

    public String getNextPaymentId() throws SQLException {

        String sql = "SELECT payment_id FROM Payment ORDER BY payment_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if (rst.next()){

            String paymentId = rst.getString(1);
            String subPaymentId = paymentId.substring(1);
            int lastIdIndex = Integer.parseInt(subPaymentId);
            int nextIndex = lastIdIndex + 1;
            String newId = String.format("P%03d", nextIndex);
            return newId;
        }
        return "P001";
    }
}
