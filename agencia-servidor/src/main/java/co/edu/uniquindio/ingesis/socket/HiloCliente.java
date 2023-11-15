package co.edu.uniquindio.ingesis.socket;

import co.edu.uniquindio.ingesis.model.*;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import lombok.extern.java.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Optional;

@Log
public class HiloCliente implements Runnable{
    private final Socket socket;
    private final AgenciaServidor agencia;
    public HiloCliente(Socket socket, AgenciaServidor agencia){

        this.socket = socket;
        this.agencia = agencia;

    }
    @Override
    public void run() {

        try {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );

            //Se lee el mensaje enviado por el cliente
            Mensaje mensaje = (Mensaje) in.readObject();

            //Se captura el tipo de mensaje
            String tipo = mensaje.getTipo();

            //Se captura el contenido del mensaje
            Object contenido = mensaje.getContenido();

            //Según el tipo de mensaje se invoca el método correspondiente
            switch (tipo) {
                case "registrarCliente":
                    registrarCliente((Client) contenido, out);
                    break;
                    /*
                case "hacerReservacion":
                    hacerReservacion(out);
                    break;
                case "calificarGuia":
                    calificarGuia(out);
                    break;
                case "calificarDestino":
                    calificarDestino(out);
                    break;
                case "modificarDestino":
                    modificarDestino(out);
                    break;
                case "modificarPaquete":
                    modificarPaquete(out);
                    break;
                case "modificarGuia":
                    modificarGuia(out);
                    break;
                case "modificarPerfil":
                    modificarPerfil(out);
                    break;
                case "eliminarDestinoName":
                    eliminarDestinoName(out);
                    break;
                case "eliminarRuta":
                    eliminarRuta(out);
                    break;
                case "eliminarLenguaje":
                    eliminarLenguaje(out);
                    break;
                case "eliminarDestino":
                    eliminarDestino(out);
                    break;
                case "eliminarPaquete":
                    eliminarPaquete(out);
                    break;
                case "eliminarGuia":
                    eliminarGuia(out);
                    break;
                case "agregarGuia":
                    agregarGuia(out);
                    break;
                case "agregarPaquete":
                    agregarPaquete(out);
                    break;
                case "agregarDestino":
                    agregarDestino(out);
                    break;
                case "agregarImagenDestino":
                    agregarImagenDestino(out);
                    break;
                case "agregarLenguajeGuia":
                    agregarLenguajeGuia(out);
                    break;
                case "agregarDestinoEnPaquete":
                    agregarDestinoEnPaquete(out);
                    break;
                case "logIn":
                    logInHilo(out);
                    break;
                case "cancelarReserva":
                    cancelarReserva((Reservation) contenido, out);
                    break;
                case "confirmarReserva":
                    confirmarReserva((Reservation) contenido, out);
                    break;
                case "recompensasPorReservas":
                    recompensasPorReservas((Client) contenido, out);
                    break;
                case "alertaOfertasEspeciales":
                    alertaOfertasEspeciales();
                    break; */
            }

            //Se cierra la conexión del socket para liberar los recursos asociados
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void registrarCliente(Client cliente, ObjectOutputStream out) throws IOException {
        try {
            agencia.registrarCliente(cliente.getUserId(), cliente.getPassword(), cliente.getFullName(), cliente.getMail(), cliente.getPhoneNumber(), cliente.getResidence());
            out.writeObject("Cliente agregado correctamente.");
        }catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void hacerReservacion(String clientID, String mailClient, Toggle selectedToggle, RadioButton radioBttonSI, RadioButton radioBttonNO, String selectedGuia, String nroCupos, String selectedPackageName, ObjectOutputStream out) throws IOException {
        try{
            agencia.hacerReservacion(clientID, mailClient, selectedToggle, radioBttonSI, radioBttonNO, selectedGuia, nroCupos, selectedPackageName);
            out.writeObject("Se ha hecho la reservación.");
        } catch (Exception e) {
            out.writeObject(e.getMessage());
        }
    }

    public void calificarGuia(TouristGuide touristGuide, Toggle calificacionSelectedToggle, RadioButton radioBtton1Estrella, RadioButton radioBtton2Estrella, RadioButton radioBtton3Estrella, RadioButton radioBtton4Estrella, RadioButton radioBtton5Estrella, ObjectOutputStream out) throws IOException {
        try {
            agencia.calificarGuia(touristGuide,calificacionSelectedToggle, radioBtton1Estrella, radioBtton2Estrella, radioBtton3Estrella, radioBtton4Estrella, radioBtton5Estrella);
            out.writeObject("Se ha calificado un guía.");
        } catch (Exception e) {
            out.writeObject(e.getMessage());
        }
    }

    public void calificarDestino(Destino destino, String comentario, Toggle selectedToggle, RadioButton radioBtton1EstrellaDestino, RadioButton radioBtton2EstrellaDestino, RadioButton radioBtton3EstrellaDestino, RadioButton radioBtton4EstrellaDestino, RadioButton radioBtton5EstrellaDestino, ObjectOutputStream out) throws IOException {
        try {
            agencia.calificarDestino(destino, comentario, selectedToggle, radioBtton1EstrellaDestino, radioBtton2EstrellaDestino, radioBtton3EstrellaDestino, radioBtton4EstrellaDestino, radioBtton5EstrellaDestino);
            out.writeObject("Se ha calificado un destino.");
        } catch (Exception e) {
            out.writeObject(e.getMessage());
        }
    }

    public void modificarDestino(Destino selectedDestino, String nuevoNombre, String nuevaCiudad, String nuevaDescrpcion, String nuevaLocalDate, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarDestino(selectedDestino, nuevoNombre, nuevaCiudad, nuevaDescrpcion, nuevaLocalDate);
            out.writeObject("Se ha modificado un destino.");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void modificarPaquete(TouristPackage selectedPackage, String nuevoNombrePaquete, double nuevoPrecio, int nuevosCupos, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarPaquete(selectedPackage, nuevoNombrePaquete, nuevoPrecio, nuevosCupos, nuevaFechaInicio, nuevaFechaFin);
            out.writeObject("Se ha modificado un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void modificarGuia(TouristGuide selectedGuia, String nuevoGuideID, String nuevoGuideName, String nuevaExperiencia, String nuevoRating, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarGuia(selectedGuia, nuevoGuideID, nuevoGuideName, nuevaExperiencia, nuevoRating);
            out.writeObject("Se ha modificado un guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void modificarPerfil(String clientID, String nuevoName, String nuevoMail, String nuevoNumero, String nuevoResidence, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarPerfil(clientID, nuevoName, nuevoMail, nuevoNumero, nuevoResidence);
            out.writeObject("Se ha modificado su perfil");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarDestinoName(Optional<TouristPackage> touristPackage, String destinoABorrar, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarDestinoName(touristPackage, destinoABorrar);
            out.writeObject("Se ha eliminado un destino de un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarRuta(Optional<Destino> destino, String rutaABorrar, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarRuta(destino, rutaABorrar);
            out.writeObject("Se ha eliminado una imagen de un destino");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarLenguaje(Optional<TouristGuide> touristGuide, String lenguajeABorrar, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarLenguaje(touristGuide, lenguajeABorrar);
            out.writeObject("Se ha eliminado un idioma de un guía");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarDestino(ObservableList<Destino> destinoObservableList, Destino selectedDestino, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarDestino(destinoObservableList, selectedDestino);
            out.writeObject("Se ha eliminado un destino");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarPaquete(ObservableList<TouristPackage> packageObservableList, TouristPackage selectedPackage, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarPaquete(packageObservableList, selectedPackage);
            out.writeObject("Se ha eliminado un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarGuia(ObservableList<TouristGuide> touristGuideObservableList, TouristGuide selectedGuia, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarGuia(touristGuideObservableList, selectedGuia);
            out.writeObject("Se ha eliminado un guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarGuia(ObservableList<TouristGuide> touristGuideObservableList, TouristGuide nuevoGuia, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarGuia(touristGuideObservableList, nuevoGuia);
            out.writeObject("Se ha creado un guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarPaquete(ObservableList<TouristPackage> packageObservableList, TouristPackage nuevoPaquete, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarPaquete(packageObservableList, nuevoPaquete);
            out.writeObject("Se ha creado un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarDestino(ObservableList<Destino> destinoObservableList, Destino nuevoDestino, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarDestino(destinoObservableList, nuevoDestino);
            out.writeObject("Se ha creado un destino");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarImagenDestino(ObservableList<String> observableListRutas, String ruta, Destino destino, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarImagenDestino(observableListRutas, ruta, destino);
            out.writeObject("Se ha agregado una imagen");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarLenguajeGuia(ObservableList<String> observableListLenguajes, String lenguaje, TouristGuide touristGuide, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarLenguajeGuia(observableListLenguajes, lenguaje, touristGuide);
            out.writeObject("Se ha agregado un lenguaje al guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarDestinoEnPaquete(ObservableList<String> observableListDestinationName, String selectedItem, TouristPackage touristPackage, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarDestinoEnPaquete(observableListDestinationName, selectedItem, touristPackage);
            out.writeObject("Se ha agregado un destino al paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void logInHilo(String id, String password, ObjectOutputStream out) throws IOException {
        try {
            agencia.logIn(id, password);
            out.writeObject("Se ha iniciado sesion");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void cancelarReserva(Reservation reserva, ObjectOutputStream out) throws IOException {
        try {
            agencia.cancelarReserva(reserva);
            out.writeObject("Se ha cancelado una reserva");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void confirmarReserva(Reservation reserva, ObjectOutputStream out) throws IOException {
        try {
            agencia.cancelarReserva(reserva);
            out.writeObject("Se ha confirmado una reserva");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void recompensasPorReservas(Client client, ObjectOutputStream out) throws IOException {
        try {
            agencia.recompensasPorReservas(client);
            out.writeObject("Se ha recompensado un cliente");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void alertaOfertasEspeciales(ObjectOutputStream out) throws IOException {
        try {
            agencia.alertaOfertasEspeciales();
            out.writeObject("Se ha alertado de ofertas a un cliente");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }
}