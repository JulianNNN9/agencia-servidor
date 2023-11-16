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
public class LogInDatos implements Serializable {
    private String Id;
    private String password;
}
