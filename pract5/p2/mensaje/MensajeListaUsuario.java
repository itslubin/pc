package pract5.p2.mensaje;

public class MensajeListaUsuario extends MensajeString {

    public MensajeListaUsuario(int id_from, int id_to, String contenido) {
        super(id_from, id_to, contenido);
    }

    @Override
    public int getTipo() {
        return 2;
    }

}
