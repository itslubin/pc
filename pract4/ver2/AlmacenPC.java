package pract4.ver2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlmacenPC implements Almacen {
    int N = 10;
    private Queue<Producto> buf;
    
    private Lock lock;
    private Condition con;
    private Condition pro;

    public AlmacenPC() {
        lock = new ReentrantLock();
        con = lock.newCondition();
        pro = lock.newCondition();
        buf = new LinkedList<>();
    }

    @Override
    public void almacenar(Producto producto) {
        lock.lock();
        try{
            while (buf.size() == N) {
                pro.await(); // wait until there is space in the buffer
            }
            buf.add(producto);
            con.signal(); // notify that there is a new product in the buffer
        } catch (Exception e){
            e.printStackTrace();
        }
        lock.unlock();
    }

    @Override
    public Producto extraer() {
        Producto prod = null;
        lock.lock();
        try{
            while (buf.isEmpty()) {
                con.await(); // wait until there is a product in the buffer
            }
            prod = buf.remove();
            pro.signalAll(); // notify that there is space in the buffer
        } catch (Exception e){
            e.printStackTrace();
        }
        lock.unlock();
        return prod;
    }
}
