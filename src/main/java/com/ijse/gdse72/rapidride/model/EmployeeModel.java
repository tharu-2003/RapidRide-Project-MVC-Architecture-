package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.UserDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {
    public ArrayList<EmployeeDto> getEmployees() throws SQLException {
        String sql = "SELECT * FROM Employee";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<EmployeeDto> employeesDtos = new ArrayList<>();

        while (rst.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            employeesDtos.add(employeeDto);
        }
        return employeesDtos;
    }
    public boolean saveEmployee(EmployeeDto employeeDto) throws SQLException {

        String sql = "insert into Employee(employee_id,name,role,contact,gmail) values(?,?,?,?,?)";

        return CrudUtil.execute(
                    sql,
                    employeeDto.getEmployee_id(),
                    employeeDto.getEmployee_name(),
                    employeeDto.getEmployee_role(),
                    employeeDto.getEmployee_contact(),
                    employeeDto.getEmployee_gmail()
            );

    }

    public boolean deleteEmployee(String employeeId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE employee_id = ? ";
        return CrudUtil.execute(sql, employeeId);
    }

    public boolean updateEmployee(EmployeeDto employeeDto) throws SQLException {
        String sql = "UPDATE Employee SET name = ?, role = ?, contact = ? , gmail = ? WHERE employee_id = ?";
        return CrudUtil.execute(
                sql,
                employeeDto.getEmployee_name(),
                employeeDto.getEmployee_role(),
                employeeDto.getEmployee_contact(),
                employeeDto.getEmployee_gmail(),
                employeeDto.getEmployee_id()
        );
    }

    public EmployeeDto searchEmployeeId(String employeeId) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE employee_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, employeeId);
        if (rst.next()) {
            return new EmployeeDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    public EmployeeDto searchEmployeeContact(String contact) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE contact = ? ";
        ResultSet rst = CrudUtil.execute(sql, contact);
        if (rst.next()) {
            return new EmployeeDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    public String getNextEmplpyeeId() throws SQLException {
        String sql = "SELECT employee_id FROM Employee ORDER BY employee_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if(rst.next()){

            String employeeId = rst.getString(1);
            String subEmployeeId = employeeId.substring(1);
            int lastEmployeeId = Integer.parseInt(subEmployeeId);
            int nextEmployeeId = lastEmployeeId + 1;
            String newEmployeeId = String.format("E%03d",nextEmployeeId);
            return newEmployeeId;
        }
        return "E001";
    }

    public boolean chekEmployee(String employeeId) throws SQLException {
        String sql = "SELECT employee_id FROM Employee WHERE employee_id = ? ";

        ResultSet rst = CrudUtil.execute(sql, employeeId);

        if (rst.next()) {
            return true;
        }
        return false;

    }
}
