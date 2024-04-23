package pract5.p2.mensaje;

public class MensajeMenu extends MensajeString{

    public MensajeMenu(int id_from, int id_to, String contenido) {
        super(id_from, id_to, contenido);
    }

	@Override
	public int getTipo() {
		return 11;
	}
}
