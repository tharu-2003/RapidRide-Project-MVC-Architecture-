package com.ijse.gdse72.rapidride.dto;

import lombok.*;

import java.sql.Time;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingDto {
    private String booking_id;
    private String ride_to;
    private double distance;
    private Date required_date;
    private double total_amount;
    private String status;
    private int vehicle_count;
    private String customer_id;
}
