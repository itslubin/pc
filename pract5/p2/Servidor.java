package pract5.p2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private int id = 0;
    private volatile int counter = 0;
    private ServerSocket serverSocket;
    private volatile Map<Integer, Usuario> usuariosRegistrados; // Clave: ID_Usuario, Valor: Usuario
    private volatile Map<Integer, ConexionCliente> conexionesClientes; // Clave: ID_Usuario, Valor: ConexionCliente

    // Sincronización comunicación OC
    private volatile Map<Integer, LockTicket> lockClientes; // Clave: ID_Usuario, Valor: Lock

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        usuariosRegistrados = new ConcurrentMap<>(new LESemaphore());
        conexionesClientes = new ConcurrentMap<>(new LEMonitor());
        lockClientes = new HashMap<>();
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

    public int getID() { // Read
        return id;
    }

    public int getNewId() throws InterruptedException { // Write
        int c = ++counter;
        return c;
    }

    public void lock(int id) throws InterruptedException {
        lockClientes.get(id).takeLock(id);
    }

    public void unlock(int id) {
        lockClientes.get(id).releaseLock(id);
    }

    public void registarLock(int id, LockTicket lock) { // write
        lockClientes.put(id, lock);
    }

    public void registrarUsuario(int id, Usuario usuario) throws InterruptedException, IOException { // Write
        usuariosRegistrados.put(id, usuario);
    }

    public Map<Integer, Usuario> getUsuariosRegistrados() { // Read
        return usuariosRegistrados;
    }

    public void addConexion(int id, ConexionCliente conexion) throws InterruptedException { // Write
        conexionesClientes.put(id, conexion);
    }

    public void removeConexion(int id) throws InterruptedException { // Write
        conexionesClientes.remove(id);
    }
    
    public void addFichero(String filename, int id) throws IOException { // write
    	usuariosRegistrados.get(id).addInformacionCompartida(new Informacion(filename));
    }

    public Map<Integer, ConexionCliente> getConexionesClientes() { // Read
        return conexionesClientes;
    }

    public boolean buscarCliente(int id) { // Read
        return usuariosRegistrados.containsKey(id);
    }

    public ConexionCliente getConexionCliente(int id) { // Read
        return conexionesClientes.get(id);
    }

    public Usuario getUsuario(int clientID) { // Read
        return usuariosRegistrados.get(clientID);
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