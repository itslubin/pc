package pract5.p2;

public class MensajeString extends Mensaje {
	private String contenido;

    public MensajeString(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

	@Override
	public int getTipo() {
		// TODO Auto-generated method stub
		return 0;
	}
}
