public class HashTable {
	Node[] table;

	HashTable() {
		table = new Node[11];
	}

	public class Node {
		String city;
		int id;
		Node next;

		Node(String city, int id) {
			this.city = city;
			this.id = id;
			next = null;
		}
	}

	public int ELFhash(String city) {
		long h = 0;
		long g;
		int i;
		for (i = 0; i < city.length(); i++) {
			h = (h << 4) + (int) city.charAt(i);
			g = h & 0xF0000000L;
			if (g != 0) {
				h ^= g >>> 24;
			}
			h &= ~g;
		}
		return (int) h % 11;
	}

	public void add(String city, int id) {
		Node node = new Node(city, id);
		if (table[ELFhash(node.city)] == null) {
			table[ELFhash(node.city)] = node;
		} else {
			Node current = table[ELFhash(node.city)];
			while (current.next != null) {
				current = current.next;
			}
			current.next = node;
		}
	}

	public int get(String city) {
		Node head = table[ELFhash(city)];
		while(head != null) {
			if(head.city.equals(city)) {
				return head.id;
			}
			head = head.next;
		}
		return -1;
	}

	public void print() {
		int i = 0;
		for (Node node : table) {
			System.out.print("position-" + i + ": ");
			while (node != null) {
				System.out.print(node.city + " ");
				node = node.next;
			}
			i++;
			System.out.println();
		}
	}
}
