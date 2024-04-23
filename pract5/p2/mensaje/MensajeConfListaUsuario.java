package pract5.p2.mensaje;

public class MensajeConfListaUsuario extends Mensaje {
    MensajeConfListaUsuario(int id_from, int id_to, int tipo) {
        super(id_from, id_to, tipo);
        //TODO Auto-generated constructor stub
    }

    @Override
    public int getTipo() {
        // TODO Auto-generated method stub
        return 6;
    }
}
