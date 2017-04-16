package com.flash.disruptor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhangj52 on 3/24/2017.
 */
public class BlockingQueueExample {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1024);

        Runnable producer = new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 1024; i++) {
                    try {
                        queue.put(i);
                        System.out.println("Put " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread producerThread = new Thread(producer);
        producerThread.start();

        Runnable consumer = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    Integer got = null;
                    try {
                        got = queue.take();
                        System.out.println("Got : " + (got + 1));
                        if (got == 1023) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }
}
