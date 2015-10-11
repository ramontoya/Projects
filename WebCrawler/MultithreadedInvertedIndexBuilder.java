import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * MultithreadedInvertedIndexBuilder traverses directories for files, parses
 * each file into words and then stores each word into the inverted index data
 * structure.
 * 
 * @author // RAUL MONTOYA
 */
public class MultithreadedInvertedIndexBuilder {

	private final WorkQueue pool;
	private int wait;
	private final HashSet<String> master;
	private final MultiReaderLock lock;

	public MultithreadedInvertedIndexBuilder() {
		pool = new WorkQueue();
		wait = 0;
		master = new HashSet<String>();
		lock = new MultiReaderLock();
	}

	public MultithreadedInvertedIndexBuilder(int threads) {
		pool = new WorkQueue(threads);
		wait = 0;
		master = new HashSet<String>();
		lock = new MultiReaderLock();
	}

	/**
	 * Parses each file into words and then stores each word into the inverted
	 * index data structure.
	 * 
	 * @param file
	 *            to be parsed
	 * @param dataStructure
	 *            to store each word from parsed-file
	 */
	public void parseFile(String file, ThreadSafeInvertedIndex dataStructure) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(file),
				Charset.forName("UTF-8"))) {
			/*
			 * While: file contains lines, separates each lines into words
			 * excluding: spaces, punctuation, capitalization, and special
			 * characters. For each individual word, adds it to inverted-index.
			 */
			int counter = 0;
			String line; // represents each line in file
			ThreadSafeInvertedIndex local = new ThreadSafeInvertedIndex();

			while ((line = br.readLine()) != null) {
				line = line.toLowerCase().trim();
				String[] words = line.split("\\s");
				for (String word : words) { // for word in line
					word = word.replaceAll("\\W", "");
					word = word.replaceAll("_", "");
					if (word.length() > 0) {
						counter++;
						local.addWords(word, file, counter);
					}
				}
			}
			dataStructure.addAll(local);
		} catch (FileNotFoundException e) {
			System.out.println("Error: File " + file + " was not found.");
		} catch (Exception e) {
			System.out.println("Error: File " + file + " is null.");
		}
	}

	/**
	 * Traverses directories and sub-directories for files.
	 * 
	 * @param file
	 *            either a directory or file (contained in a directory)
	 * @param data
	 *            inverted index data structure that will store information
	 */
	public void parseAllFiles(String directory, ThreadSafeInvertedIndex data) {
		// Is this a directory?
		if (Files.isDirectory(Paths.get(directory))) {
			// If so, create a new thread that will traverse it.
			pool.execute(new SubDirectory(directory, data));
		} else {
			// In the case that it's a file instead,
			if (Paths.get(directory).getFileName().toString().toLowerCase()
					.endsWith(".txt")) {
				parseFile(directory.toString(), data);
			}
		}
		finish(); // Make sure all work is done before writing to any files.
	}

	private class SubDirectory implements Runnable {
		private final String directory;
		private final ThreadSafeInvertedIndex data;

		public SubDirectory(String directory, ThreadSafeInvertedIndex data) {
			this.directory = directory;
			this.data = data;
			incrementPending();
		}

		@Override
		public void run() {
			try {
				for (Path path : Files.newDirectoryStream(Paths.get(directory))) {
					// Is this a directory?
					if (Files.isDirectory(path)) {
						// If so, create a new thread that will traverse it.
						pool.execute(new SubDirectory(path.toString(), data));
					} else {
						// In the case that it's a file instead,
						if (path.getFileName().toString().toLowerCase()
								.endsWith(".txt")) {
							parseFile(path.toString(), data);
						}
					}
				}
			} catch (IOException e) {
				System.out.println("There was an issue reading the directory.");
			}
			decrementPending();
		}
	}

	/**
	 * Parses each url into words and then stores each word into the inverted
	 * index data structure.
	 * 
	 * @param url
	 *            is parsed
	 * @param dataStructure
	 *            stores each word from parsed url
	 */
	public void parseLink(String url, ThreadSafeInvertedIndex dataStructure) {
		int counter = 0;
		ThreadSafeInvertedIndex local = new ThreadSafeInvertedIndex();

		ArrayList<String> words = HTMLCleaner.fetchWords(url);

		for (String word : words) { // for word in words-list
			word = word.replaceAll("\\W", "");
			word = word.replaceAll("_", "");
			if (word.length() > 0) {
				counter++;
				local.addWords(word, url, counter);
			}
		}
		dataStructure.addAll(local);
	}

	/**
	 * Adds seed-url to master list. Then starts up a recursive thread which
	 * will begin processing the seed-url; downloading, parsing, storing, etc.
	 * 
	 * @param url
	 *            starts up thread
	 * @param data
	 *            stores each word from parsed-file
	 */
	public void parseAllLinks(String seedUrl, ThreadSafeInvertedIndex data) {
		lock.lockWrite();
		master.add(seedUrl);
		lock.unlockWrite();

		// create a worker for seed-url
		pool.execute(new Parent(seedUrl, data));

		finish(); // Make sure all work is done before writing to any files.
	}

	/**
	 * Takes in a seed-url, downloads web page and collects all the url links
	 * within that web page. Continually does this until 50 links have been
	 * collected. After 50 have been collected, parses each link into words that
	 * are found on that web page and stores each word in inverted index.
	 * 
	 * @param url
	 *            to download and parse
	 * @param data
	 *            to store each word from parsed-file
	 */
	private void updateMaster(String url, ThreadSafeInvertedIndex data) {
		String html = HTMLCleaner.fetchHTML(url);
		ArrayList<String> children = new ArrayList<>();
		children.addAll(HTMLLinkParser.listLinks(html));
		lock.lockWrite(); // LOCK
		// for each child
		for (String child : children) {
			if (master.size() < 50) {
				// make sure child is an absolute link before adding to master
				child = makeAbsolute(url, child);
				// ignore fragment portion of url to ensure uniqueness
				if (!master.contains(child.replaceAll("#.*", ""))) {
					master.add(child);
					// start up thread to process child-url
					pool.execute(new Parent(child, data));
				}
			} else {
				break; // exit loop after collecting 50 links
			}
		}
		lock.unlockWrite(); // UNLOCK
		parseLink(url, data);
	}

	/**
	 * Takes in a relative link, implements URL class to make it absolute.
	 * 
	 * @param relative
	 *            link that will be made absolute
	 */
	private String makeAbsolute(String url, String link) {
		try {
			URL base = new URL(url);
			URL absolute = new URL(base, link);
			return absolute.toString();
		} catch (MalformedURLException e) {
			System.out.println("Unable to make an absolute link for " + link);
		}
		return link;
	}

	private class Parent implements Runnable {
		private final String url;
		private final ThreadSafeInvertedIndex data;

		public Parent(String url, ThreadSafeInvertedIndex data) {
			this.url = url;
			this.data = data;
			incrementPending();
		}

		@Override
		public void run() {
			updateMaster(url, data);
			decrementPending();
		}
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
}
