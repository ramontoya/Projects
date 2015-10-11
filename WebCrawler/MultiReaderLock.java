/**
 * A simple custom lock that allows simultaneously read operations, but
 * disallows simultaneously write and read/write operations.
 * 
 * You do not need to implement any form or priority to read or write
 * operations. The first thread that acquires the appropriate lock should be
 * allowed to continue.
 * 
 * @author CS 212 Software Development
 * @author University of San Francisco
 */
public class MultiReaderLock {
	/**
	 * Initializes a multi-reader (single-writer) lock.
	 */
	private int reader;
	private int writer;

	public MultiReaderLock() {
		reader = 0;
		writer = 0;
	}

	/**
	 * Will wait until there are no active writers in the system, and then will
	 * increase the number of active readers.
	 */
	public synchronized void lockRead() {
		while (writer > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("There is one or more writers.");
			}
		}
		reader++;
	}

	/**
	 * Will decrease the number of active readers, and notify any waiting
	 * threads if necessary.
	 */
	public synchronized void unlockRead() {
		reader--;
		if (reader <= 0) {
			notifyAll();
		}
	}

	/**
	 * Will wait until there are no active readers or writers in the system, and
	 * then will increase the number of active writers.
	 */
	public synchronized void lockWrite() {
		while (writer > 0 || reader > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("There is one or more readers or writers.");
			}
		}
		writer++;
	}

	/**
	 * Will decrease the number of active writers, and notify any waiting
	 * threads if necessary.
	 */
	public synchronized void unlockWrite() {
		writer--;
		notifyAll();
	}
}