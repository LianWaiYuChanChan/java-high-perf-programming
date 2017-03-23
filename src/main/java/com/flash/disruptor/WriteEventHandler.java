package com.flash.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * Created by zhangj52 on 3/23/2017.
 */
public class WriteEventHandler implements EventHandler<WriteEvent> {

    public void onEvent(WriteEvent writeEvent, long sequence, boolean endOfBatch) throws Exception {
        if (writeEvent != null && writeEvent.get() != null) {
            String message = writeEvent.get();

            // Put you business logic here.
            // here it will print only the submitted message.
            System.out.println(message + " processed.");
        }
    }
}
