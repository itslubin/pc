package pract1;

public class MiThread extends Thread {
    private int identificador;
    private int tiempoMilisegundos;

    public MiThread(int identificador, int tiempoMilisegundos) {
        this.identificador = identificador;
        this.tiempoMilisegundos = tiempoMilisegundos;
    }

    @Override
    public void run() {
        System.out.println("Thread " + identificador + " iniciado.");
        try {
            Thread.sleep(tiempoMilisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + identificador + " terminado.");
    }
    
    public static void main(String[] args) {
        int N = 5; // Número de threads
        int T = 1000; // Tiempo en milisegundos que dormirán los threads

        Thread[] threads = new Thread[N];

        // Crear e iniciar los threads
        for (int i = 0; i < N; i++) {
            threads[i] = new MiThread(i, T);
            threads[i].start();
        }

        // Esperar a que todos los threads terminen utilizando join
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Todos los threads han terminado.");
    }
}
