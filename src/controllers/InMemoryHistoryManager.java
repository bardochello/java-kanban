package controllers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Map<Integer, Node> taskHistoryMap = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }


        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(taskHistoryMap.get(id));
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        // Удаляем узел из связного списка
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        // Удаляем задачу из мапы
        taskHistoryMap.remove(node.data.getId());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node newNode = new Node(tail, task, null);

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        taskHistoryMap.put(task.getId(), newNode);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> nodeTasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            nodeTasks.add(current.data);
            current = current.next;
        }

        return nodeTasks;
    }
}
