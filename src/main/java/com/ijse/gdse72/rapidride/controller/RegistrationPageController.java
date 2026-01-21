package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.UserDto;
import com.ijse.gdse72.rapidride.model.EmployeeModel;
import com.ijse.gdse72.rapidride.model.UserModel;
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
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationPageController extends Application implements Initializable {

    @FXML
    private Button btnCreate;

    @FXML
    private ImageView iconHide;

    @FXML
    private Label lblUserId;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserName1;

    @FXML
    private Label lblUserName2;

    @FXML
    private Label lblUserName21;

    @FXML
    private Label lblUserName211;

    @FXML
    private ImageView lblpasswordEye;

    @FXML
    private Hyperlink linkLogIn;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPassword1;

    @FXML
    private TextField txtUserName;

    private final UserModel userModel = new UserModel();
    private final EmployeeModel employeeModel = new EmployeeModel();

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeFocusText();
        try {
            String nextUserID = userModel.getNextCusomerId();
            lblUserId.setText(nextUserID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void createAccount(ActionEvent event) throws Exception {

        String user_id = lblUserId.getText();
        String employee_id = txtEmployeeId.getText();
        String name = txtName.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();


         UserDto userDto = new UserDto(user_id,employee_id,name,userName,password);

         boolean isChekEmployee =employeeModel.chekEmployee(employee_id);

        if(isChekEmployee){
            try {
                boolean isSaved = userModel.saveUser(userDto);

                if(isSaved){

                    Window window = btnCreate.getScene().getWindow();
                    Stage stage = (Stage) window;
                    stage.close();

                    start(new Stage());
                    new Alert(Alert.AlertType.INFORMATION,"Successfully saved the customer!").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Failed to saved!").show();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            showAlert(Alert.AlertType.WARNING,"You are not an Empoyee..!");
        }

    }

    @FXML
    void logIn(ActionEvent event) throws Exception {
        Window window = btnCreate.getScene().getWindow();
        Stage stage = (Stage) window;
        stage.close();

        start(new Stage());
    }

    @FXML
    void showHide(MouseEvent event) {

    }

    public void changeFocusText() {

        TextField[] textFields = {txtEmployeeId, txtName, txtUserName,txtPassword};

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

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.jpg")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
