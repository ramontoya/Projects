public class PriorityQueue {
	private Elem[] heap;
	private int[] heapFinder;
	private int maxsize;
	private int size;

	public PriorityQueue(int max) {
		maxsize = max;
		heap = new Elem[maxsize + 1];
		size = 0;
		heap[0] = new Elem(-1, Integer.MIN_VALUE);
	}

	public class Elem {
		int elem;
		double priority;

		Elem(int elem, double priority) {
			this.elem = elem;
			this.priority = priority;
		}
	}

	private int leftchild(int pos) {
		return 2 * pos;
	}

	private int rightchild(int pos) {
		return 2 * pos + 1;
	}

	private int parent(int pos) {
		return pos / 2;
	}

	private boolean isleaf(int pos) {
		return ((pos > size / 2) && (pos <= size));
	}

	private void swap(int swapUp) {
		Elem tmp;
		int swapDown = swapUp / 2;
		tmp = heap[swapUp];
		heap[swapUp] = heap[swapDown];
		heap[swapDown] = tmp;
	}

	public void insert(int elem, double priority) {
		size++;
		heap[size] = new Elem(elem, priority);
		int current = size;
		while (heap[current].priority < heap[parent(current)].priority) {
			swap(current);
			current = parent(current);
		}
	}

	public void print() {
		int i;
		int j;
		for (i = 1; i <= size; i++) {
			System.out.print("h: "+heap[i].elem + " ");
		}
		System.out.println();

		for (j = 1; j <= size; j++) {
			System.out.print("p: "+(int)heap[j].priority + " ");
		}

		System.out.println();
	}

	public Elem removemin() {
		Elem temp = heap[1];
		heap[1] = heap[size];
		heap[size] = temp;
		size--;
		if (size != 0)
			pushdown(1);
		return heap[size + 1];
	}

	private void pushdown(int position) {
		int smallestchild;
		while (!isleaf(position)) {
			smallestchild = leftchild(position);
			if ((smallestchild < size)
					&& (heap[smallestchild].priority > heap[smallestchild + 1].priority))
				smallestchild = smallestchild + 1;
			if (heap[position].priority <= heap[smallestchild].priority)
				return;
			Elem temp = heap[position];
			heap[position] = heap[smallestchild];
			heap[smallestchild] = temp;
			position = smallestchild;
		}
	}

	private int heapFinder(int index) {
		heapFinder = new int[heap.length];
		heapFinder[0] = -1;
		for (int i = 1; i < heap.length; i++) {
			heapFinder[heap[i].elem] = i;
		}
		return heapFinder[index];
	}

	public void reduceKey(int elem, double newPriority) {
		int index = heapFinder(elem);
		heap[index].priority = newPriority;
		while (heap[index].priority < heap[parent(index)].priority) {
			swap(index);
			index = parent(index);
		}
	}

	public boolean empty() {
		if (size == 0) {
			return true;
		}
		return false;
	}
}