package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

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
            
            // Obtiene el nombre del cliente
            mensaje = (Mensaje) in.readObject();
            
            Cliente c = ((MensajeCliente) mensaje).getCliente();
            
            if (c.getClientID() == 0) { // Es un nuevo cliente
            	usuario = new Usuario(c.getNombre(),
                        clientSocket.getInetAddress().getHostAddress());
                        
                // Añadir el cliente a la tabla
                servidor.registrarUsuario(clientID, usuario);
                
            }
            
            else { // Es un usuario existente
//            	boolean find = servidor.buscarCliente(c.getClientID());
//            	if (!find) {
//            		
//            	}
            	usuario = servidor.getUsuario(c.getClientID());
            	
            }
            
            servidor.addConexion(clientID, new ConexionCliente(clientSocket, usuario));
            

            while (true) {
                String menu = servidor.getMenu();
                out.writeObject(new MensajeMenu(servidor.getID(), c.getClientID(), menu));

                mensaje = (Mensaje) in.readObject();
                if (mensaje.getTipo() == 9) { // Obtenemos la opcion
                    int op = ((MensajeOp) mensaje).getContenido();

                    if (op == 1) {
                        Map<Integer, ConexionCliente> clientes = servidor.getConexionesClientes();
                        int contador = 1;
                        String listaUsuarios = "Usuarios conectados:\n";
                        for (Map.Entry<Integer, ConexionCliente> c1 : clientes.entrySet()) {
                            Usuario u = c1.getValue().getUsuario();
                            listaUsuarios += contador++ + ". " + u.getNombreUsuario() + " | ID: " + c1.getKey() + "\n";
                            listaUsuarios += "Información compartida:\n";
                            int contador2 = 1;
                            for (Informacion i : u.getInformacionCompartida()) {
                                listaUsuarios += contador2++ + ". " + i.getNombre() + "\n";
                            }
                        }
                        out.writeObject(new MensajeString(servidor.getID(), c.getClientID(), listaUsuarios));
                        // TODO: Recibir la confirmacion de la lista del OyenteServidor

                    } else if (op == 2) {
                    	boolean found = false;
                        mensaje = (Mensaje) in.readObject();
                        String filename = ((MensajeString) mensaje).getContenido();
                        Map<Integer, ConexionCliente> clientes = servidor.getConexionesClientes();
                        for (Map.Entry<Integer, ConexionCliente> c1 : clientes.entrySet()) {
                            Usuario u = c1.getValue().getUsuario();
                            for (Informacion i : u.getInformacionCompartida()) { // buscar el archivo solicitado
                                if (i.getNombre().equals(filename)) {
                                    found = true;
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