package pract5.p2.mensaje;

public class MensajeConf extends MensajeString{

	public MensajeConf(int id_from, int id_to, String contenido) {
		super(id_from, id_to, contenido);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public int getTipo() {
        // TODO Auto-generated method stub
        return 8;
    }

}