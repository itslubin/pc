package pract3;

import java.util.concurrent.Semaphore;


public class AlmacenNP implements Almacen {
    int N = 10;
    Producto buf[];
    private volatile int front = 0;
    private volatile int rear = 0;
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutexP;
    private Semaphore mutexC;

    public AlmacenNP() {
        this.empty = new Semaphore(N);
        this.full = new Semaphore(0);
        this.mutexC = new Semaphore(1);
        this.mutexP = new Semaphore(1);
        buf = new Producto[N];
    }

    @Override
    public void almacenar(Producto producto) {
        try{
            empty.acquire();
            mutexP.acquire();

            this.buf[rear] = producto;
            rear = (rear + 1) % N;

            mutexP.release();
            full.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Producto extraer() {

        try{
            full.acquire();
            mutexC.acquire();

            Producto prod = buf[front];
            front = (front + 1) % N;

            mutexC.release();
            empty.release();

            return prod;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
