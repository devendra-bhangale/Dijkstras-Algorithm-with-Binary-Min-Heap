package ospfpackage;

import java.util.HashMap;

import graphpackage.Vertex;

/* Binary Minimum Heap Priority Queue */
public class MinHeapQueue {
	protected final int ROOT = 1;
	protected HashMap<Integer, Vertex> queue;
	protected int size;

	/* create a new empty min heap queue */
	public MinHeapQueue() {
		queue = new HashMap<Integer, Vertex>();
		size = 0;
	}

	/* insert a new vertex at the end of the queue */
	public int insert(Vertex vertex) {
		size++;
		queue.put(size, vertex);

		int index = floatUp(size); // keep swapping this new vertex with its parent, until the parent is at least as large or you reach root
		
		return index;
	}

	/* extract the Vertex (at root) with minimum distance from source */
	public Vertex extractMin() {
		int index = size;
		Vertex vertex = getVertex(ROOT);

		queue.remove(ROOT); // remove min vertex at root
		queue.put(ROOT, getVertex(index)); // replace the root with last vertex
		queue.remove(index); // remove the last vertex
		size--;

		floatDown(); // float down the value at root to min-heapify

		return vertex;
	}

	/* New key has lower value; hence update the value and increase key priority */
	public int increasePriority(int index, double dist, Vertex prevVertex) {
		queue.get(index).distance = dist;
		queue.get(index).prev = prevVertex;
		return floatUp(index);
	}

	/* keep swapping the newly added vertex with its parent, until the parent is at least as large or you reach root */
	public int floatUp(int index) {
		while (hasParent(index) && (getVertex(getParentIndex(index)).distance > getVertex(index).distance)) {
			swap(index, getParentIndex(index));
			index = getParentIndex(index);
		}
		
		return index;
	}

	/* float down the vertex at root (after min extraction) to its correct position in the min heap */
	public void floatDown() {
		int index = ROOT;
		int smallerChild;

		while (hasLeftChild(index)) {
			/* find the index of the smaller child amongst the left child and right child if it exists */
			if (hasRightChild(index))
				smallerChild = (getVertex(getLeftChildIndex(index)).distance <= getVertex(getRightChildIndex(index)).distance) ? getLeftChildIndex(index) : getRightChildIndex(index);
			else
				smallerChild = getLeftChildIndex(index);

			/* check if the parent is greater than the child and swap if yes or else break */
			if (getVertex(index).distance > getVertex(smallerChild).distance)
				swap(index, smallerChild);
			else
				break;

			/* update the index to its latest position */
			index = smallerChild;
		}
	}

	/* swap the two vertices at given 'index' and 'parent' positions in the min heap queue */
	public void swap(int index, int parent) {
		Vertex temp = getVertex(index);

		queue.remove(index);
		queue.put(index, getVertex(parent));

		queue.remove(parent);
		queue.put(parent, temp);
	}

	/* get the vertex at mentioned 'index' */
	public Vertex getVertex(int index) {
		Vertex vertex = queue.get(index);
		return vertex;
	}

	/* return true if the element has a parent; else return false */
	protected boolean hasParent(int index) {
		return (index > ROOT);
	}

	/* return the 'index' of the parent of the given child at index 'index' */
	protected int getParentIndex(int index) {
		return (index / 2);
	}

	/* return true if the element has left child; else return false */
	protected boolean hasLeftChild(int index) {
		return (getLeftChildIndex(index) <= size);
	}

	/* return the 'index' of the left child of the given parent at index 'index' */
	protected int getLeftChildIndex(int index) {
		return (index * 2);
	}

	/* return true if the element has right child; else return false */
	protected boolean hasRightChild(int index) {
		return (getRightChildIndex(index) <= size);
	}

	/* return the 'index' of the right child of the given parent at index 'index' */
	protected int getRightChildIndex(int index) {
		return ((index * 2) + 1);
	}

	/* return size of the queue */
	public int getQueueSize() {
		return size;
	}
}
