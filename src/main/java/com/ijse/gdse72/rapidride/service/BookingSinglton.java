package com.ijse.gdse72.rapidride.service;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BookingSinglton {
    private static final BookingSinglton instance = new BookingSinglton();

    private String bookingId;
    private String customerId;
    private String status;
    private double amount;
    private String rideTo;
    private double distance;
    private Date date;
    private int vehicleCount;

    public static BookingSinglton getInstance(){
        return instance;
    }
}
