package com.ijse.gdse72.rapidride.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class VehicleDto {
    private String vehicle_id;
    private String  vehicle_type;
    private String vehicle_status;
    private double vehicle_fuel_to_km;
    private String vehicle_name;
}
