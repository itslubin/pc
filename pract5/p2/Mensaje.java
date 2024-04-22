package pract5.p2;

import java.io.Serializable;

// Clase padre de la que heredan todos los tipos de mensajes
public abstract class Mensaje implements Serializable {
	
	int id_from;
	int id_to;
	int tipo;
	
	/* Tipos de mensajes:
	 * 0: Mensaje de conexion
	 * 1: Mensaje de lista de usuarios
	 * 2: Mensaje emitir fichero
	 * 3: Mensaje preparado para recibir fichero CS
	 * 4: Mensaje preparado para recibir fichero SC
	 * 5: Mensaje de confirmación conexión
	 * 6: Mensaje de confirmación de lista de usuarios
	 * 7: Mensaje cerrar conexión
	 * 8: Mensaje tipo String
	 * 9: Mensaje Opcion
	 * 10: Mensaje tipo Usuario
	 * 11: Mensaje de menu
	 * */

	Mensaje(int id_from, int id_to, int tipo) {
		this.id_from = id_from;
		this.id_to = id_to;
		this.tipo = tipo;
	}
	
    public abstract int getTipo();

	public int getIdFrom() {
		return id_from;
	}

	public int getIdTo() {
		return id_to;
	}
}