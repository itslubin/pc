package pract5.p2.mensaje;
import pract5.p2.Cliente;

public class MensajeCliente extends Mensaje {
	private Cliente c;

	public MensajeCliente(int id_from, int id_to, Cliente cliente) {
		super(id_from, id_to);
		this.c = cliente;
	}

	public Cliente getCliente() {
		return c;
	}

	@Override
	public int getTipo() {
		return 10;
	}

}
