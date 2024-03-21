package pract4.ver1;

public class MiThreadDecr extends Thread{
	private Entero n;

    public MiThreadDecr(Entero ent) {
        this.n = ent;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 1000; i++) {
    		n.decrease();
        }
    }
}
