package com.flash.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * Created by zhangj52 on 3/23/2017.
 */
public class WriteEventProducer {

    private final Disruptor<WriteEvent> disruptor;

    public WriteEventProducer(Disruptor<WriteEvent> disruptor) {
        this.disruptor = disruptor;
    }

    private static final EventTranslatorOneArg<WriteEvent, String> TRANSLATOR_ONE_ARG =
            new EventTranslatorOneArg<WriteEvent, String>() {
                public void translateTo(WriteEvent writeEvent, long sequence, String message) {
                    System.out.println("Inside translator");
                    writeEvent.set(message);
                }
            };

    public void onData(String message) {
        System.out.println("Publishing " + message);
        // publish the message to disruptor
        disruptor.publishEvent(TRANSLATOR_ONE_ARG, message);
    }
}
