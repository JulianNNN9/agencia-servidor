package co.edu.uniquindio.ingesis.socket;

import co.edu.uniquindio.ingesis.datos.*;
import co.edu.uniquindio.ingesis.model.*;
import lombok.extern.java.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
                case "hacerReservacion":
                    hacerReservacion( (ReservaDatos) contenido, out );
                    break;
                case "getReservations":
                    getReservations(out);
                    break;
                case "getTouristGuide":
                    getTouristGuides(out);
                    break;
                case "getDestinos":
                    getDestinos(out);
                    break;
                case "getTouristPackage":
                    getTouristPackages(out);
                    break;
                case "getClientes":
                    getClientes(out);
                    break;
                case "getAdmins":
                    getAdmins(out);
                    break;
                case "calificarGuia":
                    calificarGuia((CalificarGuiaDatos) contenido, out);
                    break;
                case "calificarDestino":
                    calificarDestino((CalificarDestinoDatos) contenido, out);
                    break;
                case "modificarDestino":
                    modificarDestino((ModificarDestinoDatos) contenido, out);
                    break;
                case "modificarPaquete":
                    modificarPaquete((ModificarPaquetesDatos) contenido, out);
                    break;
                case "modificarGuia":
                    modificarGuia((ModificarGuiaDatos) contenido, out);
                    break;
                case "modificarPerfil":
                    modificarPerfil((ModificarPerfilDatos) contenido, out);
                    break;
                case "eliminarDestinoName":
                    eliminarDestinoName((EliminarDestinoName) contenido, out);
                    break;
                case "eliminarRuta":
                    eliminarRuta((EliminarRutaDatos) contenido, out);
                    break;
                case "eliminarLenguaje":
                    eliminarLenguaje((EliminarLenguajeDatos) contenido, out);
                    break;
                case "eliminarDestino":
                    eliminarDestino((Destino) contenido, out);
                    break;
                case "eliminarPaquete":
                    eliminarPaquete((TouristPackage) contenido, out);
                    break;
                case "eliminarGuia":
                    eliminarGuia((TouristGuide) contenido, out);
                    break;
                case "agregarGuia":
                    agregarGuia((TouristGuide) contenido, out);
                    break;
                case "agregarPaquete":
                    agregarPaquete((TouristPackage) contenido, out);
                    break;
                case "agregarDestino":
                    agregarDestino((Destino) contenido, out);
                    break;
                case "agregarImagenDestino":
                    agregarImagenDestino((AgregarImagenDestinoDatos) contenido, out);
                    break;
                case "agregarLenguajeGuia":
                    agregarLenguajeGuia((AgregarLenguajeGuiaDatos) contenido, out);
                    break;
                case "agregarDestinoEnPaquete":
                    agregarDestinoEnPaquete((AgregarDestinoEnPaqueteDatos) contenido, out);
                    break;
                case "logIn":
                    logInHilo((LogInDatos) contenido, out);
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
                    alertaOfertasEspeciales(out);
                    break;
            }

            //Se cierra la conexión del socket para liberar los recursos asociados
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void getClientes(ObjectOutputStream out) throws IOException {
        out.writeObject( agencia.listarClientes() );
    }

    private void getAdmins(ObjectOutputStream out) throws IOException{
        out.writeObject( agencia.listarAdmins() );
    }

    private void getReservations(ObjectOutputStream out) throws IOException {
        out.writeObject( agencia.listarReservations() );
    }

    private void getTouristGuides(ObjectOutputStream out) throws IOException {
        out.writeObject( agencia.listarGuias() );
    }

    private void getDestinos(ObjectOutputStream out) throws IOException {
        out.writeObject( agencia.listarDestinos() );
    }

    private void getTouristPackages(ObjectOutputStream out) throws IOException {
        out.writeObject( agencia.listarPaquetes() );
    }

    public void registrarCliente(Client cliente, ObjectOutputStream out) throws IOException {
        try {
            agencia.registrarCliente(cliente.getUserId(), cliente.getPassword(), cliente.getFullName(), cliente.getMail(), cliente.getPhoneNumber(), cliente.getResidence());
            out.writeObject("Te has registrado correctamente.");
        }catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void hacerReservacion(ReservaDatos reservaDatos, ObjectOutputStream out) throws IOException {
        try{
            agencia.hacerReservacion(reservaDatos.getClientID(), reservaDatos.getSelectedGuia(), reservaDatos.getNroCupos(), reservaDatos.getSelectedPackageName());
            out.writeObject("Has hecho una reservación éxitosa.");
        } catch (Exception e) {
            out.writeObject(e.getMessage());
        }
    }

    public void calificarGuia(CalificarGuiaDatos calificarGuiaDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.calificarGuia(calificarGuiaDatos.getTouristGuide(), calificarGuiaDatos.getEstrellas());
            out.writeObject("Se ha calificado un guía.");
        } catch (Exception e) {
            out.writeObject(e.getMessage());
        }
    }

    public void calificarDestino(CalificarDestinoDatos calificarDestinoDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.calificarDestino(calificarDestinoDatos.getDestino(), calificarDestinoDatos.getComentario(), calificarDestinoDatos.getEstrellas());
            out.writeObject("Se ha calificado un destino.");
        } catch (Exception e) {
            out.writeObject(e.getMessage());
        }
    }

    public void modificarDestino(ModificarDestinoDatos modificarDestinoDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarDestino(modificarDestinoDatos.getSelectedDestino(), modificarDestinoDatos.getNuevoName(), modificarDestinoDatos.getNuevaCiudad(), modificarDestinoDatos.getNuevaDescripcion(), modificarDestinoDatos.getNuevoClima());
            out.writeObject("Se ha modificado un destino.");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void modificarPaquete(ModificarPaquetesDatos modificarPaquetesDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarPaquete(modificarPaquetesDatos.getSelectedPackage(), modificarPaquetesDatos.getNombrePaquete(), modificarPaquetesDatos.getPrecio(), modificarPaquetesDatos.getCupo(), modificarPaquetesDatos.getFechaInicio(), modificarPaquetesDatos.getFechaFin());
            out.writeObject("Se ha modificado un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void modificarGuia(ModificarGuiaDatos modificarGuiaDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarGuia(modificarGuiaDatos.getSelectedGuia(), modificarGuiaDatos.getGuideID(), modificarGuiaDatos.getFullNameGuide(), modificarGuiaDatos.getExperience(), modificarGuiaDatos.getRating(), modificarGuiaDatos.getRutaFoto());
            out.writeObject("Se ha modificado un guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void modificarPerfil(ModificarPerfilDatos modificarPerfilDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.modificarPerfil(modificarPerfilDatos.getClientID(), modificarPerfilDatos.getNuevoNombre(), modificarPerfilDatos.getNuevoMail(), modificarPerfilDatos.getNuevoNumero(), modificarPerfilDatos.getNuevaResidencia());
            out.writeObject("Ha modificado su perfil");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarDestinoName(EliminarDestinoName eliminarDestinoName, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarDestinoName(eliminarDestinoName.getPackageSeleccionadoOpcional(), eliminarDestinoName.getSelectedDestino());
            out.writeObject("Se ha eliminado un destino de un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarRuta(EliminarRutaDatos eliminarRutaDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarRuta(eliminarRutaDatos.getDestinoSeleccionadoOpcional(), eliminarRutaDatos.getSelectedRuta());
            out.writeObject("Se ha eliminado una imagen de un destino");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarLenguaje(EliminarLenguajeDatos eliminarLenguajeDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarLenguaje(eliminarLenguajeDatos.getGuideSeleccionadoOpcional(), eliminarLenguajeDatos.getSelectedLenguaje());
            out.writeObject("Se ha eliminado un idioma de un guía");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarDestino(Destino selectedDestino, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarDestino(selectedDestino);
            out.writeObject("Se ha eliminado un destino");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarPaquete( TouristPackage selectedPackage, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarPaquete( selectedPackage);
            out.writeObject("Se ha eliminado un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void eliminarGuia( TouristGuide selectedGuia, ObjectOutputStream out) throws IOException {
        try {
            agencia.eliminarGuia( selectedGuia);
            out.writeObject("Se ha eliminado un guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarGuia(TouristGuide nuevoGuia, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarGuia(nuevoGuia);
            out.writeObject("Se ha creado un guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarPaquete(TouristPackage nuevoPaquete, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarPaquete( nuevoPaquete);
            out.writeObject("Se ha creado un paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarDestino(Destino nuevoDestino, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarDestino(nuevoDestino);
            out.writeObject("Se ha creado un destino");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarImagenDestino(AgregarImagenDestinoDatos agregarImagenDestinoDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarImagenDestino(agregarImagenDestinoDatos.getRuta(), agregarImagenDestinoDatos.getDestino());
            out.writeObject("Se ha agregado una imagen");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarLenguajeGuia(AgregarLenguajeGuiaDatos agregarLenguajeGuiaDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarLenguajeGuia(agregarLenguajeGuiaDatos.getLenguaje(), agregarLenguajeGuiaDatos.getTouristGuide());
            out.writeObject("Se ha agregado un lenguaje al guia");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void agregarDestinoEnPaquete(AgregarDestinoEnPaqueteDatos agregarDestinoEnPaqueteDatos, ObjectOutputStream out) throws IOException {
        try {
            agencia.agregarDestinoEnPaquete(agregarDestinoEnPaqueteDatos.getSelectedDestino(), agregarDestinoEnPaqueteDatos.getTouristPackage());
            out.writeObject("Se ha agregado un destino al paquete");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void logInHilo(LogInDatos logInDatos, ObjectOutputStream out) throws IOException {
        try {
            out.writeObject(agencia.logIn(logInDatos.getId(), logInDatos.getPassword()));
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void cancelarReserva(Reservation reserva, ObjectOutputStream out) throws IOException {
        try {
            agencia.cancelarReserva(reserva);
            out.writeObject("Has cancelado una reserva");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void confirmarReserva(Reservation reserva, ObjectOutputStream out) throws IOException {
        try {
            agencia.confirmarReserva(reserva);
            out.writeObject("Has confirmado una reserva");
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void recompensasPorReservas(Client client, ObjectOutputStream out) throws IOException {
        try {
            out.writeObject(agencia.recompensasPorReservas(client));
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }

    public void alertaOfertasEspeciales(ObjectOutputStream out) throws IOException {
        try {
            out.writeObject(agencia.alertaOfertasEspeciales());
        } catch (Exception e){
            out.writeObject(e.getMessage());
        }
    }
}