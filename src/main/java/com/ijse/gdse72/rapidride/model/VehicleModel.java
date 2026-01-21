package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.VehicleDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleModel {
    public ArrayList<VehicleDto> getVehicles()throws SQLException {
        String sql = "SELECT * FROM Vehicle";

        ResultSet rst = CrudUtil.execute(sql);
        ArrayList<VehicleDto> vehicleDtos = new ArrayList<>();

        while (rst.next()){
            VehicleDto vehicleDto = new VehicleDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getString(5)
            );
            vehicleDtos.add(vehicleDto);
        }
        return vehicleDtos;
    }

    public boolean saveVehicle(VehicleDto vehicleDto) throws SQLException {

        String sql = "INSERT INTO Vehicle (vehicle_id, name, type, fuel_to_km, status) VALUES (?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                vehicleDto.getVehicle_id(),
                vehicleDto.getVehicle_name(),
                vehicleDto.getVehicle_type(),
                vehicleDto.getVehicle_fuel_to_km(),
                vehicleDto.getVehicle_status()
        );

    }

    public boolean deleteVehicle(String vehicle_id) throws SQLException {
        String sql = "DELETE FROM Vehicle WHERE vehicle_id = ?";
        return CrudUtil.execute(sql,vehicle_id);
    }

    public boolean updateVehicle(VehicleDto vehicleDto) throws SQLException {
        String sql = "UPDATE Vehicle SET name = ?, type = ?, fuel_to_km = ?, status = ? WHERE vehicle_id = ?";
        return CrudUtil.execute(
                sql,
                vehicleDto.getVehicle_name(),
                vehicleDto.getVehicle_type(),
                vehicleDto.getVehicle_fuel_to_km(),
                vehicleDto.getVehicle_status(),
                vehicleDto.getVehicle_id()
        );
    }

    public String getNextVehicleId() throws SQLException {
        String sql = "SELECT vehicle_id FROM Vehicle ORDER BY vehicle_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if(rst.next()){

            String vehicleId = rst.getString(1);
            String subVehicleId = vehicleId.substring(1);

            int lastVehicleId = Integer.parseInt(subVehicleId);
            int nextVehicleId = lastVehicleId + 1;

            String newVehicleId = String.format("V%03d",nextVehicleId);
            return newVehicleId;
        }
        return "V001";
    }

    public VehicleDto searchvehicleId(String vehicleId) throws SQLException {
        String sql = "SELECT * FROM Vehicle WHERE vehicle_id = ?";

        ResultSet rst =CrudUtil.execute(sql,vehicleId);

        if(rst.next()){

            return new VehicleDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    public String getVehicleId() {
        String sql = "SELECT vehicle_id FROM Vehicle WHERE status = 'available' ORDER BY vehicle_id LIMIT 1";
        String vehicleId = null;

        try (ResultSet rs = CrudUtil.execute(sql)) {
            if (rs.next()) {
                vehicleId = rs.getString("vehicle_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleId;
    }
}
