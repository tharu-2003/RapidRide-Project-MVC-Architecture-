package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.DriverDto;
import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.UserDto;
import com.ijse.gdse72.rapidride.dto.tm.CustomerTm;
import com.ijse.gdse72.rapidride.dto.tm.EmployeeTm;
import com.ijse.gdse72.rapidride.model.CustomerModel;
import com.ijse.gdse72.rapidride.model.UserModel;
import com.ijse.gdse72.rapidride.util.CrudUtil;
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

public class CustomerPaneController implements Initializable {

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> clmContact;

    @FXML
    private TableColumn<?, ?> clmCustomerId;

    @FXML
    private TableColumn<?, ?> clmGmail;

    @FXML
    private TableColumn<?, ?> clmName;

    @FXML
    private TableColumn<?, ?> clmUserId;

    @FXML
    private TableView<CustomerTm> customerTable;

    @FXML
    private ImageView imgSearch;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblUserId;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtGmail;

    @FXML
    private TextField txtName;


    private final CustomerModel customerModel = new CustomerModel();
    private final UserModel userModel = new UserModel();

    public String logingUserId;

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearPage();
        loadData();
        visibleData();
        changeFocusText();

    }

    public void getUserId()  {

        String userName = logUserName;

        try {
            UserDto userDto = userModel.searchUserName(userName);

            if (userDto != null) {

                logingUserId = userDto.getUser_id();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void visibleData(){
        clmCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        clmGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        clmUserId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
    }

    private void loadData(){

        try {
            ArrayList<CustomerDto> customerDtos = customerModel.getCustomers();
            ObservableList<CustomerTm> customerTms = FXCollections.observableArrayList();

            for(CustomerDto customerDto : customerDtos){
                CustomerTm customerTm = new CustomerTm(
                        customerDto.getCustomer_id(),
                        customerDto.getName(),
                        customerDto.getContact(),
                        customerDto.getGmail(),
                        customerDto.getUser_id()
                );
                customerTms.add(customerTm);
            }
            customerTable.setItems(customerTms);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPage(){

        getUserId();

        txtSearch.setText("");
        lblCustomerId.setText("");
        txtName.clear();
        txtContact.clear();
        txtGmail.clear();
        lblUserId.setText(logingUserId);

        try {
            String nextCustomerID = customerModel.getNextCusomerId();
            lblCustomerId.setText(nextCustomerID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        clearPage();
    }

    @FXML
    void clickOnAction(MouseEvent event) {

        CustomerTm selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if(selectedCustomer != null){
            lblCustomerId.setText(selectedCustomer.getCustomer_id());
            txtName.setText(selectedCustomer.getName());
            txtContact.setText(selectedCustomer.getContact());
            txtGmail.setText(selectedCustomer.getGmail());
            lblUserId.setText(selectedCustomer.getUser_id());
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
            CustomerTm selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer != null) {
                try {
                    boolean isDeleted = customerModel.deleteCustomer(selectedCustomer.getCustomer_id());

                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully!").show();
                        loadData(); // Refresh table
                        clearPage(); // Clear form fields
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to Delete Customer!").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a Customer to delete!").show();
            }

    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String customer_id = lblCustomerId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String gmail = txtGmail.getText();
        String user_id = lblUserId.getText();

        CustomerDto customerDto = new CustomerDto(customer_id,name,contact,gmail,user_id);

        try {
            boolean isSaved = customerModel.saveCustomer(customerDto);

            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Successfully saved the customer!").show();
                loadData();
                clearPage();
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to saved!").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void searchOnAction(MouseEvent event) {searchCustomer();}

    @FXML
    void searchOnAction2(ActionEvent event) {searchCustomer();}

    void searchCustomer(){
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d","");

        if(check.equals("C")){
            String customerId = search;

            if (customerId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an customer ID or Contact to search!");
                return;
            }

            try {
                CustomerDto customerDto = customerModel.searchCustomerId(customerId);
                if (customerDto != null) {
                    lblCustomerId.setText(customerDto.getCustomer_id());
                    txtName.setText(customerDto.getName());
                    txtContact.setText(customerDto.getContact());
                    txtGmail.setText(customerDto.getGmail());
                    lblUserId.setText(customerDto.getUser_id());

                } else {
                    showAlert(Alert.AlertType.WARNING, "Customer Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }else {
            String contact = search;

            if (contact.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Driver ID or Contact to search!");
                return;
            }

            try {
                CustomerDto customerDto = customerModel.searchCustomerContact(contact);
                if (customerDto != null) {
                    lblCustomerId.setText(customerDto.getCustomer_id());
                    txtName.setText(customerDto.getName());
                    txtContact.setText(customerDto.getContact());
                    txtGmail.setText(customerDto.getGmail());
                    lblUserId.setText(customerDto.getUser_id());

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

        String userId = lblUserId.getText();
        String customerId = lblCustomerId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String gmail = txtGmail.getText();

        CustomerDto customerDto = new CustomerDto(customerId, name, contact, gmail, userId);
        try {
            boolean isUpdated = customerModel.updateCustomer(customerDto);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Customer Updated Successfully!");
                loadData();
                clearPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to Update Customer!");
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
