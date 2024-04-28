package pract5.p2.mensaje;

import java.io.Serializable;

// Clase padre de la que heredan todos los tipos de mensajes
public abstract class Mensaje implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id_from;
	int id_to;
	int tipo;

	/*
	 * Tipos de mensajes:
	 * 0: Mensaje de conexion
	 * 1: Mensaje de confirmación conexión
	 * 2: Mensaje de lista de usuarios
	 * 3: Mensaje de confirmación de lista de usuarios
	 * 4: Mensaje de pedir fichero
	 * 5: Mensaje de emitir fichero
	 * 6: Mensaje de preparado para enviar fichero CS
	 * 7: Mensaje de preparado para enviar fichero SC
	 * 8: Mensaje de fichero
	 * 9: Mensaje de cerrar conexión
	 * 10: Mensaje de error
	 * 11: Mensaje de subir fichero
	 * 12: Mensaje de confirmar subir fichero
	 */

	Mensaje(int id_from, int id_to) {
		this.id_from = id_from;
		this.id_to = id_to;
	}

	public abstract int getTipo();

	public int getIdFrom() {
		return id_from;
	}

	public int getIdTo() {
		return id_to;
	}
}