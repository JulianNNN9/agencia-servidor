package co.edu.uniquindio.ingesis.model;

import co.edu.uniquindio.ingesis.enums.ReservationStatus;
import co.edu.uniquindio.ingesis.exceptions.*;
import co.edu.uniquindio.ingesis.utils.*;
import lombok.Getter;
import lombok.extern.java.Log;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log @Getter

public class AgenciaServidor {

    List<TouristGuide> touristGuides;
    List<Reservation> reservations;
    List<TouristPackage> touristPackages;
    List<Destino> destinos;
    List<Client> clients;
    List<Admin> admins;

    private final String RUTA_TOURISTGUIDES = "G:\\IntelliJ IDEA - workspace\\agencia-servidor\\agencia-servidor\\src\\main\\resources\\persistencia\\touristGuides.ser";
    private final String RUTA_ADMINS = "G:\\IntelliJ IDEA - workspace\\agencia-servidor\\agencia-servidor\\src\\main\\resources\\persistencia\\admins.ser";
    private final String RUTA_DESTINOS = "G:\\IntelliJ IDEA - workspace\\agencia-servidor\\agencia-servidor\\src\\main\\resources\\persistencia\\destinos.ser";
    private final String RUTA_RESERVATIONS = "G:\\IntelliJ IDEA - workspace\\agencia-servidor\\agencia-servidor\\src\\main\\resources\\persistencia\\reservations.ser";
    private final String RUTA_CLIENTS = "G:\\IntelliJ IDEA - workspace\\agencia-servidor\\agencia-servidor\\src\\main\\resources\\persistencia\\clients.ser";
    private final String RUTA_TOURISTPACKAGE = "G:\\IntelliJ IDEA - workspace\\agencia-servidor\\agencia-servidor\\src\\main\\resources\\persistencia\\touristPackages.ser";

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

        ArrayList<TouristGuide> aux = (ArrayList<TouristGuide>) archiveUtils.deserializerObjet(RUTA_TOURISTGUIDES);

        this.touristGuides = Objects.requireNonNullElseGet(aux, ArrayList::new);

        //Cargar reservaciones

        ArrayList<Reservation> aux1 = (ArrayList<Reservation>) archiveUtils.deserializerObjet(RUTA_RESERVATIONS);

        this.reservations = Objects.requireNonNullElseGet(aux1, ArrayList::new);

        //Cargar paquetes

        ArrayList<TouristPackage> aux2 = (ArrayList<TouristPackage>) archiveUtils.deserializerObjet(RUTA_TOURISTPACKAGE);

        this.touristPackages = Objects.requireNonNullElseGet(aux2, ArrayList::new);

        //Cargar destinos

        ArrayList<Destino> aux3 = (ArrayList<Destino>) archiveUtils.deserializerObjet(RUTA_DESTINOS);

        this.destinos = Objects.requireNonNullElseGet(aux3, ArrayList::new);

        //Cargar clientes

        ArrayList<Client> aux4 = (ArrayList<Client>) archiveUtils.deserializerObjet(RUTA_CLIENTS);

        this.clients = Objects.requireNonNullElseGet(aux4, ArrayList::new);

        //Cargar admins

        //ArrayList<Admin> aux5 = (ArrayList<Admin>) archiveUtils.deserializerObjet(RUTA_ADMINS);

        //this.admins = Objects.requireNonNullElseGet(aux5, ArrayList::new);

        this.admins = new ArrayList<>();

