package pract4.ver1;

import java.util.LinkedList;
import java.util.Queue;

public class AlmacenPC implements Almacen {
    int N = 10;
    private Queue<Producto> buf;

    public AlmacenPC() {
        buf = new LinkedList<>();
    }

    @Override
    public synchronized void almacenar(Producto producto) {
        try{
            while (buf.size() == N) {
                wait(); // wait until there is space in the buffer
            }
            buf.add(producto);
            notify(); // notify that there is a new product in the buffer
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Producto extraer() {
        Producto prod = null;

        try{
            while (buf.isEmpty()) {
                wait(); // wait until there is a product in the buffer
            }
            prod = buf.remove();
            notify(); // notify that there is space in the buffer
        } catch (Exception e){
            e.printStackTrace();
        }

        return prod;
    }
}
