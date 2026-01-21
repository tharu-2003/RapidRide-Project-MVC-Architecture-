package com.ijse.gdse72.rapidride.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class VehicleTm {
    private String vehicleId;
    private String status;
    private String name;
    private String type;
    private double fuleToKm;
}
