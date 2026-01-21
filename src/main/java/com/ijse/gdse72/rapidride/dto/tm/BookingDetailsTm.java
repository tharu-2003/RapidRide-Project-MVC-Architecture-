package com.ijse.gdse72.rapidride.dto.tm;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingDetailsTm {
    private Date booking_date;
    private Time booking_time;
    private String booking_id;
    private String vehicle_id;
}
