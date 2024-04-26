package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pract5.p2.mensaje.*;

public class Emisor implements Runnable {
    private String filename;
    private ServerSocket serverSocket;

    public Emisor(int port, String filename) throws IOException {
        serverSocket = new ServerSocket(port);
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            Socket clientSocket = serverSocket.accept();

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject(new MensajeFichero(0, 0, filename));
            System.out.println("Fichero " + filename + " enviado");

            Mensaje mensaje = (Mensaje) in.readObject();
            if (mensaje.getTipo() == 7)
                System.out.println("Conexión cerrada");

            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
