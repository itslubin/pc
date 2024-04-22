package pract5.p2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

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
			
			// TODO: clientID y serverID como mandamos los datos ya inicializados si el
			// servidor no sabe quienes somos??
			
			out.writeObject(new MensajeCliente(0, 0, cliente));

			// Mostrar menú
			mensaje = (Mensaje) in.readObject();
			ClientID = mensaje.getIdTo();
			ServerID = mensaje.getIdFrom();
			cliente.setClientID(ClientID);
			cliente.setServerID(ServerID);
			
			if (mensaje.getTipo() == 11) { // Recibimos el menu
				System.out.println(((MensajeMenu) mensaje).getContenido()); 
				int op = scanner.nextInt();
				
				// 1. El cliente pide una opcion
				out.writeObject(new MensajeOp(ClientID, ServerID, op));

				if (op == 1) {
					// Recibe la lista de usuarios registrados
					mensaje = (Mensaje) in.readObject();
					if (mensaje.getTipo() == 8) { // TODO: No debería ser el mensajeListaUsuarios ??
						System.out.println(((MensajeString) mensaje).getContenido());
					} else {
						System.out.println("Error al recibir la lista de usuarios");
					}

					// 4. Confirmar la recepción de la lista de usuarios registrados
					out.writeObject(new MensajeString(cliente.getClientID(), cliente.getServerID(), "Confirmación de recepción de la lista de usuarios registrados"));

				}
			} else {
				System.out.println("Error al recibir el mensaje");
			}

			// Cierra el scanner y la conexión
			scanner.close();
			clientSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
