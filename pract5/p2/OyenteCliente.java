package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

                Lock lock = new ReentrantLock();
                servidor.registarLock(clientID, lock);
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

            // 2.1 Mandamos menu
            out.writeObject(new MensajeMenu(servidor.getID(), clientID, menu));

            while (true) {

                // 1.2 Obtenemos el mensaje del usuario (opcion)
                mensaje = (Mensaje) in.readObject();
                
                // Cojemos el lock del cliente
                servidor.lock(clientID);

                // El cliente quiere la lista
                if (mensaje.getTipo() == 1) {

                    Map<Integer, ConexionCliente> clientes = servidor.getConexionesClientes();
                    int contador = 1;
                    String listaUsuarios = "\nUsuarios conectados:\n";
                    for (Map.Entry<Integer, ConexionCliente> c1 : clientes.entrySet()) {
                        Usuario u = c1.getValue().getUsuario();
                        listaUsuarios += "\n" + contador++ + ". Nombre: " + u.getNombreUsuario() + " | ID: "
                                + c1.getKey()
                                + "\n";
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
                    	if (c1.getKey() != clientID) {
                    		Usuario u = c1.getValue().getUsuario();
                            // buscar el archivo solicitado
                            for (Informacion i : u.getInformacionCompartida()) {
                                if (i.getNombre().equals(filename)) {
                                    emitorID = c1.getKey();
                                }
                            }
                    	}
                    }
                    
                    System.out.println("Fin del bucle de buscar emisor");
                    

                    if (emitorID != -1) {

                        // Cojemos al lock del cliente emisor
                        servidor.lock(emitorID);
                        
                        System.out.println("Encontramos un emisor");
                        
                        // Llamar al Cliente emisor, TODO: crear otro OyenteServidor/OyenteCliente para tener otro Socket
                        ConexionCliente cc = servidor.getConexionCliente(emitorID);
                        Socket s = cc.getSocket();

                        outE = new ObjectOutputStream(s.getOutputStream());
                        inE = new ObjectInputStream(s.getInputStream());

                        // 3.1 Mensaje emitir fichero al cliente emisor
                        outE.writeObject(
                                new MensajeEmitirFichero(servidor.getID(), emitorID, "Emito fichero", filename));

                        // 4.2 Recibir Mensaje Preparado CS
                        mensaje = (Mensaje) inE.readObject();
                        
                        System.out.println(((MensajePreparadoCS) mensaje).getContenido());
                        
                        // 5.1 Mensaje Preparado SC al cliente receptor
                        if (mensaje.getTipo() == 3) {
                        	out.writeObject(
                                    new MensajePreparadoSC(servidor.getID(), clientID,
                                            "Preparado para recibir el fichero " + filename));
                        }

                        // Liberamos el lock
                        servidor.unlock(emitorID);

                    } else {
                        out.writeObject(new MensajeError(servidor.getID(), clientID,
                                "Error al recibir el mensaje preparado SC, el fichero "
                                        + filename + " no se ha encontrado"));
                        System.out.println(
                                "Fichero " + filename + " solicitado por el cliente " + clientID + " no encontrado");
                        continue;
                    }

                } else if (mensaje.getTipo() == 7) { // TODO
                    System.out.println(((MensajeCerrarConexion) mensaje).getContenido());

                    // Quitamos al Cliente del servidor
                    servidor.removeConexion(clientID);
                    break;
                }

                servidor.unlock(clientID);
            }

            // Cierra la conexión
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}