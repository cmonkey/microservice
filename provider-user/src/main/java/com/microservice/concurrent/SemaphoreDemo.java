package com.microservice.concurrent;

import java.util.concurrent.Semaphore;

// 经典的信号量，通过计数器对共享职员的访问
// Semaphore(int count) : 创建拥有count个许可证的信号量
// acquire()/acquire(int num) : 获取1/num个许可证
// release()/release(int num): 释放1/num个许可证
public class SemaphoreDemo{
    public static void main (String [] args) {
        Semaphore semaphore = new Semaphore(2);

        Person p1 = new Person("A", semaphore);
        p1.start();

        Person p2 = new Person("B", semaphore);
        p2.start();

        Person p3 = new Person("C", semaphore);
        p3.start();
    }
}
class Person extends Thread{

    private Semaphore semaphore;

    public Person(String name, Semaphore semaphore){
        super(name);
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        System.out.println(getName() + " is waiting ......");

        try {
            semaphore.acquire();
            Thread.sleep(1000);
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println(getName() + "is done!");
        semaphore.release();
    }
}