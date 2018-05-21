// Thread safe Evicting Queue implemented as a Java's Double Linked List.

package com.threadsafedatastructs.EvictingQueue;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class EvictingQueue<T> {
    private final int maxSize;
    private List<T> data = new LinkedList<>();
    private boolean isLocked = false;

    public EvictingQueue(int _maxSize) {
        this.maxSize = _maxSize;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public T head() {    /* Peek the first element */
        return this.data.get(0);
    }

    public T tail() {    /* Peek the last element */
        return this.data.get(this.data.size() - 1);
    }

    public synchronized T popFirst() {
        if (!this.data.isEmpty()) {
            return data.remove(0);
        }
        throw new NoSuchElementException();
    }

    public synchronized T popLast() {
        if (!this.data.isEmpty()) {
            return data.remove(data.size() - 1);
        }
        throw new NoSuchElementException();
    }

    public synchronized boolean push(T item) {
        if (this.data.size() <= maxSize) {
            if (this.data.size() == maxSize) {
                this.popFirst();
                this.data.add(0, item);
                return true;
            }
            this.data.add(0, item);
            return true;
        }
        throw new IndexOutOfBoundsException();
    }

    public synchronized boolean append(T item) {
        if (this.data.size() <= maxSize) {
            if (this.data.size() == maxSize) {
                this.popFirst();
                this.data.add(0, item);
                return true;
            }
            this.data.add(item);
            return true;
        }
        throw new IndexOutOfBoundsException();
    }

    public synchronized ArrayDeque<T> pollAll() {
        ArrayDeque<T> o = new ArrayDeque<>(this.data);
        this.data.clear();
        return o;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public int getMAX_SIZE() {
        return maxSize;
    }
}

