package io.codelirium;

import io.codelirium.examples.pingpong.Application;
import io.codelirium.examples.pingpong.lockfree.LockFreePingPong;
import io.codelirium.examples.pingpong.locking.LockingPingPong;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LockingTest extends TestCase {

	public LockingTest(final String testName) {
		super(testName);
	}


	public static Test suite() {
		return new TestSuite(LockingTest.class);
	}


	public void testLockingPingPong() {
		new LockingPingPong(Application.TOTAL_LOOPS).run();
	}

	public void testLockFreePingPong() {
		new LockFreePingPong(Application.TOTAL_LOOPS).run();
	}
}
