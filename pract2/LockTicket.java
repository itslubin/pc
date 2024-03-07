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
		// TODO Auto-generated method stub
		turn[id] = number.getAndAdd(1);
		turn = turn;
		while(turn[id] != next);
//		while(turn[id] != next) {
//			// Add a small delay to reduce memory contention as some thread might be checking the condition (access to "next") constantly without giving other threads a chance to proceed
//            Thread.yield(); // The Thread.yield() method is a way for a thread to voluntarily give up its current time slice and allow other threads to run.
//		}
	}

	@Override
	public void releaseLock(int id) {
		// TODO Auto-generated method stub
		next = next + 1;
	}

}
