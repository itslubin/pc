package pract5.p2;

public class MensajeInt extends Mensaje {
	private int contenido;

    public MensajeInt(int contenido, int id_from, int id_to) {
        super(id_from, id_to, 0);
        this.contenido = contenido;
    }

    public int getContenido() {
        return contenido;
    }

	@Override
	public int getTipo() {
		return 9;
	}
}
