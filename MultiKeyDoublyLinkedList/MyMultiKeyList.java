import java.util.NoSuchElementException;

public class MyMultiKeyList implements MultiKeyList {

	private MyListElem[] head;
	private MyListElem[] tail;
	private int listSize;
	private int linkSize;

	MyMultiKeyList(int numKeys) {
		linkSize = numKeys;
		head = new MyListElem[linkSize];
		tail = new MyListElem[linkSize];
		listSize = 0;
	}

	/* Returns total number of links contained in list. */
	public int size() {
		return listSize;
	}

	/*
	 * Takes in a link and object, if link-size is compatible, calls a private
	 * add-helper-method to add link while maintaining program invisibility.
	 */
	public void add(Comparable[] keys, Object data) {
		if (keys.length == linkSize) {
			addHelper(new MyListElem(keys, data));
		} else if (keys.length < linkSize) {
			throw new IllegalArgumentException(data
					+ " does not contain enough keys!");
		} else if (keys.length > linkSize) {
			throw new IllegalArgumentException(data
					+ " contains too many keys!");
		}
	}

	private void addHelper(MyListElem newLink) {
		/* If list is empty, adds link, points head and tail to that link. */
		if (listSize == 0) {
			for (int i = 0; i < linkSize; i++) {
				head[i] = tail[i] = newLink;
			}
			/* Else, inserts link, key-by-key in sorted order. */
		} else {
			for (int i = 0; i < linkSize; i++) {
				MyListElem current = head[i];
				boolean added = false;
				while (current != null) {
					/* Compares ith key in newLink to ith key in current. */
					if (current.compareTo(newLink.key(i), i) >= 0) {
						/* Appends link to head of list. */
						if (current.previous[i] == null) {
							newLink.next[i] = head[i];
							head[i].previous[i] = newLink;
							head[i] = newLink;
							added = true;
							break;
						} else {
							/* Inserts link in-between two links. */
							current.previous[i].next[i] = newLink;
							newLink.previous[i] = current.previous[i];
							newLink.next[i] = current;
							current.previous[i] = newLink;
							added = true;
							break;
						}
					}
					current = current.next[i];
				}
				/* Appends link to tail of list. */
				if (!added) {
					newLink.previous[i] = tail[i];
					tail[i].next[i] = newLink;
					tail[i] = newLink;
				}
			}
		}
		listSize++;
	}

	public MultiKeyListIterator iterator(int keyIndex) {
		if (keyIndex >= 0 && keyIndex < linkSize) {
			return new MyMultiKeyListIterator(keyIndex);
		}
		throw new IllegalArgumentException(keyIndex
				+ " is not within the range of allowed keys!");
	}

	/* Iterates list sorted by keyIndex, returns link in position index. */
	public MyListElem get(int index, int keyIndex) {
		if (keyIndex >= 0 && keyIndex < linkSize && index >= 0
				&& index < listSize) {
			MyListElem link = head[keyIndex];
			for (int i = 0; i < index; i++) {
				link = link.next[keyIndex];
			}
			return link;
		}
		throw new IllegalArgumentException(keyIndex + " is out of range!");
	}

	/* Removes the ith link in the list using the given key index. */
	public void removeIndex(int index, int keyIndex) {
		removeBlock(get(index, keyIndex));
	}

	/* Removes the link matching *all* keys. */
	public void remove(Comparable[] keys) {
		if (keys.length == linkSize) {
			MyListElem remove = head[0];
			while (remove != null) {
				if (remove.compareKeys(keys)) {
					removeBlock(remove);
					break;
				}
				remove = remove.next[0];
			}
		} else {
			throw new IllegalArgumentException(
					"keys is not compatible with list!");
		}
	}

	/* Removes the link matching the key at the given index. */
	public void remove(Comparable key, int keyIndex) {
		MyListElem remove = head[keyIndex];
		while (remove != null) {
			if (remove.key(keyIndex) == key) {
				removeBlock(remove);
				break;
			}
			remove = remove.next[keyIndex];
		}
	}

	/* Important block of code that will be called by all other remove methods. */
	private void removeBlock(MyListElem link) {

		/* For each key contained in the given link: */
		for (int key = 0; key < linkSize; key++) {
			/* If link makes up the entire list. */
			if (listSize == 1) {
				head = new MyListElem[linkSize];
				tail = new MyListElem[linkSize];
			}
			/* If key lies between two keys: */
			if (link.previous[key] != null && link.next[key] != null) {
				link.previous[key].next[key] = link.next[key];
				link.next[key].previous[key] = link.previous[key];
			}
			/* If key lies at the head of list: */
			if (link.previous[key] == null && link.next[key] != null) {
				head[key] = head[key].next[key];
				head[key].previous[key] = null;
			}
			/* If key lies at the tail of list: */
			if (link.next[key] == null && link.previous[key] != null) {
				tail[key] = tail[key].previous[key];
				tail[key].next[key] = null;
			}
		}
		listSize--;
	}

	private class MyMultiKeyListIterator implements MultiKeyListIterator {
		private MyListElem cursor;
		private MyListElem next;
		private MyListElem previous;
		private final int key;

		public MyMultiKeyListIterator(int key) {
			previous = null;
			cursor = null;
			next = head[key];
			this.key = key;
		}

		/* Checks if cursor points to a valid next link. */
		public boolean hasNext() {
			if (next != null) {
				return true;
			}
			return false;
		}

		/* Returns next link, moves cursor forward. */
		public MyListElem next() {
			if (hasNext()) {
				cursor = next;
				previous = next;
				next = next.next[key];
				return cursor;
			}
			throw new NoSuchElementException(
					"Reference to null pointer, there is no next!");
		}

		/* Checks if cursor points to a valid previous link. */
		public boolean hasPrevious() {
			if (previous != null) {
				return true;
			}
			return false;
		}

		/* Returns previous link, moves cursor backward. */
		public MyListElem previous() {
			if (hasPrevious()) {
				cursor = previous;
				next = previous;
				previous = previous.previous[key];
				return cursor;
			}
			throw new NoSuchElementException(
					"Reference to null pointer, there is no previous!");
		}

		/* Removes the link last returned by next() or previous(). */
		public void remove() {
			if (cursor != null) {
				removeBlock(cursor);
				cursor = null;
			} else {
				throw new IllegalStateException(
						"Called remove twice consecutively or remove was called before next().");
			}
		}
	}
}
