package pract2;

import java.util.Arrays;

public class LockBakery implements Lock{
	
	private int N;
	private volatile int[] turn;
	
	public LockBakery(int N) {
		this.N = N;
		this.turn = new int[N + 1];
	}
	
	private boolean comp(int a, int b, int c, int d) {
		return (a > c) || (a==c && b > d);
	}
	
	@Override
	public void takeLock(int id) {
		// TODO Auto-generated method stub
		turn[id] = 1;
		turn[id] = Arrays.stream(turn).max().orElseThrow() + 1;
		for (int k = 1; k <= N; ++k) {
			if (k != id) {
				while (turn[k] != 0 && comp(turn[id], id, turn[k], k)) {
					Thread.yield();
				};
			}
		}
	}

	@Override
	public void releaseLock(int id) {
		// TODO Auto-generated method stub
		turn[id] = 0;
	}

}
