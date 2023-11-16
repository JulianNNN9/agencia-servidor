package co.edu.uniquindio.ingesis.datos;

import co.edu.uniquindio.ingesis.model.TouristPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ModificarPaquetesDatos implements Serializable {
    private TouristPackage selectedPackage;
    private String nombrePaquete;
    private double precio;
    private int cupo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
