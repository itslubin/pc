package pract3;

import java.util.concurrent.Semaphore;


public class AlmacenB1 implements Almacen {
    Producto buf;
    private Semaphore empty;
    private Semaphore full;

    private Boolean im_empty;

    public AlmacenB1() {
        this.empty = new Semaphore(1);
        this.full = new Semaphore(0);
        this.im_empty = true;
    }

    @Override
    public void almacenar(Producto producto) {
        try{
            empty.acquire();

            if(!im_empty) System.out.println("Error almacenar");
            this.buf = producto;

            full.release();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public Producto extraer() {

        try{
            full.acquire();

            Producto prod = buf;

            empty.release();

            return prod;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
