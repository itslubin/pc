package pract5.p2.mensaje;

public class MensajeSubirFichero extends MensajeString {
	
	String filename;
	
	public MensajeSubirFichero(int id_from, int id_to, String contenido, String file) {
		super(id_from, id_to, contenido);
		filename = file;
	}
	
	@Override
	public int getTipo() {
		return 11;
	}
	
	public String getFilename() {
        return filename;
    }

}
