package com.flash.disruptor;

/**
 * Created by zhangj52 on 3/23/2017.
 */
public class WriteEvent {

    private String message;

    public void set(String message){
        this.message = message;
    }

    public String get() {
        return this.message;
    }
}
