package pract5.p2.mensaje;

public class MensajeConexion extends Mensaje {
	private int id;
	private String nombre;
    
    public MensajeConexion(int id_from, int id_to,  int id, String nombre) {
        super(id_from, id_to);
		this.id = id;
		this.nombre = nombre;
    }

    @Override
    public int getTipo() {
        return 0;
    }

	public int getID() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
}
