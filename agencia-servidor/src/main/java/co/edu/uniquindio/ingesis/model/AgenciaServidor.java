package co.edu.uniquindio.ingesis.model;

import co.edu.uniquindio.ingesis.enums.ReservationStatus;
import co.edu.uniquindio.ingesis.exceptions.*;
import co.edu.uniquindio.ingesis.utils.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.extern.java.Log;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@Log @Getter

public class AgenciaServidor {

    List<TouristGuide> touristGuides;
    List<Reservation> reservations;
    List<TouristPackage> touristPackages;
    List<Destino> destinos;
    List<Client> clients;
    List<Admin> admins;

    private static AgenciaServidor agenciaServidor;

    public AgenciaServidor() {

        try {

            FileHandler fh = new FileHandler("logs.log", true);
            fh.setFormatter(new SimpleFormatter());
            log.addHandler(fh);

        } catch (IOException e) {
            log.severe(e.getMessage());
        }

        //Cargar guía

        new Thread(() -> {

            ArrayList<TouristGuide> aux = (ArrayList<TouristGuide>) archiveUtils.deserializerObjet("src/main/resources/persistencia/touristGuides.ser");

            this.touristGuides = Objects.requireNonNullElseGet(aux, ArrayList::new);

        }).start();

        //Cargar reservaciones

        new Thread(() -> {
            ArrayList<Reservation> aux1 = (ArrayList<Reservation>) archiveUtils.deserializerObjet("src/main/resources/persistencia/reservations.ser");

            this.reservations = Objects.requireNonNullElseGet(aux1, ArrayList::new);

        }).start();

        //Cargar paquetes

        new Thread(() -> {

            ArrayList<TouristPackage> aux2 = (ArrayList<TouristPackage>) archiveUtils.deserializerObjet("src/main/resources/persistencia/touristPackages.ser");

            this.touristPackages = Objects.requireNonNullElseGet(aux2, ArrayList::new);

        }).start();

        //Cargar destinos

        new Thread(() -> {

            ArrayList<Destino> aux3 = (ArrayList<Destino>) archiveUtils.deserializerObjet("src/main/resources/persistencia/destinos.ser");

            this.destinos = Objects.requireNonNullElseGet(aux3, ArrayList::new);

        }).start();


        //Cargar clientes

        new Thread(() -> {

            ArrayList<Client> aux4 = (ArrayList<Client>) archiveUtils.deserializerObjet("src/main/resources/persistencia/clients.ser");

            this.clients = Objects.requireNonNullElseGet(aux4, ArrayList::new);

        }).start();


        //Cargar admins

        new Thread(() -> {

            ArrayList<Admin> aux5 = (ArrayList<Admin>) archiveUtils.deserializerObjet("src/main/resources/persistencia/admins.ser");

            this.admins = Objects.requireNonNullElseGet(aux5, ArrayList::new);

        }).start();

    }

    public static AgenciaServidor getInstance(){

        if (agenciaServidor == null){
            agenciaServidor = new AgenciaServidor();
        }

        log.info("Se ha creado una instancia de Travel Agency");

        return agenciaServidor;

    }

    public void serializarDestinos(){
        archiveUtils.serializerObjet("src/main/resources/persistencia/destinos.ser", destinos);
    }

    public void serializarGuias(){
        archiveUtils.serializerObjet("src/main/resources/persistencia/touristGuides.ser", touristGuides);
    }

    public void serializarPaquetes(){
        archiveUtils.serializerObjet("src/main/resources/persistencia/touristPackages.ser", touristPackages);
    }

    public void serizalizarClientes(){
        archiveUtils.serializerObjet("src/main/resources/persistencia/clients.ser", clients);
    }

    public void serializarReservaciones(){
        archiveUtils.serializerObjet("src/main/resources/persistencia/reservations.ser", reservations);
    }

    /*
     * Primer método adicional
     */

