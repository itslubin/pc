package pract5.p2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Servidor {
    private int id = 0;
    private volatile int counter = 0;
    private ServerSocket serverSocket;
    private volatile Map<Integer, Usuario> usuariosRegistrados; // Clave: ID_Usuario, Valor: Usuario
    private volatile Map<Integer, ConexionCliente> conexionesClientes; // Clave: ID_Usuario, Valor: ConexionCliente
    
    // Variables Lectores-escritores
    int nr = 0;
    int nw = 0;
    int dw = 0;
    private Lock lock;
    private Condition oktoread;
    private Condition oktowrite;

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        usuariosRegistrados = new HashMap<>();
        conexionesClientes = new HashMap<>();
        lock = new ReentrantLock();
        oktoread = lock.newCondition();
        oktowrite = lock.newCondition();
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
    	request_read();
    	int _id = id;
    	release_read();
        return _id;
    }
    
    public int getNewId() throws InterruptedException { // Write
    	request_write();
    	int c = ++counter;
    	release_write();
        return c;
    }

    public void registrarUsuario(int id, Usuario usuario) throws InterruptedException { // Write
    	request_write();
        usuariosRegistrados.put(id, usuario);
        release_write();
    }

    public Map<Integer, Usuario> getUsuariosRegistrados() { // Read
    	request_read();
    	Map<Integer, Usuario> aux = new HashMap<>(usuariosRegistrados);
    	release_read();
        return aux;
    }

    public void addConexion(int id, ConexionCliente conexion) throws InterruptedException { // Write
    	request_write();
        conexionesClientes.put(id, conexion);
        release_write();
    }

    public void removeConexion(int id) throws InterruptedException { // Write
    	request_write();
        conexionesClientes.remove(id);
        release_write();
    }

    public Map<Integer, ConexionCliente> getConexionesClientes() { // Read
    	request_read();
    	Map<Integer, ConexionCliente> aux = new HashMap<>(conexionesClientes);
    	release_read();
        return aux;
    }

    public boolean buscarCliente(int id) { // Read
    	boolean c;
    	request_read();
    	c = usuariosRegistrados.containsKey(id);
    	release_read();
        return c;
    }
    
    public ConexionCliente getConexionCliente(int id) { // Read
    	request_read();
    	ConexionCliente cc = conexionesClientes.get(id);
    	release_read();
    	return cc;
    }

    public Usuario getUsuario(int clientID) { // Read
    	request_read();
    	Usuario u = usuariosRegistrados.get(clientID);
    	release_read();
        return u;
    }

    public boolean hayClientesConectados() { // Read
    	request_read();
    	boolean b = !conexionesClientes.isEmpty();
    	release_read();
        return b;
    }
    
    public String getMenu() {
        return "Operations:\n1. List available information \n2. Download files\n3. Exit\nChoose an option: ";
    }
    
    public void request_read() {
        while (nw > 0) {
            try {
                oktoread.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nr++;
    }

    public void release_read() {
        nr--;
        if (nr==0) oktowrite.signal();
        
    }

    public void request_write() {
        while (nr > 0 || nw > 0) {
            try {
            	dw++;
                oktowrite.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nw++;
    }

    public void release_write() {
        nw--;
        if (dw > 0) {
        	dw--;
        	oktowrite.signal();
        }
        else {
        	oktoread.signal();
        }
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