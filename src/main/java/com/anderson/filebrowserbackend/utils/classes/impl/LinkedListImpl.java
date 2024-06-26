package com.anderson.filebrowserbackend.utils.classes.impl;
import com.anderson.filebrowserbackend.utils.classes.interfaces.LinkedList;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListImpl<T> implements LinkedList<T>, Serializable {
    private Node<T> head;
    private int size = 0;

    public LinkedListImpl() {
        this.head = null;
    }

    private static class Node<T> implements Serializable {
        private final T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            next = null;
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public void add(T item) {
        Node<T> neWNode = new Node<>(item);
        if (head == null) {
            head = neWNode;
        } else {
            Node<T> currNode = head;
            while (currNode.next != null) {
                currNode = currNode.next;
            }
            currNode.next = neWNode;
        }
        size++;
    }

    @Override
    public void add(int index, Object item) throws Exception {
        Node<T> newNode = (Node<T>) new Node<>(item);
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index is out of range!");
        }
        if (index == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> currNode = head;
            for (int i = 0; i < index - 1; i++) {
                currNode = currNode.next;
            }
            newNode.next = currNode.next;
            currNode.next = newNode;
        }
        size++;
    }

    @Override
    public void addFirst(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = head;
        head = newNode;
        size++;
    }

    @Override
    public void addLast(T item) {
        add(item);

    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<T> currNode = head;
        for (int i = 0; i < index; i++) {
            currNode = currNode.next;
        }
        return currNode.data;
    }

    @Override
    public T getFirst() {
        if (head == null || isEmpty() || size == 0) {
            throw new IllegalStateException("List is empty");
        }
        return head.data;
    }

    @Override
    public T getLast() {
        if (head == null || isEmpty() || size == 0) {
            throw new IllegalStateException("List is empty");
        }
        Node<T> currNode = head;
        while (currNode.next != null) {
            currNode = currNode.next;
        }
        return currNode.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        if (index == 0) {
            return removeFirst();
        }
        if (index == size() - 1) {
            return removeLast();
        }
        Node<T> currNode = head;
        for (int i = 0; i < index - 1; i++) {
            currNode = currNode.next;
        }

        T data = currNode.next.data;
        currNode.next = currNode.next.next;

        size--;
        return data;
    }

    @Override
    public void remove(T t) {
        if (head == null || isEmpty() || size == 0) {
            throw new IllegalStateException("List is empty");
        }
        if (head.data.equals(t)) {
            removeFirst();
            return;
        }
        Node<T> currNode = head;
        while (currNode.next != null) {
            if (currNode.next.data.equals(t)) {
                currNode.next = currNode.next.next;
                size--;
                return;
            }
            currNode = currNode.next;
        }
    }

    @Override
    public T removeFirst() {
        if (head == null || isEmpty() || size == 0) {
            throw new IllegalStateException("List is empty");
        }
        Node<T> aux = head;
        head = head.next;
        size--;
        return aux.data;
    }

    @Override
    public T removeLast() {
        if (head == null || isEmpty() || size == 0) {
            throw new IllegalStateException("List is empty");
        }
        if (head.next == null) {
            T data = head.data;
            head = null;
            size--;
            return data;
        }

        Node<T> currNode = head;
        while (currNode.next.next != null) {
            currNode = currNode.next;
        }
        Node<T> lastNode = currNode.next;
        currNode.next = null;
        size--;

        return lastNode.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    //For test
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> currNode = head;
        while (currNode != null) {
            sb.append(currNode.data);
            if (currNode.next != null) {
                sb.append(", ");
            }
            currNode = currNode.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
