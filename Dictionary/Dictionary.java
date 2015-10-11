import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Dictionary {

	public DictionaryNode dictionary;

	/**
	 * Creates an empty dictionary.
	 */
	public Dictionary() {
		dictionary = null;
	}

	/**
	 * Creates and populates a dictionary from a file. The file should be a text
	 * file containing all of the words to add to the dictionary, with one word
	 * on each line.
	 * 
	 * @param file
	 *            - Name of the file containing the words to add to the
	 *            dictionary
	 * @throws IOException
	 */
	public Dictionary(String file) throws IOException {
		dictionary = null;
		try (BufferedReader br = Files.newBufferedReader(Paths.get(file),
				Charset.forName("UTF-8"))) {
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				dictionary = add(dictionary, line);
			}
		}
	}

	private class DictionaryNode {
		private DictionaryNode[] alphabet;
		public String prefix;
		private boolean validBit;

		public DictionaryNode(String prefix, boolean validBit) {
			this.prefix = prefix;
			this.validBit = validBit;
			alphabet = new DictionaryNode[26];
		}

		public String suffix(String common, String suffix) {
			return suffix.substring(common.length());
		}
	}

	public String commonPrefix(String prefix, String word) {
		int similar = 0;
		int stop = Math.min(prefix.length(), word.length());
		for (int i = 0; i < stop; i++) {
			if (prefix.charAt(i) == word.charAt(i)) {
				similar++;
			} else {
				break;
			}
		}
		return prefix.substring(0, similar);
	}

	public int index(String word) {
		return (int) word.charAt(0) - 97;
	}

	/**
	 * Adds a word into the dictionary.
	 * 
	 * @param word
	 *            - The word to insert
	 */
	public void add(String word) {
		dictionary = add(dictionary, word.toLowerCase());
	}

	private DictionaryNode add(DictionaryNode dictionary, String word) {
		if (dictionary == null) {
			return new DictionaryNode(word, true);
		} else if (dictionary.prefix.equals(word)) {
			if (!dictionary.validBit) {
				dictionary.validBit = true;
			}
			return dictionary;
		} else {
			String common = commonPrefix(dictionary.prefix, word);
			String suffix = dictionary.suffix(common, dictionary.prefix);
			String suffixWord = dictionary.suffix(common, word);
			if (!suffix.isEmpty()) {
				DictionaryNode newDictionary = new DictionaryNode(common, false);
				dictionary.prefix = suffix;
				newDictionary.alphabet[index(suffix)] = dictionary;
				newDictionary.alphabet[index(suffixWord)] = add(
						newDictionary.alphabet[index(suffixWord)], suffixWord);
				return newDictionary;
			} else {
				dictionary.alphabet[index(suffixWord)] = add(
						dictionary.alphabet[index(suffixWord)], suffixWord);
				return dictionary;
			}
		}
	}

	/**
	 * Checks to see if a word is in the dictionary. Check should be
	 * case-insensitive.
	 * 
	 * @param word
	 *            - The word to check
	 * @return true if the word is in the dictionary
	 */
	public boolean check(String word) {
		return check(dictionary, word.toLowerCase());
	}

	private boolean check(DictionaryNode dictionary, String word) {
		if (dictionary == null) {
			return false;
		} else if (!word.startsWith(dictionary.prefix)) {
			return false;
		} else if (dictionary.prefix.equals(word)) {
			if (!dictionary.validBit) {
				return false;
			}
			return true;
		} else {
			String suffix = dictionary.suffix(dictionary.prefix, word);
			return check(dictionary.alphabet[index(suffix)], suffix);
		}
	}

	/**
	 * Checks to see if a prefix() matches a word in the dictionary. Check
	 * should be case-insensitive.
	 * 
	 * @param prefix
	 *            () - The prefix() to check
	 * @return true if the prefix() matches at least one word in the dictionary
	 */
	public boolean checkPrefix(String prefix) {
		return checkPrefix(dictionary, prefix.toLowerCase());
	}

	private boolean checkPrefix(DictionaryNode dictionary, String prefix) {
		if (dictionary == null) {
			return false;
		} else if (!prefix.startsWith(dictionary.prefix)) {
			return false;
		} else if (prefix.startsWith(dictionary.prefix)) {
			return true;
		} else if (dictionary.prefix.equals(prefix)) {
			if (!dictionary.validBit) {
				return true;
			}
			return false;
		} else {
			String suffix = dictionary.suffix(dictionary.prefix, prefix);
			return checkPrefix(dictionary.alphabet[index(suffix)], suffix);
		}
	}

	/**
	 * Prints out the contents of the dictionary, in alphabetical order, one
	 * word per line.
	 */
	public void print() {
		print(dictionary, "");
	}

	private void print(DictionaryNode dictionary, String fullWord) {
		fullWord += dictionary.prefix;
		if (dictionary.validBit) {
			System.out.println(fullWord);
		}
		for (DictionaryNode child : dictionary.alphabet) {
			if (child != null) {
				print(child, fullWord);
			}
		}
	}

	/**
	 * Prints out the tree structure of the dictionary, in a pre-order fashion.
	 */
	public void printTree() {
		printTree(dictionary, "");
	}

	private void printTree(DictionaryNode dictionary, String indentation) {
		if (dictionary.validBit) {
			System.out.println(indentation + dictionary.prefix + "<T>");
			indentation += "  ";
		} else {
			if (!dictionary.prefix.isEmpty()) {
				System.out.println(indentation + dictionary.prefix);
				indentation += "  ";
			}
		}
		for (DictionaryNode child : dictionary.alphabet) {
			if (child != null) {
				printTree(child, indentation);
			}
		}
	}

	/**
	 * Return an array of the entries in the dictionary that are as close as
	 * possible to the parameter word. If the word passed in is in the
	 * dictionary, then return an array of length 1 that contains only that
	 * word.
	 * 
	 * @param word
	 *            - The word word to check
	 * @param numSuggestion
	 *            - The length of the array to return, disregard if dictionary
	 *            contains word
	 * @return An array of the closest entries in the dictionary to the target
	 *         word or an array containing target word
	 */
	public String[] suggest(String word, int numSuggestions) {
		return suggest(dictionary.alphabet[index(word)], word, numSuggestions);
	}

	private String[] suggest(DictionaryNode dictionary, String word,
			int numSuggestions) {
		ArrayList<String> check = tooFewLetters(word);
		ArrayList<String> missing = new ArrayList<>();
		if (!check.isEmpty()) {
			for (String words : check) {
				if (check(words)) {
					missing.add(words);
				}
			}
		}
		ArrayList<String> check2 = mistakenLetters(word);
		ArrayList<String> typo = new ArrayList<>();
		if (!check2.isEmpty()) {
			for (String words : check2) {
				if (check(words)) {
					typo.add(words);
				}
			}
		}
		ArrayList<String> check3 = tooManyLetters(word);
		ArrayList<String> extra = new ArrayList<>();
		if (!check3.isEmpty()) {
			for (String words : check3) {
				if (check(words)) {
					extra.add(words);
				}
			}
		}
		ArrayList<String> check4 = transposedLetters(word);
		ArrayList<String> transposed = new ArrayList<>();
		if (!check4.isEmpty()) {
			for (String words : check4) {
				if (check(words)) {
					transposed.add(words);
				}
			}
		}
		if (check(word)) {
			String[] exact = { word };
			return exact;
		} else if (check(transpose(word))) {
			String[] exact = { transpose(word) };
			return exact;
		} else {
			ArrayList<String> suggestions = new ArrayList<String>();
			suggestions = storeTree(dictionary, "", suggestions, word);
			String[] closest = new String[numSuggestions];
			ArrayList<String> allCases = new ArrayList<>();
			ArrayList<String> removeDuplicates = new ArrayList<>();
			if (!missing.isEmpty()) {
				allCases.addAll(missing);
			}
			if (!extra.isEmpty()) {
				allCases.addAll(extra);
			}
			if (!typo.isEmpty()) {
				allCases.addAll(typo);
			}
			if (!transposed.isEmpty()) {
				allCases.addAll(transposed);
			}
			allCases.addAll(suggestions);
			for (String words : allCases) {
				if (!removeDuplicates.contains(words)) {
					removeDuplicates.add(words);
				}
			}
			for (int i = 0; i < numSuggestions; i++) {
				closest[i] = removeDuplicates.get(i);
			}
			return closest;
		}
	}

	private ArrayList<String> storeTree(DictionaryNode dictionary,
			String fullWord, ArrayList<String> list, String original) {
		fullWord += dictionary.prefix;
		if (dictionary.validBit) {
			insertionSort(list, fullWord, original);
		}
		for (DictionaryNode child : dictionary.alphabet) {
			if (child != null) {
				storeTree(child, fullWord, list, original);
			}
		}
		return list;
	}

	private void insertionSort(ArrayList<String> list, String newWord,
			String original) {
		int i = 0;
		for (String word : list) {
			String common = commonPrefix(original, word);
			String compare = commonPrefix(original, newWord);
			if (common.length() >= compare.length()) {
				i++;
			} else {
				break;
			}
		}
		list.add(i, newWord);
	}

	public static String transpose(String word) {
		char[] reverse = new char[word.length()];
		int i = 0;
		for (int j = word.length() - 1; j >= 0; j--) {
			reverse[i] = word.charAt(j);
			i++;
		}
		String transpose = "";
		for (char ch : reverse) {
			transpose += ch;
		}
		return transpose.toString();
	}

	public static ArrayList<String> tooFewLetters(String word) {
		ArrayList<String> missedLetters = new ArrayList<>();
		for (int i = 0; i < word.length() + 1; i++) {
			for (int j = 97; j < 123; j++) {
				String add = String.valueOf((char) (j));
				if (i > 0) {
					String variation = word.substring(0, i) + add
							+ word.substring(i);
					missedLetters.add(variation);
				} else {
					String variation = add + word;
					missedLetters.add(variation);
				}
			}
		}
		return missedLetters;
	}

	public static ArrayList<String> mistakenLetters(String word) {
		ArrayList<String> replacedLetters = new ArrayList<>();
		for (int i = 0; i < word.length() + 1; i++) {
			for (int j = 97; j < 123; j++) {
				String add = String.valueOf((char) (j));
				if (i > 0) {
					String variation = word.substring(0, i - 1) + add
							+ word.substring(i);
					replacedLetters.add(variation);
				}
			}
		}
		return replacedLetters;
	}

	public static ArrayList<String> tooManyLetters(String word) {
		ArrayList<String> removedLetters = new ArrayList<>();
		for (int i = 0; i < word.length(); i++) {
			if (i > 0) {
				String variaiton = word.substring(0, i) + word.substring(i + 1);
				removedLetters.add(variaiton);
			} else {
				String variation = word.substring(1);
				removedLetters.add(variation);
			}
		}
		return removedLetters;
	}

	public static ArrayList<String> transposedLetters(String word) {
		ArrayList<String> transposeLetters = new ArrayList<>();
		if (word.length() > 2) {
			for (int i = 0; i < word.length(); i++) {
				if (i == 0) {
					String variation = String.valueOf(word.charAt(i + 1))
							+ String.valueOf(word.charAt(i))
							+ word.substring(i + 2);
					transposeLetters.add(variation);
				} else if (i == word.length() - 1) {
					String variation = word.substring(0, i - 1)
							+ String.valueOf(word.charAt(i)
									+ String.valueOf(word.charAt(i - 1)));
					transposeLetters.add(variation);
				} else {
					String variation = word.substring(0, i)
							+ String.valueOf(word.charAt(i + 1)
									+ String.valueOf(word.charAt(i))
									+ word.substring(i + 2));
					transposeLetters.add(variation);
				}
			}
		}
		return transposeLetters;
	}
}
