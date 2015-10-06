public class MyListElem implements ListElem {

	public MyListElem[] next;
	public MyListElem[] previous;
	public Comparable[] keys;
	private Object data;

	public MyListElem(Comparable[] keys, Object data) {
		this.keys = new Comparable[keys.length];
		for (int i = 0; i < keys.length; i++) {
			this.keys[i] = keys[i];
		}
		this.data = data;
		next = new MyListElem[numKeys()];
		previous = new MyListElem[numKeys()];
	}

	/* Returns total number of keys, link contains. */
	public int numKeys() {
		return keys.length;
	}

	/* Returns the specified key, link contains. */
	public Comparable key(int index) {
		return keys[index];
	}

	/* Returns data associated with link. */
	public Object data() {
		return data;
	}

	/* Compares specified key with that of another link. */
	public int compareTo(Comparable object, int index) {
		return key(index).compareTo(object);
	}

	/* Compares all keys with that of another link. */
	public boolean compareKeys(Comparable[] compare) {
		int counter = 0;
		for(int i = 0; i < compare.length; i++) {
			if(keys[i].equals(compare[i])) {
				counter++;
			}
		}
		if(counter == compare.length) {
			return true;
		}
		return false;
	}
}
