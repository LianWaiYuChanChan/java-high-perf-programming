package com.flash;


import java.util.concurrent.atomic.AtomicInteger;

public class IntegerAddOperation {

    public void addNormal() {

        int n = Integer.MAX_VALUE;
        int k = 0;
        long start = System.currentTimeMillis();
        for(int i = 0; i < n; i ++ ) {
            k++;
        }
        long end = System.currentTimeMillis();
        System.out.println(k);
        System.out.println(end - start);

    }

    public void addNormalWithMointor() {

        int n = Integer.MAX_VALUE;
        int k = 0;
        long start = System.currentTimeMillis();
        for(int i = 0; i < n; i ++ ) {
            synchronized (this) {
                k++;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(k);
        System.out.println(end - start);
    }

    public void addWithAtomicInteger() {
        int n = Integer.MAX_VALUE;
        AtomicInteger k = new AtomicInteger(0);
        long start = System.currentTimeMillis();

        for(int i = 0; i < n; i++) {
            k.incrementAndGet();
        }

        long end = System.currentTimeMillis();
        System.out.println(k);
        System.out.println(end - start);
    }

    public static void main(String[] args) {

        IntegerAddOperation s = new IntegerAddOperation();
        //s.addNormal(); //4 milliseconds?
        //s.addNormalWithMointor(); //63 seconds
        s.addWithAtomicInteger(); //17 seconds

    }

}
