package com.flash.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * http://ifeve.com/disruptor-getting-started/
 * Created by zhangj52 on 3/24/2017.
 */
public class IntegerEventDisruptorExample {

    public static class IntegerEvent {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newCachedThreadPool();
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;// Construct the Disruptor
        Disruptor<IntegerEvent> disruptor = new Disruptor<>(IntegerEvent::new, bufferSize, executor);
        // 可以使用lambda来注册一个EventHandler
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("Event got: " + event.getValue()));
        // Start the Disruptor, starts all threads running
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<IntegerEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (int l = 0; true; l++) {
            bb.putInt(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.setValue(buffer.getInt(0)), bb);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
