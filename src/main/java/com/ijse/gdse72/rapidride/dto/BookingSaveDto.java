package com.ijse.gdse72.rapidride.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class BookingSaveDto {

    private String vehicleId;

    private ArrayList<PaymentDto> paymentDtos;
    private ArrayList<BookingDto> bookingDtos;
    private ArrayList<BookingDetailsDto> bookingDetailsDtos;

}
