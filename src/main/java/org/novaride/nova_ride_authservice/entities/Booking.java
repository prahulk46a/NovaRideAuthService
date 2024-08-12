package org.novaride.nova_ride_authservice.entities;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Booking extends BaseModel{

    @Enumerated(EnumType.STRING)

    private BookingStatus bookingStatus;

    private Date startTime;
    private Date endTime;
    private Long totalDistance;

    @ManyToOne
    private Passenger passenger;


    @ManyToOne
    private Driver driver;


}