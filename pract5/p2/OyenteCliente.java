package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Semaphore;

import pract5.p2.mensaje.*;

public class OyenteCliente implements Runnable {
    // Constructor y método run para la escucha del cliente

    // Lista compartida de los usuarios conectados
    private Usuario usuario;
    private Servidor servidor;
    private Socket clientSocket;
    private int clientID;

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

            // 1.2 Obtener el mensaje de conexión
            mensaje = (Mensaje) in.readObject();

            clientID = ((MensajeConexion) mensaje).getID();
            if (clientID == 0 || !servidor.buscarCliente(clientID)) { // Es un nuevo cliente o no existe
                clientID = servidor.getNewId();
                usuario = new Usuario(((MensajeConexion) mensaje).getNombre(),
                        clientSocket.getInetAddress().getHostAddress());

                // Añadir el cliente a la tabla
                servidor.registrarUsuario(clientID, usuario);
            } else {
                usuario = servidor.getUsuario(clientID);
            }

            out.writeObject(new MensajeConfConexion(clientID, clientID, "Conexión confirmada"));
            servidor.registarLock(clientID, new LockTicket());
            servidor.addConexion(clientID, new ConexionCliente(clientSocket, usuario, in, out));

            while (true) {

                // 1.2 Obtenemos el mensaje del usuario
                mensaje = (Mensaje) in.readObject();

                // El cliente quiere la lista
                if (mensaje.getTipo() == 2) {

                    Map<Integer, ConexionCliente> clientes = servidor.getConexionesClientes();
                    int contador = 1;
                    String listaUsuarios = "\nUsuarios conectados:\n";
                    for (Map.Entry<Integer, ConexionCliente> c1 : clientes.entrySet()) {
                        Usuario u = c1.getValue().getUsuario();
                        listaUsuarios += "\n" + contador++ + ". Nombre: " + u.getNombreUsuario() + " | ID: "
                                + c1.getKey() + "\n";
                        listaUsuarios += "Información compartida:\n";
                        int contador2 = 1;
                        for (Informacion i : u.getInformacionCompartida()) {
                            listaUsuarios += contador2++ + ". " + i.getNombre() + "\n";
                        }
                    }
                    
                    // Cojemos el lock del cliente
                    servidor.lock(clientID);
                    
                    // 2.1 Manda la lista
                    out.writeObject(new MensajeConfListaUsuario(servidor.getID(), clientID, listaUsuarios));
                    
                    // Liberamos el lock del cliente
                    servidor.unlock(clientID);
                }

                else if (mensaje.getTipo() == 4) { // El cliente quiere descargar un fichero
                    int emitorID = -1;
                    // 2.2 Recibimos el nombre del fichero
                    String filename = ((MensajePedirFichero) mensaje).getContenido();
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
                    
                    
                    if (emitorID != -1) {
                        // Cojemos al lock del cliente emisor
                        servidor.lock(emitorID);

                        // Llamar al Cliente emisor
                        ConexionCliente cc = servidor.getConexionCliente(emitorID);
                        ObjectOutputStream outE = cc.getOut();
                        
                        
                        // 3.1 Mensaje emitir fichero al cliente emisor
                        outE.writeObject(
                                new MensajeEmitirFichero(servidor.getID(), emitorID, "Emito fichero", filename,
                                        clientID));

                        // Liberamos el lock
                        servidor.unlock(emitorID);

                    } else {
                    	// Cojemos el lock del cliente
                        servidor.lock(clientID);
                        out.writeObject(new MensajeError(servidor.getID(), clientID,
                                "### Log cliente: Error al recibir el mensaje preparado SC, el fichero " + filename
                                        + " no se ha encontrado ###"));
                        
                        // Liberamos el lock del cliente
                        servidor.unlock(clientID);
                        
                        System.out.println(
                                "Fichero " + filename + " solicitado por el cliente " + clientID + " no encontrado");
                    }

                }

                else if (mensaje.getTipo() == 6) {
                    // 5.1 Mensaje Preparado SC al cliente receptor
                    System.out.println(((MensajePreparadoCS) mensaje).getContenido());

                    int receptorID = ((MensajePreparadoCS) mensaje).getReceptorID();
                    // Cojemos al lock del cliente receptor
                    servidor.lock(receptorID);

                    ConexionCliente cc = servidor.getConexionCliente(receptorID);
                    ObjectOutputStream outR = cc.getOut();

                    // Llamar al Cliente receptor
                    outR.writeObject(new MensajePreparadoSC(servidor.getID(), receptorID,
                            "### Log cliente: Preparado para recibir el fichero ##"));

                    // Liberamos el lock
                    servidor.unlock(receptorID);

                    System.out.println("Finish Preparado SC");
                }

                else if (mensaje.getTipo() == 9) { // Cerrar conexión
                    System.out.println(((MensajeCerrarConexion) mensaje).getContenido());
                    
                    // Cojemos el lock del cliente
                    servidor.lock(clientID);

                    out.writeObject(new MensajeCerrarConexion(servidor.getID(), clientID, "Conexion cerrada"));
                    
                    // Liberamos el lock del cliente
                    servidor.unlock(clientID);
                    
                    // Quitamos al Cliente del servidor
                    servidor.removeConexion(clientID);
                    break;
                }
                
                else if (mensaje.getTipo() == 11) { // Subir fichero
                	System.out.println(((MensajeSubirFichero) mensaje).getContenido());
                	
                	servidor.addFichero(((MensajeSubirFichero) mensaje).getFilename(), clientID);
                	
                	// Cojemos el lock del cliente
                    servidor.lock(clientID);
                    
                	out.writeObject(new MensajeSubirFicheroConf(clientID, clientID, "### Log cliente: Fichero compartido con exito ###"));
                	
                	// Liberamos el lock del cliente
                    servidor.unlock(clientID);
                }
            }

            // Cierra la conexión
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}