package ospfpackage;

import java.util.HashMap;

import graphpackage.Vertex;

/* Binary Minimum Heap Priority Queue */
public class MinHeapQueue {
	protected final int ROOT = 1; // root vertex is at positon 1 in the queue
	protected HashMap<Integer, Vertex> queue; // data structure to hold the queue
	protected int size; // current number of vertices in the queue

	/* create a new empty min heap queue */
	public MinHeapQueue() {
		queue = new HashMap<Integer, Vertex>();
		size = 0;
	}

	/* insert a new vertex at the end of the queue and update its index in the queue */
	public void insert(Vertex vertex) {
		size++;
		vertex.queueIndex = size;
		queue.put(size, vertex);

		floatUp(size); // keep swapping this new vertex with its parent, until the parent is at least as large or you reach root
	}

	/* extract the Vertex (at root) with minimum distance from source */
	public Vertex extractMin() {
		int index = size;
		Vertex vertex = getVertex(ROOT);

		queue.remove(ROOT); // remove min vertex at root
		if (index > 1) {
			queue.put(ROOT, getVertex(index)); // replace the root with last vertex
			queue.remove(index); // remove the last vertex
			getVertex(ROOT).queueIndex = ROOT; // update the index of vertex in the queue
		}
		size--; // update the number of vertices left in the queue

		floatDown(ROOT); // float down the value at root to min-heapify

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
	public void floatDown(int parent) {
		int smallerChild;
		while (hasLeftChild(parent)) {
			/* find the index of the smaller child amongst the left child and right child if it exists */
			if (hasRightChild(parent))
				smallerChild = (getVertex(getLeftChildIndex(parent)).distance <= getVertex(getRightChildIndex(parent)).distance) ? getLeftChildIndex(parent) : getRightChildIndex(parent);
			else
				smallerChild = getLeftChildIndex(parent);

			/* check if the parent is greater than the child and swap if yes or else break */
			if (getVertex(parent).distance > getVertex(smallerChild).distance)
				swap(smallerChild, parent);
			else
				break;

			/* update the parent index to its latest position */
			parent = smallerChild;
		}
	}

	/* swap the two vertices at given 'index' and 'parent' positions in the min heap queue; also update their indices in the queue */
	public void swap(int index, int parent) {
		Vertex temp = getVertex(index);

		queue.remove(index);
		queue.put(index, getVertex(parent));
		getVertex(index).queueIndex = index;

		queue.remove(parent);
		queue.put(parent, temp);
		getVertex(parent).queueIndex = parent;
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
