package co.edu.uniquindio.ingesis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class User implements Serializable {

    private String userId;
    private String password;

}
