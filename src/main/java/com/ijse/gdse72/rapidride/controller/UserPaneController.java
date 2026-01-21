package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.UserDto;
import com.ijse.gdse72.rapidride.dto.tm.EmployeeTm;
import com.ijse.gdse72.rapidride.dto.tm.UserTm;
import com.ijse.gdse72.rapidride.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.ijse.gdse72.rapidride.controller.LoginPageConroller.logUserName;

public class UserPaneController implements Initializable {

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> clmEmployeeId;

    @FXML
    private TableColumn<?, ?> clmName;

    @FXML
    private TableColumn<?, ?> clmPassword;

    @FXML
    private TableColumn<?, ?> clmUserId;

    @FXML
    private TableColumn<?, ?> clmUserName;

    @FXML
    private ImageView imgSearch;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUserName;

    @FXML
    private Label myUserId;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private Label lblUserId;

    @FXML
    private Label lblPassword;

    @FXML
    private TableView<UserTm> userTable;

    private final UserModel userModel = new UserModel();
    private final LoginPageConroller loginPageConroller = new LoginPageConroller();

    private String logUserId;

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearPage();
        loadData();
        visibleData();
        changeFocusText();

        logUserId = getUserId();

        myUserId.setText(logUserId);
        btnDelete.setDisable(true);

        if(logUserId.equals("U001")){
            btnDelete.setDisable(false);
        }

    }

    void visibleData(){
        clmUserId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        clmEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmUserName.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        clmPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    private void loadData(){
        try {
            ArrayList<UserDto> userDtos = userModel.getUsers();
            ObservableList<UserTm> userTms = FXCollections.observableArrayList();

            for(UserDto userDto : userDtos){
                UserTm userTm = new UserTm(
                        userDto.getUser_id(),
                        userDto.getEmployee_id(),
                        userDto.getName(),
                        userDto.getUser_name(),
                        userDto.getPassword()
                );
                userTms.add(userTm);
            }
            userTable.setItems(userTms);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPage(){

        txtSearch.setText("");
        lblUserId.setText(logUserId);
        lblEmployeeId.setText("");
        txtName.clear();
        txtUserName.clear();
        lblPassword.setText("");
    }

    public String getUserId()  {

        String userName = logUserName;

        try {
            UserDto userDto = userModel.searchUserName(userName);

            if (userDto != null) {

                lblUserId.setText(userDto.getUser_id());
                txtName.setText(userDto.getName());
                txtUserName.setText(userDto.getUser_name());
                lblPassword.setText(userDto.getPassword());
                lblEmployeeId.setText(userDto.getEmployee_id());

                return userDto.getUser_id();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        clearPage();
    }

    @FXML
    void clickOnAction(MouseEvent event) {
        UserTm selectedUser = userTable.getSelectionModel().getSelectedItem();

        if(selectedUser != null){
            lblUserId.setText(selectedUser.getUser_id());
            lblEmployeeId.setText(selectedUser.getEmployee_id());
            txtName.setText(selectedUser.getName());
            txtUserName.setText(selectedUser.getUser_name());
            lblPassword.setText(selectedUser.getPassword());
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        if(logUserId.equals("U001")){
            UserTm selectedUser= userTable.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {

                if(!selectedUser.getUser_id().equals("U001")) {
                    try {
                        boolean isDeleted = userModel.deleteUser(selectedUser.getUser_id());

                        if (isDeleted) {
                            showAlert(Alert.AlertType.INFORMATION, "User Deleted Successfully!");
                            loadData();
                            clearPage();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Failed to Delete User!");
                        }
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
                    }
                }else {
                    showAlert(Alert.AlertType.ERROR,"Can not Delete Admin");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Please select an User to delete!");
            }
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {

        String userId = lblUserId.getText();
        String name = txtName.getText();
        String userName = txtUserName.getText();
        String password = lblPassword.getText();
        String employeeId = lblEmployeeId.getText();

        if (!userId.isEmpty() &&
                !name.isEmpty() &&
                !userName.isEmpty() &&
                !password.isEmpty() &&
                !employeeId.isEmpty()) {

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Are you sure you want to Update this user?");
            confirmAlert.setContentText("Please enter your password to conform.");

            // To Get A Password input field
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");

            VBox dialogPaneContent = new VBox();
            dialogPaneContent.getChildren().addAll(new Label("Password:"), passwordField);
            confirmAlert.getDialogPane().setContent(dialogPaneContent);

            confirmAlert.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {
                    String userPassword = passwordField.getText();
                    String userIdUp = logUserId;
                    //String userIdUp = lblUserId.getText();

                    try {
                        UserDto userDto1 = userModel.confirmation(userIdUp);

                        String storedPassword =userDto1.getPassword();

                        if (storedPassword.equals(userPassword)) {
                            UserDto userDto = new UserDto(
                                    userId,
                                    employeeId,
                                    name,
                                    userName,
                                    password
                            );
                            boolean isUpdated = userModel.updateUser(userDto);
                            if (isUpdated) {
                                showAlert(Alert.AlertType.INFORMATION, "User Updated Successfully!");
                                loadData();
                                clearPage();
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Failed to Update User!").show();
                            }
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Invalid Password Please Try Again....!").show();
                        }
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
                    }

                }
            });
        }else {
            new Alert(Alert.AlertType.WARNING, "Please fill out all fields!").show();
        }

//        String userId = lblUserId.getText();
//        String name = txtName.getText();
//        String userName = txtUserName.getText();
//        String password = lblPassword.getText();
//        String employeeId = lblEmployeeId.getText();
//
//        UserDto userDto = new UserDto(userId, employeeId, name, userName, password);
//        try {
//            boolean isUpdated = userModel.updateUser(userDto);
//            if (isUpdated) {
//                showAlert(Alert.AlertType.INFORMATION, "User Updated Successfully!");
//                loadData();
//                clearPage();
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Failed to Update User!");
//            }
//        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
//        }
    }

    @FXML
    void searchOnAction(MouseEvent event) {searchUser();}

    @FXML
    void searchOnAction2(ActionEvent event) {searchUser();}

    void searchUser(){
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d","");

        if(check.equals("U")){
            String userId = search;

            if (userId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an User name or Id to search!");
                return;
            }

            try {
                UserDto userDto = userModel.searchUserId(userId);
                if (userDto != null) {
                    lblUserId.setText(userDto.getUser_id());
                    txtName.setText(userDto.getName());
                    txtUserName.setText(userDto.getUser_name());
                    lblPassword.setText(userDto.getPassword());
                    lblEmployeeId.setText(userDto.getEmployee_id());

                } else {
                    showAlert(Alert.AlertType.WARNING, "User Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }else {
            String userName = search;

            if (userName.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an User name or Id to search!");
                return;
            }

            try {
                UserDto userDto = userModel.searchUserName(userName);
                if (userDto != null) {
                    lblUserId.setText(userDto.getUser_id());
                    txtName.setText(userDto.getName());
                    txtUserName.setText(userDto.getUser_name());
                    lblPassword.setText(userDto.getPassword());
                    lblEmployeeId.setText(userDto.getEmployee_id());

                } else {
                    showAlert(Alert.AlertType.WARNING, "User Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }
    }

    public void changeFocusText() {

        TextField[] textFields = {txtName, txtUserName};

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
