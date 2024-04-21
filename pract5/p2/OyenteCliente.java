package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class OyenteCliente implements Runnable {
    // Constructor y método run para la escucha del cliente

    // Lista compartida de los usuarios conectados
    private Usuario usuario;
    private Servidor servidor;
    private Socket clientSocket;

    public OyenteCliente(Socket clientSocket, Servidor servidor) {
        this.clientSocket = clientSocket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            // Obtiene los streams de entrada y salida para comunicarse con el cliente
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            Mensaje mensaje;
            int clientID = servidor.getNewId();
            
            // TODO: Añadir op para ver si es usuario ya registrado o no (0 en caso de no existir y >0 en caso de existir)
            
            // Obtiene el nombre del cliente
            mensaje = (Mensaje) in.readObject();
            if (mensaje.getTipo() == 8) {
                usuario = new Usuario(clientID, ((MensajeString) mensaje).getContenido(),
                        clientSocket.getInetAddress().getHostAddress());
                        
                // Añadirlo a la tabla el cliente
                servidor.registrarUsuario(usuario);
                
                servidor.addConexion(new ConexionCliente(clientSocket, usuario));
            }

            while (true) {
                String menu = servidor.getMenu();
                out.writeObject(new MensajeString(servidor.getID(), usuario.getId(), menu));

                mensaje = (Mensaje) in.readObject();
                if (mensaje.getTipo() == 9) {
                    int op = ((MensajeInt) mensaje).getContenido();

                    if (op == 1) {
                        List<ConexionCliente> clientes = servidor.getConexionesClientes();
                        int contador = 1;
                        String listaUsuarios = "Usuarios conectados:\n";
                        for (ConexionCliente c : clientes) {
                            Usuario u = c.getUsuario();
                            listaUsuarios += contador++ + ". " + u.getNombreUsuario() + " | ID: " + u.getId() + "\n";
                            listaUsuarios += "Información compartida:\n";
                            int contador2 = 1;
                            for (Informacion i : u.getInformacionCompartida()) {
                                listaUsuarios += contador2++ + ". " + i.getNombre() + "\n";
                            }
                        }
                        out.writeObject(new MensajeString(servidor.getID(), usuario.getId(), listaUsuarios));
                        // TODO: Recibir la confirmacion de la lista del OyenteServidor

                    } else if (op == 2) {
                        mensaje = (Mensaje) in.readObject();
                        String filename = ((MensajeString) mensaje).getContenido();
                        List<ConexionCliente> clientes = servidor.getConexionesClientes();
                        for (ConexionCliente c : clientes) {
                            Usuario u = c.getUsuario();
                            for (Informacion i : u.getInformacionCompartida()) {
                                if (i.getNombre().equals(filename)) {
                                    
                                }
                            }
                        }
                        
                    } else if (op == 3) {
                        break;
                    }

                }
            }

            // Cierra la conexión
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}