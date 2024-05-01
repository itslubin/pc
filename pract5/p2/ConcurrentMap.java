package pract5.p2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Monitor
public class ConcurrentMap<K, V> implements Map<K, V> {

    private Map<K, V> map;
    private LE le;

    public ConcurrentMap(LE le) {
        map = new HashMap<>();
        this.le = le;
    }
    
    @Override
    public int size() { // read
        le.request_read();
        try {
            return map.size();
        } finally {
            le.release_read();
        }
    }

    @Override
    public boolean isEmpty() { // read
    	le.request_read();
        try {
            return map.isEmpty();
        } finally {
        	le.release_read();
        }
    }

    @Override
    public boolean containsKey(Object key) { // read
        le.request_read();
        try {
            return map.containsKey(key);
        } finally {
        	le.release_read();
        }
    }

    @Override
    public boolean containsValue(Object value) { // read
    	le.request_read();
        try {
            return map.containsValue(value);
        } finally {
        	le.release_read();
        }
    }

    @Override
    public V get(Object key) { // read
    	le.request_read();
        try {
            return map.get(key);
        } finally {
        	le.release_read();
        }
    }

    @Override
    public V put(K key, V value) { // write
    	le.request_write();
        try {
            return map.put(key, value);
        } finally {
        	le.release_write();
        }
    }

    @Override
    public V remove(Object key) { // write
    	le.request_write();
        try {
            return map.remove(key);
        } finally {
        	le.release_write();
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
