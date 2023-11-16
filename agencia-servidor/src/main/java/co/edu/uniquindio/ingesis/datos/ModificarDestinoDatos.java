package co.edu.uniquindio.ingesis.datos;

import co.edu.uniquindio.ingesis.model.Destino;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ModificarDestinoDatos implements Serializable {
    private Destino selectedDestino;
    private String nuevoName;
    private String nuevaCiudad;
    private String nuevaDescripcion;
    private String nuevoClima;
}
