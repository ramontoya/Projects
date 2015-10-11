/**
 * A class that represents a node of a Linked List.
 */
class LinkElem {

    private int element;
    private LinkElem next;

    public LinkElem(int elem, LinkElem next) {
    	element = elem;
    	this.next = next;
    }

    public LinkElem(int elem) {
    	element = elem;
    	next = null;
    }

    public int elem() {
    	return element;
    }

    public LinkElem next() {
    	return next;
    }

    public void setElem(int elem) {
    	element = elem;
    }

    public void setNext(LinkElem next) {
    	this.next = next;
    }
    
    
}