package pract5.p2.mensaje;

public class MensajePreparadoCS extends MensajeString {
    int receptorID;

    public MensajePreparadoCS(int id_from, int id_to, String contenido, int receptorID) {
        super(id_from, id_to, contenido);
        this.receptorID = receptorID;
    }

    @Override
    public int getTipo() {
        return 6;
    }

    public int getReceptorID() {
        return receptorID;
    }

}
