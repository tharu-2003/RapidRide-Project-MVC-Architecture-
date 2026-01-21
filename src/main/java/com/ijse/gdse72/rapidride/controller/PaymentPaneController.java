package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.BookingDto;
import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.PaymentDto;
import com.ijse.gdse72.rapidride.dto.tm.PaymentTm;
import com.ijse.gdse72.rapidride.model.PaymentModel;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaymentPaneController implements Initializable {

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnFinished;

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
    private TableView<PaymentTm> paymentTable;

    @FXML
    private ComboBox<String> cmbPaymentMethod;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private ImageView imgSearch;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtAmount;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblBookingId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    private final PaymentModel paymentModel = new PaymentModel();

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
        clmPaymentId.setCellValueFactory(new PropertyValueFactory<>("payment_id"));
        clmBookingId.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        clmPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        clmStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData(){
        try {
            ArrayList<PaymentDto> paymentDtos = paymentModel.getPayments();
            ObservableList<PaymentTm> paymentTms = FXCollections.observableArrayList();

            for(PaymentDto paymentDto : paymentDtos){
                PaymentTm paymentTm =new PaymentTm(
                        paymentDto.getPayment_id(),
                        paymentDto.getBooking_id(),
                        paymentDto.getAmount(),
                        paymentDto.getPayment_method(),
                        paymentDto.getDate(),
                        paymentDto.getTime(),
                        paymentDto.getStatus()
                );
                paymentTms.add(paymentTm);
            }
            paymentTable.setItems(paymentTms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPage(){
        txtSearch.setText("");
        lblPaymentId.setText("");
        lblBookingId.setText("");
        txtAmount.clear();
        cmbPaymentMethod.getSelectionModel().clearSelection();
        lblDate.setText(LocalDate.now().toString());
        lblTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        cmbStatus.getSelectionModel().clearSelection();

        try {
            String nextPaymentID = paymentModel.getNextPaymentId();
            lblPaymentId.setText(nextPaymentID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loadStatus();
        loadPaymentMethod();
    }

    private void loadPaymentMethod() {

        ObservableList<String> observableList = FXCollections.observableArrayList("Cash" , "Card" , "Bank transfer");
        cmbPaymentMethod.setItems(observableList);
    }

    private void loadStatus() {

        ObservableList<String> observableList = FXCollections.observableArrayList( "finished");
        cmbStatus.setItems(observableList);
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        clearPage();
    }

    @FXML
    void clickOnAction(MouseEvent event) {
        PaymentTm selectedPayment = paymentTable.getSelectionModel().getSelectedItem();

        if(selectedPayment != null){
            lblPaymentId.setText(selectedPayment.getPayment_id());
            lblBookingId.setText(selectedPayment.getBooking_id());
            txtAmount.setText(String.valueOf(selectedPayment.getAmount()));
            cmbPaymentMethod.setValue(selectedPayment.getPayment_method());
            lblDate.setText(String.valueOf(selectedPayment.getDate()));
            lblTime.setText(String.valueOf(selectedPayment.getTime()));
            cmbStatus.setValue(selectedPayment.getStatus());
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {

    }

    @FXML
    void finishedOnAction(ActionEvent event) {

    }

    @FXML
    void searchOnAction(MouseEvent event) {
        searchPayment();
    }

    @FXML
    void searchOnAction2(ActionEvent event) {
        searchPayment();
    }

    void searchPayment(){
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d","");

        if(check.equals("B")){
            String bookingId = search;

            if (bookingId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Booking ID or Payment ID to search!");
                return;
            }

            try {
                PaymentDto paymentDto = paymentModel.searchBookingId(bookingId);
                if (paymentDto != null) {

                    lblBookingId.setText(paymentDto.getBooking_id());
                    txtAmount.setText(String.valueOf(paymentDto.getAmount()));

                } else {
                    showAlert(Alert.AlertType.WARNING, "Booking Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }else if(check.equals("P")) {
            String paymentId = search;

            if (paymentId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Booking ID or Payment ID to search!");
                return;
            }

            try {


                PaymentDto paymentDto = paymentModel.searchPaymentId(paymentId);

                if (paymentDto != null) {

                    lblPaymentId.setText(paymentDto.getPayment_id());
                    lblBookingId.setText(paymentDto.getBooking_id());
                    cmbStatus.setValue(paymentDto.getStatus());
                    txtAmount.setText(String.valueOf(paymentDto.getAmount()));
                    cmbPaymentMethod.setValue(paymentDto.getPayment_method());
                    lblDate.setText(String.valueOf(paymentDto.getDate()));
                    lblTime.setText(String.valueOf(paymentDto.getTime()));

                } else {
                    showAlert(Alert.AlertType.WARNING, "Customer Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {

    }

}