        this.admins.add(Admin.builder()
                        .userId("admin")
                        .password("123").build());

    }

    public static AgenciaServidor getInstance(){

        if (agenciaServidor == null){
            agenciaServidor = new AgenciaServidor();
        }

        log.info("Se ha creado una instancia de Travel Agency");

        return agenciaServidor;

    }

    public List<Destino> listarDestinos (){
        return destinos;
    }

    public List<Admin> listarAdmins() {
        return admins;
    }

    public List<Reservation> listarReservations (){
        return reservations;
    }

    public List<TouristGuide> listarGuias (){
        return touristGuides;
    }

    public List<TouristPackage> listarPaquetes (){
        return touristPackages;
    }

    public List<Client> listarClientes(){
        return clients;
    }

    public void serializarDestinos(){
        archiveUtils.serializerObjet(RUTA_DESTINOS, destinos);
    }

    public void serializarGuias(){
        archiveUtils.serializerObjet(RUTA_TOURISTGUIDES, touristGuides);
    }

    public void serializarPaquetes(){
        archiveUtils.serializerObjet(RUTA_TOURISTPACKAGE, touristPackages);
    }

    public void serizalizarClientes(){
        archiveUtils.serializerObjet(RUTA_CLIENTS, clients);
    }

    public void serializarReservaciones(){
        archiveUtils.serializerObjet(RUTA_RESERVATIONS, reservations);
    }

    /*
     * Primer método adicional
     */

    public String recompensasPorReservas(Client client){

        int reservacionesConfirmadas = 0;

        if (client.getReservationList() != null){
            reservacionesConfirmadas = (int) client.getReservationList().stream().filter(reservation -> reservation.getReservationStatus().equals(ReservationStatus.CONFIRMED)).count();

        }

        if (reservacionesConfirmadas > 0){

            if (reservacionesConfirmadas > 2){
                client.getDescuentos().add(0.1);
                return "¡Tenemos un descuento para ti del 10% en tu próxima reserva!";
            }

            if (reservacionesConfirmadas > 4){
                client.getDescuentos().add(0.2);
                return "¡Tenemos un descuento para ti del 20% en tu próxima reserva!";
            }

            if (reservacionesConfirmadas > 6){
                client.getDescuentos().add(0.3);
                return "¡Tenemos un descuento para ti del 30% en tu próxima reserva!";
            }
        }


        return "";
    }

    /**
     * Segundo método adicinal
     */

    public String alertaOfertasEspeciales(){
        return "";
    }

    public void hacerReservacion(String clientID, String selectedGuia, String nroCupos, String selectedPackageName) throws AtributoVacioException, CuposInvalidosException {

        Optional<TouristPackage> paqueteSeleccionado = touristPackages.stream().filter(touristPackage -> touristPackage.getName().equals(selectedPackageName)).findFirst();

        if (nroCupos == null || nroCupos.isEmpty() ||
                selectedPackageName == null || selectedPackageName.isEmpty()) {

            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (selectedGuia!= null) {

            Optional<TouristPackage> touristPackage = touristPackages.stream().filter(touristPackage1 -> touristPackage1.getName().equals(selectedPackageName)).findFirst();

            if (touristPackage.isPresent()) {

                if (Integer.parseInt(nroCupos) > touristPackage.get().getQuota()) {
                    log.info("Cupos inválidos.");
                    throw new CuposInvalidosException("Cupos inválidos.");
                }

                paqueteSeleccionado.ifPresent(aPackage -> aPackage.setPrice(aPackage.getPrice() - Double.parseDouble(nroCupos)));

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

                log.info("se ha hecho una reservación del paquete " + selectedPackageName + " para el " + touristPackage.get().getStartDate() + ".");

                Optional<Client> client = clients.stream().filter(client1 -> client1.getUserId().equals(clientID)).findFirst();

                if (client.isPresent()) {

                    double precio = 0.0;

                    if (client.get().getDescuentos() != null){
                        precio = paqueteSeleccionado.get().getPrice() - paqueteSeleccionado.get().getPrice() * client.get().getDescuentos().get(0);
                        client.get().getDescuentos().remove(0);
                    }

                    String mensajeCorreo = "¡Gracias por hacer la reserva!\n\n" +
                            "Detalles de la reserva:\n" +
                            "Precio del paquete reservado" + precio + "\n" +
                            "Paquete Turístico: " + nuevaReservacion.getTouristPackage().getName() + "\n" +
                            "Fecha de Solicitud: " + nuevaReservacion.getRequestDate() + "\n" +
                            "Estado de la Reserva: " + nuevaReservacion.getReservationStatus() + "\n" +
                            "Fecha de Inicio: " + nuevaReservacion.getStartDate() + "\n" +
                            "Fecha de Fin: " + nuevaReservacion.getEndDate() + "\n" +
                            "Guía Turístico: " + (nuevaReservacion.getTouristGuide() != null ? nuevaReservacion.getTouristGuide().getFullName() : "No asignado") + "\n" +
                            "Número de Personas: " + nuevaReservacion.getNumberOfPeople() + "\n" +
                            "\n" +
                            "Recuerda que para confirmar por completo tu reserva debes confirmarla desde tu tabla de reservaciones.";

                    if (esCorreoValido(client.get().getMail())) {
                        enviarCorreoConfirmacion(client.get(), mensajeCorreo);
                    }

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

        if (selectedGuia == null) {

            Optional<TouristPackage> touristPackage = touristPackages.stream().filter(touristPackage1 -> touristPackage1.getName().equals(selectedPackageName)).findFirst();

            if (Integer.parseInt(nroCupos) > touristPackage.get().getQuota()){

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

            log.info("se ha hecho una reservación del paquete " + selectedPackageName + " para el " + touristPackage.get().getStartDate() + ".");

            Optional<Client> client = clients.stream().filter(client1 -> client1.getUserId().equals(clientID)).findFirst();

            if (client.isPresent()){

                String mensajeCorreo = "¡Gracias por hacer la reserva!\n\n" +
                        "Detalles de la reserva:\n" +
                        "Paquete Turístico: " + nuevaReservacion.getTouristPackage().getName() + "\n" +
                        "Fecha de Solicitud: " + nuevaReservacion.getRequestDate() + "\n" +
                        "Estado de la Reserva: " + nuevaReservacion.getReservationStatus() + "\n" +
                        "Fecha de Inicio: " + nuevaReservacion.getStartDate() + "\n" +
                        "Fecha de Fin: " + nuevaReservacion.getEndDate() + "\n" +
                        "Guía Turístico: " + (nuevaReservacion.getTouristGuide() != null ? nuevaReservacion.getTouristGuide().getFullName() : "No asignado") + "\n" +
                        "Número de Personas: " + nuevaReservacion.getNumberOfPeople() + "\n" +
                        "\n" +
                        "Recuerda que para confirmar por completo tu reserva debes confirmarla desde tu tabla de reservaciones.";

                if (esCorreoValido(client.get().getMail())) {
                    enviarCorreoConfirmacion(client.get(), mensajeCorreo);
                }

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

    private void enviarCorreoConfirmacion(Client client, String mensajeCorreo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Cambia esto al host del servidor SMTP que estás utilizando
        props.put("mail.smtp.port", "587"); // Cambia esto al puerto adecuado

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bastonsito98@gmail.com", "tmxk phbv pvzk hdcn");
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bastonsito98@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(client.getMail()));
            message.setSubject("Confirmación de Reserva");

            // Personaliza el cuerpo del mensaje con la información de la reserva
            message.setText(mensajeCorreo);

            Transport.send(message);

            System.out.println("Correo electrónico enviado con éxito.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean esCorreoValido(String correo) {
        String expresionRegular = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(expresionRegular);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public void calificarGuia(TouristGuide touristGuide, String estrellas) throws AtributoVacioException {

        if (estrellas == null){

            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (estrellas.equals("1")){
            touristGuide.getRatingList().add(1);
        }
        if (estrellas.equals("2")){
            touristGuide.getRatingList().add(2);
        }
        if (estrellas.equals("3")){
            touristGuide.getRatingList().add(3);
        }
        if (estrellas.equals("4")){
            touristGuide.getRatingList().add(4);
        }
        if (estrellas.equals("5")){
            touristGuide.getRatingList().add(5);
        }

        Double promedioCalificaciones = touristGuide.getRatingList().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        touristGuide.setRating(promedioCalificaciones);

        serializarGuias();
    }

    public void calificarDestino(Destino destino, String comentario, String estrellas) throws AtributoVacioException {

        if (estrellas == null){
            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (comentario != null){
            destino.getComentarios().add(comentario);
        }

        if (estrellas.equals("1")){
            destino.getRatingList().add(1);
        }
        if (estrellas.equals("2")){
            destino.getRatingList().add(2);
        }
        if (estrellas.equals("3")){
            destino.getRatingList().add(3);
        }
        if (estrellas.equals("4")){
            destino.getRatingList().add(4);
        }
        if (estrellas.equals("5")){
            destino.getRatingList().add(5);
        }

        Double promedioCalificaciones = destino.getRatingList().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        destino.setRating(promedioCalificaciones);

        serializarDestinos();

    }

    public void modificarDestino(Destino selectedDestino, String nuevoNombre, String nuevaCiudad, String nuevaDescrpcion, String nuevoClima) {

        Optional<Destino> destino = destinos.stream().filter(destino1 -> destino1.equals(selectedDestino)).findFirst();

        destino.get().setName(nuevoNombre);
        destino.get().setCity(nuevaCiudad);
        destino.get().setDescription(nuevaDescrpcion);
        destino.get().setWeather(nuevoClima);

        serializarDestinos();

    }
    public void modificarPaquete(TouristPackage selectedPackage, String nuevoNombrePaquete, double nuevoPrecio, int nuevosCupos, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin)  {

        Optional<TouristPackage> touristPackage = touristPackages.stream().filter(touristPackage1 -> touristPackage1.equals(selectedPackage)).findFirst();

        touristPackage.get().setName(nuevoNombrePaquete);
        touristPackage.get().setPrice(nuevoPrecio);
        touristPackage.get().setQuota(nuevosCupos);
        touristPackage.get().setStartDate(nuevaFechaInicio);
        touristPackage.get().setEndDate(nuevaFechaFin);

        serializarPaquetes();

    }

    public void modificarGuia(TouristGuide selectedGuia, String nuevoGuideID, String nuevoGuideName, String nuevaExperiencia, String nuevoRating, String rutaFoto) {

        Optional<TouristGuide> touristGuide = touristGuides.stream().filter(touristGuide1 -> touristGuide1.equals(selectedGuia)).findFirst();

        touristGuide.get().setId(nuevoGuideID);
        touristGuide.get().setFullName(nuevoGuideName);
        touristGuide.get().setExperience(nuevaExperiencia);
        touristGuide.get().setRating(Double.valueOf(nuevoRating));
        touristGuide.get().setRutaFoto(rutaFoto);

        serializarGuias();
    }

    public void modificarPerfil(String clientID, String nuevoName, String nuevoMail, String nuevoNumero, String nuevoResidence) throws AtributoVacioException {

        if (nuevoName == null || nuevoName.isEmpty() ||
                nuevoMail == null || nuevoMail.isEmpty() ||
                nuevoNumero == null || nuevoNumero.isEmpty() ||
                nuevoResidence == null){

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

    public void eliminarDestinoName(TouristPackage touristPackage, String destinoABorrar) {

        Optional<TouristPackage> paqueteSeleccionado = touristPackages.stream().filter(touristPackage1 -> touristPackage1.equals(touristPackage)).findFirst();

        if (paqueteSeleccionado.isPresent()){

            List<String> destinosSinEliminar = paqueteSeleccionado.get().getDestinosName().stream().filter(s -> !s.equals(destinoABorrar)).toList();

            paqueteSeleccionado.get().getDestinosName().clear();
            paqueteSeleccionado.get().getDestinosName().addAll(destinosSinEliminar );

            serializarPaquetes();
        }


    }

    public void eliminarRuta(Destino destino, String rutaABorrar){

        Optional<Destino> destinoSeleccionado = destinos.stream().filter(destino1 -> destino1.equals(destino)).findFirst();

        if (destinoSeleccionado.isPresent()) {

            List<String> rutasSinEliminar;

            rutasSinEliminar = destinoSeleccionado.get().getImagesHTTPS().stream().filter(s -> !s.equals(rutaABorrar)).toList();

            destinoSeleccionado.get().getImagesHTTPS().clear();
            destinoSeleccionado.get().getImagesHTTPS().addAll(rutasSinEliminar);

            serializarDestinos();
        }
    }

    public void eliminarLenguaje(TouristGuide touristGuide, String lenguajeABorrar){

        Optional<TouristGuide> guiaSeleccionado = touristGuides.stream().filter(touristGuide1 -> touristGuide1.equals(touristGuide)).findFirst();

        if (guiaSeleccionado.isPresent()) {

            List<String> languajeSinEliminar = guiaSeleccionado.get().getLanguages().stream().filter(s -> !s.equals(lenguajeABorrar)).toList();

            guiaSeleccionado.get().getLanguages().clear();
            guiaSeleccionado.get().getLanguages().addAll(languajeSinEliminar);

            serializarGuias();
        }
    }


    public void eliminarDestino(Destino selectedDestino) {

        destinos.removeIf(destino -> destino.equals(selectedDestino));

        serializarDestinos();
    }

    public void eliminarPaquete(TouristPackage selectedPackage) {
        touristPackages.removeIf(touristPackage -> touristPackage.equals(selectedPackage));
        serializarPaquetes();
    }

    public void eliminarGuia( TouristGuide selectedGuia) {
        touristGuides.removeIf(touristGuide -> touristGuide.equals(selectedGuia));
        serializarGuias();
    }

    public void agregarGuia( TouristGuide nuevoGuia) {

        touristGuides.add(nuevoGuia);

        serializarGuias();

        log.info("Se ha registrado un nuevo guia.");

    }

    public void agregarPaquete( TouristPackage nuevoPaquete) throws ErrorEnIngresoFechasException {

        if (nuevoPaquete.getDuration() < 0){
            log.info("Las fechas fueron incorrectamente colocadas, la fecha de inicio no puede ser después de la fecha de fin.");
            throw new ErrorEnIngresoFechasException("Las fechas fueron incorrectamente colocadas, la fecha de inicio no puede ser después de la fecha de fin.");
        }

        touristPackages.add(nuevoPaquete);

        serializarPaquetes();

        log.info("Se ha creado un nuevo paquete.");

    }

    public void agregarDestino(Destino nuevoDestino) throws RepeatedInformationException, AtributoVacioException {

        if (nuevoDestino.getName().isEmpty() ||
                nuevoDestino.getCity().isEmpty() ||
                nuevoDestino.getDescription().isEmpty() ||
                nuevoDestino.getWeather().isEmpty()) {

            log.info("Se ha intentado agregar un destino con campos vacios.");
            throw new AtributoVacioException("Se ha intentado agregar un destino con campos vacios.");
        }

        if (destinos.stream().anyMatch(destination -> destination.getName().equals(nuevoDestino.getName()))){

            log.severe("Se ha intentado crear un Destino existente.");
            throw new RepeatedInformationException("Se ha intentado crear un Destino existente.");
        }

        destinos.add(nuevoDestino);

        serializarDestinos();

        log.info("Se ha creado un nuevo destino.");

        }

    public void agregarImagenDestino(String ruta, Destino destino) throws RutaInvalidaException {

        File archivo = new File(ruta);
        boolean esRutaDeArchivo = archivo.exists() && archivo.isFile();

        if (!esRutaDeArchivo){
            log.severe("Se ha intentado agregar una imagen invalida a un destino.");
            throw new RutaInvalidaException("Se ha intentado agregar una imagen invalida a un destino.");
        }

        for (Destino d : destinos) {
            if (d.equals(destino)) {
                d.getImagesHTTPS().add(ruta);
                break;
            }
        }

        serializarDestinos();
    }


    public void agregarLenguajeGuia( String lenguaje, TouristGuide touristGuide) {

        for (TouristGuide t : touristGuides) {
            if (t.equals(touristGuide)) {
                t.getLanguages().add(lenguaje);
                break;
            }
        }

        serializarGuias();
    }

    public void agregarDestinoEnPaquete( String selectedItem, TouristPackage touristPackage) {

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

    public String logIn(String id, String password) throws UserNoExistingException, AtributoVacioException {

        if (id == null || id.isBlank() || password == null || password.isBlank()){
            log.info("se ha intentado registrar un cliente con campos obligatorios vacios");
            throw new AtributoVacioException("Se ha hecho un intento de registro de cliente con campos vacios");
        }

        if (clients.stream().anyMatch(client -> client.getUserId().equals(id))){
            return validateLogInDataUser(id, password, 0);
        } else if (admins.stream().anyMatch(client -> client.getUserId().equals(id))) {
            return validateLogInDataAdmin(id, password, 0);
        } else {
            return "";
        }

    }

    public void registrarCliente(String userId, String password, String fullname, String mail , String phoneNumber, String residence) throws AtributoVacioException, RepeatedInformationException {

        if(userId == null || userId.isBlank() ||
                password == null || password.isBlank() ||
                fullname == null || fullname.isBlank() ||
                mail == null || mail.isBlank() ||
                phoneNumber == null || phoneNumber.isBlank() ||
                residence == null || residence.isBlank()){

            log.info("se ha intentado registrar un cliente con campos obligatorios vacios");
            throw new AtributoVacioException("Se ha hecho un intento de registro de cliente con campos vacios");
        }

        if(clients.stream().anyMatch(cliente -> cliente.getUserId().equals(userId))){
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

        serizalizarClientes();

        log.info("se ha registrado un cliente con el user ID " + userId);

    }


    private String validateLogInDataAdmin(String id, String password, int i) throws UserNoExistingException {

        if (i >= admins.size()) {

            log.info("Se ha hecho un intento de registro con informacion incorrecta.");
            throw new UserNoExistingException("Usuario no existente");

        }

        Admin currentAdmin = admins.get(i);

        if (currentAdmin.getUserId().equals(id)) {

            if (currentAdmin.getPassword().equals(password)) {

                log.info("El admin con el id " + id + " ha hecho un inicio de sesión.");
                return "Admin";
            } else {

                log.info("Se ha intentado un inicio de sesión con contraseña incorrecta.");
                return "Admin";
            }

        } else {

            validateLogInDataAdmin(id, password, ++i);
        }

        return "Admin";
    }

    public String validateLogInDataUser(String id, String password, int i) throws UserNoExistingException {

        if (i >= clients.size()) {

            log.info("Se ha hecho un intento de registro con informacion incorrecta.");
            throw new UserNoExistingException("Usuario no existente");

        }

        Client currentClient = clients.get(i);

        if (currentClient.getUserId().equals(id)) {
            if (currentClient.getPassword().equals(password)) {

                log.info("El cliente con el id " + id + " ha hecho un inicio de sesión.");
                return "Client";
            } else {

                log.info("Se ha intentado un inicio de sesión con contraseña incorrecta.");
                return "Client";
            }

        } else {
            validateLogInDataUser(id, password, ++i);
        }
        return "Client";
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
