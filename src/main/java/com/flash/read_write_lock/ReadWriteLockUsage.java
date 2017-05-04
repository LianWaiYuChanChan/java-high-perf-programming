package com.flash.read_write_lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

/**
 * http://tutorials.jenkov.com/java-concurrency/read-write-locks.html
 * Created by zhangj52 on 5/4/2017.
 */
public class ReadWriteLockUsage {

    private static class SetZeroThread implements Runnable {
        private final AtomicBoolean endingSignal;
        private SharedValue sharedValue;
        private ReadWriteLock rwLock;

        public SetZeroThread(SharedValue sharedValue, ReadWriteLock rwLock, AtomicBoolean endingSignal) {
            this.sharedValue = sharedValue;
            this.rwLock = rwLock;
            this.endingSignal = endingSignal;
        }

        @Override
        public void run() {
            while (true) {

                Lock writeLock = rwLock.writeLock();
                writeLock.lock();
                try {
                    sharedValue.setValue(0L);
                    //Database may need 10 mlli seconds.
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread() + " set 0.");
                } finally {
                    writeLock.unlock();
                }

                //Set Zero operation is less. If I sleep before. In test, Final value always 0.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (endingSignal.get()) {
                    System.out.println(Thread.currentThread() + "SetZero Ending ");
                    break;
                }
            }
        }
    }

    private static class AddOneThread implements Runnable {
        private final AtomicBoolean endingSignal;
        private ReadWriteLock rwLock;
        private SharedValue sharedValue;

        public AddOneThread(SharedValue sharedValue, ReadWriteLock rwLock, AtomicBoolean endingSignal) {
            this.sharedValue = sharedValue;
            this.rwLock = rwLock;
            this.endingSignal = endingSignal;
        }

        @Override
        public void run() {
            while (true) {
                Lock readLock = rwLock.readLock();
                readLock.lock();
                try {
                    sharedValue.addOne();
                    //Database may need 10 milli seconds.
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread() + " add 1 : " + sharedValue.getValue());
                } finally {
                    readLock.unlock();
                }
                if (endingSignal.get()) {
                    System.out.println(Thread.currentThread() + "AddOne Ending ");
                    break;
                }
            }
        }
    }

    private static class SharedValue {
        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public void addOne() {
            value++;
        }

        public SharedValue(long value) {
            this.value = value;
        }

        volatile long value;
    }

    /**
     * Problem Cedric:
     * The requirement is from Cedric:
     * We have a global integer value, for example, i. We have two kinds of threads:
     * SetZerorThread: One thread can set i as 0, and race condition is not allowed for this operation.
     * AddOneThread: Other threads try to add i++ concurrently. For these threads, he doesn't need synchronization and tolerable to
     * race condition.
     * Be notable that there is only one SetZeroThread and Multiple AddOneThread. The number of MulitpleThread is no fixed.
     * Performance constraint:At least 100 add one operation per seconds.
     * One important info is that the operation will go into database.
     * <p>
     *
     * Solution:
     * 1. Use ReadWriteLock. A little weird. Result: 8829/0.5.    17658 add one per seconds.
     * 2. Other solution?
     */
    public void solutionUseReadWriteLock() throws InterruptedException {
        //Prepare thread
        AtomicBoolean endingSignal = new AtomicBoolean(false);
        SharedValue sharedValue = new SharedValue(10L);
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        SetZeroThread setZeroThread = new SetZeroThread(sharedValue, rwLock, endingSignal);
        List<Runnable> addOneTrheads = new ArrayList<>();
        IntStream.range(0, 10).forEach(j -> addOneTrheads.add(new AddOneThread(sharedValue, rwLock, endingSignal)));
        ExecutorService setZeroExecutor = Executors.newSingleThreadExecutor();
        setZeroExecutor.submit(setZeroThread);
        ExecutorService addOneExecutor = Executors.newFixedThreadPool(10);
//        addOneTrheads.forEach(t -> addOneExecutor.submit(t));
        addOneTrheads.forEach(t -> (new Thread(t)).start());

        Thread.sleep(2 * 1000);

        boolean success = false;
        do {
            success = endingSignal.compareAndSet(false, true);
        } while (!success);

        boolean allDone;
        setZeroExecutor.shutdown();
        do {
            allDone = setZeroExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } while (!allDone);

        addOneExecutor.shutdown();
        do {
            allDone = addOneExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } while (!allDone);
        System.out.println("Final value: " + sharedValue.getValue());

    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockUsage usage = new ReadWriteLockUsage();
        usage.solutionUseReadWriteLock();
    }
}
