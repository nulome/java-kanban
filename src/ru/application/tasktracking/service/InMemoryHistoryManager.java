package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;
import java.util.HashMap;



public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;
    private final HashMap<Integer, Node<Task>> nodeHistory = new HashMap<>();


    private void linkLast(Task task) {
        Node<Task> newHead = new Node<>(task, head);
        nodeHistory.put(task.getUniqueId(), newHead);
        head.next = newHead;
        head = newHead;
    }

    private void removeNode(Node<Task> swapNode) {
        Node<Task> prevNode = swapNode.prev;
        Node<Task> nextNode = swapNode.next;
        if (prevNode == null && nextNode == null) {
            head = null;
            tail = null;
        } else if (prevNode == null) {
            nextNode.prev = null;
            tail = nextNode;
        } else if (nextNode == null) {
            prevNode.next = null;
            head = prevNode;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        nodeHistory.remove(swapNode.data.getUniqueId());
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
        if (!nodeHistory.isEmpty()) {
            if (nodeHistory.containsKey(task.getUniqueId())) {
                Node<Task> swapNode = nodeHistory.get(task.getUniqueId());
                removeNode(swapNode);
                linkLast(task);
            } else {
                linkLast(task);
            }
        } else {
            head = new Node<>(task);
            tail = head;
            nodeHistory.put(task.getUniqueId(), head);
        }
    }

    @Override
    public void removeHistory(int id) {
        if (nodeHistory.containsKey(id)) {
            Node<Task> node = nodeHistory.get(id);
            removeNode(node);
        }
    }

}
