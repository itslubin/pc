package pract5.p2;

public class MensajeString extends Mensaje {
	private String contenido;

    public MensajeString(int id_from, int id_to, String contenido) {
        super(id_from, id_to, 0);
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

	@Override
	public int getTipo() {
		return 8;
	}
}
