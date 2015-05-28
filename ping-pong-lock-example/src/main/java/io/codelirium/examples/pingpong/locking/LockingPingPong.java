package io.codelirium.examples.pingpong.locking;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class LockingPingPong implements Runnable {

	private static final String PREFIX = "[Locking Ping-Pong] ";

	private int pingCounter;
	private int pongCounter;

	private Lock pingLock;
	private Lock pongLock;

	private Condition pingCondition;
	private Condition pongCondition;

	private int totalLoops;

	private CountDownLatch startLatch;


	public LockingPingPong(final int totalLoops) {
		this.pingLock = new ReentrantLock();
		this.pongLock = new ReentrantLock();

		this.pingCondition = pingLock.newCondition();
		this.pongCondition = pongLock.newCondition();

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
			try {
				pingLock.lock();
				pingCounter = i;
				pingCondition.signalAll();
			} finally {
				pingLock.unlock();
			}

			try {
				pongLock.lock();
				while (pongCounter != i) {
					pongCondition.awaitUninterruptibly();
				}
			} finally {
				pongLock.unlock();
			}
		});
	}

	public void pongWork() {

		try {
			startLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		IntStream.range(1, totalLoops).forEach((int i) -> {
			try {
				pingLock.lock();
				while (pingCounter != i) {
					pingCondition.awaitUninterruptibly();
				}
			} finally {
				pingLock.unlock();
			}

			try {
				pongLock.lock();
				pongCounter = i;
				pongCondition.signalAll();
			} finally {
				pongLock.unlock();
			}
		});
	}
}
