package pract3;

import java.util.concurrent.Semaphore;

public class Main1 {
	public static void main(String[] args) {
		
		/*Utilizamos un unico semaforo = 1 para manejar la exclusion mutua*/
		
		Semaphore sem = new Semaphore(1);
		Entero e = new Entero();
		int N = 10;
		
		Thread[] threads = new Thread[N*2];

        for (int i = 0; i < N; ++i) {
            threads[i] = new MiThreadIncr(sem, e);
            threads[i + N] = new MiThreadDecr(sem, e);
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
