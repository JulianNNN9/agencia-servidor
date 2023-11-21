package co.edu.uniquindio.ingesis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class TouristPackage implements Serializable {

    private List<String> destinosName = new ArrayList<>();
    private String name;
    private Double price;
    private Double precioOriginal;
    private Integer quota;
    private LocalDate startDate;
    private LocalDate endDate;
    private long duration;
    private LocalDate fechaDescuento;

}
