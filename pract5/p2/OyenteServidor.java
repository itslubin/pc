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

					System.out.print(((MensajeMenu) mensaje).getContenido());
					int op = scanner.nextInt();

					// Limpiar buffer
					scanner.nextLine();

					if (op == 1) {
						// 1.1 El cliente elige la opción de listar usuarios
						out.writeObject(new MensajeListaUsuario(ClientID, ServerID, "Listar usuarios"));

						// 2.2 Recibe la lista de usuarios registrados
						mensaje = (Mensaje) in.readObject();
						if (mensaje.getTipo() == 8) {
							System.out.println(((MensajeString) mensaje).getContenido());
						} else {
							System.out.println("Error al recibir la lista de usuarios");
						}

						// 3.1 Confirmar la recepción de la lista de usuarios registrados
						out.writeObject(new MensajeConfListaUsuario(ClientID, ServerID,
								String.valueOf(ClientID)
										+ " ha confirmado la recepción de la lista de usuarios registrados"));

					}

					else if (op == 2) { // TODO
						// 1.1 El cliente elige la opción de descargar fichero
						out.writeObject(new MensajePedirFichero(ClientID, ServerID, "Pedir fichero"));

						System.out.println("Introduzca el fichero que quiere descargar: ");
						String file_name = scanner.nextLine();

						// 2.1 Enviar el nombre del fichero
						out.writeObject(new MensajeString(ClientID, ServerID, file_name));

						// 5.2 Recibido Mensaje Preparado SC
						mensaje = (Mensaje) in.readObject();
						if (mensaje.getTipo() == 4) {
							System.out.println(((MensajePreparadoSC) mensaje).getContenido());
						} else { // TODO:innecesario
							System.out.println("Error al recibir el mensaje preparado SC");
						}
					}

					else if (op == 3) { // TODO
						// 1.1 El cliente elige la opción de cerrar conexión
						out.writeObject(new MensajeCerrarConexion(ClientID, ServerID, String.valueOf(ClientID)
								+ "ha cerrado la conexión"));
						System.out.println("Conexión cerrada");
						
						break;
					}

				}

				else if (mensaje.getTipo() == 2) { // Recibimos emitir fichero
					// 3.2 Recibir Mensaje emitir fichero
					System.out.println(((MensajeEmitirFichero) mensaje).getContenido());
					System.out.println("Fichero: " + ((MensajeEmitirFichero) mensaje).getFilename());
					// 4.1 Mensaje Preparado CS
					out.writeObject(new MensajePreparadoCS(ClientID, ServerID, String.valueOf(ClientID) +
							"preparado para recibir fichero"));
				} else {
					System.out.println("Error al recibir el mensaje emitir fichero");
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
