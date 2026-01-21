package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.DriverDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public ArrayList<CustomerDto> getCustomers() throws SQLException {
        String sql = "SELECT * FROM Customer";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<CustomerDto> customerDtos = new ArrayList<>();

        while (rst.next()){
            CustomerDto customerDto =new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            customerDtos.add(customerDto);
        }
        return customerDtos;
    }

    public boolean saveCustomer(CustomerDto customerDto) throws SQLException {
        String sql = "INSERT INTO Customer(customer_id, name, contact, gmail, user_id) VALUES (?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                customerDto.getCustomer_id(),
                customerDto.getName(),
                customerDto.getContact(),
                customerDto.getGmail(),
                customerDto.getUser_id()
        );
    }

    public boolean deleteCustomer(String customer_id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE customer_id = ? ";
        return CrudUtil.execute(sql, customer_id);
    }

    public String getNextCusomerId() throws SQLException {

        String sql = "SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if (rst.next()){

            String customerId = rst.getString(1);
            String subcustomerId = customerId.substring(1);
            int lastIdIndex = Integer.parseInt(subcustomerId);
            int nextIndex = lastIdIndex + 1;
            String newId = String.format("C%03d", nextIndex);
            return newId;
        }
        return "C001";
    }

    public CustomerDto searchCustomerId(String customerId) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE customer_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, customerId);
        if (rst.next()) {
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    public CustomerDto searchCustomerContact(String contact) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE contact = ? ";
        ResultSet rst = CrudUtil.execute(sql, contact);
        if (rst.next()) {
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }

    public boolean updateCustomer(CustomerDto customerDto) throws SQLException {
        String sql = "UPDATE Customer SET user_id = ?, name = ?, contact = ? , gmail = ? WHERE customer_id = ?";
        return CrudUtil.execute(
                sql,
                customerDto.getUser_id(),
                customerDto.getName(),
                customerDto.getContact(),
                customerDto.getGmail(),
                customerDto.getCustomer_id()
        );
    }
}
