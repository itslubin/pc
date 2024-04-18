package pract5.p1;

import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run() {
    	
        try {
            // Obtiene los streams de entrada y salida para comunicarse con el cliente
        	ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        	ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            
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
}