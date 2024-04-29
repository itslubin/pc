package pract2;

import java.util.concurrent.atomic.AtomicInteger;


public class LockTicket implements Lock{
	
	private AtomicInteger number;
	private int next;
	private volatile int[] turn;
	
	public LockTicket(int N) { // N es numero de hilos
		this.number = new AtomicInteger(1);
		this.next = 1;
		this.turn = new int[N + 1];
	}
	
	@Override
	public void takeLock(int id) {
		turn[id] = number.getAndAdd(1);
		turn = turn;
		while(turn[id] != next);
	}

	@Override
	public void releaseLock(int id) {
		next = next + 1;
	}

}
