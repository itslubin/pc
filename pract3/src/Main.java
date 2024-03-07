public class Main {
    public static void main(String[] args) throws InterruptedException {
        int P = 10;
        int C = 10;
        int N = 5;
        int M = 5;


        Almacen almacen = new AlmacenNP();
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