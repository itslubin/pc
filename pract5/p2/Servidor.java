package pract5.p2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Servidor {
    private List<Usuario> usuariosRegistrados;
    private List<ConexionCliente> conexionesClientes;
    

    // Métodos para la gestión de usuarios y conexiones
    public static void main(String[] args) {
    	try {
            // Crea un servidor en el puerto 1234
            ServerSocket serverSocket = new ServerSocket(1232);
            
            System.out.println("Servidor esperando conexiones...");
            
            while (true) {
                // Espera a que un cliente se conecte
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado");
                
                // Crea un nuevo hilo para manejar la conexión con el cliente
                Thread clientThread = new Thread(new OyenteCliente(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
     }
	}
}