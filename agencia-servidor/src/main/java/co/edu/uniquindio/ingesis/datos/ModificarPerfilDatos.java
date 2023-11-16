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
public class ModificarPerfilDatos implements Serializable {
    private String clientID;
    private String nuevoNombre;
    private String nuevoMail;
    private String nuevoNumero;
    private String nuevaResidencia;
}
