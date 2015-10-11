import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Constructs a nested data structure (more specifically, a treeMap<String,
 * treeMap<String, ArrayList<Integer>>>), capable of storing words, with
 * multiple file-names associated with each word, and multiple Integers
 * associated with each file-name.
 * 
 * @author // RAUL MONTOYA
 */
public final class ThreadSafeInvertedIndex {
	private final TreeMap<String, TreeMap<String, ArrayList<Integer>>> dataStructure;

	private final MultiReaderLock lock;

	public ThreadSafeInvertedIndex() {
		dataStructure = new TreeMap<>();
		lock = new MultiReaderLock();
	}

	/**
	 * Checks inverted-index to see whether word and file-name are or are not
	 * contained. Then, either word or filename can be added or updated
	 * accordingly.
	 * 
	 * @param word
	 *            each word
	 * @param fileName
	 *            file in which, word was found
	 * @param position
	 *            index number in the text-file, in which word is located
	 */
	public void addWords(String word, String fileName, int position) {
		lock.lockWrite(); // LOCK
		/*
		 * Condition: if inverted-index does not contain word, then word is
		 * added as a "key" in TreeMap and a new (nested) TreeMap is declared as
		 * a "value"...
		 */
		if (!dataStructure.containsKey(word)) {
			dataStructure.put(word, new TreeMap<String, ArrayList<Integer>>());
		}

		/*
		 * Condition: if inverted-index contains word but does NOT contain
		 * file-name, then new TreeMap is initialized with fileName as its key
		 * and ArrayList is updated to include position
		 */
		if (!dataStructure.get(word).containsKey(fileName)) {
			TreeMap<String, ArrayList<Integer>> nestedData = dataStructure
					.get(word);
			nestedData.put(fileName, new ArrayList<Integer>());
			ArrayList<Integer> positions = new ArrayList<Integer>();
			positions.add(position);
		}

		/*
		 * Condition: if inverted-index contains word and also contains
		 * file-name, then add the "position" of word into ArrayList<Integer>.
		 */
		dataStructure.get(word).get(fileName).add(position);
		lock.unlockWrite(); // UNLOCK
	}

	/**
	 * Checks inverted-index to see whether word and file-name are or are not
	 * contained. Then, either word or filename can be added or updated
	 * accordingly.
	 * 
	 * @param word
	 *            each word
	 * @param fileName
	 *            file in which, word was found
	 * @param position
	 *            index number in the text-file, in which word is located
	 */
	public void addAll(ThreadSafeInvertedIndex other) {
		lock.lockWrite(); // LOCK
		for (String word : other.dataStructure.keySet()) {
			if (!this.dataStructure.containsKey(word)) {
				this.dataStructure.put(word, other.dataStructure.get(word));
			} else {
				for (String file : other.dataStructure.get(word).keySet()) {
					if (!dataStructure.get(word).containsKey(file)) {
						ArrayList<Integer> positions = new ArrayList<>();
						positions.addAll(other.dataStructure.get(word)
								.get(file));
						dataStructure.get(word).put(file, positions);
					} else {
						dataStructure
								.get(word)
								.get(file)
								.addAll(other.dataStructure.get(word).get(file));
					}
				}
			}
		}
		lock.unlockWrite(); // UNLOCK
	}

	/**
	 * Outputs the inverted index to a text-file. Therefore, the output should
	 * be a file that contains the contents of your inverted index in sorted
	 * order...
	 * 
	 * @param file
	 *            blank, new text-file in which, to write inverted index content
	 */
	public void printOut(String file) {
		/*
		 * printOut: prints inverted-index to console and also writes
		 * inverted-index to file, using BuffererWriter().
		 */
		Path input = new File(file).toPath(); // java 7
		lock.lockRead(); // LOCK
		try (BufferedWriter writer = Files.newBufferedWriter(input,
				StandardCharsets.UTF_8)) {
			for (String key : dataStructure.keySet()) {
				writer.write(key);
				writer.newLine();
				for (String path : dataStructure.get(key).keySet()) {
					writer.write('"' + path + '"');
					for (Integer num : dataStructure.get(key).get(path)) {
						writer.write(", " + num);
					}
					writer.newLine();
				}
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println("There was an Input/Output error.");
		}
		lock.unlockRead(); // UNLOCK
	}

	/**
	 * For each line, compares it to each words in tail map, if they match, then
	 * EITHER SearchResult object is created AND stored, OR SearchResult object
	 * is accessed AND updated. SearchResult objects are then sorted by
	 * relevance and mapped to corresponding line.
	 * 
	 * @param ArrayList
	 *            queries, each line (from query-file)
	 * @param InvertedIndex
	 *            data, inverted-index of words, each word will be compared to
	 *            each line in queryWords
	 * 
	 */
	public ArrayList<SearchResult> partialSearch(ArrayList<String> queries) {
		HashMap<String, SearchResult> resultMap = new HashMap<>();
		lock.lockRead(); // LOCK
		for (String query : queries) {
			SortedMap<String, TreeMap<String, ArrayList<Integer>>> efficient = dataStructure
					.tailMap(query);
			for (String word : efficient.keySet()) {
				if (word.startsWith(query)) {
					for (String path : efficient.get(word).keySet()) {
						int frequency = efficient.get(word).get(path).size();
						int position = efficient.get(word).get(path).get(0);
						if (!resultMap.containsKey(path)) {
							SearchResult attributes = new SearchResult(path,
									frequency, position);
							resultMap.put(path, attributes);
						} else {
							resultMap.get(path).updateFrequency(frequency);
							resultMap.get(path).updatePosition(position);
						}
					}
				} else {
					break; // exit this for-loop
				}
			}
		}
		lock.unlockRead(); // UNLOCK
		ArrayList<SearchResult> resultList = new ArrayList<>();
		resultList.addAll(resultMap.values());
		Collections.sort(resultList);
		return resultList;
	}
}