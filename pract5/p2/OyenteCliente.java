package pract5.p2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pract5.p1.Mensaje;

public class OyenteCliente implements Runnable {
	// Lista compartida de los usuarios conectados
    private Socket clientSocket;
    
    public OyenteCliente(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            // Obtiene los streams de entrada y salida para comunicarse con el cliente
        	ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        	ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        	
        	// Pide el nombre y manda sus datos
        	out.writeObject(new MensajeString("Indique el nombre de usuario: \n"));
        	
        	in.readObject();
        	
        	// Añadirlo a la tabla el cliente 
        	
        	// ¿como se puede añadir un cliente a la table?
        	// ¿que identificador le pongo al cliente?
        	
        	
            
            // Lee el nombre del archivo que el cliente quiere obtener
            Mensaje mensaje = (Mensaje) in.readObject();
            StringBuilder line = new StringBuilder();
            // Abre el archivo y envía su contenido al cliente
            try (BufferedReader fileReader = new BufferedReader(new FileReader(mensaje.getContenido()))) {
                String line2;
                while ((line2 = fileReader.readLine()) != null) {
                	line.append(line2);
                }
            } catch (IOException e) {
                // Si hay un error al leer el archivo, envía un mensaje de error al cliente
                line.append("Error al leer el archivo: " + e.getMessage());
            }
            
            out.writeObject(new Mensaje(line.toString()));
            
            // Cierra la conexión
            clientSocket.close();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    // Constructor y método run para la escucha del cliente
}