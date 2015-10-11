/**
 * Implements all classes and checks to ensure that command-line arguments are
 * valid in order to run program.
 * 
 * @author // RAUL MONOTOYA
 */
public class Driver {

	/**
	 * Recursively processes all text files in a directory and builds an
	 * inverted index which stores the mapping from words to their corresponding
	 * files and stores the positions within each document.
	 * 
	 * Similarly, recursively processes all web-links in a seed-url web page and
	 * builds an inverted index which stores the mapping of words found on each
	 * web-link to its corresponding web-link and stores the positions of the
	 * words within each web-link.
	 * 
	 * @param args
	 *            command-line arguments to be passed in
	 */
	public static void main(String[] args) {
		ArgumentParser parser = new ArgumentParser(args);
		ThreadSafeInvertedIndex data = new ThreadSafeInvertedIndex();

		MultithreadedInvertedIndexBuilder builder;
		MultiThreadedQueryParser queryParser;

		if (parser.hasFlag("-t") && parser.hasValue("-t")) {
			try {
				int threads = Integer.parseInt(parser.getValue("-t"));
				if (threads < 1) {
					builder = new MultithreadedInvertedIndexBuilder();
					queryParser = new MultiThreadedQueryParser();
				} else {
					builder = new MultithreadedInvertedIndexBuilder(threads);
					queryParser = new MultiThreadedQueryParser(threads);
				}
			} catch (Exception ex) {
				builder = new MultithreadedInvertedIndexBuilder();
				queryParser = new MultiThreadedQueryParser();
			}
		} else {
			builder = new MultithreadedInvertedIndexBuilder();
			queryParser = new MultiThreadedQueryParser();
		}

		if (parser.hasFlag("-d")) {
			builder.parseAllFiles(parser.getValue("-d"), data);
			if (parser.hasFlag("-i") || !parser.hasFlag("-q")) {
				if (parser.hasValue("-i")) {
					data.printOut(parser.getValue("-i"));
				} else {
					data.printOut("invertedindex.txt");
				}
			}
			if (parser.hasFlag("-q")) {
				if (parser.hasFlag("-r")) {
					if (!parser.hasValue("-r")) {
						queryParser.parsesQuery(parser.getValue("-q"), data);
						queryParser.printResults("searchresults.txt");
					} else {
						queryParser.parsesQuery(parser.getValue("-q"), data);
						queryParser.printResults(parser.getValue("-r"));
					}
				}
			}
		}

		if (parser.hasFlag("-u") && parser.hasValue("-u")) {
			builder.parseAllLinks(parser.getValue("-u"), data);
			if (parser.hasFlag("-i") || !parser.hasFlag("-q")) {
				if (parser.hasValue("-i")) {
					data.printOut(parser.getValue("-i"));
				} else {
					data.printOut("invertedindex.txt");
				}
			}
			if (parser.hasFlag("-q")) {
				if (parser.hasFlag("-r")) {
					if (!parser.hasValue("-r")) {
						queryParser.parsesQuery(parser.getValue("-q"), data);
						queryParser.printResults("searchresults.txt");
					} else {
						queryParser.parsesQuery(parser.getValue("-q"), data);
						queryParser.printResults(parser.getValue("-r"));
					}
				}
			}
		}

		builder.shutdown();
		queryParser.shutdown();
	}
}