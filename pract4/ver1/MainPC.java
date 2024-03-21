package pract4.ver1;

public class MainPC {
    public static void main(String[] args) throws InterruptedException {
    	
    	/*Vamos a tener en total C * M consumiciones y P * N producciones*/
    	
        int P = 10; // Numero de iteraciones de los productores
        int C = 10; // Numero de iteraciones de los consumidores
        int N = 5; // Numero de procesos productores
        int M = 5; // Numero de procesos consumidores

        Almacen almacen = new AlmacenPC();
        Thread[] threads = new Thread[N+M];

        for (int i = 0; i < N; ++i) {
            threads[i] = new Productor(almacen, P, i + 1);
            threads[i].start();
        }

        for (int i = N; i < N + M; ++i) {
            threads[i] = new Consumidor(almacen, C, i + 1);
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