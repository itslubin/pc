package pract2;

public class MiThreadIncr extends Thread {
	private Lock lock;
	private Entero n;
	private int id;

    public MiThreadIncr(Lock lock, Entero ent, int id) {
        this.lock = lock;
        this.n = ent;
        this.id = id;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 100000; i++) {
    		lock.takeLock(id);
            
    		n.increase();
            
            lock.releaseLock(id);
        }
    }
}
