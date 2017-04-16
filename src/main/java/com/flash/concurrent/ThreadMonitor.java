package com.flash.concurrent;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by zhangj52 on 4/16/2017.
 */
public class ThreadMonitor {

    //Monitor what? status or result.
    //If status, I can hold the threads' reference and just call Thread.
    public static void main(String[] args) {
        ThreadMonitor threadMonitor = new ThreadMonitor();
        //threadMonitor.monitorStatus();
        threadMonitor.monitorStatusJupitor();

    }

    public class SomeTask implements Runnable {
        public SomeTask(int taskId) {
            this.taskId = taskId;
        }

        private int taskId;

        @Override
        public void run() {
            System.out.println(taskId + " Started!");
            int randInt = (int) (Math.random() * 10);
            System.out.println(taskId + " Total steps: " + randInt);

            for (int i = 0; i < randInt; i++) {
                System.out.println(taskId + " Step: " + randInt);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(taskId + " Terminated!");
        }
    }



    public void monitorStatus() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        System.out.println(threadPool.toString());
        List<Runnable> tasks = IntStream.range(0, 10)
                .mapToObj(SomeTask::new).collect(Collectors.toList());
        tasks.forEach(threadPool::execute);

        int activeCount = threadPool.getActiveCount();
        while (activeCount > 0) {
            System.out.println("-------------- Thread Pool Status: " + threadPool);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activeCount = threadPool.getActiveCount();
        }

        System.out.println("Before shut down");

        threadPool.shutdown();

    }

    private static class SomeTaskJupitor implements Runnable {
        public SomeTaskJupitor(CountDownLatch countDownLatch, int taskId) {
            this.countDownLatch = countDownLatch;
            this.taskId = taskId;
        }

        private CountDownLatch countDownLatch;
        private int taskId;

        @Override
        public void run() {
            System.out.println(taskId + " Started!");
            int randInt = (int) (Math.random() * 10);
            System.out.println(taskId + " Total steps: " + randInt);

            for (int i = 0; i < randInt; i++) {
                System.out.println(taskId + " Step: " + randInt);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(taskId + " Terminated!");
            countDownLatch.countDown();
        }
    }

    /**
     * Check Thread State
     */
    public void monitorStatusJupitor() {
        int tsskNumber = 10;
        CountDownLatch countDownLatch = new CountDownLatch(tsskNumber);

        List<Runnable> tasks = IntStream.range(0, tsskNumber)
                .mapToObj(taskId -> new SomeTaskJupitor(countDownLatch, taskId))
                .collect(Collectors.toList());

        List<Thread> threads = tasks.stream()
                .map(Thread::new)
                .collect(Collectors.toList());

        threads.forEach(Thread::start);

        while (true) {
            try {
                long start = System.currentTimeMillis();
                countDownLatch.await(2, TimeUnit.SECONDS);
                long duration = System.currentTimeMillis() - start;
                System.out.println(duration/10000);
                if ((duration / 1000) < 2) {
                    System.out.println("Done");
                    break;
                } else {
                    System.out.println("Not Done.");
                    threads.forEach(thread -> System.out.println("State of thread " + thread.getId() + ": " + thread.getState()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Thread result. More accurate, task execution 's result or exception.
     */
    public void mointorResult(){

    }


    /**
     * Task's process (%xx). Need task itself to update its progress.
     */
    public void mointorProgress() {

    }


}
