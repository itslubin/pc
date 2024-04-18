package pract5.p2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import pract5.p1.Mensaje;

public class Cliente {
    private String nombreUsuario;
    private Servidor servidor;
    private boolean sesionIniciada;

    // Métodos para interactuar con el servidor y otros clientes
    public static void main(String[] args) {
    	try {
            // Conecta al servidor en el puerto 1234 en localhost
            Socket socket = new Socket("localhost", 1232);
            
            // Obtén los streams de entrada y salida para comunicarte con el servidor
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            
            // Envía el nombre del archivo que se desea obtener
            outputStream.writeObject(new Mensaje("archivo.txt"));
            
            // Lee la respuesta del servidor y muestra el contenido del archivo

            System.out.println(((Mensaje) inputStream.readObject()).getContenido());
            
            // Cierra la conexión
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
}