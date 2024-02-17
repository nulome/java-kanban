package ru.application.tasktracking.service;


import ru.application.tasktracking.objects.Task;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return data.equals(node.data);
    }
}
