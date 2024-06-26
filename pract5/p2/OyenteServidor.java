package pract5.p2;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import pract5.p2.mensaje.*;

public class OyenteServidor implements Runnable { // Se encarga de recibir los mensajes
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

			// Confirmacion datos de cliente
			mensaje = (Mensaje) in.readObject();
			if (mensaje.getTipo() == 1) {
				System.out.println("Conexión confirmada");
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
					System.out.println(((MensajeConfListaUsuario) mensaje).getContenido());
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
					System.out.println(((MensajePreparadoSC) mensaje).getContenido());
	
					Thread receptor = new Thread(new Receptor("localhost", 1235, cliente));
					receptor.start();

					cond.signal();
					lock.unlock();
				}
				
				// Recibimos confirmacion de subida de fichero
				else if (mensaje.getTipo() == 12) {
					lock.lock();
					System.out.println(((MensajeSubirFicheroConf) mensaje).getContenido());
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
					System.out.println(((MensajeError) mensaje).getContenido());
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
