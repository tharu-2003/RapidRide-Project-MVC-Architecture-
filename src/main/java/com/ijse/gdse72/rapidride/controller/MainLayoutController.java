package com.ijse.gdse72.rapidride.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainLayoutController extends Application {

    @FXML
    private Button btnBookingView;

    @FXML
    private Button btnCustomerView;

    @FXML
    private Button btnDriverView;

    @FXML
    private Button btnEmployeeView;

    @FXML
    private Button btnPaymentView;

    @FXML
    private Button btnUserView;

    @FXML
    private Button btnVehicleView;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Pane pasetedPane;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnBookingDetailsView;

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @FXML
    void bookingDetailsView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/BookingDetailsPane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void bookingView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/BookingPane.fxml")));
        } catch (IOException e) {
            new Alert(Alert.AlertType.INFORMATION,"Fall to load Booking Page" + e.getMessage());
        }
    }

    @FXML
    void customerView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/CustomerPane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void driverView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/DriverPane.fxml")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void employeeView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/EmployeePane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void paymentView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/PaymentPane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void userView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/UserPane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void vehicleView(ActionEvent event) {
        pasetedPane.getChildren().clear();
        try {
            pasetedPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/VehiclePane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void logOut(ActionEvent event) throws Exception {
//        mainAnchorPane.getChildren().clear();
//        mainAnchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml")));

        Window window = btnBookingView.getScene().getWindow();
        Stage stage = (Stage) window;
        stage.close();

        start(new Stage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.jpg")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
