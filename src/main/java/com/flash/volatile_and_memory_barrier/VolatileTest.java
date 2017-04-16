package com.flash.volatile_and_memory_barrier;

/**
 * Created by zhangj52 on 3/28/2017.
 */
public class VolatileTest {
    public VolatileTest(int nonVolatile, int isVolatile) {
        this.nonVolatile = nonVolatile;
        this.isVolatile = isVolatile;
    }

    private int nonVolatile;
    private volatile int isVolatile;


    public void opsOnVolatile() {
        System.out.println("Start");
        isVolatile = 100;
        System.out.println("End");
    }

    public void opsOnNonVolatile() {
        System.out.println("Start non volatile.");
        nonVolatile = 20;
        System.out.println("End non volatile.");
    }

    public void readVolatile() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 1000; i++) {
            isVolatile = i;
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Volatile:" + duration + " " + isVolatile);

    }

    public void readNonVolatile() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 1000; i++) {
            nonVolatile = i;
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Non volatile:" + duration + " " + nonVolatile);
    }

    public static void main(String[] args) {
        VolatileTest s = new VolatileTest(10, 100);
        s.opsOnVolatile();
        s.opsOnNonVolatile();
        s.readNonVolatile();
        s.readVolatile();
    }

}
