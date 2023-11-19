package co.edu.uniquindio.ingesis.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @NoArgsConstructor @AllArgsConstructor @SuperBuilder

public class Client extends User implements Serializable {

    private String fullName;
    private String mail;
    private String phoneNumber;
    private String residence;
    private List<Reservation> reservationList = new ArrayList<>();
    private List<Double> descuentos = new ArrayList<>();

}
