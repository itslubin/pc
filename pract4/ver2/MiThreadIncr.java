package pract4.ver2;

public class MiThreadIncr extends Thread {
	private Entero n;

    public MiThreadIncr(Entero ent) {
        this.n = ent;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 1000; i++) {
    		n.increase();
        }
    }
}
