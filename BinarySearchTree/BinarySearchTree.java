import java.util.ArrayList;
import java.util.List;

class BinarySearchTree<T extends Comparable<T>> {

	private class BSTNode {
		public T data;
		public BSTNode left;
		public BSTNode right;

		BSTNode(T newdata) {
			data = newdata;
		}
	}

	private BSTNode root;

	public void Insert(T elem) {
		root = Insert(root, elem);
	}

	public boolean Find(T elem) {
		return Find(root, elem);
	}

	public void Delete(T elem) {
		root = Delete(root, elem);
	}

	/* Helper method, creates new ArrayList and calls sort(). */
	private ArrayList<T> callSort() {
		ArrayList<T> sorted = new ArrayList<T>();
		return sort(root, sorted);
	}

	/* Helper method, stores and returns tree contents in sorted order. */
	private ArrayList<T> sort(BSTNode root, ArrayList<T> sorted) {
		if (root != null) {
			sort(root.left, sorted);
			sorted.add(root.data);
			sort(root.right, sorted);
		}
		return sorted;
	}

	/* Returns the ith largest element in the binary search tree. */
	public Comparable ElementAt(int i) {
		return callSort().get(i);
	}

	/*
	 * Returns the number of elements in the tree greater or equal to low and
	 * less than or equal to high. Only examines from beginning of list until
	 * the first appearance of T high.
	 */
	public int numInRange(T low, T high) {
		int lowIndex = 0;
		while (!ElementAt(lowIndex).equals(low)) {
			lowIndex++;
		}
		int highIndex = lowIndex;
		while (!ElementAt(highIndex).equals(high)) {
			highIndex++;
		}
		return highIndex - lowIndex + 1;
	}

	/*
	 * Reorders the tree so that it is balanced; height is as small as possible.
	 */
	public void Balance() {
		root = balance(new BinarySearchTree<T>(), callSort());
	}

	/*
	 * Creates a new tree, inserts all of the elements from the sorted list.
	 */
	private BSTNode balance(BinarySearchTree<T> tree, List<T> sorted) {
		int size = sorted.size();
		int mid = size / 2;

		if (size == 1) {
			tree.Insert(sorted.get(0));
			return tree.root;
		} else if (size == 2) {
			tree.Insert(sorted.get(0));
			tree.Insert(sorted.get(1));
			return tree.root;
		}
		tree.Insert(sorted.get(mid));
		balance(tree, sorted.subList(0, mid));
		return balance(tree, sorted.subList(mid + 1, size));

	}

	public void Print() {
		Print(root);
	}

	public int Height() {
		return Height(root);
	}

	private int Height(BSTNode tree) {
		if (tree == null) {
			return 0;
		}
		return 1 + Math.max(Height(tree.left), Height(tree.right));
	}

	private boolean Find(BSTNode tree, T elem) {
		if (tree == null) {
			return false;
		}
		if (elem.compareTo(tree.data) == 0) {
			return true;
		}
		if (elem.compareTo(tree.data) < 0) {
			return Find(tree.left, elem);
		} else {
			return Find(tree.right, elem);
		}
	}

	private T Minimum(BSTNode tree) {
		if (tree == null) {
			return null;
		}
		if (tree.left == null) {
			return tree.data;
		} else {
			return Minimum(tree.left);
		}
	}

	private void Print(BSTNode tree) {
		if (tree != null) {
			Print(tree.left);
			System.out.println(tree.data);
			Print(tree.right);
		}
	}

	private BSTNode Insert(BSTNode tree, T elem) {
		if (tree == null) {
			return new BSTNode(elem);
		}
		if (elem.compareTo(tree.data) < 0) {
			tree.left = Insert(tree.left, elem);
			return tree;
		} else {
			tree.right = Insert(tree.right, elem);
			return tree;
		}
	}

	private BSTNode Delete(BSTNode tree, T elem) {
		if (tree == null) {
			return null;
		}
		if (tree.data.compareTo(elem) == 0) {
			if (tree.left == null) {
				return tree.right;
			} else if (tree.right == null) {
				return tree.left;
			} else {
				if (tree.right.left == null) {
					tree.data = tree.right.data;
					tree.right = tree.right.right;
					return tree;
				} else {
					tree.data = RemoveSmallest(tree.right);
					return tree;
				}
			}
		} else if (elem.compareTo(tree.data) < 0) {
			tree.left = Delete(tree.left, elem);
			return tree;
		} else {
			tree.right = Delete(tree.right, elem);
			return tree;
		}
	}

	T RemoveSmallest(BSTNode tree) {
		if (tree.left.left == null) {
			T smallest = tree.left.data;
			tree.left = tree.left.right;
			return smallest;
		} else {
			return RemoveSmallest(tree.left);
		}
	}

	public static void main(String args[]) {
		BinarySearchTree<Integer> t = new BinarySearchTree<Integer>();
		for (int x = 0; x < 31; x++)
			t.Insert(new Integer(x));
		System.out.println(t.ElementAt(new Integer(5)));
		System.out.println(t.numInRange(new Integer(10), new Integer(15)));
		System.out.println(t.Height());
		t.Balance();
		System.out.println(t.ElementAt(new Integer(5)));
		System.out.println(t.numInRange(new Integer(10), new Integer(15)));
		System.out.println(t.Height());
	}

}