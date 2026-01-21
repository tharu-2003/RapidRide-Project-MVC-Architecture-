package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.UserDto;
import com.ijse.gdse72.rapidride.dto.tm.EmployeeTm;
import com.ijse.gdse72.rapidride.model.EmployeeModel;
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


import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.ijse.gdse72.rapidride.controller.LoginPageConroller.logUserName;

public class EmployeePaneController implements Initializable {

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
    private TableColumn<EmployeeTm, String> clmContact;

    @FXML
    private TableColumn<EmployeeTm, String> clmEmployeeId;

    @FXML
    private TableColumn<EmployeeTm, String> clmGmail;

    @FXML
    private TableColumn<EmployeeTm, String> clmName;

    @FXML
    private TableColumn<EmployeeTm, String> clmRole;

    @FXML
    private TableView<EmployeeTm> employeeTable;

    @FXML
    private ImageView imgSearch;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtGmail;

    @FXML
    private TextField txtName;

    private final EmployeeModel employeeModel = new EmployeeModel();
   // private final UserPaneController userPaneController = new UserPaneController();
    private final UserModel userModel = new UserModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clearPage();
        loadData();
        visibleData();
        changeFocusText();

        String logingUserId = getUserId();

        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        if(logingUserId.equals("U001")){
            btnSave.setDisable(false);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    public String getUserId()  {

        String userName = logUserName;

        try {
            UserDto userDto = userModel.searchUserName(userName);

            if (userDto != null) {
                return userDto.getUser_id();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void visibleData() {
        clmEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        clmRole.setCellValueFactory(new PropertyValueFactory<>("employee_role"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("employee_contact"));
        clmGmail.setCellValueFactory(new PropertyValueFactory<>("employee_gmail"));
    }

    public void clearPage() {
        txtSearch.setText("");
        txtContact.clear();
        lblEmployeeId.setText("");
        txtGmail.clear();
        txtName.clear();
        cmbRole.getSelectionModel().clearSelection();

        loadEmployeeRole();

        try {
            String nextEmployeeId = employeeModel.getNextEmplpyeeId();
            lblEmployeeId.setText(nextEmployeeId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEmployeeRole() {

        ObservableList<String> observableList = FXCollections.observableArrayList("Manager" , "Receotionist");
        cmbRole.setItems(observableList);
    }

    private void loadData() {
        try {
            ArrayList<EmployeeDto> employeeDtos = employeeModel.getEmployees();
            ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

            for (EmployeeDto employeeDto : employeeDtos) {
                EmployeeTm employeeTm = new EmployeeTm(
                        employeeDto.getEmployee_id(),
                        employeeDto.getEmployee_name(),
                        employeeDto.getEmployee_role(),
                        employeeDto.getEmployee_contact(),
                        employeeDto.getEmployee_gmail()
                );
                employeeTms.add(employeeTm);
            }
            employeeTable.setItems(employeeTms);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (lblEmployeeId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Employee ID cannot be empty!");
            return false;
        }
        if (txtName.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Name cannot be empty!");
            return false;
        }
        if (cmbRole.getItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Role cannot be empty!");
            return false;
        }
        if (!txtContact.getText().matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Contact must be a valid 10-digit number!");
            return false;
        }
        if (!txtGmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert(Alert.AlertType.WARNING, "Gmail must be a valid email address!");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        clearPage();
    }

    @FXML
    void clickOnAction(MouseEvent mouseEvent) {
        EmployeeTm selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            lblEmployeeId.setText(selectedEmployee.getEmployee_id());
            txtName.setText(selectedEmployee.getEmployee_name());
            cmbRole.setValue(selectedEmployee.getEmployee_role());
            txtContact.setText(selectedEmployee.getEmployee_contact());
            txtGmail.setText(selectedEmployee.getEmployee_gmail());
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        EmployeeTm selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            try {
                boolean isDeleted = employeeModel.deleteEmployee(selectedEmployee.getEmployee_id());

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Employee Deleted Successfully!");
                    loadData();
                    clearPage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to Delete Employee!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select an Employee to delete!");
        }
    }

    @FXML
    void saveOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        String employeeId = lblEmployeeId.getText();
        String name = txtName.getText();
        String role = String.valueOf(cmbRole.getValue());
        String contact = txtContact.getText();
        String gmail = txtGmail.getText();

        EmployeeDto employeeDto = new EmployeeDto(employeeId, name, role, contact, gmail);
        try {
            boolean isSaved = employeeModel.saveEmployee(employeeDto);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Employee Saved Successfully!");
                loadData();
                clearPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to Save Employee!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        }
    }

    @FXML
    void searchOnAction(MouseEvent event) {
        searchEmployee();
    }

    @FXML
    void searchOnAction2(ActionEvent event) {
        searchEmployee();
    }

    void searchEmployee() {
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d", "");

        if (check.equals("E")) {
            String employeeId = search;

            if (employeeId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Employee ID or Contact to search!");
                return;
            }

            try {
                EmployeeDto employeeDto = employeeModel.searchEmployeeId(employeeId);
                if (employeeDto != null) {
                    lblEmployeeId.setText(employeeDto.getEmployee_id());
                    txtName.setText(employeeDto.getEmployee_name());
                    cmbRole.setValue(employeeDto.getEmployee_role());
                    txtContact.setText(employeeDto.getEmployee_contact());
                    txtGmail.setText(employeeDto.getEmployee_gmail());
                } else {
                    showAlert(Alert.AlertType.WARNING, "Employee Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        } else {
            String contact = search;

            if (contact.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Employee ID or Contact to search!");
                return;
            }

            try {
                EmployeeDto employeeDto = employeeModel.searchEmployeeContact(contact);
                if (employeeDto != null) {
                    lblEmployeeId.setText(employeeDto.getEmployee_id());
                    txtName.setText(employeeDto.getEmployee_name());
                    cmbRole.setValue(employeeDto.getEmployee_role());
                    txtContact.setText(employeeDto.getEmployee_contact());
                    txtGmail.setText(employeeDto.getEmployee_gmail());
                } else {
                    showAlert(Alert.AlertType.WARNING, "Employee Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        String employeeId = lblEmployeeId.getText();
        String name = txtName.getText();
        String role = String.valueOf(cmbRole.getValue());
        String contact = txtContact.getText();
        String gmail = txtGmail.getText();

        EmployeeDto employeeDto = new EmployeeDto(employeeId, name, role, contact, gmail);
        try {
            boolean isUpdated = employeeModel.updateEmployee(employeeDto);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Employee Updated Successfully!");
                loadData();
                clearPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to Update Employee!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        }
    }

    public void changeFocusText() {

        TextField[] textFields = {txtName, txtContact, txtGmail};

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