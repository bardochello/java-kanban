package controllers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> taskHistoryMap = new HashMap<>();
    private Node head;
    private Node tail;

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

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        removeNode(taskHistoryMap.get(task.getId()));
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

        Node curPrevNode = node.prev;
        Node curNextNode = node.next;
        taskHistoryMap.remove(node.data.getId());

        if (curNextNode != null) {
            curNextNode.prev = curPrevNode;
        } else {
            tail = curPrevNode;
        }

        if (curPrevNode != null) {
            curPrevNode.next = curNextNode;
        } else {
            head = curNextNode;
        }
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
        Node nextNode = head;

        while (nextNode != null) {
            nodeTasks.add(nextNode.data);
            nextNode = taskHistoryMap.get(nextNode.data.getId()).next;
        }

        return nodeTasks;
    }
}
