/**
 * SearchResult is an object class, which creates a SearchResult object. Each
 * object has three attributes associated with it: String path, integer
 * frequency, and integer position.
 * 
 * @author // RAUL MONTOYA
 */
public class SearchResult implements Comparable<SearchResult> {

	private final String path; // attribute
	private int position; // attribute
	private int frequency; // attribute

	SearchResult(String path, int frequency, int position) { // Constructor
		this.path = path;
		this.position = position;
		this.frequency = frequency;
	}

	/**
	 * Algorithm for comparing and sorting multiple SearchResult objects.
	 * 
	 * * @param SearchResult other object to compare with.
	 */
	public String getPath() { // getter method for path
		return this.path;
	}

	/**
	 * Returns the frequency of a specific word within a specific file.
	 */
	public int getFrequency() { // getter method for frequency
		return this.frequency;
	}

	/**
	 * Returns the first appearance of a specific word within a specific file.
	 */
	public int getPosition() { // getter method for position
		return this.position;
	}

	/**
	 * Updates frequency of a word by adding a new integer to it.
	 * 
	 * * @param frequency, other integer to add to frequency
	 */
	public void updateFrequency(Integer frequency) { // setter method for
														// frequency
		this.frequency += frequency;
	}

	/**
	 * Updates the first appearance of a word if another word in the same
	 * multiple-word query has a smaller integer as it's first appearance.
	 * 
	 * * @param position, other value to to compare to/replace position
	 */
	public void updatePosition(Integer position) { // setter method for position
		if (this.position > position) {
			this.position = position;
		}
	}

	/**
	 * Algorithm for comparing and sorting multiple SearchResult objects.
	 * 
	 * * @param SearchResult other object to compare with.
	 */
	@Override
	public int compareTo(SearchResult o) { // algorithm for sorting multiple
											// objects
		if (this.getFrequency() != o.getFrequency()) {
			return Integer.compare(o.getFrequency(), getFrequency());
		} else {
			if (this.getPosition() != o.getPosition()) {
				return Integer.compare(getPosition(), o.getPosition());
			} else {
				if (this.getPath() != o.getPath()) {
					return getPath().compareTo(o.getPath());
				}
			}
		}
		return 0;
	}

	/**
	 * Will return a formatted version of each object's attributes, ready for
	 * printing to a file.
	 * 
	 */
	public String format() {
		String format = '"' + this.getPath() + '"' + ", " + this.getFrequency()
				+ ", " + this.getPosition();
		return format;
	}
}