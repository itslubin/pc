package pract5.p2.mensaje;
import pract5.p2.Cliente;

public class MensajeCliente extends Mensaje {
	private int id;
	private String nombre;

	public MensajeCliente(int id_from, int id_to, int id, String nombre) {
		super(id_from, id_to);
		this.id = id;
		this.nombre = nombre;
	}

	public int getID() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public int getTipo() {
		return 10;
	}

}
