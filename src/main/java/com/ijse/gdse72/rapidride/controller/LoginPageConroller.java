package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.db.DBConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginPageConroller extends Application implements Initializable {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button btnSingIn;

    @FXML
    private ImageView iconHide;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserName1;

    @FXML
    private ImageView lblpasswordEye;

    @FXML
    private Hyperlink linkFogetPassword;

    @FXML
    private Hyperlink linkSignIn;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPassword1;

    @FXML
    private TextField txtUserName;

    public static String logUserName;

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeFocusText();
    }

    @FXML
    void fogetPassword(ActionEvent event) {

    }

    @FXML
    void showHide(MouseEvent event) {
//        String password = txtPassword.getText();
//        txtPassword1.setText(password);

    }

    @FXML
    void enterPasswordOnAction(ActionEvent event) {
        try {
            signIn(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void signIn(ActionEvent event) throws Exception {

        logUserName = txtUserName.getText();

            String Name = txtUserName.getText();
            String Password = txtPassword.getText();

            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT user_id FROM User WHERE user_name = ? AND password = ?";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, Password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {

                    Window window = lblUserName.getScene().getWindow();
                    Stage stage = (Stage) window;
                    stage.close();

                    start(new Stage());

//                        mainAnchorPane.getChildren().clear();
//                        mainAnchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml")));

                } else {
                    new Alert(Alert.AlertType.ERROR, "Incorrect username or password").show();

                }

            } catch (IOException e) {
                e.printStackTrace(); // Print stack trace for debugging
                new Alert(Alert.AlertType.ERROR, "Database error occurred").show();
            }

    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.jpg")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void signUp(ActionEvent event) throws Exception {

        Window window = lblUserName.getScene().getWindow();
        Stage stage = (Stage) window;
        stage.close();

        start2(new Stage());


    }

    public void start2(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/RegistrationPage.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.jpg")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void changeFocusText() {

        TextField[] textFields = {txtUserName, txtPassword};

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
