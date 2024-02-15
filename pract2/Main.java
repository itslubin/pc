package pract2;

public class Main {
	
	public static void main(String[] args) {
		
		int N = 10;
		Lock lock = new LockRompeEmpate(N);
	    Entero n = new Entero();
	    Thread[] threads = new Thread[N];
	    
	    for (int i = 0; i < N/2; ++i) {
	    	
	    	threads[i] = new MiThreadIncr(lock, n, i + 1);
	    	threads[i].start();
	    }
	    
	    for (int i = N/2; i < N; ++i) {
	    	threads[i] = new MiThreadDecr(lock, n, i + 1);
	    	threads[i].start();
	    }
	    
	    for (int i = 0; i < N; i++) {
            try {
            	threads[i].join();
            	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	    
	    
	    System.out.println(n.getn());
	}
}
