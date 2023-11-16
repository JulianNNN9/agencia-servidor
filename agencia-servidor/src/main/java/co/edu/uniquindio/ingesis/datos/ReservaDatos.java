package co.edu.uniquindio.ingesis.datos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservaDatos implements Serializable {

    private String clientID;
    private String mailClient;
    private String selectedGuia;
    private String nroCupos;
    private String selectedPackageName;
}
