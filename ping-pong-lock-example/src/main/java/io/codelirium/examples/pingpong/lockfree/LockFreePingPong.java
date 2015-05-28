package io.codelirium.examples.pingpong.lockfree;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class LockFreePingPong implements Runnable {

	private static final String PREFIX = "[Lock-free Ping-Pong] ";

	private volatile int pingCounter;
	private volatile int pongCounter;

	private int totalLoops;

	private CountDownLatch startLatch;


	public LockFreePingPong(final int totalLoops) {
		this.totalLoops = totalLoops;
		this.startLatch = new CountDownLatch(1);
	}


	public void run() {

		Thread pingThread = new Thread(this::pingWork);
		Thread pongThread = new Thread(this::pongWork);

		pingThread.start();
		pongThread.start();

		final long startTime = System.nanoTime();
		startLatch.countDown();

		try {
			pingThread.join();
			pongThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		System.out.println(PREFIX + "Elapsed: " + (System.nanoTime() - startTime) / totalLoops + " ns per loop on average");
		System.out.println(PREFIX + "Counter 1: " + pingCounter);
		System.out.println(PREFIX + "Counter 2: " + pongCounter);
	}

	public void pingWork() {

		try {
			startLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		IntStream.range(1, totalLoops).forEach((int i) -> {
			pingCounter = i;

			while (pongCounter != i) {
				Thread.yield();
			}
		});
	}

	public void pongWork () {

		try {
			startLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		IntStream.range(1, totalLoops).forEach((int i) -> {
			while (pingCounter != i) {
				Thread.yield();
			}

			pongCounter = i;
		});
	}
}
