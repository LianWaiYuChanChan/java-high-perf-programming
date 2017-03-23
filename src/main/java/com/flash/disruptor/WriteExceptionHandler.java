package com.flash.disruptor;

import com.lmax.disruptor.ExceptionHandler;

/**
 * Created by zhangj52 on 3/23/2017.
 */
public class WriteExceptionHandler implements ExceptionHandler {
    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
