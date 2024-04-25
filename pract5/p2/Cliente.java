package pract5.p2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import pract5.p2.mensaje.*;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre = "";
    private int ClientID;
    private int ServerID;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private Mensaje mensaje;
    private Lock lock;
    private Condition cond;

    public Cliente(String host, int port) throws Exception {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();

        // Conecta al servidor en el puerto 1234 en localhost
        clientSocket = new Socket(host, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void execute() throws Exception {
        // Pide el nombre y manda sus datos
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce tu ID de usuario o 0 en caso de nuevo usuario: ");
        ClientID = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del scanner

        if (ClientID == 0) {
            System.out.println("Introduce tu nombre de usuario: ");
            nombre = scanner.nextLine();
        }

        Thread clientThread = new Thread(new OyenteServidor(this, clientSocket, lock, cond));
        clientThread.start();

        // 1.1 Mandar datos cliente
        out.writeObject(new MensajeConexion(0, 0, ClientID, nombre));
        lock.lock();
        cond.await();
        lock.unlock();

        while (true) {
            // Mostar menu
            System.out.println(getMenu());

            int op = scanner.nextInt();
            // Limpiar buffer
            scanner.nextLine();

            if (op == 1) { // Pedir lista de usuarios
                lock.lock();
                // 1.1 El cliente elige la opción de listar usuarios
                out.writeObject(new MensajeListaUsuario(ClientID, ServerID, "Listar usuarios"));

                // Esperar a que el servidor envíe la lista de usuarios
                cond.await();
                System.out.println(((MensajeConfListaUsuario) mensaje).getContenido());
                lock.unlock();
            }

            else if (op == 2) { // Descargar fichero
                lock.lock();
                // 1.1 El cliente elige la opción de descargar fichero
                System.out.println("Introduzca el fichero que quiere descargar: ");
                String file_name = scanner.nextLine();

                // 2.1 Enviar el nombre del fichero
                out.writeObject(new MensajePedirFichero(ClientID, ServerID, file_name));

                // Esperar a que el servidor envíe el preparado SC
                cond.await();
                if (mensaje.getTipo() == 7) {
                    System.out.println(((MensajePreparadoSC) mensaje).getContenido());

					Thread receptor = new Thread(new Receptor("localhost", 1235));
					receptor.start();
                }
                else {
                    System.out.println(((MensajeError) mensaje).getContenido());
                }
                lock.unlock();
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

    public void emitirFichero(MensajeEmitirFichero mensaje) throws Exception {
        // 3.2 Recibir Mensaje emitir fichero
        System.out.println("Recibido mensaje emitir fichero de " + mensaje.getClienteID());
        System.out.println("Fichero: " + mensaje.getContenido());

        Thread emisor = new Thread(new Emisor(1235, "localhost"));
        emisor.start();

        // 4.1 Mensaje Preparado CS
        out.writeObject(new MensajePreparadoCS(ClientID, ServerID,
                String.valueOf(ClientID) + "preparado para recibir fichero", mensaje.getClienteID()));
    }

    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int ClientID) {
        this.ClientID = ClientID;
    }

    public int getServerID() {
        return ServerID;
    }

    public void setServerID(int ServerID) {
        this.ServerID = ServerID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
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