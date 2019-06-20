import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * This class implements the IndexedUnsortedList interface as a double linked
 * list.
 *
 * @author Joshua McKerracher
 *
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private int count; // size of the list
	private int modCount; // tracks changes to the list
	public DLLNode<T> head, tail; // head and tail nodes
	public DLLNode<T> end; // dummy node for the end of the list

	/**
	 * Default constructor
	 */
	public IUDoubleLinkedList() {

		// Initializes head and tail nodes.
		head = tail = null;

		// Sets number of nodes to 0.
		count = 0;

		modCount = 0;

		end = new DLLNode<T>(null);
	}

	/**
	 * Adds the specified element to the front of this list.
	 *
	 * @param element the element to be added to the front of this list
	 */
	@Override
	public void addToFront(T element) {
		DLLNode<T> temp = new DLLNode<T>(element);
		DLLNode<T> oldHead = head;
		// Adds the new element to an empty list.
		if (head == null && tail == null) {
			head = temp;
			tail = temp;
			head.setNext(tail);
			tail.setPrevious(head);
			tail.setNext(end);
			end.setPrevious(tail);
			// Adds the new element to the list.
		} else {
			head = temp;
			head.setNext(oldHead);
			oldHead.setPrevious(head);
		}
		count++;
		modCount++;
	}

	/**
	 * Adds the specified element to the rear of this list.
	 *
	 * @param element the element to be added to the rear of this list
	 */
	@Override
	public void addToRear(T element) {
		// Creates a new node to be added to the list.
		DLLNode<T> temp = new DLLNode<T>(element);

		// Adds the new element to an empty list.
		if (tail == null && head == null) {
			head = temp;
			tail = temp;
			head.setNext(tail);
			tail.setPrevious(head);
			tail.setNext(end);
			end.setPrevious(tail);

			// Adds the new element to the list.
		} else {
			tail.setNext(temp);
			temp.setPrevious(tail);
			tail = temp;
			tail.setNext(end);
			end.setPrevious(tail);
		}
		count++;
		modCount++;
	}

	/**
	 * Adds the specified element to the rear of this list.
	 *
	 * @param element the element to be added to the rear of the list
	 */
	@Override
	public void add(T element) {

		// Creates a new node to be added to the list.
		DLLNode<T> temp = new DLLNode<T>(element);

		// Adds the new element to an empty list.
		if (tail == null && head == null) {
			head = temp;
			tail = temp;
			head.setNext(tail);
			tail.setPrevious(head);
			tail.setNext(end);
			end.setPrevious(tail);

		} else {

			// Adds the new element to an unempty list.
			tail.setNext(temp);
			temp.setPrevious(tail);
			tail = temp;
			tail.setNext(end);
			end.setPrevious(tail);
		}
		count++;
		modCount++;
	}

	/**
	 * Adds the specified element after the specified target.
	 *
	 * @param element the element to be added after the target
	 *
	 * @param target  the target is the item that the element will be added after
	 *
	 * @throws NoSuchElementException if target element is not in this list
	 */
	@Override
	public void addAfter(T element, T target) {

		// initializes the new node to be added to the list.
		DLLNode<T> newNode = new DLLNode<T>(element);

		// Initializes the node used to iterate over the elements in the list.
		DLLNode<T> current = head;

		// Gets the index of the target. Returns -1 if the element isn't in the list.
		int targetIndex = indexOf(target);

		// Throws exception if the target isn't in the list.
		if (targetIndex == -1) {
			throw new NoSuchElementException("Source: addAfter(T element, T target");
		}

		// If there's only one element, makes the new node the tail.
		if (count == 1 && (head.getElement().equals(target))) {

			// The only element must be the head, sets the head's next to the new element.
			head.setNext(newNode);
			newNode.setPrevious(head);

			// The new node is now the tail.
			tail = newNode;
			tail.setNext(end);
			end.setPrevious(tail);
		}

		// Three potential add cases for the list if there's more than one element.
		if (count > 1) {

			// If the target element is the head:
			if (targetIndex == 0) {
				// Assigns the new node's next as head's next.
				newNode.setNext(head.getNext());

				// Gets head's next and sets its previous to the new node.
				head.getNext().setPrevious(newNode);

				// The new node's previous is the head.
				newNode.setPrevious(head);

				// Head's next is the new node.
				head.setNext(newNode);
			}

			// If the target is the tail:
			if (targetIndex == (count - 1)) {
				// Sets the tail's next as the new node.
				tail.setNext(newNode);

				// Assigns newNode's previous to the tail.
				newNode.setPrevious(tail);

				// Assigns the tail variable to the newNode.
				tail = newNode;

				// Ensures tail's next is still the end node.
				tail.setNext(end);

				// Ensures the end node's previous is still tail.
				end.setPrevious(tail);
			}

			// If the target isn't the head of the tail:
			if (targetIndex > 0 && targetIndex < (count - 1)) {
				// Iterates to the target node & captures it in current.
				for (int i = 0; i < targetIndex; i++) {
					current = current.getNext();
				}

				// Assigns newNode's next to current's next.
				newNode.setNext(current.getNext());

				// Assigns current's next to newNode.
				current.getNext().setPrevious(newNode);

				// Sets newNode's previous to current.
				newNode.setPrevious(current);

				// Sets current's next to the newNode.
				current.setNext(newNode);
			}
		}
		count++;
		modCount++;
	}

	/**
	 * Inserts the specified element at the specified index.
	 *
	 * @param index   the index into the array to which the element is to be
	 *                inserted.
	 * @param element the element to be inserted into the array
	 *
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
	 *                                   index > size)
	 */
	@Override
	public void add(int index, T element) {

		// Checks if the index is valid.
		if ((index < 0 || index > count)) {
			throw new IndexOutOfBoundsException("Source: Add(index, element) method");
		}

		// Creates the new nodes.
		DLLNode<T> newNode = new DLLNode<T>(element);

		// Creates the node used to iterate through the list.
		DLLNode<T> current = head;

		/******************************************
		 * Special cases.
		 ******************************************/

		// If adding to index 0 and the list is empty:
		if (tail == null && head == null && index == 0) {
			head = newNode;
			tail = newNode;
			head.setNext(tail);
			tail.setPrevious(head);
			tail.setNext(end);
			end.setPrevious(tail);
		}

		// If there's only one element and index = 1.
		if (count == 1 && index == 1) {
			head.setNext(newNode);
			newNode.setPrevious(head);
			tail = newNode;
			tail.setNext(end);
			end.setPrevious(tail);
		}
		// If there's only one element and index = 0.
		if (count == 1 && index == 0) {
			head = newNode;
			tail = current;
			head.setNext(tail);
			tail.setPrevious(head);
		}

		// If the newNode is going into head's position.
		if (index == 0 && count > 1) {
			newNode.setNext(head);
			head.setPrevious(newNode);
			head = newNode;
		}

		// If adding to the index after tail:
		if (index == (count) && count > 1) {
			tail.setNext(newNode);
			newNode.setPrevious(tail);
			tail = newNode;
			tail.setNext(end);
			end.setPrevious(tail);
		}

		/******************************************
		 * End of special cases.
		 ******************************************/

		// General add case.
		if (index > 0 && index < (count)) {
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			newNode.setPrevious(current.getPrevious());
			current.getPrevious().setNext(newNode);
			newNode.setNext(current);
			current.setPrevious(newNode);
		}
		count++;
		modCount++;
	}

	/**
	 * Removes and returns the first element from this list.
	 *
	 * @return the first element from this list
	 *
	 * @throws NoSuchElementException if list contains no elements
	 */
	@Override
	public T removeFirst() {

		// Checks if the list is empty and throws exception if it is.
		if (count <= 0) {
			throw new NoSuchElementException("Source: removeFirst");
		}

		// Captures the head in a temporary variable.
		DLLNode<T> temp = head;

		// Captures the element to be returned.
		T tempElement = temp.getElement();

		// Removes the only item in the list.
		if (count == 1) {
			head = null;
			tail = null;

		} else {

			// Removes from list with more than one element.
			DLLNode<T> newHead = head.getNext();
			head = newHead;
			temp.setNext(null);
			temp.setPrevious(null);
		}

		count--;
		modCount++;
		return tempElement;
	}

	/**
	 * Removes and returns the last element from this list.
	 *
	 * @return the last element from this list
	 *
	 * @throws NoSuchElementException if list contains no elements
	 */
	@Override
	public T removeLast() {

		// Throws an exception if the list is empty.
		if (count <= 0) {
			throw new NoSuchElementException("Source: removeFirst");
		}

		// Captures the element to be returned.
		T tempElement = tail.getElement();

		// Removes the only element in the list.
		if (count == 1) {
			head = null;
			tail = null;

		} else {

			// Removes from list with more than one element.
			DLLNode<T> newTail = tail.getPrevious();
			tail.setPrevious(null);
			tail = newTail;
			tail.setNext(end);
			end.setPrevious(tail);
		}

		count--;
		modCount++;
		return tempElement;
	}

	/**
	 * Removes and returns the specified element from this list.
	 *
	 * @param element the element to be removed from the list
	 *
	 * @return tempElement the element that is removed.
	 *
	 * @throws NoSuchElementException if element is not in this list
	 */
	@Override
	public T remove(T element) {

		// Gets the index of the element to be removed. Returns -1 if the element isn't
		// in the list.
		int targ = indexOf(element);

		// Throws an exception if the target element isn't in the list.
		if (targ == -1) {
			throw new NoSuchElementException("Source: remove(element)");
		}

		// Initializes the node used to iterate through the list.
		DLLNode<T> temp = head;

		// Initializes the element to be returned by this method.
		T tempElement = null;

		// Removes the only element in the list.
		if (count == 1) {
			tempElement = head.getElement();
			head = null;
			tail = null;

		} else {

			/******************************
			 * 3 general remove cases *****
			 ******************************/

			// Removes the head.
			if (targ == 0) {

				// Gets head's next and assigns it to newHead.
				DLLNode<T> newHead = head.getNext();

				// Gets the element to be returned.
				tempElement = head.getElement();

				// Assigns head to the newHead.
				head = newHead;

				// Removes the old head's next reference.
				temp.setNext(null);
			}

			// Removes the tail.
			if (targ == (count - 1)) {

				// Assigns tail's previous to newTail.
				DLLNode<T> newTail = tail.getPrevious();

				// Gets the element to be retured.
				tempElement = tail.getElement();

				// Removes tail's previous link.
				tail.setPrevious(null);

				// Assigns tail to the newTail.
				tail = newTail;

				// Sets the new tail's next link to end.
				tail.setNext(end);
				end.setPrevious(tail);
			}

			// Removes an element that's not the head or tail.
			if (targ > 0 && targ < (count - 1)) {

				// Gets the node.
				for (int i = 0; i < targ; i++) {
					temp = temp.getNext();
				}

				// Gets the element to be returned.
				tempElement = temp.getElement();

				// Removes the node.
				temp.getPrevious().setNext(temp.getNext());
				temp.getNext().setPrevious(temp.getPrevious());
			}
		}

		count--;
		modCount++;
		return tempElement;
	}

	/**
	 * Removes and returns the element at the specified index.
	 *
	 * @param index the index of the element to be retrieved
	 * @return tempElement the element that is removed.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
	 *                                   index >= size)
	 */
	@Override
	public T remove(int index) {

		// Checks if the index is in bounds.
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException("Source: remove(index)");
		}

		// Captures the element to be returned.
		DLLNode<T> temp = head;
		T tempElement = null;

		// Removes the only element in the list.
		if (count == 1) {
			tempElement = head.getElement();
			head = null;
			tail = null;

			// The rest of the remove cases.
		} else {

			// Removes the head.
			if (index == 0) {
				DLLNode<T> newHead = head.getNext();
				tempElement = head.getElement();
				head = newHead;
				temp.setNext(null);
			}

			// Removes the tail.
			if (index == (count - 1)) {
				DLLNode<T> newTail = tail.getPrevious();
				tempElement = tail.getElement();
				tail.setPrevious(null);
				tail = newTail;
				tail.setNext(end);
				end.setPrevious(tail);
			}

			// Removes an element that's not a head or tail.
			if (index > 0 && index < (count - 1)) {
				// Gets the node.
				for (int i = 0; i < index; i++) {
					temp = temp.getNext();
				}
				// Removes the node.
				tempElement = temp.getElement();
				temp.getPrevious().setNext(temp.getNext());
				temp.getNext().setPrevious(temp.getPrevious());
			}
		}
		count--;
		modCount++;
		return tempElement;
	}

	/**
	 * Returns a reference to the element at the specified index.
	 *
	 * @param index the index to which the reference is to be retrieved from
	 * @return the element at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
	 *                                   index >= size)
	 */
	@Override
	public void set(int index, T element) {

		// Checks if the index is in range.
		if ((index < 0 || index >= count)) {
			throw new IndexOutOfBoundsException("Source: set(index, element");
		}

		// The iterating node.
		DLLNode<T> temp = head;

		// Sets the head element.
		if (index == 0) {
			head.setElement(element);
		}

		// Sets the tail element.
		if (index == (count - 1)) {
			tail.setElement(element);
		}

		// Sets the element for a node that's not the tail or head.
		if (index > 0 && index < (count - 1)) {
			for (int i = 0; i < index; i++) {
				temp = temp.getNext();
			}
			temp.setElement(element);
		}
		modCount++;
	}

	/**
	 * Returns a reference to the element at the specified index.
	 *
	 * @param index the index to which the reference is to be retrieved from
	 * @return the element at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
	 *                                   index >= size)
	 */
	@Override
	public T get(int index) {

		// Checks if the index is valid.
		if ((index < 0 || index >= count)) {
			throw new IndexOutOfBoundsException("Source: set element at index method.");
		}

		// Creates the iterating node.
		DLLNode<T> tempNode = head;

		// Gets the node at the target index.
		for (int i = 0; i < index; i++) {
			tempNode = tempNode.getNext();
		}
		return tempNode.getElement();
	}

	/**
	 * Returns the index of the specified element.
	 *
	 * @param element the element for the index is to be retrieved
	 * @return the integer index for this element or -1 if element is not in the
	 *         list
	 */
	@Override
	public int indexOf(T element) {

		// Sets the default return index value to -1.
		int index = -1;

		// Used to track how many times the while loop runs.
		int tracker = 0;

		// Initializes a node used to iterate through the list.
		DLLNode<T> temp = head;

		// Runs while temp isn't null *OR* tracker is less than count.
		while (temp != null || tracker < count) {

			// Checks if the current node's element is the target element.
			if (temp.getElement() == element) {

				// If the element has been found, assign tracker's value to index.
				index = tracker;
			}

			// While the element hasn't been found, increment tracker.
			tracker++;

			// While the element hasn't been found, get the next node.
			temp = temp.getNext();
		}

		return index;
	}

	/**
	 * Returns a reference to the first element in this list.
	 *
	 * @return a reference to the first element in this list
	 *
	 * @throws NoSuchElementException if list contains no elements
	 */
	@Override
	public T first() {
		if (count == 0) {
			throw new NoSuchElementException("Source: first() method.");
		}

		return head.getElement();
	}

	/**
	 * Returns a reference to the last element in this list.
	 *
	 * @return a reference to the last element in this list
	 * @throws NoSuchElementException if list contains no elements
	 */
	@Override
	public T last() {

		if (head == null && tail == null) {
			throw new NoSuchElementException("Source: last() method.");
		}

		return tail.getElement();
	}

	/**
	 * Returns true if this list contains the specified target element.
	 *
	 * @param target the target that is being sought in the list
	 * @return bool true if the list contains this element, else false
	 */
	@Override
	public boolean contains(T target) {
		boolean bool = false;

		// Creates the iterating node.
		DLLNode<T> temp = head;

		// Iterates through the list until the target is found.
		while (temp != null) {
			if (temp.getElement() == target) {
				bool = true;
			}
			temp = temp.getNext();
		}
		return bool;
	}

	/**
	 * Returns true if this list contains no elements.
	 *
	 * @return bool true if this list contains no elements
	 */
	@Override
	public boolean isEmpty() {
		boolean bool = true;

		// False if there's at least one element.
		if (count > 0) {
			bool = false;
		}
		// Flase if the head isn't empty.
		if (head != null) {
			bool = false;
		}

		return bool;
	}

	/**
	 * Returns the number of elements in this list.
	 *
	 * @return count the integer representation of number of elements in this list
	 */
	@Override
	public int size() {
		return count;
	}

	/**
	 * Returns a string representation of the contents of the double linked list.
	 *
	 * @return a string of the elements
	 */
	@Override
	public String toString() {

		String str = "";

		DLLNode<T> temp = head;
		int tracker = 0;

		while (tracker < count) {
			str += temp.getElement().toString();
			if (tracker < count - 1) {
				// Adds a comma to the string until the 2nd to last element.
				str += ", ";
			}
			temp = temp.getNext();
			tracker++;
		}

		if (count == 0) {
			str += "The list is empty.";
		}

		return str;
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) listIterator();
	}

	/*******************************************************
	 *
	 * ListIterator class
	 *
	 ********************************************************/
	private class IUListIterator implements ListIterator<T> {

		private int position; // Position of the iterator.
		private int iteratorModCount; // Modifications done to the list by the
		private boolean wasNextCalled; // Tracks if next was called.
		private boolean wasPreviousCalled; // Tracks if previous was called.
		private boolean wasRemoveCalled; // Tracks if remove was called.
		private boolean wasAddCalled; // Tracks if add was called.
		DLLNode<T> current;

		/*
		 * Default constructor
		 */
		public IUListIterator() {
			position = 0;
			iteratorModCount = modCount;
			wasNextCalled = false;
			wasPreviousCalled = false;
			wasRemoveCalled = false;
			wasAddCalled = false;
			current = head;
		}

		/**
		 * Overloaded constructor that allows a List Iterator to be constructed and
		 * start at a particular index position in the list.
		 *
		 * @param startIndex the index the new List Iterator starts at.
		 */
		public IUListIterator(int startIndex) {

			if ((startIndex < 0 || startIndex > count)) {
				throw new IndexOutOfBoundsException();
			}

			current = head;

			// Gets the node at the startIndex.
			for (int i = 0; i < startIndex; i++) {
				current = current.getNext();
			}

			position = startIndex;
			iteratorModCount = modCount;
			wasNextCalled = false;
			wasPreviousCalled = false;
			wasRemoveCalled = false;
			wasAddCalled = false;
		}

		/**
		 * Returns true if this list iterator has more elements when traversing the list
		 * in the forward direction. (In other words, returns true if next() would
		 * return an element rather than throwing an exception.)
		 *
		 * @return bool
		 */
		@Override
		public boolean hasNext() {

			boolean bool = false;

			if (position < count) {
				bool = true;
			}

			if (wasNextCalled == true && count == 1 && position == count) {
				bool = false;
			}

			return bool;
		}

		/**
		 * Returns the next element in the list and advances the cursor position. This
		 * method may be called repeatedly to iterate through the list, or intermixed
		 * with calls to previous() to go back and forth. (Note that alternating calls
		 * to next and previous will return the same element repeatedly.)
		 *
		 * @return temp
		 */
		@Override
		public T next() {
			if (modCount != iteratorModCount) {
				throw new ConcurrentModificationException("Source: IUSingleLinkedListIterator next method");
			}

			if (hasNext() == false) {
				throw new NoSuchElementException("Iterator - next - end of list");
			}

			wasNextCalled = true;
			wasPreviousCalled = false;

			// Captures the element to be returned.
			T temp = current.getElement();

			current = current.getNext();
			position++;

			return temp;
		}

		/**
		 * Returns true if this list iterator has more elements when traversing the list
		 * in the reverse direction. (In other words, returns true if previous() would
		 * return an element rather than throwing an exception.)
		 *
		 */
		@Override
		public boolean hasPrevious() {
			boolean bool = false;

			if (position >= 1 && count >= 1) {
				bool = true;
			}

			return bool;
		}

		/**
		 * Returns the previous element in the list and moves the cursor position
		 * backwards. This method may be called repeatedly to iterate through the list
		 * backwards, or intermixed with calls to next() to go back and forth. (Note
		 * that alternating calls to next and previous will return the same element
		 * repeatedly.)
		 *
		 * @return temp the element
		 */
		@Override
		public T previous() {
			if (modCount != iteratorModCount) {
				throw new ConcurrentModificationException("Source: IUSingleLinkedListIterator next method");
			}

			if (hasPrevious() == false) {
				throw new NoSuchElementException("Iterator - previous - end of list");
			}

			T temp = current.getPrevious().getElement();
			wasPreviousCalled = true;
			wasNextCalled = false;
			current = current.getPrevious();
			position--;
			return temp;
		}

		/**
		 * Returns the index of the element that would be returned by a subsequent call
		 * to next(). (Returns list size if the list iterator is at the end of the
		 * list.)
		 *
		 * @return position the index of the next element
		 */
		@Override
		public int nextIndex() {
			return position;
		}

		/**
		 * Returns the index of the element that would be returned by a subsequent call
		 * to previous(). (Returns -1 if the list iterator is at the beginning of the
		 * list.)
		 *
		 * @return the index of the previous element
		 */
		@Override
		public int previousIndex() {
			return (position - 1);
		}

		/**
		 * Removes from the list the last element that was returned by next() or
		 * previous() (optional operation). This call can only be made once per call to
		 * next or previous. It can be made only if add(E) has not been called after the
		 * last call to next or previous.
		 */
		@Override
		public void remove() {

			if (count == 0 || (wasNextCalled == false && wasPreviousCalled == false)) {
				throw new IllegalStateException("Source: List Iterator remove method");
			}

			if (modCount != iteratorModCount) {
				throw new ConcurrentModificationException("Source: IUSingleLinkedListIterator next method");
			}

			// can be made only if add(E) has not been called after the last call to next or
			// previous.
			if ((wasNextCalled == true || wasPreviousCalled == true) && (wasAddCalled == true)) {
				throw new IllegalStateException("Source: List Iterator remove method");
			}

			// Remove after next was called
			if (wasNextCalled) {

				if (count == 1) {
					head.setNext(null);
					tail.setNext(null);
					tail.setPrevious(null);
				} else {

					// Removes the head.
					if (position == 1) {
						DLLNode<T> tempHead = head;
						head = head.getNext();
						tempHead.setNext(null);
						tempHead.setPrevious(null);
					}

					// Removes the tail.
					if (position == count) {
						DLLNode<T> oldTail = tail;
						tail = tail.getPrevious();
						tail.setNext(end);
						oldTail.setNext(null);
						oldTail.setPrevious(null);
					}

					// Removes an element that's not a head or tail.
					if (position > 1 && position < count) {
						current.getPrevious().getPrevious().setNext(current);
						current.setPrevious(current.getPrevious().getPrevious());

					}
				}
				wasNextCalled = false;
				count--;
				position--;
			}

			// Remove after previous was called
			if (wasPreviousCalled) {

				if (count == 1) {
					head.setNext(null);
					tail.setPrevious(null);
				} else {

					// Removes the head.
					if (position == 0) {
						DLLNode<T> tempHead = head;
						head = head.getNext();
						tempHead.setNext(null);
						head.setPrevious(null);
						current = head;
						current.setPrevious(null);
						current.setNext(head.getNext());
					}

					// Removes the tail.
					if (position == (count - 1)) {
						DLLNode<T> oldTail = tail;
						tail = tail.getPrevious();
						tail.setNext(end);
						oldTail.setNext(null);
						oldTail.setPrevious(null);
						end.setPrevious(tail);
						current.setPrevious(tail);
						current.setNext(end);
					}

					// Removes an element that's not a head or tail.
					if (position > 0 && position <= (count - 2)) {
						current.getPrevious().setNext(current.getNext());
						current.getNext().setPrevious(current.getPrevious());
						current = current.getNext();
					}
				}
				wasPreviousCalled = false;
				count--;
			}
			wasNextCalled = false;
			modCount++;
			iteratorModCount++;
			// Adding this for the set method:
			wasRemoveCalled = true;
		}

		/**
		 * Replaces the last element returned by next() or previous() with the specified
		 * element (optional operation). This call can be made only if neither remove()
		 * nor add(E) have been called after the last call to next or previous.
		 *
		 * @param e the new element inserted into the list
		 */
		@Override
		public void set(T e) {

			if (wasRemoveCalled || wasAddCalled || count == 0) {
				throw new IllegalStateException("Source: List Iterator set method");
			}

			if (!wasNextCalled && !wasPreviousCalled) {
				throw new IllegalStateException("Source: List Iterator set method");
			}

			if (wasNextCalled) {
				if (current.getPrevious().getElement() != e) {
					current.getPrevious().setElement(e);
				}
			}
			if (wasPreviousCalled) {
				if (current.getNext().getElement() != e) {
					current.getNext().setElement(e);
				}
			}

		}

		/**
		 * Inserts the specified element into the list (optional operation). The element
		 * is inserted immediately before the element that would be returned by next(),
		 * if any, and after the element that would be returned by previous(), if any.
		 * (If the list contains no elements, the new element becomes the sole element
		 * on the list.) The new element is inserted before the implicit cursor: a
		 * subsequent call to next would be unaffected, and a subsequent call to
		 * previous would return the new element. (This call increases by one the value
		 * that would be returned by a call to nextIndex or previousIndex.)
		 *
		 * @param e the new element added to the list
		 */
		@Override
		public void add(T e) {

			DLLNode<T> addNode = new DLLNode<T>(e);

			// Add cases preceded by a call to next.
			if (wasNextCalled) {
				// Only special case: empty list to node.
				if (count == 0) {
					head = addNode;
					tail = addNode;
					head.setNext(tail);
					tail.setPrevious(head);
					tail.setNext(end);
					end.setPrevious(tail);
				} else {
					// If adding after the tail
					if (position == count) {
						tail.setNext(addNode);
						addNode.setNext(end);
						addNode.setPrevious(tail);
						end.setPrevious(addNode);
						current = tail;
						tail = addNode;
						current = current.getNext();
						current = current.getNext();
						position++;
					}
					// If adding before the head
					if (position == 0) {
						addNode.setNext(head);
						head.setPrevious(addNode);
						head = addNode;
					}
					// General case:
					if (position > 0 && position < count) {
						current.getPrevious().setNext(addNode);
						addNode.setPrevious(current.getPrevious());
						addNode.setNext(current);
						current.setPrevious(addNode);
						position++;
					}
				}
			}

			// Add cases preceded by a call to previous.
			if (wasPreviousCalled) {
				if (count == 0) {
					head = addNode;
					tail = addNode;
					head.setNext(tail);
					tail.setPrevious(head);
					tail.setNext(end);
					end.setPrevious(tail);
				} else {

					// If adding after the tail
					if (position == count) {
						tail.setNext(addNode);
						addNode.setNext(end);
						addNode.setPrevious(tail);
						end.setPrevious(addNode);
						tail = addNode;
					}

					// If adding before the head
					if (position == 0) {
						addNode.setNext(head);
						head.setPrevious(addNode);
						head = addNode;
						current.setPrevious(head);
					}

					// General case: inserts after the element that would be returned by previous.
					if (position > 0 && position < count) {
						current.getPrevious().setNext(addNode);
						addNode.setPrevious(current.getPrevious());
						addNode.setNext(current);
						current.setPrevious(addNode);

					}
					position++;
				}
			}

			if (!wasPreviousCalled && !wasNextCalled && position == 0) {
				if (count == 0) {
					head = addNode;
					tail = addNode;
					head.setNext(tail);
					tail.setPrevious(head);
					tail.setNext(end);
					end.setPrevious(tail);
					current = head;
					position++;
				} else {
					current.setPrevious(addNode);
					addNode.setNext(head);
					head.setPrevious(addNode);
					head = addNode;
					position++;
				}
			}
			count++;
			modCount++;
			iteratorModCount++;
			// Used in the set method.
			wasAddCalled = true;
		}
	}

	/**
	 * Returns a new List Iterator object starting at the starting index.
	 *
	 * @param startingIndex the index that the new List Iterator starts at.
	 *
	 * @return listIterator the new list iterator object at the specified index
	 */
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new IUListIterator(startingIndex);
	}

	/**
	 * @return a new ListIterator object.
	 *
	 */
	@Override
	public ListIterator<T> listIterator() {
		return new IUListIterator();
	}
}
