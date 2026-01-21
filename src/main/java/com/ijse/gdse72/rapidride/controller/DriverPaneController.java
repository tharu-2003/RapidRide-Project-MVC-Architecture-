package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.DriverDto;
import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.VehicleDto;
import com.ijse.gdse72.rapidride.dto.tm.DriverTm;
import com.ijse.gdse72.rapidride.dto.tm.EmployeeTm;
import com.ijse.gdse72.rapidride.model.DriverModel;
import com.ijse.gdse72.rapidride.model.EmployeeModel;
import com.ijse.gdse72.rapidride.model.VehicleModel;
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

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DriverPaneController implements Initializable {

    @FXML
    public ImageView imgPlus2;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAddVehicle;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> clmContact;

    @FXML
    private TableColumn<?, ?> clmDriverId;

    @FXML
    private TableColumn<?, ?> clmGmail;

    @FXML
    private TableColumn<?, ?> clmName;

    @FXML
    private TableColumn<?, ?> clmVehecleId;

    @FXML
    private TableView<DriverTm> driverTable;

    @FXML
    private Label lblDriverId;

    @FXML
    private Label lblVehicleId;

    @FXML
    private ImageView imgSearch;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtGmail;

    @FXML
    private TextField txtName;

    @FXML
    private AnchorPane driverAnchorPane;

//    @FXML
//    private ComboBox<String> cmbVehicleId;

    private final DriverModel driverModel = new DriverModel();
    private final VehicleModel vehicleModel = new VehicleModel();

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            clearPage();
            loadData();
            visibleData();
            changeFocusText();

            btnAddVehicle.setDisable(true);


        } catch (SQLException e) {
//            throw new RuntimeException(e);
            new Alert(Alert.AlertType.WARNING,"errorr " + e.getMessage()).show();
        }
    }

    void visibleData(){
        clmDriverId.setCellValueFactory(new PropertyValueFactory<>("driver_id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        clmGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        clmVehecleId.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
    }

    private void loadData() {

        try {
            ArrayList<DriverDto> driverDtos = driverModel.getDrivers();
            ObservableList<DriverTm> driverTms = FXCollections.observableArrayList();

            for (DriverDto driverDto : driverDtos) {
                DriverTm driverTm = new DriverTm(
                        driverDto.getDriver_id(),
                        driverDto.getName(),
                        driverDto.getContact(),
                        driverDto.getGmail(),
                        driverDto.getVehicle_id()
                );
                driverTms.add(driverTm);
            }
            driverTable.setItems(driverTms);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void clearPage() throws SQLException {
        txtSearch.setText("");
        lblDriverId.setText("");
        txtName.setText("");
        txtContact.setText("");
        txtGmail.setText("");
        lblVehicleId.setText("");

        String nextDriverId = driverModel.getNextDriverId();
        lblDriverId.setText(nextDriverId);

        String nextVehicleId = vehicleModel.getNextVehicleId();
        lblVehicleId.setText(nextVehicleId);

    }


    @FXML
    void gamailEnterOnAction(ActionEvent event) {
        try {
            String nextDriverId = driverModel.getNextDriverId();

            if(lblDriverId.getText().equals(nextDriverId) &&
                    !(txtName.getText().isEmpty()) &&
                            !(txtContact.getText().isEmpty()) &&
                                !(txtGmail.getText().isEmpty())){

                btnAddVehicle.setDisable(false);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void refreshOnAction(ActionEvent event) throws SQLException {
        clearPage();
    }

    public void clickOnAction(MouseEvent mouseEvent) {
        DriverTm selectedDriver = driverTable.getSelectionModel().getSelectedItem();

        if (selectedDriver != null) {
            lblDriverId.setText(selectedDriver.getDriver_id());
            txtName.setText(selectedDriver.getName());
            txtContact.setText(selectedDriver.getContact());
            txtGmail.setText(selectedDriver.getGmail());
            lblVehicleId.setText(selectedDriver.getVehicle_id());
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        DriverTm selectedDriver = driverTable.getSelectionModel().getSelectedItem();

        if (selectedDriver != null) {
            try {
                boolean isDeleted = driverModel.deleteDriver(selectedDriver.getDriver_id());

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Driver Deleted Successfully!").show();
                    loadData(); // Refresh table
                    clearPage(); // Clear form fields
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to Delete Driver!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a Driver to delete!").show();
        }
    }


    @FXML
    void AddVehicleOnAction(ActionEvent event) {

        driverAnchorPane.setDisable(true);
        showAddVehiclePopup(driverAnchorPane,"/view/VehiclePane.fxml");

    }


    private void showAddVehiclePopup(AnchorPane mainPane,String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent popupContent = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(driverAnchorPane.getScene().getWindow());
            popupStage.setTitle("Add Vehicle");

            Scene popupScene = new Scene(popupContent);
            popupStage.setScene(popupScene);

            popupStage.setOnHidden(e -> mainPane.setDisable(false));
            popupStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchOnAction(MouseEvent event) {searchDriver();}

    @FXML
    void searchOnAction2(ActionEvent event) {searchDriver();}

    void searchDriver(){
        String search = txtSearch.getText();

        String check = search.replaceAll("\\d","");

        if(check.equals("D")){
            String driverId = search;

            if (driverId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter an Driver ID or Contact to search!");
                return;
            }

            try {
                DriverDto driverDto = driverModel.searchDriverId(driverId);
                if (driverDto != null) {
                    lblDriverId.setText(driverDto.getDriver_id());
                    txtName.setText(driverDto.getName());
                    txtContact.setText(driverDto.getContact());
                    txtGmail.setText(driverDto.getGmail());
                    lblVehicleId.setText(driverDto.getVehicle_id());

                } else {
                    showAlert(Alert.AlertType.WARNING, "Driver Not Found!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
            }
        }else if(check.equals("V")){

                String vehicleId = search;

                if(vehicleId.isEmpty()){
                    showAlert(Alert.AlertType.WARNING,"Please enter an Vehicle ID to Search!");
                    return;
                }

                try {
                    VehicleDto vehicleDto = vehicleModel.searchvehicleId(vehicleId);

                    if(vehicleDto != null){

                        lblVehicleId.setText(vehicleDto.getVehicle_id());

                    }else {
                        showAlert(Alert.AlertType.WARNING, "Vehicle Not Found!");
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
                DriverDto driverDto = driverModel.searchDriverContact(contact);
                if (driverDto != null) {
                    lblDriverId.setText(driverDto.getDriver_id());
                    txtName.setText(driverDto.getName());
                    txtContact.setText(driverDto.getContact());
                    txtGmail.setText(driverDto.getGmail());
                    lblVehicleId.setText(driverDto.getVehicle_id());

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
        String driverId = lblDriverId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String gmail = txtGmail.getText();
        String vehicleId = lblVehicleId.getText();

        DriverDto driverDto = new DriverDto(driverId, name, contact, gmail,vehicleId);

        try {
            boolean isUpdated = driverModel.updateDriver(driverDto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION,"Successfully Updated the Driver!").show();
                loadData();
                clearPage();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to Updated!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    //    @FXML
//    void saveOnAction(ActionEvent event) {
//        String driverId = txtDriverId.getText();
//        String name = txtName.getText();
//        String contact = txtContact.getText();
//        String gmail = txtGmail.getText();
//        String vehicleId = txtVehicleId.getText();
//
//        DriverDto driverDto = new DriverDto(driverId, name, contact, gmail,vehicleId);
//        try {
//            boolean isSaved = driverModel.saveDriver(driverDto);
//            if (isSaved) {
//                new Alert(Alert.AlertType.INFORMATION,"Successfully saved the driver!").show();
//                loadData();
//                clearPage();
//            }else{
//                new Alert(Alert.AlertType.ERROR,"Failed to Save!").show();
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
