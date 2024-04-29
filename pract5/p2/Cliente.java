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

public class Cliente implements Serializable { // Se encarga del envio de mensajes

    private static final long serialVersionUID = 1L;
    private String nombre = "";
    private int ClientID;
    private int ServerID;
    private Socket clientSocket;
    private ObjectOutputStream out;
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

            int op;
            String aux = scanner.nextLine();
            try {
                op = Integer.parseInt(aux);
            } catch (NumberFormatException e) {
                System.out.println("Opción " + aux + " no válida");
                continue;
            }

            if (op == 1) { // Pedir lista de usuarios
                lock.lock();
                // 1.1 El cliente elige la opción de listar usuarios
                out.writeObject(new MensajeListaUsuario(ClientID, ServerID, "Listar usuarios"));

                // Esperar a que el servidor envíe la lista de usuarios
                cond.await();
                lock.unlock();
            }

            else if (op == 2) { // Descargar fichero
                // 1.1 El cliente elige la opción de descargar fichero
                System.out.println("Introduzca el fichero que quiere descargar: ");
                String file_name = scanner.nextLine();

                lock.lock();
                // 2.1 Enviar el nombre del fichero
                out.writeObject(new MensajePedirFichero(ClientID, ServerID, file_name));

                // Esperar a que el servidor envíe el preparado SC
                cond.await();
                lock.unlock();
            }
            
            else if (op == 3) { // Subir fichero
            	System.out.println("Introduzca el nombre del fichero que quiere subir: ");
            	String file_name = scanner.nextLine();

            	lock.lock();
                // 2.1 Enviar el nombre del fichero
                out.writeObject(new MensajeSubirFichero(ClientID, ServerID, "Cliente " + ClientID + " sube el fichero " + file_name,  file_name));
                
                cond.await(); // Esperar a recibir el mensaje
                lock.unlock();
            }

            else if (op == 4) { // Salir
                lock.lock();
                // 1.1 El cliente elige la opción de cerrar conexión
                out.writeObject(new MensajeCerrarConexion(ClientID, ServerID, String.valueOf(ClientID)
                        + " ha cerrado la conexión"));
                System.out.println("Conexión cerrada");
                lock.unlock();

                break;
            }

            else {
                // Opcion numerica no valida
                System.out.println("Opción " + op + " no válida");
            }
        }

        scanner.close();
        clientThread.join();
    }

    public String getMenu() {
        return "\nMenú de operaciones:\n1. Mostrar lista de usuarios conectados \n2. Descargar fichero \n3. Subir fichero \n4. Salir \nElija una opción: ";
    }

    public void emitirFichero(MensajeEmitirFichero mensaje) throws Exception {
        // 3.2 Recibir Mensaje emitir fichero
        System.out.println("### Log cliente: Recibido mensaje emitir fichero de " + mensaje.getClienteID() + " ###");
        System.out.println("### Log cliente: Fichero: " + mensaje.getFilename() + " ###");

        Thread emisor = new Thread(new Emisor(1235, mensaje.getFilename()));
        emisor.start();

        lock.lock();
        // 4.1 Mensaje Preparado CS
        out.writeObject(new MensajePreparadoCS(ClientID, ServerID,
                String.valueOf(ClientID) + " preparado para emitir fichero", mensaje.getClienteID()));
        lock.unlock();
    }

    public void compartirFichero(String filename) throws IOException {
        lock.lock();
        out.writeObject(new MensajeSubirFichero(ClientID, ServerID, "Cliente " + ClientID + " sube el fichero " + filename,  filename));
        lock.unlock();
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