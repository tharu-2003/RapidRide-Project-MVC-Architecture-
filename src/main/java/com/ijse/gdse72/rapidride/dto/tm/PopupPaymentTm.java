package com.ijse.gdse72.rapidride.dto.tm;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PopupPaymentTm {
    private String payment_id;
    private String booking_id;
    private String payment_method;
    private double amount;
    private Date date;
    private Time time;
    private String status;
}
