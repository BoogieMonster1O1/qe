package com.shrishdeshpande.qe.util;

import java.util.LinkedList;

public class TaskQueue extends LinkedList<Runnable> {
    private final Thread owner;

    public TaskQueue() {
        this.owner = Thread.currentThread();
    }

    public TaskQueue(Thread owner) {
        this.owner = owner;
    }

    public void execute() {
        if (Thread.currentThread() != owner) {
            throw new IllegalStateException("TaskQueue can only be run by the thread that created it");
        }

        while (!isEmpty()) {
            poll().run();
        }
    }

    public void execute(Runnable task) {
        add(task);
    }
}
