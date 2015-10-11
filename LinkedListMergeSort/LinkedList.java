public class LinkedList {

	private LinkElem head = null;
	private LinkElem tail = null;

	/*
	 * Adds an element to the linked list
	 */
	public void add(int elem) {
		LinkElem newNode = new LinkElem(elem);
		if (head == null) {
			head = newNode;
			tail = head;
		} else {
			if (tail != null) {
				tail.setNext(newNode);
				newNode.setNext(null);
				tail = newNode;
			}
		}
	}

	/*
	 * Prints all the elements of the list
	 */
	public void printList() {
		LinkElem curr = head;
		while (curr != null) {
			System.out.print(curr.elem() + " ");
			curr = curr.next();
		}
		System.out.println();
	}

	/*
	 * Finds the middle element in the linked list that starts with link
	 */
	private static LinkElem findMiddle(LinkElem link) {
		LinkElem slow, fast;
		slow = link;
		fast = link.next();
		while ((fast != null) && fast.next() != null) {
			slow = slow.next();
			fast = fast.next().next();
		}
		return slow;
	}

	/*
	 * Merges two linked lists: one with head1 and the other with head2 Returns
	 * the head of the merged list
	 */
	public static LinkElem mergeLists(LinkElem head1, LinkElem head2) {
		LinkedList merged = new LinkedList();
		while (head1 != null || head2 != null) {
			if (head1 != null && head2 != null) {
				if (head1.elem() < head2.elem()) {
					merged.add(head1.elem());
					head1 = head1.next();
				} else if (head1.elem() > head2.elem()) {
					merged.add(head2.elem());
					head2 = head2.next();
				} else {
					merged.add(head1.elem());
					merged.add(head2.elem());
					head1 = head1.next();
					head2 = head2.next();
				}
			} else {
				if (head1 == null) {
					merged.add(head2.elem());
					head2 = head2.next();
				} else {
					merged.add(head1.elem());
					head1 = head1.next();
				}
			}
		}
		return merged.head;
	}

	/*
	 * Recursively divides the LinkedList into two sublists: one that goes from
	 * the element "begLink" to the middle element of the list; and another one
	 * that goes from the element after the middle element and till the
	 * "endLink". Merges the lists using mergeLists method and returns the new
	 * head.
	 */
	public static LinkElem divideList(LinkElem begLink, LinkElem endLink) {
		LinkElem left = begLink;
		LinkElem middle = findMiddle(begLink);
		LinkElem right = middle.next();
		LinkedList leftList = new LinkedList();
		LinkedList rightList = new LinkedList();
		while (left != right) {
			leftList.add(left.elem());
			left = left.next();
		}
		while (right != null) {
			rightList.add(right.elem());
			right = right.next();
		}
		if (begLink != null && begLink.next() != null) {
			leftList.head = divideList(leftList.head, middle);
			rightList.head = divideList(rightList.head, endLink);
		}
		LinkElem head = mergeLists(leftList.head, rightList.head);
		return head;
	}

	/*
	 * Sort this linked list using merge sort This method should call divideList
	 */
	public void mergeSort() {
		head = divideList(head, tail);
	}

	public static void main(String[] args) {
		LinkedList list = new LinkedList();
		int[] test = { 15, 24, 66, 18, 14, 55, 9, 1, 2012, 78, 22, 37 };
		for (int i = 0; i < test.length; i++) {
			list.add(test[i]);
		}
		list.printList();

		list.mergeSort();

		list.printList();

	}
}