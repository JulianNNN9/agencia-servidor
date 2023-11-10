package co.edu.uniquindio.ingesis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class Destino implements Serializable {

    private String name;
    private String city;
    private String description;
    private List<String> imagesHTTPS = new ArrayList<>();
    private String weather;
    private List<String> comentarios;
    private List<Integer> ratingList = new ArrayList<>();
    private Double rating;

}
