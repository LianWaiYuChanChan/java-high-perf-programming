package com.flash.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhangj52 on 3/23/2017.
 */
public class LMAXWriter {
    private int ringBufferSize;
    private Disruptor<WriteEvent> disruptor;
    private WriteEventProducer writeEventProducer;

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public void init() {
        // create a thread pool executor to be used by disruptor
        Executor executor = Executors.newCachedThreadPool();

        // initialize our event factory
        WriteEventFactory factory = new WriteEventFactory();

        if (ringBufferSize == 0) {
            ringBufferSize = 1024;
        }

        // ring buffer size always has to be the power of 2.
        // so if it is not, make it equal to the nearest integer.
        double power = Math.log(ringBufferSize) / Math.log(2);
        if (power % 1 != 0) {
            power = Math.ceil(power);
            ringBufferSize = (int) Math.pow(2, power);
            System.out.println("New ring buffer size = " + ringBufferSize);
        }

        // initialize our event handler.
        WriteEventHandler handler = new WriteEventHandler();

        // initialize the disruptor
        disruptor = new Disruptor<WriteEvent>(factory, ringBufferSize, executor);
        disruptor.handleEventsWith(handler);

        // set our custom exception handler
        ExceptionHandler exceptionHandler = new WriteExceptionHandler();
        disruptor.handleExceptionsFor(handler).with(exceptionHandler);

        // start the disruptor and get the generated ring buffer instance
        disruptor.start();

        // initialize the event producer to submit messages
        writeEventProducer = new WriteEventProducer(disruptor);

        System.out.println("Disruptor engine started successfully.");
    }

    public void submitMessage(String message) {
        if (writeEventProducer != null ) {
            // publish the messages via event producer
            writeEventProducer.onData(message);
        }
    }

    public void close() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
    }
}
