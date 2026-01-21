package com.ijse.gdse72.rapidride.dto.tm;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentTm {
    private String payment_id;
    private String booking_id;
    private double amount;
    private String payment_method;
    private Date date;
    private Time time;
    private String status;
}
