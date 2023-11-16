package co.edu.uniquindio.ingesis.datos;

import co.edu.uniquindio.ingesis.model.TouristPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EliminarDestinoName implements Serializable {
    private Optional<TouristPackage> packageSeleccionadoOpcional;
    private String selectedDestino;
}
