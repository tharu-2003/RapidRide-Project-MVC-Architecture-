package com.ijse.gdse72.rapidride.dto;

import lombok.*;

import java.sql.Time;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingDetailsDto {
    private Date booking_date;
    private Time booking_time;
    private String booking_id;
    private String vehicle_id;
}
