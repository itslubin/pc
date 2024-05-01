package pract5.p2;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pract5.p2.mensaje.*;

public class Receptor implements Runnable {
    private Cliente cliente;
    private Socket clientSocket;

    public Receptor(String host, int port, Cliente cliente) throws UnknownHostException, IOException, ClassNotFoundException {
        clientSocket = new Socket(host, port);
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            Mensaje mensaje = (Mensaje) in.readObject();
            if (mensaje.getTipo() == 8)
                System.out.println("### Log cliente: Fichero " + ((MensajeFichero) mensaje).getFilename() + " recibido ###");
            else
                System.out.println("### Log cliente: Fichero recibido erroneo ###");

            out.writeObject(new MensajeCerrarConexion(0, 0, "Cerrando conexión"));
            System.out.println("### Log cliente: Conexión receptor cerrada ###");

            ((MensajeFichero) mensaje).save(cliente.getNombre() + cliente.getClientID());
            cliente.compartirFichero(((MensajeFichero) mensaje).getFilename());

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
