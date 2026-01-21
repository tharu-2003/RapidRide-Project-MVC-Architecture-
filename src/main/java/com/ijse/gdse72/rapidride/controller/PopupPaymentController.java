package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.db.DBConnection;
import com.ijse.gdse72.rapidride.dto.BookingDetailsDto;
import com.ijse.gdse72.rapidride.dto.BookingDto;
import com.ijse.gdse72.rapidride.dto.BookingSaveDto;
import com.ijse.gdse72.rapidride.dto.PaymentDto;
import com.ijse.gdse72.rapidride.dto.tm.BookingDetailsTm;
import com.ijse.gdse72.rapidride.dto.tm.BookingTm;
import com.ijse.gdse72.rapidride.dto.tm.PaymentTm;
import com.ijse.gdse72.rapidride.dto.tm.PopupPaymentTm;
import com.ijse.gdse72.rapidride.model.PaymentModel;
import com.ijse.gdse72.rapidride.model.PopupPaymentModel;
import com.ijse.gdse72.rapidride.model.VehicleModel;
import com.ijse.gdse72.rapidride.service.BookingSinglton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopupPaymentController {

    @FXML
    private Button btnComform;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> clmAmount;

    @FXML
    private TableColumn<?, ?> clmBookingId;

    @FXML
    private TableColumn<?, ?> clmDate;

    @FXML
    private TableColumn<?, ?> clmPaymentId;

    @FXML
    private TableColumn<?, ?> clmPaymentMethod;

    @FXML
    private TableColumn<?, ?> clmStatus;

    @FXML
    private TableColumn<?, ?> clmTime;

    @FXML
    private TableView<PopupPaymentTm> paymentTable;

    @FXML
    private ComboBox<String> cmbPaymentMethod;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TextField txtAmount;

    @FXML
    private Label txtBookingId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblTime;

    private final BookingSinglton bookingSinglton = BookingSinglton.getInstance();
    private final PaymentModel paymentModel = new PaymentModel();
    private final VehicleModel vehicleModel = new VehicleModel();
    private final PopupPaymentModel popupPaymentModel = new PopupPaymentModel();

    private final ObservableList<PopupPaymentTm> popuppaymentTms = FXCollections.observableArrayList();
    private final ObservableList<PaymentTm> paymentTms = FXCollections.observableArrayList();
    private final ObservableList<BookingTm> bookingTms = FXCollections.observableArrayList();
    private final ObservableList<BookingDetailsTm> bookingDetailsTms = FXCollections.observableArrayList();

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    public void initialize(){
        bookingIDTxt.setVisible(false);
        btnBill.setVisible(false);

        String bookingId = bookingSinglton.getBookingId();
        txtBookingId.setText(bookingId);

        setCellValues();

        try {
            lblTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

            String nextPaymentID = paymentModel.getNextPaymentId();
            lblPaymentId.setText(nextPaymentID);

            loadStatus();   //combo box
            loadPaymentMethod();  //combo box

            lblDate.setText(LocalDate.now().toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadPaymentMethod() {

        ObservableList<String> observableList = FXCollections.observableArrayList("Cash" , "Card" , "Bank transfer");
        cmbPaymentMethod.setItems(observableList);
    }

    private void loadStatus() {

        ObservableList<String> observableList = FXCollections.observableArrayList("haif-paid" , "full-paid");
        cmbStatus.setItems(observableList);
    }

    private void setCellValues() {
        clmPaymentId.setCellValueFactory(new PropertyValueFactory<>("payment_id"));
        clmBookingId.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        clmPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        clmStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        paymentTable.setItems(popuppaymentTms);
    }

    @FXML
    void clickOnAction(MouseEvent event) {

    }

    @FXML
    void comformOnAction(ActionEvent event) throws SQLException, JRException {

        //Transaction Ek Ghapan
        String bookingId = bookingSinglton.getBookingId();
        String customerId = bookingSinglton.getCustomerId();
        String isPendingstatus = bookingSinglton.getStatus();
        double bookingAmount = bookingSinglton.getAmount();
        String rideTo = bookingSinglton.getRideTo();
        double distance = bookingSinglton.getDistance();
        Date date = bookingSinglton.getDate();
        int vehicleCount = bookingSinglton.getVehicleCount();

        String vehicleId = vehicleModel.getVehicleId();

        String paymentId = lblPaymentId.getText();
        double payAmount = Double.parseDouble(txtAmount.getText());
        String paymentMethod = cmbPaymentMethod.getValue();
        Time time = Time.valueOf(lblTime.getText());
        String PayedStatus = cmbStatus.getValue();

        System.out.println(bookingId + " | " + customerId + " | " + isPendingstatus + " | " + bookingAmount + " | " + rideTo + " | " + distance + " | " + date + " | " + vehicleCount + " | " + vehicleId);

        ArrayList<PaymentDto> paymentDtos = new ArrayList<>();
        ArrayList<BookingDto> bookingDtos = new ArrayList<>();
        ArrayList<BookingDetailsDto> bookingDetailsDtos = new ArrayList<>();

     //   for(PaymentTm paymentTm : paymentTms){
            PaymentDto paymentDto = new PaymentDto(
                    paymentId,
                    bookingId,
                    payAmount,
                    paymentMethod,
                    date,
                    time,
                    PayedStatus
            );
            paymentDtos.add(paymentDto);
        //}
        BookingDto bookingDto = new BookingDto(
                bookingId,
                rideTo,
                distance,
                date,
                bookingAmount,
                isPendingstatus,
                vehicleCount,
                customerId
        );
        bookingDtos.add(bookingDto);

        BookingDetailsDto bookingDetailsDto = new BookingDetailsDto(
                date,
                time,
                bookingId,
                vehicleId
        );
        bookingDetailsDtos.add(bookingDetailsDto);

        BookingSaveDto bookingSaveDto = new BookingSaveDto(
                vehicleId,

                paymentDtos,
                bookingDtos,
                bookingDetailsDtos
        );
        boolean isSaved = popupPaymentModel.placeBooking(bookingSaveDto);

        if(isSaved){
            new Alert(Alert.AlertType.CONFIRMATION,"Done ");
            genarateReport(bookingId);
        }else{
            new Alert(Alert.AlertType.ERROR,"Somethin went wrong...");
        }
    }

    @FXML
    private TextField bookingIDTxt;


    @FXML
    private Button btnBill;

    @FXML
    void printBill(ActionEvent event) throws JRException, SQLException {
        genarateReport(bookingIDTxt.getText());
    }

    private void genarateReport(String bookingID) throws JRException, SQLException {

            PopupPaymentTm selectedItem = paymentTable.getSelectionModel().getSelectedItem();

//            if(selectedItem == null) {
//                return;
//            }
//
//            try {
//                JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/RapidRide.jrxml"));
//
//                Connection connection = DBConnection.getInstance().getConnection();
//
//                Map<String, Object> parameters = new HashMap<>();
//
//                parameters.put("bookingId", "B001");
//
//                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
//
//                JasperViewer.viewReport(jasperPrint, false);
//
//            } catch (JRException e) {
//
//                new Alert(Alert.AlertType.ERROR, "Failed to load the report. Please try again.").showAndWait();
//                e.printStackTrace();
//            } catch (SQLException e) {
//
//                new Alert(Alert.AlertType.ERROR, "Database error: Data could not be retrieved.").showAndWait();
//                e.printStackTrace();
//            } catch (Exception e) {
//
//                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred. Please try again.").showAndWait();
//                e.printStackTrace();
//            }

        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/report.jrxml"));
        Connection connection = DBConnection.getInstance().getConnection();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Booking Id", bookingID); // Use the correct parameter key here
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer.viewReport(jasperPrint,false);

    }

    @FXML
    void addOnAction(ActionEvent event) {

        String paymentId = lblPaymentId.getText();
        String bookingId = txtBookingId.getText();
        String paymentMethod = cmbPaymentMethod.getValue();
        String status = cmbStatus.getValue();
        double amount = Double.parseDouble(txtAmount.getText());
        Date date = Date.valueOf(lblDate.getText());
        Time time = Time.valueOf(lblTime.getText());

        PopupPaymentTm paymentTm = new PopupPaymentTm();

        paymentTm.setPayment_id(paymentId);
        paymentTm.setBooking_id(bookingId);
        paymentTm.setAmount(amount);
        paymentTm.setPayment_method(paymentMethod);
        paymentTm.setDate(date);
        paymentTm.setTime(time);
        paymentTm.setStatus(status);

        popuppaymentTms.add(paymentTm);
    }

    @FXML
    void searchOnAction(ActionEvent event) {

    }

    @FXML
    void updateOnAction(ActionEvent event) {

    }

}
