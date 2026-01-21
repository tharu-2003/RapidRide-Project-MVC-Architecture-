package com.ijse.gdse72.rapidride.dto.tm;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingTm {
    private String booking_id;
    private String ride_to;
    private double distance;
    private Date required_date;
    private double total_amount;
    private String status;
    private int vehicle_count;
    private String customer_id;
}
