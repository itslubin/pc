package pract3;

public class Main3 {
    public static void main(String[] args) throws InterruptedException {
    	
    	int P = 10; // Numero de iteraciones de los Writer
        int C = 10; // Numero de iteraciones de los Reader
        int N = 10; // Numero de procesos Writer
        int M = 10; // Numero de procesos Reader


        AlmacenLB almacen = new AlmacenLB();
        Thread[] threads = new Thread[N+M];

        for (int i = 0; i < N; ++i) {
            threads[i] = new Writer(almacen, P, i + 1);
            threads[i].start();
        }

        for (int i = N; i < N + M; ++i) {
            threads[i] = new Reader(almacen, C, i + 1);
            threads[i].start();
        }

        for (int i = 0; i < N + M; i++) {
            try {
                threads[i].join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}