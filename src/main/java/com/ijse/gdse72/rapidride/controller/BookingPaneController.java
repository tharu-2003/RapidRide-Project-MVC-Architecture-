package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.BookingDto;
import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.tm.BookingTm;
import com.ijse.gdse72.rapidride.dto.tm.PopupPaymentTm;
import com.ijse.gdse72.rapidride.model.BookingModel;
import com.ijse.gdse72.rapidride.model.CustomerModel;
import com.ijse.gdse72.rapidride.service.BookingSinglton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;



public class BookingPaneController implements Initializable {


    @FXML
    private AnchorPane bookingAnchorPane;

    @FXML
    private TableView<BookingTm> bookingTable;

    @FXML
    private Button btnComplited;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnPay;

    @FXML
    private Button btnAddBookin;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> clmBookingId;

    @FXML
    private TableColumn<?, ?> clmCustomerId;

    @FXML
    private TableColumn<?, ?> clmDistance;

    @FXML
    private TableColumn<?, ?> clmRequaiedDate;

    @FXML
    private TableColumn<?, ?> clmRideTo;

    @FXML
    private TableColumn<?, ?> clmStatus;

    @FXML
    private TableColumn<?, ?> clmTotalAmount;

    @FXML
    private TableColumn<?, ?> clmVehicleCount;

    @FXML
    private ImageView imgSearch;

    @FXML
    private ImageView imgPlus;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblBookingId;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTotalAmount;

   // @FXML
  //  private ComboBox<String> cmbCustomerId;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtRequaiedDate;

    @FXML
    private TextField txtRideTo;

    @FXML
    private TextField txtVehicleCount;

    private final BookingModel bookingModel = new BookingModel();

