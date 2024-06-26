package com.anderson.filebrowserbackend.utils.classes.interfaces;

public interface LinkedList <T> extends Iterable<T> {
    void add(T item);
    void add(int index, Object item) throws Exception;
    void addFirst(T item);
    void addLast(T item);
    T get(int index);
    T getFirst();
    T getLast();
    T remove(int index);
    void remove(T t);
    T removeFirst();
    T removeLast();
    boolean isEmpty();
    int size();
    void clear();
}
