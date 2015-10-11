import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * QueryParser reads in a given text-file and generates a list of single-word
 * and multiple-word queries. This list will later be compared with
 * inverted-index from project1.
 * 
 * @author // RAUL MONTOYA
 */
public class MultiThreadedQueryParser {

	private final LinkedHashMap<String, ArrayList<SearchResult>> searchResults;

	private final WorkQueue pool;
	private final MultiReaderLock lock;
	private int wait;

	public MultiThreadedQueryParser() {
		searchResults = new LinkedHashMap<>();
		pool = new WorkQueue();
		lock = new MultiReaderLock();
		wait = 0;
	}

	public MultiThreadedQueryParser(int threads) {
		searchResults = new LinkedHashMap<>();
		pool = new WorkQueue(threads);
		lock = new MultiReaderLock();
		wait = 0;
	}

	/**
	 * Parses each file into lines. Formats those lines into words. Then, adds
	 * those lines to a list (queries).
	 * 
	 * @param String
	 *            queryFile, file that is to be read and parsed
	 */
	public void parsesQuery(String queryFile, ThreadSafeInvertedIndex data) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(queryFile),
				Charset.forName("UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				lock.lockWrite(); // LOCK
				searchResults.put(line, null);
				lock.unlockWrite(); // UNLOCK
				pool.execute(new Query(line, data));
			}
		} catch (NoSuchFileException e) {
			System.out.println("No such file");
		} catch (IOException e1) {
			System.out
					.println("There was an error parsing lines from the queryFile ---.");
		}
		finish();// Make sure all work is done before writing to any files.
	}

	/**
	 * Helper method, that helps a thread wait until all of the current work is
	 * done. This is useful for resetting the counters or shutting down the work
	 * queue.
	 */
	public synchronized void finish() {
		try {
			while (wait > 0) {
				this.wait();
			}
		} catch (InterruptedException e) {
			System.out.println("There is still some undone work.");
		}
	}

	/**
	 * Will shutdown the work queue after all the current pending work is
	 * finished. Necessary to prevent our code from running forever in the
	 * background.
	 */
	public void shutdown() {
		finish();
		pool.shutdown();
	}

	/**
	 * Handles per-directory parsing. If a subdirectory is encountered, a new
	 * {@link Query} is created to handle that subdirectory.
	 */
	private class Query implements Runnable {
		private final String line;
		private final ThreadSafeInvertedIndex data;

		public Query(String line, ThreadSafeInvertedIndex data) {
			this.line = line;
			this.data = data;
			incrementPending();
		}

		@Override
		public void run() {
			ArrayList<String> queries = new ArrayList<>();
			ArrayList<SearchResult> process = new ArrayList<>();
			for (String word : line.split("\\s")) {
				queries.add(word);
			}
			process = data.partialSearch(queries);
			lock.lockWrite(); // LOCK
			searchResults.put(line, process); // store what is processed
			lock.unlockWrite(); // UNLOCK
			decrementPending();
		}
	}

	/**
	 * Takes in as a parameter, LinkedHashMap and prints its contents to a
	 * specified text file.
	 * 
	 * @param LinkedHashMap
	 *            results, containing partial search results
	 * @param String
	 *            file, text file in which to write
	 * 
	 */
	public void printResults(String file) {
		lock.lockRead(); // LOCK
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file),
				StandardCharsets.UTF_8)) {
			for (String line : searchResults.keySet()) {
				writer.write(line);
				writer.newLine();
				if (searchResults.get(line) != null) {
					for (SearchResult attribute : searchResults.get(line)) {
						writer.write(attribute.format());
						writer.newLine();
					}
					writer.newLine();
				} else {
					writer.newLine();
				}
			}
		} catch (IOException e) {
			System.out.println("There was an issue reading the file.");
		}
		lock.unlockRead(); // UNLOCK
	}

	/**
	 * Indicates that we now have additional "pending" work to wait for. We need
	 * this since we can no longer call join() on the threads. (The threads keep
	 * running forever in the background.)
	 * 
	 * We made this a synchronized method in the outer class, since locking on
	 * the "this" object within an inner class does not work.
	 */
	private synchronized void incrementPending() {
		wait++;
	}

	/**
	 * Indicates that we now have one less "pending" work, and will notify any
	 * waiting threads if we no longer have any more pending work left.
	 */
	private synchronized void decrementPending() {
		wait--;

		if (wait <= 0) {
			this.notifyAll();
		}
	}
}