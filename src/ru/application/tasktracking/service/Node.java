package ru.application.tasktracking.service;



public class Node <Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public Node(Task data, Node <Task> node) {
        this.data = data;
        this.next = null;
        this.prev = node;
    }
}
