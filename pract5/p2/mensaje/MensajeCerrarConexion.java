package pract5.p2.mensaje;

public class MensajeCerrarConexion extends MensajeString {
    MensajeCerrarConexion(int id_from, int id_to, String contenido) {
        super(id_from, id_to, contenido);
        
    }

    @Override
    public int getTipo() {
        // TODO Auto-generated method stub
        return 7;
    }
}
