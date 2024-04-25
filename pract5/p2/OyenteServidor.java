package pract5.p2;

import java.io.ObjectInputStream;
import java.net.Socket;
import pract5.p2.mensaje.*;

public class OyenteServidor implements Runnable {
	// Constructor y método run para la escucha del servidor
	Cliente cliente;
	Socket clientSocket;
	int ClientID;
	int ServerID;

	public OyenteServidor(Cliente cliente, Socket clientSocket) {
		this.cliente = cliente;
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			Mensaje mensaje;

			// Obtén los streams de entrada y salida para comunicarte con el servidor
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

			// 2.2 Confirmacion datos de cliente
			// 5.2 Recibido Mensaje Preparado SC
			mensaje = (Mensaje) in.readObject();
			if (mensaje.getTipo() == 5) {
				System.out.println("Conección confirmada");
			} else {
				System.out.println("Error al establecer la conexión");
			}

			ClientID = mensaje.getIdTo();
			ServerID = mensaje.getIdFrom();
			cliente.setClientID(ClientID);
			cliente.setServerID(ServerID);

			while (true) {
				// 2.2 Recibe la lista de usuarios registrados

				mensaje = (Mensaje) in.readObject();

				if (mensaje.getTipo() == 2) { // Recibimos emitir fichero
					cliente.emitirFichero((MensajeEmitirFichero) mensaje);
				}

				else if (mensaje.getTipo() == 1) {
					System.out.println(((MensajeString) mensaje).getContenido());
				}

				else if (mensaje.getTipo() == 4) {
					if (mensaje.getTipo() == 4) {
						System.out.println(((MensajePreparadoSC) mensaje).getContenido());

						// Thread receptor = new Thread(new Receptor("localhost", 1235));
						// receptor.start();
					} else {
						System.out.println(((MensajeError) mensaje).getContenido());
					}
				}

				else if (mensaje.getTipo() == 7) {
					break;
				}
			}

			clientSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
