package com.ijse.gdse72.rapidride.model;

import com.ijse.gdse72.rapidride.dto.CustomerDto;
import com.ijse.gdse72.rapidride.dto.EmployeeDto;
import com.ijse.gdse72.rapidride.dto.UserDto;
import com.ijse.gdse72.rapidride.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {
    public ArrayList<UserDto> getUsers() throws SQLException {

        String sql = "SELECT * FROM User";

        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<UserDto> userDtos = new ArrayList<>();

        while (rst.next()){
            UserDto userDto = new UserDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public UserDto searchUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM User WHERE user_id = ? ";
        ResultSet rst = CrudUtil.execute(sql, userId);
        if (rst.next()) {
            return new UserDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    public UserDto searchUserName(String userName) throws SQLException {
        String sql = "SELECT * FROM User WHERE user_name = ? ";
        ResultSet rst = CrudUtil.execute(sql, userName);
        if (rst.next()) {
            return new UserDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }

    public boolean deleteUser(String userId) throws SQLException {
        String sql = "DELETE FROM User WHERE user_id = ? ";
        return CrudUtil.execute(sql, userId);
    }

    public boolean updateUser(UserDto userDto) throws SQLException {
        String sql = "UPDATE User SET employee_id = ?, user_name = ?, name = ? , password = ? WHERE user_id = ?";
        return CrudUtil.execute(
                sql,
                userDto.getEmployee_id(),
                userDto.getUser_name(),
                userDto.getName(),
                userDto.getPassword(),
                userDto.getUser_id()
        );
    }

    public UserDto confirmation(String userId) throws SQLException {

        String sql = "SELECT * FROM User WHERE user_id = ?";

        ResultSet rst = CrudUtil.execute(sql, userId);

        if (rst.next()) {
            return new UserDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }

    public boolean saveUser(UserDto userDto) throws SQLException {
        String sql = "INSERT INTO User(user_id, employee_id, name, user_name,password) VALUES (?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                userDto.getUser_id(),
                userDto.getEmployee_id(),
                userDto.getName(),
                userDto.getUser_name(),
                userDto.getPassword()
        );
    }

    public String getNextCusomerId() throws SQLException {

        String sql = "SELECT user_id FROM User ORDER BY user_id DESC LIMIT 1";

        ResultSet rst = CrudUtil.execute(sql);

        if (rst.next()){

            String userId = rst.getString(1);
            String subUserId = userId.substring(1);
            int lastIdIndex = Integer.parseInt(subUserId);
            int nextIndex = lastIdIndex + 1;
            String newId = String.format("U%03d", nextIndex);
            return newId;
        }
        return "U001";
    }
}
