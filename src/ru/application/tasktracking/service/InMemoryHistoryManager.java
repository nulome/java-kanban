package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;
    private final HashMap<Integer, Node<Task>> nodeHistory = new HashMap<>();


    private void linkLast(Task task) {

        final Node<Task> newHead = new Node<>(task, head);
        if (head == null) {
            head = newHead;
            tail = newHead;
            tail.next = newHead;
        } else {
            head.next = newHead;
            head = newHead;
        }
    }

    private void removeNode(int id) {
        if (nodeHistory.containsKey(id)) {
            if (nodeHistory.size() == 1) {
                head = null;
                tail = null;
            } else {
                Node<Task> swapNode = nodeHistory.get(id);
                if (swapNode.prev == null) {
                    swapNode.next.prev = null;
                    tail = swapNode.next;
                } else if (swapNode.next == null) {
                    swapNode.prev.next = null;
                    head = swapNode.prev;
                } else {
                    swapNode.prev.next = swapNode.next;
                    swapNode.next.prev = swapNode.prev;
                }
                nodeHistory.remove(id);
            }
        }
    }


    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            history.add(node.data);
            node = node.prev;
        }
        return history;
    }

    @Override
    public void addHistory(Task task) {
        removeNode(task.getUniqueId());
        linkLast(task);
        nodeHistory.put(task.getUniqueId(), head);
    }

    @Override
    public void removeHistory(int id) {
        removeNode(id);
    }

}
