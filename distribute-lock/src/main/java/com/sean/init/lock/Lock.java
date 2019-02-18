package com.sean.init.lock;

public interface Lock {
    boolean lock() throws InterruptedException;

    void unlock() ;
}