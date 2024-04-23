package pract5.p2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Servidor {
    private int id = 0;
    private volatile int counter = 0;
    private ServerSocket serverSocket;
    private volatile Map<Integer,Usuario> usuariosRegistrados; // Clave: ID_Usuario, Valor: Usuario
    private volatile Map<Integer,ConexionCliente> conexionesClientes; // Clave: ID_Usuario, Valor: ConexionCliente

    private Semaphore mutex = new Semaphore(1);

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        usuariosRegistrados = new HashMap<>();
        conexionesClientes = new HashMap<>();
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

    public void registrarUsuario(int id, Usuario usuario) throws InterruptedException {
        mutex.acquire();
        usuariosRegistrados.put(id, usuario);
        mutex.release();
    }

    public Map<Integer,Usuario> getUsuariosRegistrados() {
        usuariosRegistrados = new HashMap<>(this.usuariosRegistrados);
        return usuariosRegistrados;
    }

    public void addConexion(int id, ConexionCliente conexion) throws InterruptedException {
        mutex.acquire();
        conexionesClientes.put(id, conexion);
        mutex.release();
    }

    public void removeConexion(int id) throws InterruptedException {
        mutex.acquire();
        conexionesClientes.remove(id);
        mutex.release();
    }

    public Map<Integer,ConexionCliente> getConexionesClientes() {
        conexionesClientes = new HashMap<>(this.conexionesClientes);
        return conexionesClientes;
    }

    public String getMenu() {
        return "Operations:\n1. List available information \n2. Download files\n3. Exit\nChoose an option: ";
    }
    
    public boolean buscarCliente(int id) {
		return usuariosRegistrados.containsKey(id);
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

	public Usuario getUsuario(int clientID) {
		return usuariosRegistrados.get(clientID);
	}

	public boolean hayClientesConectados() {
		return !conexionesClientes.isEmpty();
	}

}