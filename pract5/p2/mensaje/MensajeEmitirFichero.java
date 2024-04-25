package pract5.p2.mensaje;

public class MensajeEmitirFichero extends MensajeString {
    String filename;
    int ClienteID;

    public MensajeEmitirFichero(int id_from, int id_to, String contenido, String filename, int ClienteID) {
        super(id_from, id_to, contenido);
        this.filename = filename;
        this.ClienteID = ClienteID;
    }

    @Override
    public int getTipo() {
        return 5;
    }

    public String getFilename() {
        return filename;
    }

    public int getClienteID() {
        return ClienteID;
    }

}
