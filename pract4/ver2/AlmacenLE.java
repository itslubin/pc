package pract4.ver2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlmacenLE {
    int N = 10;
    Producto buf[];
    int nr = 0;
    int nw = 0;
    int dw = 0;
    private Lock lock;
    private Condition oktoread;
    private Condition oktowrite;

    public AlmacenLE() {
        lock = new ReentrantLock();
        oktoread = lock.newCondition();
        oktowrite = lock.newCondition();
        buf = new Producto[N];
    }

    public void request_read() {
        while (nw > 0) { // Solucion 2: añadir || dr > 0
            try {
                oktoread.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nr++;
    }

    public void release_read() {
        nr--;
        if (nr==0) oktowrite.signal();
        
    }

    public void request_write() {
        while (nr > 0 || nw > 0) {
            try {
            	dw++;
                oktowrite.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nw++;
    }

    public void release_write() {
        nw--;
        if (dw > 0) {
        	dw--;
        	oktowrite.signal();
        }
        else {
        	oktoread.signalAll();
        }
        // Para solucion justa 2: cambiar orden de condiciones
    }

    public void escribir(Producto producto, int pos) {
        this.buf[pos] = producto;
    }

    public Producto leer(int pos) {
        Producto prod = buf[pos];

        return prod;
    }
}
