package pract5.p2;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pract5.p2.mensaje.*;

public class Receptor implements Runnable {
    private Socket clientSocket;

    public Receptor(String host, int port) throws UnknownHostException, IOException, ClassNotFoundException {
        clientSocket = new Socket(host, port);
    }

    @Override
    public void run() { 
        try{
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
    
            Mensaje mensaje = (Mensaje) in.readObject();
            if (mensaje.getTipo() == 8)
                System.out.println("Fichero " + ((MensajeString) mensaje).getContenido() + "recibido");
            else
                System.out.println("Fichero recibido erroneo");
            
            out.writeObject(new MensajeCerrarConexion(0, 0, "Cerrando conexión"));
    
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
