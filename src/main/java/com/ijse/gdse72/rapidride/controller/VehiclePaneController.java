package com.ijse.gdse72.rapidride.controller;

import com.ijse.gdse72.rapidride.dto.VehicleDto;
import com.ijse.gdse72.rapidride.dto.tm.EmployeeTm;
import com.ijse.gdse72.rapidride.dto.tm.VehicleTm;
import com.ijse.gdse72.rapidride.model.VehicleModel;
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

public class VehiclePaneController implements Initializable {


    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<VehicleTm,Double> clmFuelToKm;

    @FXML
    private TableColumn<VehicleTm,String> clmName;

    @FXML
    private TableColumn<VehicleTm,String> clmStatus;

    @FXML
    private TableColumn<VehicleTm,String> clmType;

    @FXML
    private TableColumn<VehicleTm,String> clmVehicleId;

    @FXML
    private Label lblStaus;

    @FXML
    private Label lblVehicleId;

    @FXML
    private ImageView imgSearch;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtFuelToKm;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtType;

    @FXML
    private TableView<VehicleTm> vehicleTabel;

    private final VehicleModel vehicleModel = new VehicleModel();

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearPage();
        loadTable();
        visibleData();
        changeFocusText();
        btnSave.setDisable(true);
    }

    void visibleData(){
        clmVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        clmStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clmFuelToKm.setCellValueFactory(new PropertyValueFactory<>("fuleToKm"));
    }

    private void loadTable(){
        try {
            ArrayList<VehicleDto> vehicleDtos = vehicleModel.getVehicles();
            ObservableList<VehicleTm> vehicleTms = FXCollections.observableArrayList();

            for( VehicleDto vehicleDto : vehicleDtos){
                VehicleTm vehicleTm = new VehicleTm(
                        vehicleDto.getVehicle_id(),
                        vehicleDto.getVehicle_name(),
                        vehicleDto.getVehicle_type(),
                        vehicleDto.getVehicle_status(),
                        vehicleDto.getVehicle_fuel_to_km()
                );
                vehicleTms.add(vehicleTm);
            }
            vehicleTabel.setItems(vehicleTms);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPage(){
        txtSearch.setText("");
        lblVehicleId.setText("");
        txtName.clear();
        txtType.clear();
        lblStaus.setText("available");
        txtFuelToKm.clear();

        try {
            String nextVehicleId = vehicleModel.getNextVehicleId();
            lblVehicleId.setText(nextVehicleId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void fillTextOnAction(ActionEvent event) {


        try {
            String nextVehicleId = vehicleModel.getNextVehicleId();

            if(lblVehicleId.getText().equals(nextVehicleId) &&
                    !(txtName.getText().isEmpty()) &&
                    !(txtType.getText().isEmpty()) &&
                    !(txtFuelToKm.getText().isEmpty())){

                btnSave.setDisable(false);
            }

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
        VehicleTm selectedVehicle = vehicleTabel.getSelectionModel().getSelectedItem();

        if (selectedVehicle != null) {
            lblVehicleId.setText(selectedVehicle.getVehicleId());
            txtName.setText(selectedVehicle.getName());
            txtType.setText(selectedVehicle.getType());
            lblStaus.setText(selectedVehicle.getStatus());
            txtFuelToKm.setText(String.valueOf(selectedVehicle.getFuleToKm()));
        }
    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String id = lblVehicleId.getText();
        String name = txtName.getText();
        String type = txtType.getText();
        String status = lblStaus.getText();
        double km = Double.parseDouble(txtFuelToKm.getText());

        VehicleDto vehicleDto = new VehicleDto(id,type,status,km,name);

        try {
            boolean isSaved = vehicleModel.saveVehicle(vehicleDto);

            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Successfully saved the vehicle!").show();
                loadTable();
                clearPage();
            }else {
                new Alert(Alert.AlertType.ERROR,"Failed to Save!").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void searchOnAction(MouseEvent  event) {searchVehicle();}

    @FXML
    void searchOnAction2(ActionEvent event) {searchVehicle();}

    void searchVehicle(){

        String vehicleId = txtSearch.getText();
        if(vehicleId.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Please enter an Vehicle ID to Search!");
            return;
        }

        try {
            VehicleDto vehicleDto = vehicleModel.searchvehicleId(vehicleId);

            if(vehicleDto != null){

                lblVehicleId.setText(vehicleDto.getVehicle_id());
                lblStaus.setText(vehicleDto.getVehicle_status());
                txtName.setText(vehicleDto.getVehicle_name());
                txtType.setText(vehicleDto.getVehicle_type());
                txtFuelToKm.setText(String.valueOf(vehicleDto.getVehicle_fuel_to_km()));

            }else {
                showAlert(Alert.AlertType.WARNING, "Vehicle Not Found!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        }

    }

    @FXML
    void updateOnAction(ActionEvent event) {
        String id = lblVehicleId.getText();
        String name = txtName.getText();
        String type = txtType.getText();
        String status = lblStaus.getText();
        double km = Double.parseDouble(txtFuelToKm.getText());

        VehicleDto vehicleDto = new VehicleDto(id,type,status,km,name);

        boolean isUpdated = false;
        try {
            isUpdated = vehicleModel.updateVehicle(vehicleDto);

            if(isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Successfully Updated the Vehicle!").show();
                loadTable();
                clearPage();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to Updated!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING,"Database Erro :" + e.getMessage()).show();
        }
    }

    public void changeFocusText() {

        TextField[] textFields = {txtName, txtType, txtFuelToKm};

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
//    void deleteOnAction(ActionEvent event) {
//        VehicleTm selectedVehicle = vehicleTabel.getSelectionModel().getSelectedItem();
//
//        if(selectedVehicle != null){
//            try {
//                boolean isDeleted = vehicleModel.deleteVehicle(selectedVehicle.getVehicleId());
//
//                if(isDeleted){
//
//                    new Alert(Alert.AlertType.INFORMATION, "Vehicle Deleted Successfully!").show();
//                    loadTable();
//                    clearPage();
//
//                }else {
//                    new Alert(Alert.AlertType.ERROR, "Failed to Delete Vehicle!").show();
//                }
//
//            } catch (SQLException e) {
//                new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
//            }
//        }else{
//            new Alert(Alert.AlertType.WARNING, "Please select a Vehicle to delete!").show();
//        }
//    }

}
