package pract5.p2;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import pract5.p2.mensaje.*;

public class OyenteServidor implements Runnable {
	// Constructor y método run para la escucha del servidor
	Cliente cliente;
	Socket clientSocket;
	int ClientID;
	int ServerID;
	Lock lock;
	Condition cond;

	public OyenteServidor(Cliente cliente, Socket clientSocket, Lock lock, Condition cond) {
		this.cliente = cliente;
		this.clientSocket = clientSocket;
		this.lock = lock;
		this.cond = cond;
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
			if (mensaje.getTipo() == 1) {
				System.out.println("Conección confirmada");
				lock.lock();
				cond.signal();
				lock.unlock();
			} else {
				System.out.println("Error al establecer la conexión");
			}

			ClientID = mensaje.getIdTo();
			ServerID = mensaje.getIdFrom();
			cliente.setClientID(ClientID);
			cliente.setServerID(ServerID);

			while (true) {
				mensaje = (Mensaje) in.readObject();

				// Recibimos mensaje de confirmación de lista de usuarios
				if (mensaje.getTipo() == 3) {
					lock.lock();
					cliente.setMensaje(mensaje);
					cond.signal();
					lock.unlock();
				}

				// Recibimos mensaje de emitir fichero
				else if (mensaje.getTipo() == 5) {
					cliente.emitirFichero((MensajeEmitirFichero) mensaje);
				}

				// Recibimos mensaje de preparado para enviar fichero SC
				else if (mensaje.getTipo() == 7) {
					lock.lock();
					cliente.setMensaje(mensaje);
					cond.signal();
					lock.unlock();
				}

				// Recibimos mensaje de cerrar conexión
				else if (mensaje.getTipo() == 9) {
					break;
				}

				// Recibimos mensaje de error
				else if (mensaje.getTipo() == 10) {
					lock.lock();
					cliente.setMensaje(mensaje);
					cond.signal();
					lock.unlock();
				}
			}

			clientSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
