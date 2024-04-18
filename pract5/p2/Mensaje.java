package pract5.p2;

// Clase padre de la que heredan todos los tipos de mensajes
public abstract class Mensaje {
	
	int id_from, id_to, tipo;
	
	/* Tipos de mensajes:
	 * 0: Lista de usuarios
	 * 1: String
	 * 2: Fichero
	 * 3: Imagen
	 * 4: Vídeo
	 * */
	
    public abstract int getTipo();
}