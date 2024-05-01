package pract5.p2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LEMonitor implements LE{
	private Lock lock;
    private Condition okToRead;
    private Condition okToWrite;
    private int nr = 0;
    private int nw = 0;
    private int dw = 0;
    
    public LEMonitor() {
    	lock = new ReentrantLock();
        okToRead = lock.newCondition();
        okToWrite = lock.newCondition();
    }
    
    public void request_read() {
        lock.lock();
        while (nw > 0) {
            try {
                okToRead.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nr++;
        lock.unlock();
    }

    public void release_read() {
        lock.lock();
        nr--;
        if (nr == 0)
            okToWrite.signal();
        lock.unlock();
    }

    public void request_write() {
        lock.lock();
        while (nr > 0 || nw > 0) {
            try {
                dw++;
                okToWrite.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nw++;
        lock.unlock();
    }

    public void release_write() {
        lock.lock();
        nw--;
        if (dw > 0) {
            dw--;
            okToWrite.signal();
        } else {
            okToRead.signal();
        }
        lock.unlock();
    }
}
