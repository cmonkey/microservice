package com.microservice.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// java.util.concurrent.Lock 包中提供了对锁的支持，为适用synchronized控制对资源访问提供了替代机制
// 基本操作模型，访问资源之前申请锁，访问完毕后释放锁
// lock/tryLock 申请锁
// unlock 释放锁
// 集体锁类ReentranLock 实现了Lock 接口
//
// java.util.concurrent.atom包中提供了对原子操作的支持，提供了不需要锁以及其他同步机制就可以进行一些不可中断操作，主要操作为， 获取，设置，比较等
public class LockAtomDemo{

    public static void main (String [] args) {

        new MT().start();
        new MT().start();
        new MT().start();
        new MT().start();
    }
}

class Data{
    static int i = 0;
    static Lock lock = new ReentrantLock();
    static void operate(){
        lock.lock();
        i++;
        System.out.println(i);
        lock.unlock();
    }
}

class Data2{
    static AtomicInteger ai = new AtomicInteger(0);

    static void operate(){
        System.out.println(ai.incrementAndGet());
    }
}

class MT extends Thread{
    @Override
    public void run(){

        while(true){
            try {
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }

            Data.operate();
            Data2.operate();
        }
    }
}
