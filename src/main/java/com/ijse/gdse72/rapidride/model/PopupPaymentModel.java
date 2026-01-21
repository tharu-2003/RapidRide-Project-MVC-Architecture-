package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.db.DBConnection;
import com.ijse.gdse72.rapidride.dto.BookingDetailsDto;
import com.ijse.gdse72.rapidride.dto.BookingDto;
import com.ijse.gdse72.rapidride.dto.BookingSaveDto;
import com.ijse.gdse72.rapidride.dto.PaymentDto;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PopupPaymentModel {

    public boolean placeBooking(BookingSaveDto bookingSaveDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            boolean isBookingSave = true;
            String bookingSaveSql = "INSERT INTO Booking VALUES (?,?,?,?,?,?,?,?)";

            // Check if BookingDtos is not null before proceeding
            if (bookingSaveDto.getBookingDtos() != null) {

                for (BookingDto bookingDto : bookingSaveDto.getBookingDtos()) { //boking godak awoth

                    PreparedStatement bookingPrepareStatement = connection.prepareStatement(bookingSaveSql);

                    bookingPrepareStatement.setString(1, bookingDto.getBooking_id());
                    bookingPrepareStatement.setString(2, bookingDto.getRide_to());
                    bookingPrepareStatement.setDouble(3, bookingDto.getDistance());
                    bookingPrepareStatement.setDate(4, bookingDto.getRequired_date());
                    bookingPrepareStatement.setDouble(5, bookingDto.getTotal_amount());
                    bookingPrepareStatement.setString(6, bookingDto.getStatus());
                    bookingPrepareStatement.setInt(7, bookingDto.getVehicle_count());
                    bookingPrepareStatement.setString(8, bookingDto.getCustomer_id());

                    if (!(bookingPrepareStatement.executeUpdate() > 0)) {
                        isBookingSave = false;
                    }
                }
            }

            if (isBookingSave) {
                boolean isBookingDetailsSaved = true;
                String bookingDetailsSql = "INSERT INTO Booking_Details VALUES (?,?,?,?)";

                // Check if BookingDetailsDtos is not null before proceeding
                if (bookingSaveDto.getBookingDetailsDtos() != null) {

                    for (BookingDetailsDto bookingDetailsDto : bookingSaveDto.getBookingDetailsDtos()) {

                        PreparedStatement bookingDetailsPreparedStatement = connection.prepareStatement(bookingDetailsSql);

                        bookingDetailsPreparedStatement.setDate(1, bookingDetailsDto.getBooking_date());
                        bookingDetailsPreparedStatement.setTime(2, bookingDetailsDto.getBooking_time());
                        bookingDetailsPreparedStatement.setString(3, bookingDetailsDto.getBooking_id());
                        bookingDetailsPreparedStatement.setString(4, bookingDetailsDto.getVehicle_id());

                        if (!(bookingDetailsPreparedStatement.executeUpdate() > 0)) {
                            isBookingDetailsSaved = false;
                        }
                    }
                }

                if (isBookingDetailsSaved) {
                    boolean isPaymentSaved = true;
                    String paymentDetailSql = "INSERT INTO Payment VALUES (?,?,?,?,?,?,?)";

                    if (bookingSaveDto.getPaymentDtos() != null) {

                        for (PaymentDto paymentDto : bookingSaveDto.getPaymentDtos()) {

                            PreparedStatement paymentPreparedStatement = connection.prepareStatement(paymentDetailSql);

                            paymentPreparedStatement.setString(1, paymentDto.getPayment_id());
                            paymentPreparedStatement.setString(2, paymentDto.getBooking_id());
                            paymentPreparedStatement.setDouble(3, paymentDto.getAmount()); // Corrected field
                            paymentPreparedStatement.setString(4, paymentDto.getPayment_method());
                            paymentPreparedStatement.setString(5, String.valueOf(paymentDto.getDate()));
                            paymentPreparedStatement.setString(6, String.valueOf(paymentDto.getTime()));
                            paymentPreparedStatement.setString(7, paymentDto.getStatus());

                            if (!(paymentPreparedStatement.executeUpdate() > 0)) {
                                isPaymentSaved = false;
                            }
                        }
                    }

                    if (isPaymentSaved) {
                        boolean isVehicleUse = true;
                        String vehicleUsedSql = "UPDATE Vehicle SET status = ? WHERE vehicle_id = ?"; // Corrected typo "WHWRE" to "WHERE"

                        // Check if BookingDetailsDtos is not null before proceeding
                        if (bookingSaveDto.getBookingDetailsDtos() != null) {

                            for (BookingDetailsDto bookingDetailsDto : bookingSaveDto.getBookingDetailsDtos()) {

                                PreparedStatement preparedStatementVehicle = connection.prepareStatement(vehicleUsedSql);

                                preparedStatementVehicle.setString(1, "in-use");
                                preparedStatementVehicle.setString(2, bookingDetailsDto.getVehicle_id());

                                if (!(preparedStatementVehicle.executeUpdate() > 0)) {
                                    isVehicleUse = false;
                                }
                            }
                        }

                        if (isVehicleUse) {
                            connection.commit();
                            new Alert(Alert.AlertType.CONFIRMATION, "Successfully added booking!").showAndWait();
                            return true;
                        } else {
                            connection.rollback();
                            new Alert(Alert.AlertType.ERROR, "Vehicle update error!").showAndWait();
                        }

                    } else {
                        connection.rollback();
                        new Alert(Alert.AlertType.ERROR, "Payment saving error!").showAndWait();
                    }
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Booking detail saving error!").showAndWait();
                }
            } else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Booking save error!").showAndWait();
                return false;
            }

        } catch (Exception e) {
            connection.rollback();

            new Alert(Alert.AlertType.ERROR, "Something went wrong: " + e.getMessage()).showAndWait();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
        return false;
    }
}