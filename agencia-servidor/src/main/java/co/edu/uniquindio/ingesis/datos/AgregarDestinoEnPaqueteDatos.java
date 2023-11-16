package co.edu.uniquindio.ingesis.datos;

import co.edu.uniquindio.ingesis.model.TouristPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AgregarDestinoEnPaqueteDatos implements Serializable {
    private String selectedDestino;
    private TouristPackage touristPackage;
}
