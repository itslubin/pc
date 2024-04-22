package pract5.p2;

public class MensajeCliente extends Mensaje {
	
	private Cliente c;
	
	public MensajeCliente(int id_from, int id_to, Cliente cliente) {
        super(id_from, id_to, 0);
        this.c = cliente;
    }
	
	public Cliente getCliente() {
        return c;
    }
	
	@Override
	public int getTipo() {
		// TODO Auto-generated method stub
		return 10;
	}

}
