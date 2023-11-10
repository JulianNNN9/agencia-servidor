package co.edu.uniquindio.ingesis.utils;

import lombok.extern.java.Log;

import java.io.*;
import java.util.Formatter;

@Log

public class archiveUtils {

    public static void serializerObjet(String ruta, Object objeto) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(ruta);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(objeto);

        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    public static Object deserializerObjet(String ruta) {
        try (FileInputStream fileInputStream = new FileInputStream(ruta);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){

            return objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            log.severe(e.getMessage());
        }

        return null;
    }

    public static void escribirEnArchivo(String ruta, String formato){
        try {
            FileWriter fileWriter = new FileWriter(ruta, true);
            Formatter formatter = new Formatter(fileWriter);
            formatter.format(formato+"%n");
            fileWriter.close();
        } catch (IOException e){
            log.severe(e.getMessage());
        }
    }

}
