package pract5.p2;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class LockTicket implements Lock{
	
	private AtomicInteger number;
	private int next;
	private volatile HashMap<Integer, Integer> turn;
	private int N = 1000;
	private int MAX = N + 1; // N + 1
	
	public LockTicket() {
		this.number = new AtomicInteger(1);
		this.next = 1;
		this.turn = new HashMap<>();
	}
	
	@Override
	public void takeLock(int id) {
		turn.put(id, number.getAndAdd(1));
		turn = turn;
		if (turn.get(id) == MAX) {
			number.getAndAdd(-MAX);
		}
		else if (turn.get(id) > N) {
			turn.replace(id, turn.get(id) - MAX);
		}
		turn = turn;
		while(turn.get(id) != next);
	}

	@Override
	public void releaseLock(int id) {
		next = (next % MAX) + 1;
	}

}
