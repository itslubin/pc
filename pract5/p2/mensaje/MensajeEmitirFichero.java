package pract5.p2.mensaje;

public class MensajeEmitirFichero extends MensajeString {
    String filename;

    public MensajeEmitirFichero(int id_from, int id_to, String contenido, String filename) {
        super(id_from, id_to, contenido);
        this.filename = filename;
    }

    @Override
    public int getTipo() {
        return 2;
    }

    public String getFilename() {
        return filename;
    }
    
}