    public void recompensasPorReservas(Client client){

        if (client.getReservationList().size() > 2){
            /* TODO implementar un metodo que avise al cliente que en la próximo reserva tendrá 10% de descuento */
        }

        if (client.getReservationList().size() > 4){
            /* TODO implementar un metodo que avise al cliente que en la próximo reserva tendrá 20% de descuento */
        }

        if (client.getReservationList().size() > 7){
            /* TODO implementar un metodo que avise al cliente que en la próximo reserva tendrá 30% de descuento */
        }

        /* TODO el ciente debería tener una nueva lista llamada descuentos, se deben ir añadiendo los descuentos según los gana y validarlos al momento de reservar y aplicar ese descuento*/
    }

    /**
     * Segundo método adicinal
     */

    public void alertaOfertasEspeciales(){
        /* TODO implementar un metodo que avise al cliente pos descuentos (Fin de año, navidad, semana santa)*/
    }

    public void hacerReservacion(String clientID, String mailClient, Toggle selectedToggle, RadioButton radioBttonSI, RadioButton radioBttonNO, String selectedGuia, String nroCupos, String selectedPackageName) throws AtributoVacioException, CuposInvalidosException {

        Optional<TouristPackage> aPackage = touristPackages.stream().filter(touristPackage -> touristPackage.getName().equals(selectedPackageName)).findFirst();

        String detallesReserva = "";

        if (aPackage.isPresent()) {
             detallesReserva = "Detalles de la reserva: \n" +
                    "Destino reservado: " + aPackage.get().getName() + "\n" +
                    "Fecha de la reserva: " + LocalDate.now() + "\n" +
                    "Precio del paquete: " + aPackage.get().getPrice() + "\n" +
                    "Número de cupos: " + nroCupos + "\n" +
                    "Duración: " + aPackage.get().getDuration();
        }

        if (selectedToggle == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (nroCupos == null || nroCupos.isEmpty() ||
                selectedPackageName == null || selectedPackageName.isEmpty()) {

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (selectedToggle.equals(radioBttonSI)) {

            if (selectedGuia == null || selectedGuia.isEmpty()) {

                createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
                log.info("Se ha intentado agregar un destino con campos vacios.");
                throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
            }

            Optional<TouristPackage> touristPackage = touristPackages.stream().filter(touristPackage1 -> touristPackage1.getName().equals(selectedPackageName)).findFirst();

            if (touristPackage.isPresent()) {

                if (Integer.parseInt(nroCupos) > touristPackage.get().getQuota()) {

                    createAlertError("Cupos inválidos", "La cantidad de cupos con los que desea reservar exceden los permitidos en el paquete");
                    log.info("Cupos inválidos.");
                    throw new CuposInvalidosException("Cupos inválidos.");
                }

                Optional<TouristGuide> touristGuide = touristGuides.stream().filter(touristGuide1 -> touristGuide1.getFullName().equals(selectedGuia)).findFirst();

                Reservation nuevaReservacion = Reservation.builder()
                        .touristPackage(touristPackage.get())
                        .requestDate(LocalDate.now())
                        .reservationStatus(ReservationStatus.PENDING)
                        .startDate(touristPackage.get().getStartDate())
                        .endDate(touristPackage.get().getEndDate())
                        .touristGuide(touristGuide.get())
                        .numberOfPeople(Integer.valueOf(nroCupos))
                        .build();

                createAlertInfo("Reservación éxitosa.", "Información", "Has hecho una reservación del paquete " + selectedPackageName + " para el " + touristPackage.get().getStartDate() + ".");
                log.info("se ha hecho una reservación del paquete " + selectedPackageName + " para el " + touristPackage.get().getStartDate() + ".");

                Optional<Client> client = clients.stream().filter(client1 -> client1.getUserId().equals(clientID)).findFirst();

                if (client.isPresent()) {
                    if (client.get().getReservationList() == null) {
                        client.get().setReservationList(new ArrayList<>());
                        client.get().getReservationList().add(nuevaReservacion);
                        reservations.add(nuevaReservacion);
                    } else {
                        client.get().getReservationList().add(nuevaReservacion);
                        reservations.add(nuevaReservacion);
                    }
                }
            }
        }

        if (selectedToggle.equals(radioBttonNO)) {

            Optional<TouristPackage> touristPackage = touristPackages.stream().filter(touristPackage1 -> touristPackage1.getName().equals(selectedPackageName)).findFirst();

            if (Integer.parseInt(nroCupos) > touristPackage.get().getQuota()){

                createAlertError("Cupos inválidos", "La cantidad de cupos con los que desea reservar exceden los permitidos en el paquete");
                log.info("Cupos inválidos.");
                throw new CuposInvalidosException("Cupos inválidos.");
            }

            Reservation nuevaReservacion = Reservation.builder()
                    .touristPackage(touristPackage.get())
                    .requestDate(LocalDate.now())
                    .reservationStatus(ReservationStatus.PENDING)
                    .startDate(touristPackage.get().getStartDate())
                    .endDate(touristPackage.get().getEndDate())
                    .touristGuide(null)
                    .numberOfPeople(Integer.valueOf(nroCupos))
                    .build();

            createAlertInfo("Reservación éxitosa.","Información","Has hecho una reservación del paquete " + selectedPackageName + " para el " + touristPackage.get().getStartDate() + ".");
            log.info("se ha hecho una reservación del paquete " + selectedPackageName + " para el " + touristPackage.get().getStartDate() + ".");

            Optional<Client> client = clients.stream().filter(client1 -> client1.getUserId().equals(clientID)).findFirst();

            if (client.isPresent()){
                if (client.get().getReservationList() == null){
                    client.get().setReservationList(new ArrayList<>());
                    client.get().getReservationList().add(nuevaReservacion);
                    reservations.add(nuevaReservacion);
                } else {
                    client.get().getReservationList().add(nuevaReservacion);
                    reservations.add(nuevaReservacion);
                }
            }

        }

        serizalizarClientes();
        serializarReservaciones();
    }

    public void calificarGuia(TouristGuide touristGuide, Toggle calificacionSelectedToggle, RadioButton radioBtton1Estrella, RadioButton radioBtton2Estrella, RadioButton radioBtton3Estrella, RadioButton radioBtton4Estrella, RadioButton radioBtton5Estrella) throws AtributoVacioException {

        if (calificacionSelectedToggle == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (calificacionSelectedToggle.equals(radioBtton1Estrella)){
            touristGuide.getRatingList().add(1);
        }
        if (calificacionSelectedToggle.equals(radioBtton2Estrella)){
            touristGuide.getRatingList().add(2);
        }
        if (calificacionSelectedToggle.equals(radioBtton3Estrella)){
            touristGuide.getRatingList().add(3);
        }
        if (calificacionSelectedToggle.equals(radioBtton4Estrella)){
            touristGuide.getRatingList().add(4);
        }
        if (calificacionSelectedToggle.equals(radioBtton5Estrella)){
            touristGuide.getRatingList().add(5);
        }

        Double promedioCalificaciones = touristGuide.getRatingList().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        touristGuide.setRating(promedioCalificaciones);

        serializarGuias();
    }

    public void calificarDestino(Destino destino, String comentario, Toggle selectedToggle, RadioButton radioBtton1EstrellaDestino, RadioButton radioBtton2EstrellaDestino, RadioButton radioBtton3EstrellaDestino, RadioButton radioBtton4EstrellaDestino, RadioButton radioBtton5EstrellaDestino) throws AtributoVacioException {

        if (selectedToggle == null){
            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (comentario != null){
            destino.getComentarios().add(comentario);
        }

        if (selectedToggle.equals(radioBtton1EstrellaDestino)){
            destino.getRatingList().add(1);
        }
        if (selectedToggle.equals(radioBtton2EstrellaDestino)){
            destino.getRatingList().add(2);
        }
        if (selectedToggle.equals(radioBtton3EstrellaDestino)){
            destino.getRatingList().add(3);
        }
        if (selectedToggle.equals(radioBtton4EstrellaDestino)){
            destino.getRatingList().add(4);
        }
        if (selectedToggle.equals(radioBtton5EstrellaDestino)){
            destino.getRatingList().add(5);
        }

        Double promedioCalificaciones = destino.getRatingList().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        destino.setRating(promedioCalificaciones);

        serializarDestinos();

    }

    public void modificarDestino(Destino selectedDestino, String nuevoNombre, String nuevaCiudad, String nuevaDescrpcion, String nuevaLocalDate) throws AtributoVacioException {

        if (nuevoNombre.isEmpty() ||
                nuevaCiudad.isEmpty() ||
                nuevaDescrpcion.isEmpty() ||
                nuevaLocalDate.isEmpty()) {

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        selectedDestino.setName(nuevoNombre);
        selectedDestino.setCity(nuevaCiudad);
        selectedDestino.setDescription(nuevaDescrpcion);
        selectedDestino.setWeather(nuevaLocalDate);

        serializarDestinos();

    }
    public void modificarPaquete(TouristPackage selectedPackage, String nuevoNombrePaquete, double nuevoPrecio, int nuevosCupos, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin) throws AtributoVacioException {

        if ( nuevoNombrePaquete == null || nuevoNombrePaquete.isEmpty() ||
                nuevoPrecio < 0 ||
                nuevosCupos < 0 ||
                nuevaFechaInicio == null ||
                nuevaFechaFin == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        selectedPackage.setName(nuevoNombrePaquete);
        selectedPackage.setPrice(nuevoPrecio);
        selectedPackage.setQuota(nuevosCupos);
        selectedPackage.setStartDate(nuevaFechaInicio);
        selectedPackage.setEndDate(nuevaFechaFin);

        serializarPaquetes();

    }

    public void modificarGuia(TouristGuide selectedGuia, String nuevoGuideID, String nuevoGuideName, String nuevaExperiencia, String nuevoRating) throws AtributoVacioException {

        if (nuevoGuideID == null || nuevoGuideID.isEmpty() ||
                nuevoGuideName == null || nuevoGuideName.isEmpty() ||
                nuevaExperiencia == null || nuevaExperiencia.isEmpty() ||
                nuevoRating == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un guia con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un guia con campos vacios.");
        }

        selectedGuia.setId(nuevoGuideID);
        selectedGuia.setFullName(nuevoGuideName);
        selectedGuia.setExperience(nuevaExperiencia);
        selectedGuia.setRating(Double.valueOf(nuevoRating));

        serializarGuias();
    }

    public void modificarPerfil(String clientID, String nuevoName, String nuevoMail, String nuevoNumero, String nuevoResidence) throws AtributoVacioException {

        if (nuevoName == null || nuevoName.isEmpty() ||
                nuevoMail == null || nuevoMail.isEmpty() ||
                nuevoNumero == null || nuevoNumero.isEmpty() ||
                nuevoResidence == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un guia con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un guia con campos vacios.");
        }

        Optional<Client> client = clients.stream().filter(client1 -> client1.getUserId().equals(clientID)).findFirst();

        if (client.isPresent()){
            client.get().setFullName(nuevoName);
            client.get().setMail(nuevoMail);
            client.get().setPhoneNumber(nuevoNumero);
            client.get().setResidence(nuevoResidence);
        }

        serizalizarClientes();

    }

    public void eliminarDestinoName(Optional<TouristPackage> touristPackage, String destinoABorrar) {

        if (touristPackage.isPresent()){
            List<String> destinosSinEliminar = touristPackage.get().getDestinosName().stream().filter(s -> !s.equals(destinoABorrar)).toList();

            touristPackage.get().getDestinosName().clear();
            touristPackage.get().getDestinosName().addAll(destinosSinEliminar );

            serializarPaquetes();
        }

    }

    public void eliminarRuta(Optional<Destino> destino, String rutaABorrar){

        if (destino.isPresent()) {

            List<String> rutasSinEliminar;

            rutasSinEliminar = destino.get().getImagesHTTPS().stream().filter(s -> !s.equals(rutaABorrar)).toList();

            destino.get().getImagesHTTPS().clear();
            destino.get().getImagesHTTPS().addAll(rutasSinEliminar);

            serializarDestinos();
        }
    }

    public void eliminarLenguaje(Optional<TouristGuide> touristGuide, String lenguajeABorrar){

        if (touristGuide.isPresent()) {

            List<String> languajeSinEliminar = touristGuide.get().getLanguages().stream().filter(s -> !s.equals(lenguajeABorrar)).toList();

            touristGuide.get().getLanguages().clear();
            touristGuide.get().getLanguages().addAll(languajeSinEliminar);

            serializarGuias();
        }
    }


    public void eliminarDestino(ObservableList<Destino> destinoObservableList, Destino selectedDestino) {
        destinoObservableList.remove(selectedDestino);
        destinos.removeIf(destino -> destino.equals(selectedDestino));
        serializarDestinos();
    }

    public void eliminarPaquete(ObservableList<TouristPackage> packageObservableList, TouristPackage selectedPackage) {
        packageObservableList.remove(selectedPackage);
        touristPackages.removeIf(touristPackage -> touristPackage.equals(selectedPackage));
        serializarPaquetes();
    }

    public void eliminarGuia(ObservableList<TouristGuide> touristGuideObservableList, TouristGuide selectedGuia) {
        touristGuideObservableList.remove(selectedGuia);
        touristGuides.removeIf(touristGuide -> touristGuide.equals(selectedGuia));
        serializarGuias();
    }

    public void agregarGuia(ObservableList<TouristGuide> touristGuideObservableList, TouristGuide nuevoGuia) throws AtributoVacioException, RepeatedInformationException {

        if (nuevoGuia.getId() == null || nuevoGuia.getId().isEmpty() ||
                nuevoGuia.getFullName() == null || nuevoGuia.getFullName().isEmpty() ||
                nuevoGuia.getExperience() == null || nuevoGuia.getExperience().isEmpty() ||
                nuevoGuia.getRating() == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un guia con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un guia con campos vacios.");
        }

        if (touristGuideObservableList.stream().anyMatch(touristGuide -> touristGuide.getId().equals(nuevoGuia.getId()))){

            createAlertError("Guia existente", "El guia que trataba de agregar ya se encuentra registrado.");
            log.severe("Se ha intentado registrar un guia existente.");
            throw new RepeatedInformationException("Se ha intentado registrar un guia existente.");
        }

        touristGuideObservableList.add(nuevoGuia);
        agenciaServidor.touristGuides.add(nuevoGuia);

        serializarGuias();

        log.info("Se ha registrado un nuevo guia.");

    }

    public void agregarPaquete(ObservableList<TouristPackage> packageObservableList, TouristPackage nuevoPaquete) throws AtributoVacioException, RepeatedInformationException, ErrorEnIngresoFechasException {

        if (LocalDate.now().isAfter(nuevoPaquete.getStartDate())){

            createAlertError("Error en el ingreso de fechas", "Las fechas que desea ingresar son inválidas, verifiquelas.");
            log.info("Las fechas fueron incorrectamente colocadas.");
            throw new ErrorEnIngresoFechasException("Las fechas fueron incorrectamente colocadas.");
        }

        if ( nuevoPaquete.getName() == null || nuevoPaquete.getName().isEmpty() ||
                nuevoPaquete.getPrice() == null || nuevoPaquete.getPrice().isNaN() ||
                nuevoPaquete.getQuota() == null ||
                nuevoPaquete.getStartDate() == null ||
                nuevoPaquete.getEndDate() == null){

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (packageObservableList.stream().anyMatch(touristPackage -> touristPackage.getName().equals(nuevoPaquete.getName()))){

            createAlertError("Paquete existente", "El paquete que trataba de agregar ya se encuentra registrado.");
            log.severe("Se ha intentado crear un paquete existente.");
            throw new RepeatedInformationException("Se ha intentado crear un paquete existente.");
        }

        if (nuevoPaquete.getDuration() < 0){
            createAlertError("Error en el ingreso de fechas", "Las fechas que desea ingresar son inválidas, verifiquelas.");
            log.info("Las fechas fueron incorrectamente colocadas, la fecha de inicio no puede ser después de la fecha de fin.");
            throw new ErrorEnIngresoFechasException("Las fechas fueron incorrectamente colocadas, la fecha de inicio no puede ser después de la fecha de fin.");
        }

        packageObservableList.add(nuevoPaquete);
        agenciaServidor.touristPackages.add(nuevoPaquete);

        serializarPaquetes();

        log.info("Se ha creado un nuevo paquete.");

    }

    public void agregarDestino(ObservableList<Destino> destinoObservableList, Destino nuevoDestino) throws RepeatedInformationException, AtributoVacioException {

        if (nuevoDestino.getName().isEmpty() ||
                nuevoDestino.getCity().isEmpty() ||
                nuevoDestino.getDescription().isEmpty() ||
                nuevoDestino.getWeather().isEmpty()) {

            createAlertError("Campos obligatorios", "Los campos marcados con (*) son oblogatorios");
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (destinoObservableList.stream().anyMatch(destination -> destination.getName().equals(nuevoDestino.getName()))){

            createAlertError("Destino existente", "El destino que trataba de agregar ya se encuentra registrado.");
            log.severe("Se ha intentado crear un Destino existente.");
            throw new RepeatedInformationException("Se ha intentado crear un Destino existente.");
        }

        destinoObservableList.add(nuevoDestino);
        agenciaServidor.destinos.add(nuevoDestino);

        serializarDestinos();

        log.info("Se ha creado un nuevo destino.");

        }

    public void agregarImagenDestino(ObservableList<String> observableListRutas, String ruta, Destino destino) throws RutaInvalidaException, RepeatedInformationException {

        File archivo = new File(ruta);
        boolean esRutaDeArchivo = archivo.exists() && archivo.isFile();

        if (!esRutaDeArchivo){
            createAlertError("Error en la ruta", "La ruta que trata de ingresar es inválida o inexistente.");
            log.severe("Se ha intentado agregar una imagen invalida a un destino.");
            throw new RutaInvalidaException("Se ha intentado agregar una imagen invalida a un destino.");
        }

        if (observableListRutas.stream().anyMatch(string -> string.equals(ruta))){

            createAlertError("Ruta existente", "La ruta que trataba de agregar ya se encuentra registrada.");
            log.severe("Se ha intentado crear una ruta existente.");
            throw new RepeatedInformationException("Se ha intentado crear una ruta existente.");
        }

        observableListRutas.add(ruta);

        for (Destino d : destinos) {
            if (d.equals(destino)) {
                d.getImagesHTTPS().add(ruta);
                break;
            }
        }

        serializarDestinos();
    }


    public void agregarLenguajeGuia(ObservableList<String> observableListLenguajes, String lenguaje, TouristGuide touristGuide) throws RepeatedInformationException {

        if (observableListLenguajes.stream().anyMatch(string -> string.equals(lenguaje))){

            createAlertError("Lenguaje ya ingresado", "La lenguaje que trataba de agregar ya se encuentra agregado.");
            log.severe("Se ha intentado agregar un lenaguje existente.");
            throw new RepeatedInformationException("Se ha intentado agregar un lenaguje existente.");
        }

        observableListLenguajes.add(lenguaje);

        for (TouristGuide t : touristGuides) {
            if (t.equals(touristGuide)) {
                t.getLanguages().add(lenguaje);
                break;
            }
        }

        serializarGuias();
    }

    public void agregarDestinoEnPaquete(ObservableList<String> observableListDestinationName, String selectedItem, TouristPackage touristPackage) throws RepeatedInformationException {

        if (observableListDestinationName.stream().anyMatch(string -> string.equals(selectedItem))){

            createAlertError("Destino existente", "La destino que trataba de agregar ya se encuentra registrada.");
            log.severe("Se ha intentado agregar un destino existente.");
            throw new RepeatedInformationException("Se ha intentado agregar un destino existente.");
        }

        observableListDestinationName.add(selectedItem);

        for (TouristPackage t : touristPackages) {
            if (t.equals(touristPackage)) {
                if (t.getDestinosName() != null){
                    t.getDestinosName().add(selectedItem);
                } else {
                    t.setDestinosName(new ArrayList<>());
                    t.getDestinosName().add(selectedItem);
                }

                break;
            }
        }

        serializarPaquetes();
    }

    public String logIn(String id, String password) throws WrongPasswordException, UserNoExistingException, AtributoVacioException {

        if (id == null || id.isBlank() || password == null || password.isBlank()){
            createAlertError("Campos obligatorios.", "Algunos campos son obligatorios (*)");
            log.info("se ha intentado registrar un cliente con campos obligatorios vacios");
            throw new AtributoVacioException("Se ha hecho un intento de registro de cliente con campos vacios");
        }

        if (clients.stream().anyMatch(client -> client.getUserId().equals(id))){
            validateLogInDataUser(id, password, 0);
           return "Client";
        } else if (admins.stream().anyMatch(client -> client.getUserId().equals(id))) {
            validateLogInDataAdmin(id, password, 0);
            return  "Admin";
        }
        return "";
    }

    public void registrarCliente(String userId, String password, String fullname, String mail , String phoneNumber, String residence) throws AtributoVacioException, RepeatedInformationException {

        if(userId == null || userId.isBlank() ||
                password == null || password.isBlank() ||
                fullname == null || fullname.isBlank() ||
                mail == null || mail.isBlank() ||
                phoneNumber == null || phoneNumber.isBlank() ||
                residence == null || residence.isBlank()){

            createAlertError("Campos obligatorios.", "Todos los campos son obligatorios (*)");
            log.info("se ha intentado registrar un cliente con campos obligatorios vacios");
            throw new AtributoVacioException("Se ha hecho un intento de registro de cliente con campos vacios");
        }

        if(clients.stream().anyMatch(cliente -> cliente.getUserId().equals(userId))){
            createAlertError("Usuario existente", "El usuario ya existe.");
            log.info("se intento registrar un usuario que ya existe");
            throw new RepeatedInformationException("el usuario ya exite");
        }

        Client client = Client.builder()
                .userId(userId.trim())
                .password(password.trim())
                .fullName(fullname.trim())
                .mail(mail.trim())
                .phoneNumber(phoneNumber.trim())
                .residence(residence.trim())
                .build();

        clients.add(client);
        createAlertInfo("Cuenta creada", "Información", "Se ha creado su cuenta correctamente.");

        serizalizarClientes();

        log.info("se ha registrado un cliente con el user ID " + userId);

    }


    private void validateLogInDataAdmin(String id, String password, int i) throws UserNoExistingException, WrongPasswordException {

        if (i >= admins.size()) {

            createAlertError("El usuario ingresado no existe", "Verifique los datos");
            log.info("Se ha hecho un intento de registro con informacion incorrecta.");
            throw new UserNoExistingException("Usuario no existente");

        }

        Admin currentAdmin = admins.get(i);

        if (currentAdmin.getUserId().equals(id)) {

            if (currentAdmin.getPassword().equals(password)) {

                log.info("El admin con el id " + id + " ha hecho un inicio de sesión.");

            } else {

                createAlertError("Contraseña incorrecta", "Verifique los datos");
                log.info("Se ha intentado un inicio de sesión con contraseña incorrecta.");
                throw new WrongPasswordException("Contraseña incorrecta");

            }

        } else {

            validateLogInDataAdmin(id, password, ++i);
        }

    }

    public void validateLogInDataUser(String id, String password, int i) throws UserNoExistingException, WrongPasswordException {

        if (i >= clients.size()) {

            createAlertError("El usuario ingresado no existe", "Verifique los datos");
            log.info("Se ha hecho un intento de registro con informacion incorrecta.");
            throw new UserNoExistingException("Usuario no existente");

        }

        Client currentClient = clients.get(i);

        if (currentClient.getUserId().equals(id)) {
            if (currentClient.getPassword().equals(password)) {

                log.info("El cliente con el id " + id + " ha hecho un inicio de sesión.");

            } else {

                createAlertError("Contraseña incorrecta", "Verifique los datos");
                log.info("Se ha intentado un inicio de sesión con contraseña incorrecta.");
                throw new WrongPasswordException("Contraseña incorrecta");
            }

        } else {
            validateLogInDataUser(id, password, ++i);
        }
    }

    public void generateWindow(String path, ImageView close) throws IOException {

        File url = new File(path);
        FXMLLoader loader = new FXMLLoader(url.toURL());
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.show();

        Stage stage1 = (Stage) close.getScene().getWindow();
        stage1.close();
    }

    public void createAlertError(String titleError, String contentError){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titleError);
        alert.setContentText(contentError);
        alert.show();
    }

    public void createAlertInfo(String titleError, String headerError, String contentError){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleError);
        alert.setHeaderText(headerError);
        alert.setContentText(contentError);
        alert.show();
    }

    public void cancelarReserva(Reservation reserva) {
        reserva.setReservationStatus(ReservationStatus.CANCELED);
        serizalizarClientes();
    }

    public void confirmarReserva(Reservation reserva) {
        reserva.setReservationStatus(ReservationStatus.CONFIRMED);
        serizalizarClientes();
    }
}
