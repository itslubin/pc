package pract5.p2;

import java.util.Scanner;

public class Cliente {
    private String nombre;
    private int ClientID;
    private int ServerID;
    private String host;
    private int port;

    public Cliente(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
    }

    public void execute() throws Exception {
        // Pide el nombre y manda sus datos
        System.out.println("Introduce tu nombre de usuario: ");
        Scanner scanner = new Scanner(System.in);
        nombre = scanner.nextLine();

        Thread clientThread = new Thread(new OyenteServidor(this, host, port));
        clientThread.start();
        clientThread.join();
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