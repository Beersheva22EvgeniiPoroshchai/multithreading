
package telran.multithreading.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLinkedBlockingQueue<E> implements BlockingQueue<E> {

	LinkedList<E> list = new LinkedList<>();
	int limit = Integer.MAX_VALUE;

	
	private Lock lock = new ReentrantLock();
	private Condition consumerWaiting = lock.newCondition();
	private Condition producerWaiting = lock.newCondition();

	public MyLinkedBlockingQueue(int limit) {
		this.limit = limit;
	}

	public MyLinkedBlockingQueue() {
	}

	@Override
	public E remove() {
		lock.lock();
		try {
			if (!isEmpty()) {
				E res;
				res = list.remove();
				producerWaiting.signal();
				return res;
			} else {
				throw new NoSuchElementException(); 		// return exception
			}

		} finally {
			lock.unlock();
		}
	}

	@Override
	public E poll() {
		E res;
		lock.lock();
		try {
			if (!isEmpty()) {
				res = list.remove();
				producerWaiting.signal();
			} else {
				res = null;
			}
			return res; 						// return null

		} finally {
			lock.unlock();
		}

	}

	@Override
	public E element() { 
		lock.lock();
		try {
			if (isEmpty() == false) {
				E res = list.getFirst(); 				// or element (get by index) method
				return res; 					// just return val without deleting or exception
			} else {
				throw new NoSuchElementException();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public E peek() { 
		E res;
		lock.lock();
		try {
			if (!isEmpty()) {
				res = list.getFirst(); 				// or element method
				producerWaiting.signal();
			} else {
				res = null;
			}
			return res; 				// just return without deleting or null
		} finally {
			lock.unlock();
		}

	}

	@Override
	public int size() { 
		lock.lock();
		try {
			return list.size();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.lock();
		try {
			return list.isEmpty();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Iterator iterator() { 
		lock.lock();
		try {
			return list.iterator();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Object[] toArray() { 
		lock.lock();
		try {
			return list.toArray();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Object[] toArray(Object[] a) { 
		lock.lock();
		try {
			return list.toArray(a);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection c) { 
		lock.lock();
		try {
			return list.containsAll(c);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean addAll(Collection c) { 
		lock.lock();
		try {
			boolean res = false;
			int value = size();
			for (int i = 0; i <= size(); i++) {
				list.add((E) c);
			}
			if (value != size()) {
				res = true;
			}
			return res;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean removeAll(Collection c) { 
		lock.lock();
		try {
			boolean res = list.removeAll(c);
			producerWaiting.signal();
			return res;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean retainAll(Collection c) { 
		lock.lock();
		try {
			boolean res = list.retainAll(c);
			producerWaiting.signal();
			return res;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void clear() { 
		lock.lock();
		try {
			list.clear();
			producerWaiting.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean add(Object e) { 
		lock.lock();
		boolean res = false;
		try {
			if (remainingCapacity() > 0) {
				res = list.add((E) e);
				consumerWaiting.signal();
				res = true;
			} else
				throw new IllegalStateException(); // return exception
			return res;

		} finally {
			lock.unlock();
		}

	}

	@Override
	public boolean offer(Object e) { 
		lock.lock();
		boolean res = false;
		try {
			if (remainingCapacity() > 0) {
				list.offer((E) e); 					// or add method
				consumerWaiting.signal();
				res = true;
			}

			return res; // 						ret false
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void put(E e) throws InterruptedException { 
		lock.lock();
		try {
			while (e != null && size() == limit) {
				producerWaiting.await();
			}
			list.push(e); 						// or add method?
			consumerWaiting.signal();

		} finally {
			lock.unlock();
		}

	}

	@Override
	public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
		boolean res = false;
		lock.lock();
		try {

			while (e != null && size() == limit) {
				if (producerWaiting.await(timeout, unit)) {
					list.offer((E) e);
					consumerWaiting.signal();
					res = true;
				} else {
					res = false;
				}

			}
			return res;
		} finally {
			lock.unlock();
		}

	}

	@Override
	public E take() throws InterruptedException { 
		lock.lock();
		try {
			while (isEmpty()) {
				consumerWaiting.await();
			}
			E res = list.removeFirst();           // or remove method???
			producerWaiting.signal();
			return res;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		E res = null;
		lock.lock();
		try {
			while (isEmpty()) {
				if (consumerWaiting.await(timeout, unit)) {
					res = list.removeFirst(); 			// or remove method?
					producerWaiting.signal();
				} else {
					res = null;
				}
			}
			return res;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int remainingCapacity() {
		lock.lock();
		try {
			return limit - list.size();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean remove(Object o) { 
		lock.lock();
		try {
			boolean res;
			if (o != null && list.contains(o)) {
				list.remove(o);
				res = true;
				return res;
			} else {
				throw new NullPointerException();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean contains(Object o) { 
		lock.lock();
		try {
			return list.contains(o);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int drainTo(Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(Collection c, int maxElements) {
		throw new UnsupportedOperationException();
	}
}
