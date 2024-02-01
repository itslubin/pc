package pract1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MatrizMultiplicacion implements Runnable {
    private final int[][] matrizA; // final evita la declaracion de la variable con otro valor, pero se puede cambiar sus valores
    private final int[][] matrizB;
    private final int[][] resultado;
    private final int fila;

    public MatrizMultiplicacion(int[][] matrizA, int[][] matrizB, int[][] resultado, int fila) {
        this.matrizA = matrizA;
        this.matrizB = matrizB;
        this.resultado = resultado;
        this.fila = fila;
    }

    @Override
    public void run() {
        int n = matrizA.length;

        for (int i = 0; i < n; i++) {
            resultado[fila][i] = 0;
            for (int j = 0; j < n; j++) {
                resultado[fila][i] += matrizA[fila][j] * matrizB[j][i];
            }
        }
    }
    
    public static void main(String[] args) {
        int N = 3; // Tamaño de las matrices y número de threads
        int[][] matrizA = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        int[][] matrizB = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        int[][] resultado = new int[N][N];

        // Inicialización de matrices (puedes hacerlo como desees)
        
        // Creación de un pool de threads
        ExecutorService executor = Executors.newFixedThreadPool(N);

        // Crear threads para cada fila de la matriz resultado
        for (int i = 0; i < N; i++) {
            Runnable worker = new MatrizMultiplicacion(matrizA, matrizB, resultado, i);
            executor.execute(worker);
        }

        // Apagar el pool de threads después de completar todas las tareas
        executor.shutdown();

        try {
            // Esperar a que todos los threads terminen
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            // Imprimir la matriz resultado
            System.out.println("Matriz Resultado:");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(resultado[i][j] + " ");
                }
                System.out.println();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
