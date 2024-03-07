package pract2;

public class MiThreadDecr extends Thread{
	private Lock lock;
	private Entero n;
	private int id;

    public MiThreadDecr(Lock lock, Entero ent, int id) {
        this.lock = lock;
        this.n = ent;
        this.id = id;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 1000; i++) {
    		lock.takeLock(id);
            
    		n.decrease();
            
            lock.releaseLock(id);
        }
    }
}
