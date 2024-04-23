package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import pract5.p2.mensaje.*;

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
            
            // 1.2 Obtener el nombre del cliente
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
            	clientID = c.getClientID();
            	usuario = servidor.getUsuario(clientID);            	
            }
            
            servidor.addConexion(clientID, new ConexionCliente(clientSocket, usuario));
            
            
            String menu = servidor.getMenu();
            out.writeObject(new MensajeMenu(servidor.getID(), clientID, menu)); //2.1 Mandamos menu

            while (true) {

                mensaje = (Mensaje) in.readObject(); // 1.2 Obtenemos la opcion
                if (mensaje.getTipo() == 9) { 
                    int op = ((MensajeOp) mensaje).getContenido();

                    if (op == 1) { // El cliente quiere la lista
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
                        out.writeObject(new MensajeString(servidor.getID(), clientID, listaUsuarios)); // 2.1 Manda la lista
                        
                        // Recibir la confirmacion de la lista del OyenteServidor
                        Mensaje mConfListaUsuarios = (Mensaje) in.readObject(); // 3.2 recibimos la confirmacion
                        
                        // Imprimir por consola la confirmación
                        System.out.println(((MensajeString) mConfListaUsuarios).getContenido());

                    } else if (op == 2) { // El cliente quiere descargar un fichero
                    	boolean found = false;
                        mensaje = (Mensaje) in.readObject(); // 2.2 Recibimos el nombre del fichero
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
                        
                        // 3.1 Mensaje emitir fichero al cliente emisor
                        // 4.2 Recibir Mensaje Preparado CS
                        
                        //5.1 Mensaje Preparado SC al cliente receptor
                        
                    } else if (op == 3) { // TODO
                    	// 2.2 Recibimos el mensaje de cierre de conexion
                        // Quitamos al Cliente del servidor
                    	servidor.removeConexion(clientID);
                    	
                    	// 3.1 Confirmacion ??
                    	if (!servidor.hayClientesConectados()) {
                    		break;
                    	}
                    	
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