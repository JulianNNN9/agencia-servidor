package co.edu.uniquindio.ingesis.datos;

import co.edu.uniquindio.ingesis.model.TouristGuide;
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
public class EliminarLenguajeDatos implements Serializable {
    private TouristGuide guideSeleccionadoOpcional;
    private String selectedLenguaje;
}