    BookingSinglton bookingSinglton = BookingSinglton.getInstance();
    ObservableList<BookingTm> bookingTms = FXCollections.observableArrayList();

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    private double distance;
    private int vehicleCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearPage();
        loadData();
        visibleData();
        changeFocusText();


    }

    void visibleData(){
        clmBookingId.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        clmRideTo.setCellValueFactory(new PropertyValueFactory<>("ride_to"));
        clmDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        clmRequaiedDate.setCellValueFactory(new PropertyValueFactory<>("required_date"));
        clmTotalAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        clmStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        clmVehicleCount.setCellValueFactory(new PropertyValueFactory<>("vehicle_count"));
        clmCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));


    }

    private void loadData(){
        try {
            ArrayList<BookingDto> bookingDtos = bookingModel.getBookings();
            ObservableList<BookingTm> bookingTms = FXCollections.observableArrayList();

            for(BookingDto bookingDto : bookingDtos){
                BookingTm bookingTm = new BookingTm(
                        bookingDto.getBooking_id(),
                        bookingDto.getRide_to(),
                        bookingDto.getDistance(),
                        bookingDto.getRequired_date(),
                        bookingDto.getTotal_amount(),
                        bookingDto.getStatus(),
                        bookingDto.getVehicle_count(),
                        bookingDto.getCustomer_id()
                );
                bookingTms.add(bookingTm);
            }
            bookingTable.setItems(bookingTms);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPage() {
        txtSearch.setText("");
        lblBookingId.setText("");
        txtRideTo.clear();
        txtDistance.clear();
        txtRequaiedDate.clear();
        lblTotalAmount.setText("");
        lblStatus.setText("pending");
        txtVehicleCount.clear();
        lblCustomerId.setText("");

        try {
            String nextBookingId = bookingModel.getNextBookingId();
            lblBookingId.setText(nextBookingId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //cmbCustomerId.getSelectionModel().clearSelection();

        //loadCustomerId();
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        clearPage();
    }

//    private void loadCustomerId() throws SQLException {
//
//        ArrayList<String> customerIds = bookingModel.getAllCustomerId();
//
//        ObservableList<String> observableList =FXCollections.observableArrayList();
//
//        observableList.addAll(customerIds);
//        cmbCustomerId.setItems(observableList);
//    }

    @FXML
    void clickOnAction(MouseEvent event) {
        BookingTm selectedBooking = bookingTable.getSelectionModel().getSelectedItem();

        if(selectedBooking != null){
            lblBookingId.setText(selectedBooking.getBooking_id());
            txtRideTo.setText(selectedBooking.getRide_to());
            txtDistance.setText(String.valueOf(selectedBooking.getDistance()));
            txtRequaiedDate.setText(String.valueOf(selectedBooking.getRequired_date()));
            lblTotalAmount.setText(String.valueOf(selectedBooking.getTotal_amount()));
            lblStatus.setText(selectedBooking.getStatus());
            txtVehicleCount.setText(String.valueOf(selectedBooking.getVehicle_count()));
            lblCustomerId.setText(selectedBooking.getCustomer_id());
            //cmbCustomerId.setValue(selectedBooking.getCustomer_id());
        }
    }

    @FXML
    void customerAddOnAction(MouseEvent event) throws SQLException {
        bookingAnchorPane.setDisable(true); // Disable main pane temporarily
        showAddCustomerPopup(bookingAnchorPane,"/view/CustomerPane.fxml");

//        loadCustomerId();
    }

    private void showAddCustomerPopup(AnchorPane mainPane,String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent popupContent = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(bookingAnchorPane.getScene().getWindow());
            popupStage.setTitle("Add Customer");

            Scene popupScene = new Scene(popupContent);
            popupStage.setScene(popupScene);

            popupStage.setOnHidden(e -> mainPane.setDisable(false));
            popupStage.showAndWait();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void enterVehicleCountOnAction(ActionEvent event) {
        try{
            distance = Double.parseDouble(txtDistance.getText());
            vehicleCount = Integer.parseInt(txtVehicleCount.getText());

            lblTotalAmount.setText(String.valueOf((distance*vehicleCount)*100));
        } catch (Exception e) {
            new Alert(Alert.AlertType.CONFIRMATION,"Please fill this form");
        }
    }

    @FXML
    void addBookingOnAction(ActionEvent event) {

        String bookingId = lblBookingId.getText();
        String customerId = lblCustomerId.getText();
        String status = lblStatus.getText();
        String rideTo = txtRideTo.getText();
        distance = Double.parseDouble(txtDistance.getText());
        Date date = Date.valueOf(txtRequaiedDate.getText());
        vehicleCount = Integer.parseInt(txtVehicleCount.getText());
        lblTotalAmount.setText(String.valueOf((distance*vehicleCount)*100));
        double amount = Double.parseDouble(lblTotalAmount.getText());


        BookingTm bookingTm = new BookingTm();

        bookingTm.setBooking_id(bookingId);
        bookingTm.setCustomer_id(customerId);
        bookingTm.setStatus(status);
        bookingTm.setTotal_amount(amount);
        bookingTm.setRide_to(rideTo);
        bookingTm.setDistance(distance);
        bookingTm.setRequired_date(date);
        bookingTm.setVehicle_count(vehicleCount);

        bookingTms.add(bookingTm);
        bookingTable.setItems(bookingTms);
    }

    @FXML
    void payOnAction(ActionEvent event) {

        String bookingId = lblBookingId.getText();
        String customerId = lblCustomerId.getText();
        String status = lblStatus.getText();
        double amount = Double.parseDouble(lblTotalAmount.getText());
        String rideTo = txtRideTo.getText();
        double distance = Double.parseDouble(txtDistance.getText());
        Date date = Date.valueOf(txtRequaiedDate.getText());
        int vehicleCount = Integer.parseInt(txtVehicleCount.getText());

        bookingSinglton.setBookingId(bookingId);
        bookingSinglton.setCustomerId(customerId);
        bookingSinglton.setStatus(status);
        bookingSinglton.setAmount(amount);
        bookingSinglton.setRideTo(rideTo);
        bookingSinglton.setDistance(distance);
        bookingSinglton.setDate(date);
        bookingSinglton.setVehicleCount(vehicleCount);

        bookingAnchorPane.setDisable(true);
        showPayPopup(bookingAnchorPane,"/view/PopupPayment.fxml");
    }

    private void showPayPopup(AnchorPane mainPane, String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent popupContent = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(bookingAnchorPane.getScene().getWindow());
            popupStage.setTitle("Add Payment");

            Scene popupScene = new Scene(popupContent);
            popupStage.setScene(popupScene);

            popupStage.setOnHidden(e -> mainPane.setDisable(false));
            popupStage.showAndWait();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchOnAction(MouseEvent event) {
        searchBooking();
    }

    @FXML
    void searchOnAction2(ActionEvent event) {
        searchBooking();
    }

    void searchBooking(){
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d","");

        if(check.equals("B")){
            String bookingId = search;

            if (bookingId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Booking ID or Customer ID to search!");
                return;
            }

            try {
                BookingDto bookingDto = bookingModel.searchBookingId(bookingId);
                if (bookingDto != null) {
                    lblBookingId.setText(bookingDto.getBooking_id());
                    lblCustomerId.setText(bookingDto.getCustomer_id());
                    lblStatus.setText(bookingDto.getStatus());
                    lblTotalAmount.setText(String.valueOf(bookingDto.getTotal_amount()));
                    txtRideTo.setText(bookingDto.getRide_to());
                    txtDistance.setText(String.valueOf(bookingDto.getDistance()));
                    txtRequaiedDate.setText(String.valueOf(bookingDto.getRequired_date()));
                    txtVehicleCount.setText(String.valueOf(bookingDto.getVehicle_count()));

                } else {
                    showAlert(Alert.AlertType.WARNING, "Booking Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }else {
            String customerId = search;

            if (customerId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Booking ID or Customer ID to search!");
                return;
            }

            try {


                CustomerDto customerDto = bookingModel.searchCustomerId(customerId);

                if (customerDto != null) {

                    showAlert(Alert.AlertType.INFORMATION, "Found Customer");
                    lblCustomerId.setText(customerDto.getCustomer_id());

                } else {
                    showAlert(Alert.AlertType.WARNING, "Customer Not Found!");
                    lblCustomerId.setText("");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {

    }

    @FXML
    void compliteOnAction(ActionEvent event) {

    }

    public void changeFocusText() {

        TextField[] textFields = {txtRideTo, txtDistance,txtRequaiedDate, txtVehicleCount};

        for (int i = 0; i < textFields.length; i++) {
            int currentIndex = i; // Capture the current index for the lambda
            textFields[i].setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    // Otherwise, move to the next TextField
                    int nextIndex = (currentIndex + 1) % textFields.length;
                    textFields[nextIndex].requestFocus();
                }
            });
        }
    }
}
