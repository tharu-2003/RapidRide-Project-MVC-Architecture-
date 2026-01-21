package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.BookingDetailsDto;
import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.tm.BookingDetailsTm;
import com.ijse.gdse72.rapidride.model.BookingDetailsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;


import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookingDetaisPaneController implements Initializable {

    @FXML
    private TableView<BookingDetailsTm> bookingDetailsTable;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableColumn<?, ?> clmBookingDate;

    @FXML
    private TableColumn<?, ?> clmBookingId;

    @FXML
    private TableColumn<?, ?> clmBookingTime;

    @FXML
    private TableColumn<?, ?> clmVehicleId;

    @FXML
    private TextField txtSearch;

    @FXML
    private ImageView imgSearch;

    @FXML
    private Label lblBookingDate;

    @FXML
    private Label lblBookingId;

    @FXML
    private Label lblBookingTime;

    @FXML
    private Label lblVehicleId;

    private final BookingDetailsModel bookingDetailsModel = new BookingDetailsModel();

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearPage();
        loadData();
        visibleData();
    }

    void visibleData(){
        clmBookingDate.setCellValueFactory(new PropertyValueFactory<>("booking_date"));
        clmBookingTime.setCellValueFactory(new PropertyValueFactory<>("booking_time"));
        clmBookingId.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        clmVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
    }

    private void loadData(){
        try {
            ArrayList<BookingDetailsDto> bookingDetailsDtos = bookingDetailsModel.getBookingDetails();
            ObservableList<BookingDetailsTm> bookingDetailsTms = FXCollections.observableArrayList();

            for(BookingDetailsDto bookingDetailsDto : bookingDetailsDtos){
                BookingDetailsTm bookingDetailsTm = new BookingDetailsTm(
                        bookingDetailsDto.getBooking_date(),
                        bookingDetailsDto.getBooking_time(),
                        bookingDetailsDto.getBooking_id(),
                        bookingDetailsDto.getVehicle_id()
                );
                bookingDetailsTms.add(bookingDetailsTm);
            }
            bookingDetailsTable.setItems(bookingDetailsTms);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPage(){
        txtSearch.setText("");
        lblBookingDate.setText(LocalDate.now().toString());
        lblBookingTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        lblBookingId.setText("");
        lblVehicleId.setText("");
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        clearPage();
    }

    @FXML
    void clickOnAction(MouseEvent event) {
        BookingDetailsTm selectedBookingDetail = bookingDetailsTable.getSelectionModel().getSelectedItem();

        if(selectedBookingDetail != null){
            lblBookingDate.setText(String.valueOf(selectedBookingDetail.getBooking_date()));
            lblBookingTime.setText(String.valueOf(selectedBookingDetail.getBooking_time()));
            lblBookingId.setText(selectedBookingDetail.getBooking_id());
            lblVehicleId.setText(selectedBookingDetail.getVehicle_id());
        }
    }

    @FXML
    void searchOnAction(MouseEvent event) {
        searchBookingDetails();
    }

    @FXML
    void searchOnAction2(ActionEvent event) {
        searchBookingDetails();
    }

    void searchBookingDetails(){
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d","");

        if(check.equals("B")){
            String bookingId = search;

            if (bookingId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Booking ID or Date to search!");
                return;
            }

            try {
                BookingDetailsDto bookingDetailsDto = bookingDetailsModel.searchBookingId(bookingId);
                if (bookingDetailsDto != null) {
                    lblBookingId.setText(bookingDetailsDto.getBooking_id());
                    lblVehicleId.setText(bookingDetailsDto.getVehicle_id());
                    lblBookingDate.setText(String.valueOf(bookingDetailsDto.getBooking_date()));
                    lblBookingTime.setText(String.valueOf(bookingDetailsDto.getBooking_time()));
                } else {
                    showAlert(Alert.AlertType.WARNING, "Booking Details Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }else {
            Date date = Date.valueOf(search);

//            if (date.isEmpty()) {
//                showAlert(Alert.AlertType.WARNING, "Please enter an Booking ID or Date to search!");
//                return;
//            }

            try {
                BookingDetailsDto bookingDetailsDto = bookingDetailsModel.searchBookingDate(date);
                if (bookingDetailsDto != null) {
                    lblBookingId.setText(bookingDetailsDto.getBooking_id());
                    lblVehicleId.setText(bookingDetailsDto.getVehicle_id());
                    lblBookingDate.setText(String.valueOf(bookingDetailsDto.getBooking_date()));
                    lblBookingTime.setText(String.valueOf(bookingDetailsDto.getBooking_time()));
                } else {
                    showAlert(Alert.AlertType.WARNING, "Booking Details Not Found! Date");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }
    }
}
