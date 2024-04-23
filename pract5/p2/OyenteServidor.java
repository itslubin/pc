package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import pract5.p2.mensaje.*;

public class OyenteServidor implements Runnable {
	// Constructor y método run para la escucha del servidor
	Cliente cliente;
	Socket clientSocket;
	String host;
	int port;
	int ClientID;
	int ServerID;

	public OyenteServidor(Cliente cliente, String host, int port) {
		this.cliente = cliente;
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			Mensaje mensaje;
			Scanner scanner = new Scanner(System.in);

			// Conecta al servidor en el puerto 1234 en localhost
			clientSocket = new Socket(host, port);

			// Obtén los streams de entrada y salida para comunicarte con el servidor
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

			// Envía el nombre del archivo que se desea obtener
			
			// 1.1 Mandar datos cliente
			out.writeObject(new MensajeCliente(0, 0, cliente));
			
			while (true) {
				
				mensaje = (Mensaje) in.readObject(); // 2.2 Recibimos el mensaje
				ClientID = mensaje.getIdTo();
				ServerID = mensaje.getIdFrom();
				cliente.setClientID(ClientID);
				cliente.setServerID(ServerID);
				
				if (mensaje.getTipo() == 11) { // Recibimos menu
					
					System.out.println(((MensajeMenu) mensaje).getContenido()); 
					int op = scanner.nextInt();
					
					// Limpiar buffer
					scanner.nextLine();
					
					// 1.1 El cliente pide una opcion
					out.writeObject(new MensajeOp(ClientID, ServerID, op));

					if (op == 1) {
						// 2.2 Recibe la lista de usuarios registrados
						mensaje = (Mensaje) in.readObject();
						if (mensaje.getTipo() == 1) { 
							System.out.println(((MensajeListaUsuario) mensaje).getContenido());
						} else {
							System.out.println("Error al recibir la lista de usuarios");
						}

						// 3.1 Confirmar la recepción de la lista de usuarios registrados
						out.writeObject(new MensajeConf(cliente.getClientID(), cliente.getServerID(), String.valueOf(cliente.getClientID()) + " ha confirmado la recepción de la lista de usuarios registrados"));

					}
					
					else if (op == 2) { // TODO
						System.out.println("Introduzca el fichero que quiere descargar: ");
						String file_name = scanner.nextLine();
						
						// out.writeObject(); // 2.1 Enviar el nombre del fichero
						
						// 5.2 Recibido Mensaje Preparado SC
					}
					
					else if (op == 3) { // TODO
						// 2.1 Mandar mensaje de cierre de conexion
						// 3.2 Recibir confirmacion
						break;
					}
					
				} 
				
				else if (mensaje.getTipo() == 2) { // Recibimos emitir fichero
					// 3.2 Recibir Mensaje emitir fichero
					
					// 4.1 Mensaje Preparado CS
				}
				else {
					System.out.println("Error al recibir el mensaje");
				}
			}

			// Cierra el scanner y la conexión
			scanner.close();
			clientSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
