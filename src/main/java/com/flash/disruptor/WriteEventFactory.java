package com.flash.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Created by zhangj52 on 3/23/2017.
 */
public class WriteEventFactory implements EventFactory<WriteEvent> {

    public WriteEvent newInstance() {
        return new WriteEvent();
    }
}
