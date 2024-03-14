package pract3;

import java.util.concurrent.Semaphore;

public class AlmacenLB {
    int N = 10;
    Producto buf[];

    int nr = 0;
    int nw = 0;
    Semaphore e;
    Semaphore r;
    Semaphore w;
    int dr = 0;
    int dw = 0;

    public AlmacenLB() {
        e = new Semaphore(1);
        r = new Semaphore(0);
        w = new Semaphore(0);
        buf = new Producto[N];
    }

    public void escribir(Producto producto, int pos) {
        try{
            e.acquire();
            if(nr > 0 || nw > 0){
                dw++;
                e.release();
                w.acquire();
            }
            nw++;
            e.release();

            this.buf[pos] = producto;

            e.acquire();
            nw--;
            if(dw > 0){
                dw--;
                w.release();
            } else if(dr > 0){
                dr--;
                r.release();
            } else {
                e.release();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Producto leer(int pos) {

        try{
            e.acquire();
            if(nw > 0){
                dr++;
                e.release();
                r.acquire();
            }
            nr++;
            if(dr > 0){
                dr--;
                r.release();
            } else {
                e.release();
            }

            Producto prod = buf[pos];

            e.acquire();
            nr--;
            if(nr == 0 && dw > 0){
                dw--;
                w.release();
            } else {
                e.release();
            }

            return prod;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
