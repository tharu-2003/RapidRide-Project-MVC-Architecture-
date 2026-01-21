package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.DriverDto;
import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DriverModel {
    public ArrayList<DriverDto> getDrivers() throws SQLException {
        String sql = "SELECT * FROM Driver";

        ResultSet rst = CrudUtil.execute(sql);
        ArrayList<DriverDto> driversDtos = new ArrayList<>();

        while (rst.next()) {
            DriverDto driverDto = new DriverDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            driversDtos.add(driverDto);
        }
        return driversDtos;
    }
    public boolean saveDriver(DriverDto driverDto) throws SQLException {
        String sql = "insert into Driver(driver_id,name,contact,gmail,vehicle_id) values(?,?,?,?,?)";
        return CrudUtil.execute(sql,driverDto.getDriver_id(),driverDto.getName(),driverDto.getContact(),
                driverDto.getGmail(),driverDto.getVehicle_id());

    }

    public boolean deleteDriver(String driver_id) throws SQLException {
        String sql = "DELETE FROM Driver WHERE driver_id = ? ";
        return CrudUtil.execute(sql, driver_id);
    }

    public ArrayList<String> getAllVehicleId() throws SQLException {
        String sql = "SELECT vehicle_id FROM Driver";

        ResultSet rst = CrudUtil.execute(sql);
        ArrayList<String> vehiclaIds = new ArrayList<>();

        while (rst.next()){
            vehiclaIds.add(rst.getString(1));
        }
        return vehiclaIds;
    }

    public boolean updateDriver(DriverDto driverDto) throws SQLException {
        String sql = "UPDATE Driver SET name = ? , contact = ? , gmail = ? , vehicle_id = ? WHERE driver_id = ?";
        return CrudUtil.execute(
                sql,
                driverDto.getName(),
                driverDto.getContact(),
                driverDto.getGmail(),
                driverDto.getVehicle_id(),
                driverDto.getDriver_id()
        );
    }

    public String getNextDriverId() throws SQLException {
        String sql = "SELECT driver_id FROM Driver ORDER BY driver_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if(rst.next()){
            String driverId = rst.getString(1);
            String subDriverId = driverId.substring(1);

            int lastDriverId = Integer.parseInt(subDriverId);
            int nextDriverId = lastDriverId + 1;

            String newDriverId = String.format("D%03d",nextDriverId);
            return newDriverId;
        }
        return "D001";
    }

    public DriverDto searchDriverId(String driverId) throws SQLException {
        String sql = "SELECT * FROM Driver WHERE driver_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, driverId);
        if (rst.next()) {
            return new DriverDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    public DriverDto searchDriverContact(String contact) throws SQLException {
        String sql = "SELECT * FROM Driver WHERE contact = ? ";
        ResultSet rst = CrudUtil.execute(sql, contact);
        if (rst.next()) {
            return new DriverDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
}
