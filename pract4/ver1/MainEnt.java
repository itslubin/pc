package pract4.ver1;

public class MainEnt {
	public static void main(String[] args) {
		Entero e = new Entero();
		int N = 10;
		
		Thread[] threads = new Thread[N*2];

        for (int i = 0; i < N; ++i) {
            threads[i] = new MiThreadIncr(e);
            threads[i + N] = new MiThreadDecr(e);
            threads[i].start();
            threads[i + N].start();
        }
        
        for (int i = 0; i < N*2; i++) {
            try {
                threads[i].join();

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        
        System.out.println(e.getn());
        
	}
}
