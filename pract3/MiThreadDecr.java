package pract3;

import java.util.concurrent.Semaphore;

public class MiThreadDecr extends Thread{
	private Semaphore sem;
	private Entero n;

    public MiThreadDecr(Semaphore sem, Entero ent) {
        this.sem = sem;
        this.n = ent;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 1000; i++) {
    		try {
    			sem.acquire();
                
        		n.decrease();
        		
        		sem.release();
    		}
    		catch (Exception e){
                e.printStackTrace();
            }
            
            
        }
    }
}
