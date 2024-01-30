package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    //public Node<Task> tail;
    private int size = 0;
    private ArrayList<Task> history = new ArrayList<>();
    private HashMap<Integer, Node<Task>> customHistory = new HashMap<>(); // В ключах будут храниться id задач, а в значениях Node —  узлы связного списка.


    private void linkLast(Task task) { // добавлять задачу в конец этого списка // вопрос. нужно ли ограничивать количество списка, как в списке истории.
        if (!customHistory.isEmpty()) {
            if (customHistory.containsKey(task.getUniqueId())) {
                Node<Task> swapNode = customHistory.get(task.getUniqueId());
                if (size == 1) {
                    head = new Node<>(task);
                    customHistory.put(task.getUniqueId(), head); // проверить замену списка из 1 истории, при просмотре 1 и той же задачи
                } else {
                    removeNode(swapNode);
                    newHead(task);
                }
            } else {
                newHead(task);
            }
        } else {
            head = new Node<>(task);
            customHistory.put(task.getUniqueId(), head);
            size++;
        }
    }

    private void newHead(Task task) {
        Node<Task> newHead = new Node<>(task, head);
        customHistory.put(task.getUniqueId(), newHead);
        head.next = newHead;
        head = newHead;
        size++;
    }

    private void getTasksHistory() { // собирать все задачи из HashMap в обычный ArrayList
        Node<Task> node = head;
        history.clear();
        for (int i = 0; i < size; i++) {
            history.add(node.data);
            node = node.prev;
        }
    }

    private void removeNode(Node<Task> swapNode) { // должен принимать объект Node — узел связного списка и вырезать его.
        Node<Task> prevNode = swapNode.prev;
        Node<Task> nextNode = swapNode.next;
        if (size == 1) {
            head = null;
        } else if (prevNode == null) {
            nextNode.prev = null;
        } else if (nextNode == null) {
            prevNode.next = null;
            head = prevNode;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        size--;
        customHistory.remove(swapNode.data.getUniqueId());
    }


    @Override
    public ArrayList<Task> getHistory() {
        getTasksHistory();
        return history;
    }

    @Override
    public void addHistory(Task task) {
        /*history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }*/
        linkLast(task);
    }

    @Override
    public void removeHistory(int id) {
        if (customHistory.containsKey(id)) {
            Node<Task> node = customHistory.get(id);
            removeNode(node);
        }
        // доработка
    }

}
