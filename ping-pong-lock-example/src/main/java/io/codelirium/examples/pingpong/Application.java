package io.codelirium.examples.pingpong;

import io.codelirium.examples.pingpong.lockfree.LockFreePingPong;
import io.codelirium.examples.pingpong.locking.LockingPingPong;

public class Application {

	public static final int TOTAL_LOOPS = 10 * 10 * 1000;

	public static void main (String[] args) {
		new LockingPingPong(TOTAL_LOOPS).run();
		System.out.println();
		new LockFreePingPong(TOTAL_LOOPS).run();
	}
}
