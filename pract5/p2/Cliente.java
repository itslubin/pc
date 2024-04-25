package pract5.p2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import pract5.p2.mensaje.*;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private int ClientID;
    private int ServerID;
    private Socket clientSocket;
    private Lock lock;
    private ObjectOutputStream out;

    public Cliente(String host, int port) throws Exception {
        this.lock = new ReentrantLock();

        // Conecta al servidor en el puerto 1234 en localhost
        clientSocket = new Socket(host, port);
    }

    public void execute() throws Exception {
        // Pide el nombre y manda sus datos
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce tu ID de usuario o 0 en caso de nuevo usuario: ");
        ClientID = scanner.nextInt();

        // Limpiar el buffer del scanner
        scanner.nextLine();

        if (ClientID == 0) {
            System.out.println("Introduce tu nombre de usuario: ");
            nombre = scanner.nextLine();
        }

        Thread clientThread = new Thread(new OyenteServidor(this, clientSocket));
        clientThread.start();

        out = new ObjectOutputStream(clientSocket.getOutputStream());

        // 1.1 Mandar datos cliente
        out.writeObject(new MensajeCliente(0, 0, ClientID, nombre));

        while (true) {
            // Mostar menu
            System.out.println(getMenu());

            int op = scanner.nextInt();
            // Limpiar buffer
            scanner.nextLine();

            if (op == 1) { // Pedir lista de usuarios
                // 1.1 El cliente elige la opción de listar usuarios
                out.writeObject(new MensajeListaUsuario(ClientID, ServerID, "Listar usuarios"));

                // 3.1 Confirmar la recepción de la lista de usuarios registrados
                out.writeObject(new MensajeConfListaUsuario(ClientID, ServerID, String.valueOf(ClientID) +
                        " ha confirmado la recepción de la lista de usuarios registrados"));

            }

            else if (op == 2) { // Descargar fichero
                // 1.1 El cliente elige la opción de descargar fichero
                out.writeObject(
                        new MensajePedirFichero(ClientID, ServerID, "Pedir fichero"));

                System.out.println("Introduzca el fichero que quiere descargar: ");
                String file_name = scanner.nextLine();

                // 2.1 Enviar el nombre del fichero
                out.writeObject(new MensajeString(ClientID, ServerID, file_name));
            }

            else if (op == 3) { // Salir
                // 1.1 El cliente elige la opción de cerrar conexión
                out.writeObject(new MensajeCerrarConexion(ClientID, ServerID, String.valueOf(ClientID)
                        + " ha cerrado la conexión"));
                System.out.println("Conexión cerrada");

                break;
            }

            else {
                // Opcion no valida
                System.out.println("Opción no válida");
            }
        }

        scanner.close();
        clientThread.join();
    }

    public String getMenu() {
        return "\nMenú de operaciones:\n1. Mostrar lista de usuarios conectados \n2. Descargar fichero \n3. Salir\nElija una opción: ";
    }

    public void enviarMensaje(Mensaje m) throws IOException {
        out.writeObject(m);
    }

    public void setClientID(int ClientID) {
        this.ClientID = ClientID;
    }

    public void setServerID(int ServerID) {
        this.ServerID = ServerID;
    }

    public int getClientID() {
        return ClientID;
    }

    public int getServerID() {
        return ServerID;
    }

    public String getNombre() {
        return nombre;
    }

    public void emitirFichero(MensajeEmitirFichero mensaje) throws Exception {
        // 3.2 Recibir Mensaje emitir fichero
        System.out.println(mensaje.getContenido());
        System.out.println("Fichero: " + mensaje.getFilename());
        // 4.1 Mensaje Preparado CS
        out.writeObject(new MensajePreparadoCS(
                ClientID, ServerID,
                String.valueOf(ClientID) + "preparado para recibir fichero"));

        // Thread emisor = new Thread(new Emisor(1235, "localhost"));
        // emisor.start();
    }

    // Métodos para interactuar con el servidor y otros clientes
    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente("localhost", 1234);
            cliente.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}