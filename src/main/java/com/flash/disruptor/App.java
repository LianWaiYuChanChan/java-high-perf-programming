package com.flash.disruptor;

/**
 * Demo App
 */
public class App {

    public static void main(String[] args) {
        //TODO: there is good perf test code at below URL.
        // https://github.com/LMAX-Exchange/disruptor/tree/master/src/perftest/java/com/lmax/disruptor
        LMAXWriter lmaxWriter = new LMAXWriter();
        lmaxWriter.setRingBufferSize(1024);
        lmaxWriter.init();

        // submit messages to write concurrently using disruptor
        for (int i = 0; i < 1000; i++) {
            lmaxWriter.submitMessage("Message Sequence " + i);
        }

        lmaxWriter.close();
    }
}


