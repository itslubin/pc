package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
                   // boolean find = servidor.buscarCliente(c.getClientID());
                   // if (!find) {
                   //
                   // }
                clientID = c.getClientID();
                usuario = servidor.getUsuario(clientID);
            }

            servidor.addConexion(clientID, new ConexionCliente(clientSocket, usuario));

            String menu = servidor.getMenu();

            while (true) {
                // 2.1 Mandamos menu
                out.writeObject(new MensajeMenu(servidor.getID(), clientID, menu));

                // 1.2 Obtenemos el mensaje del usuario (opcion)
                mensaje = (Mensaje) in.readObject();

                // El cliente quiere la lista
                if (mensaje.getTipo() == 1) {
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

                    // 2.1 Manda la lista
                    out.writeObject(new MensajeString(servidor.getID(), clientID, listaUsuarios));

                    // Recibir la confirmacion de la lista del OyenteServidor
                    Mensaje mConfListaUsuarios = (Mensaje) in.readObject(); // 3.2 recibimos la confirmacion

                    // Imprimir por consola la confirmación
                    System.out.println(((MensajeConfListaUsuario) mConfListaUsuarios).getContenido());

                } else if (mensaje.getTipo() == 2) { // El cliente quiere descargar un fichero
                    int emitorID = -1;
                    ObjectOutputStream outE;
                    ObjectInputStream inE;
                    // 2.2 Recibimos el nombre del fichero
                    mensaje = (Mensaje) in.readObject();
                    String filename = ((MensajeString) mensaje).getContenido();
                    Map<Integer, ConexionCliente> clientes = servidor.getConexionesClientes();
                    for (Map.Entry<Integer, ConexionCliente> c1 : clientes.entrySet()) {
                        Usuario u = c1.getValue().getUsuario();
                        // buscar el archivo solicitado
                        for (Informacion i : u.getInformacionCompartida()) {
                            if (i.getNombre().equals(filename)) {
                                emitorID = c1.getKey();
                            }
                        }
                    }

                    // 3.1 Mensaje emitir fichero al cliente emisor
                    if (emitorID != -1) {
                    	// Llamar al Cliente emisor
                    	ConexionCliente cc = servidor.getConexionCliente(emitorID);
                    	Socket s = cc.getSocket();
                    	
                    	outE = new ObjectOutputStream(s.getOutputStream());
                        inE = new ObjectInputStream(s.getInputStream());
                    	
                        outE.writeObject(
                                new MensajeEmitirFichero(servidor.getID(), emitorID, "Emitir fichero", filename));
                    } else {
                        // out.writeObject(new MensajeString(servidor.getID(), clientID, "Fichero no encontrado"));
                    	System.out.println("Fichero no encontrado");
                    	continue;
                    }
                    // 4.2 Recibir Mensaje Preparado CS
                    mensaje = (Mensaje) inE.readObject();
                    if (mensaje.getTipo() == 3) {
                        System.out.println(((MensajePreparadoCS) mensaje).getContenido());
                    } else { // TODO: innecesario, ya que solo va a poder recibir la confirmacion si previamente ya ha encontrado el fichero
                        System.out.println("Error al recibir el fichero");
                    } 

                    // 5.1 Mensaje Preparado SC al cliente receptor
                    out.writeObject(
                            new MensajePreparadoSC(servidor.getID(), clientID, "Preparado para recibir fichero"));

                } else if (mensaje.getTipo() == 7) { // TODO
                    System.out.println(((MensajeCerrarConexion) mensaje).getContenido());

                    // Quitamos al Cliente del servidor
                    servidor.removeConexion(clientID);
                    break;
                }

            }

            // Cierra la conexión
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}