package pract5.p2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Servidor {
    private int id = 0;
    private volatile int counter = 0;
    private ServerSocket serverSocket;
    private volatile List<Usuario> usuariosRegistrados;
    private volatile List<ConexionCliente> conexionesClientes;

    private Semaphore mutex = new Semaphore(1);

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        usuariosRegistrados = new java.util.ArrayList<>();
        conexionesClientes = new java.util.ArrayList<>();
    }

    public void execute() throws IOException {
        System.out.println("Servidor esperando conexiones...");

        while (true) {
            // Espera a que un cliente se conecte
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");

            // Crea un nuevo hilo para manejar la conexión con el cliente
            Thread clientThread = new Thread(new OyenteCliente(clientSocket, this));
            clientThread.start();
        }
    }

    public int getID() {
        return id;
    }

    public int getNewId() throws InterruptedException {
        mutex.acquire();
        int id_ = ++counter;
        mutex.release();
        return id_;
    }

    public void registrarUsuario(Usuario usuario) throws InterruptedException {
        mutex.acquire();
        usuariosRegistrados.add(usuario);
        mutex.release();
    }

    public List<Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    public void addConexion(ConexionCliente conexion) throws InterruptedException {
        mutex.acquire();
        conexionesClientes.add(conexion);
        mutex.release();
    }

    public void removeConexion(ConexionCliente conexion) throws InterruptedException {
        mutex.acquire();
        conexionesClientes.remove(conexion);
        mutex.release();
    }

    public List<ConexionCliente> getConexionesClientes() {
        return conexionesClientes;
    }

    public String getMenu() {
        return "Operations:\n1. List available information \n2. Download files\n3. Exit\nChoose an option: ";
    }

    // Métodos para la gestión de usuarios y conexiones
    public static void main(String[] args) {
        try {
            // Crea un servidor en el puerto 1234
            Servidor servidor = new Servidor(1234);
            servidor.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}