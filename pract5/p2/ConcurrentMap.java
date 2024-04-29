package pract5.p2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Monitor
public class ConcurrentMap<K, V> implements Map<K, V> {

    private Map<K, V> map;
    private Lock lock;
    private Condition okToRead;
    private Condition okToWrite;
    private int nr = 0;
    private int nw = 0;
    private int dw = 0;

    public ConcurrentMap() {
        map = new HashMap<>();
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
    
    @Override
    public int size() {
        lock.lock();
        try {
            return map.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return map.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) { // read
        request_read();
        try {
            return map.containsKey(key);
        } finally {
        	release_read();
        }
    }

    @Override
    public boolean containsValue(Object value) { // read
    	request_read();
        try {
            return map.containsValue(value);
        } finally {
            release_read();
        }
    }

    @Override
    public V get(Object key) { // read
        request_read();
        try {
            return map.get(key);
        } finally {
            release_read();
        }
    }

    @Override
    public V put(K key, V value) { // write
        request_write();
        try {
            return map.put(key, value);
        } finally {
            release_write();
        }
    }

    @Override
    public V remove(Object key) { // write
        request_write();
        try {
            return map.remove(key);
        } finally {
            release_write();
        }
    }

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}
}
