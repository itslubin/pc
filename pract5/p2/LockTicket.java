package pract5.p2;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class LockTicket implements Lock{
	
	private AtomicInteger number;
	private int next;
	private volatile HashMap<Integer, Integer> turn;
	
	public LockTicket() {
		this.number = new AtomicInteger(1);
		this.next = 1;
		this.turn = new HashMap<>();
	}
	
	@Override
	public void takeLock(int id) {
		turn.put(id, number.getAndAdd(1));
		turn = turn;
		while(turn.get(id) != next);
	}

	@Override
	public void releaseLock(int id) {
		next = next + 1;
	}

}
